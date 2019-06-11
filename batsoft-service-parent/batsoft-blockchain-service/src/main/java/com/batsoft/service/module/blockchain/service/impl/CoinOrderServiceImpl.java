/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-23 23:07:19
 */
package com.batsoft.service.module.blockchain.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.alibaba.fastjson.JSONObject;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.batsoft.blockchain.common.Coin;
import com.batsoft.blockchain.common.WalletResult;
import com.batsoft.blockchain.rpc.BaseRpc;
import com.batsoft.blockchain.rpc.Config;
import com.batsoft.blockchain.rpc.impl.geth.GethRpc;
import com.batsoft.blockchain.utils.BlockUtils;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.blockchain.CoinOrder;
import com.batsoft.model.module.blockchain.ErcCoin;
import com.batsoft.model.module.blockchain.Wallet;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.blockchain.dao.CoinOrderDao;
import com.batsoft.service.module.blockchain.service.CoinOrderService;
import com.batsoft.service.module.blockchain.service.ErcCoinService;
import com.batsoft.service.module.blockchain.service.WalletService;
import com.batsoft.service.module.blockchain.service.consumer.DepositConsumerService;
import com.batsoft.utils.Convert;

import lombok.extern.slf4j.Slf4j;

/**
 * <p> CoinOrderServiceImpl </p>
 * @author: Bat Admin
 * @Date :  2018-05-23 23:07:19
 */
@Service("coinOrderService")
@Slf4j
public class CoinOrderServiceImpl extends BaseServiceImpl<CoinOrder, String> implements CoinOrderService {
    private  static final String USERADDRESS=Constants.CACHE_EX_PREFIX+"user_address:";
    @Autowired
    private CoinOrderDao coinOrderDao;
    @Autowired
    private WalletService walletService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ErcCoinService ercCoinService;
    @Autowired
    private RabbitMqSender rabbitMqSender;
    @Autowired
    private DepositConsumerService depositConsumerService;

    private static final Long BTC_CONFIRM=1L;


    @Override
    public void findBlockTraction(String ethAddress) {
        QueryFilter walletFilter = new QueryFilter(Wallet.class);
        walletFilter.addFilter("status_EQ", Wallet.STATUS1);
        List<Wallet> list = walletService.find(walletFilter);

        for (Wallet wallet : list) {

            Config config = new Config(wallet.getWalletType(),wallet.getWalletCode(), wallet.getRpcProtocol(), wallet.getRpcIp(), wallet.getRpcPort(), wallet.getRpcAccount(), wallet.getRpcPassword());

            if (wallet.getWalletType().equals(Coin.ETH.getType())) {
                log.info("config===="+config.getWalletCode()+"===="+config.getCoinCode());
                    findEthBlockTraction(config,ethAddress);
            }else if(wallet.getWalletType().equals(Coin.BTC.getType())){
                    findBtcBlockTraction(config);
            }
        }
    }

    @Override
    public int hasHash(String hash) {
        return coinOrderDao.hashHash(hash);
    }

    @Override
    public List<CoinOrder> findCoinOrders() {
        QueryFilter filter=new QueryFilter(CoinOrder.class);
        filter.addFilter("consume_EQ",CoinOrder.CONSUME0);
        return find(filter);
    }

    @Override
    public void findConfirm() {
        List<CoinOrder> list=findCoinOrders();
        Wallet wallet;
        Config config=null;
        BaseRpc baseRpc;
        //区块确认数量
        BigInteger confirm=BigInteger.ZERO;
        for(CoinOrder tx : list){

            if(tx.getWalletType() != null){
                QueryFilter filter = new QueryFilter(Wallet.class);
                filter.addFilter("walletType_EQ", tx.getWalletType());
                filter.addFilter("status_EQ", Wallet.STATUS1);
                wallet = walletService.get(filter);
                 config = new Config(wallet.getWalletType(),tx.getCoinCode(),wallet.getRpcProtocol(), wallet.getRpcIp(), wallet.getRpcPort(), wallet.getRpcAccount(), wallet.getRpcPassword());
            }

            baseRpc = new BaseRpc();

            confirm=baseRpc.findConfirm(tx.getWalletType(),config,tx.getTxHash());

            if(tx.getWalletType().equals(Coin.BTC.getType())){

                 if(confirm.compareTo(BigInteger.valueOf(BTC_CONFIRM))>0){
                     tx.setConsume(CoinOrder.CONSUME1);
                     this.update(tx);

                     sendMq(tx);

                 }

            }else if(tx.getWalletType().equals(Coin.ETH.getType())){

                //目前没找到判断以太确认节点数 找到了可以加到这里
               // if(confirm.compareTo(BigInteger.valueOf(ETH_CONFIRM))>0){
                    tx.setConsume(CoinOrder.CONSUME1);
                    this.update(tx);

                    sendMq(tx);

               // }

            }
        }
    }

    /**
     * 发送充币到账消息
     * @param tx
     */
    private void sendMq(CoinOrder tx){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address",tx.getToAddress());
        jsonObject.put("coinCode",tx.getCoinCode());
        jsonObject.put("type","available");
        jsonObject.put("money",tx.getAmount());
        jsonObject.put("hash",tx.getTxHash());
        jsonObject.put("fromAddress",tx.getFromAddress());
        // 发消息 记账
        rabbitMqSender.toRedisAccount(jsonObject.toJSONString());
        //远程调用加流水
        depositConsumerService.depositAccountRecord(jsonObject);
        //远程调用加订单
        depositConsumerService.depositOrder(jsonObject);
    }

    /**
     * 查询ETH 区块交易数据
     * @param config
     */
    private void findEthBlockTraction(Config config,String ethAddress ) {
        BaseRpc baseRpc = new BaseRpc();
        BigInteger num = new BigInteger("0");
        try {
            //查区块高度
            WalletResult numberResult = baseRpc.findEthBlockNumber(config);
            if (numberResult.getSuccess()) {
                num = (BigInteger) numberResult.getData();
            }
            String redisBlockHigh= redisService.get(RedisKeyConstant.BLOCK_HIGH + config.getCoinCode());
            Long redisNumber =0L;

            if(redisBlockHigh!=null){
                redisNumber = Long.valueOf(redisBlockHigh);
            }

            // 最新区块高度
            long count = BlockUtils.getScanCount(redisNumber, Long.valueOf(num.toString()), 15L);
            Long endNumber = redisNumber + count;
            //如果缓冲中获取的区块为0 不执行
            if(redisNumber.equals(0)){
                log.info("缓冲中区块未同步==========");

            }else {
                while (redisNumber < endNumber) {

                    log.info("区块高度+++++++++++"+redisNumber+"====="+endNumber);

                    // 通过区块高度获取交易
                    WalletResult tractionResult = baseRpc.findEthBlockTransaction(config, new BigInteger((redisNumber++).toString()));

                    if (tractionResult.getSuccess()) {
                        List<EthBlock.TransactionResult> tractions = (List) tractionResult.getData();
                        for (EthBlock.TransactionResult txresult : tractions) {
                            CoinOrder coinOrder = new CoinOrder();
                            org.web3j.protocol.core.methods.response.Transaction tx = (org.web3j.protocol.core.methods.response.Transaction) txresult;
                            if (tx != null) {
                                String to = tx.getTo();
                                String from = tx.getFrom();
                                //假如from是系统配置的地址，就不执行任何逻辑
                              //  String eth_address = appConfigService.findValueByKey(AppConfig.ETH_WALLET_WHITE_ADDRESS);
                                if(from !=null && ethAddress !=null && ethAddress.equals(from)){
                                    continue;
                                }
                                if (to != null&&hasAddress(config.getCoinCode(),to) && hasHash(tx.getHash()) == 0 && BigInteger.ZERO.compareTo(tx.getValue()) < 0) {
                                    coinOrder.setAmount(Convert.fromWei(tx.getValue().toString(), Convert.Unit.ETHER));
                                    coinOrder.setTxHash(tx.getHash());
                                    coinOrder.setTransactionIndex(tx.getTransactionIndex().toString());
                                    coinOrder.setBlockNumber(tx.getBlockNumber().toString());
                                    coinOrder.setBlockHash(tx.getBlockHash());
                                    coinOrder.setFromAddress(from);
                                    coinOrder.setToAddress(to);
                                    coinOrder.setCoinCode(config.getCoinCode());
                                    coinOrder.setGas(new BigDecimal(tx.getGas().toString()));
                                    coinOrder.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
                                    coinOrder.setConsume(CoinOrder.CONSUME0);
                                    coinOrder.setWalletType(config.getWalletCode());
                                    this.save(coinOrder);
                                } else {

                                    // 判断是否是代币

                                    ErcCoin ercCoin = ercCoinService.findCoin(to);

                                    if (ercCoin != null) {
                                        String input = tx.getInput();
                                        if (StringUtils.isNotEmpty(input) && input.length() >= 138) {
                                            String data = input.substring(0, 9);
                                            data = data + input.substring(17, input.length());
                                            Function function = new Function("transfer", Arrays.asList(), Arrays.asList(new TypeReference<Address>() {
                                            }, new TypeReference<Uint256>() {
                                            }));
                                            List<Type> params = FunctionReturnDecoder.decode(data, function.getOutputParameters());
                                            // 充币地址
                                            String toAddress = params.get(0).getValue().toString();
                                            String amount = params.get(1).getValue().toString();
                                            if (StringUtils.isNotEmpty(amount) && hasHash(tx.getHash()) == 0) {
                                                // 币种
                                                String coinCode = ercCoin.getCoinCode();
                                                // 精度
                                                String unit = ercCoin.getCoinDecimals().toString();

                                                if (StringUtils.isNotEmpty(coinCode) && StringUtils.isNotEmpty(unit)) {

                                                    GethRpc gethRpc=new GethRpc(config);
                                                    TransactionReceipt receipt=gethRpc.getTransactionReceipt(tx.getHash());
                                                    if(receipt!=null&&receipt.getLogs()!=null&&receipt.getLogs().size()>0) {

                                                        log.info("info erc 20======="+toAddress);
                                                        if(hasAddress(config.getWalletCode(),toAddress)) {
                                                            String unitName="";
                                                            if("6".equals(unit)){
                                                                unitName="Tether";
                                                            }else{
                                                                //默认精度18
                                                                unitName="ether";
                                                            }
                                                            coinOrder.setAmount(Convert.fromWei(amount, Convert.Unit.fromString(unitName)));
                                                            coinOrder.setTxHash(tx.getHash());
                                                            coinOrder.setTransactionIndex(tx.getTransactionIndex().toString());
                                                            coinOrder.setBlockNumber(tx.getBlockNumber().toString());
                                                            coinOrder.setBlockHash(tx.getBlockHash());
                                                            coinOrder.setFromAddress(from);
                                                            coinOrder.setToAddress(toAddress);
                                                            coinOrder.setCoinCode(coinCode);
                                                            coinOrder.setGas(new BigDecimal(tx.getGas().toString()));
                                                            coinOrder.setGasPrice(new BigDecimal(tx.getGasPrice().toString()));
                                                            coinOrder.setConsume(CoinOrder.CONSUME0);
                                                            coinOrder.setTxInput(tx.getInput());
                                                            coinOrder.setWalletType(config.getWalletCode());

                                                            this.save(coinOrder);
                                                        }
                                                    }
                                                } else {
                                                    log.info("代币配置错误，请检查：  coinCode=" + coinCode + "  unit=" + unit);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            redisService.set(RedisKeyConstant.BLOCK_HIGH + config.getCoinCode(), redisNumber.toString(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 查询BTC 区块交易数据
     * @param config
     */
    private void findBtcBlockTraction(Config config) {
        BaseRpc baseRpc = new BaseRpc();

        try {

            // 通过区块高度获取交易
            WalletResult tractionResult = baseRpc.findBtcBlockTransaction(config);
            if (tractionResult.getSuccess()) {

                List<Bitcoin.Transaction> tractions = (List) tractionResult.getData();

                for (Bitcoin.Transaction txresult : tractions) {

                    CoinOrder coinOrder = new CoinOrder();

                    com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction tx = (com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction) txresult;

                    if (tx != null) {
                        log.info("TxId Bigin=========" + tx.txId());
                        System.out.println(tx.category()+"---"+tx.txId()+"---"+tx.amount());
                        if ("receive".equals(tx.category()) && hasHash(tx.txId()) == 0 && BigDecimal.ZERO.compareTo(new BigDecimal(tx.amount())) < 0) {
                            coinOrder.setAmount(new BigDecimal(tx.amount()));
                            coinOrder.setTxHash(tx.txId());
                            coinOrder.setTransactionIndex(String.valueOf(tx.blockIndex()));
                            coinOrder.setBlockHash(tx.blockHash());
                            coinOrder.setFromAddress("");
                            coinOrder.setToAddress(tx.address());
                            coinOrder.setCoinCode(config.getCoinCode());
                            coinOrder.setConsume(CoinOrder.CONSUME0);
                            coinOrder.setWalletType(config.getWalletCode());
                            this.save(coinOrder);
                            System.out.println("save success-->coinOrder");
                        }
                        log.info("TxId End=========" + tx.txId());

                    }
                }
            }
        }catch (Exception e){
            log.error("findBtcBlockTraction error=========="+e.getMessage());
        }
    }

    /**
     * 是否包含地址
     * @param coinCode
     * @param address
     * @return
     */
    private  boolean hasAddress(String coinCode,String address){
        boolean has=false;
        String address_redis=redisService.get(USERADDRESS+coinCode);
        if(!com.batsoft.utils.StringUtils.isEmpty(address_redis)){
            has= address_redis.contains(address);
        }
        return has;
    }

}
