/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-22 14:22:17
 */
package com.batsoft.service.module.blockchain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.blockchain.common.WalletResult;
import com.batsoft.blockchain.rpc.BaseRpc;
import com.batsoft.blockchain.rpc.Config;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.blockchain.CoinAccount;
import com.batsoft.model.module.blockchain.Wallet;
import com.batsoft.service.module.blockchain.dao.CoinAccountDao;
import com.batsoft.service.module.blockchain.service.CoinAccountService;
import com.batsoft.service.module.blockchain.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> CoinAccountServiceImpl </p>
 * @author: Bat Admin
 * @Date :  2018-05-22 14:22:17
 */
@Service("coinAccountService")
@Slf4j
public class CoinAccountServiceImpl extends BaseServiceImpl<CoinAccount, String> implements CoinAccountService {

    @Autowired
    private CoinAccountDao coinAccountDao;
    @Autowired
    private WalletService walletService;

    @Override
    public JsonResult saveCoinAddress(String coinCode, Integer count) {
        JsonResult jsonResult=new JsonResult();
        try {
            Wallet wallet = new Wallet();
            BaseRpc baseRpc = new BaseRpc();
            WalletResult walletResult = new WalletResult();
            CoinAccount coinAccount = null;

            QueryFilter filter = new QueryFilter(Wallet.class);
            filter.addFilter("walletCode_EQ", coinCode);
            filter.addFilter("status_EQ", Wallet.STATUS1);
            wallet = walletService.get(filter);

            Config config = new Config(wallet.getWalletType(),coinCode,wallet.getRpcProtocol(), wallet.getRpcIp(), wallet.getRpcPort(), wallet.getRpcAccount(), wallet.getRpcPassword());
            System.out.println("ETH create address debug--->config:"+config.toString());
            System.out.println("ETH create address debug--->create address count:"+count);
            for (int i = 0; i < count; i++) {
                coinAccount = new CoinAccount();
                walletResult = baseRpc.createAddress(wallet.getWalletType(), config);
                if (walletResult.getSuccess()) {
                    JSONObject jsonObject = (JSONObject) walletResult.getData();
                    coinAccount.setWalletCode(wallet.getWalletCode());
                    coinAccount.setWalletId(wallet.getId());
                    coinAccount.setCoinCode(coinCode);
                    coinAccount.setCoinAddress(jsonObject.get("address").toString());
                    coinAccount.setAddressPassword(jsonObject.get("password").toString());
                    if (jsonObject.get("memo") != null) {
                        coinAccount.setMemo(jsonObject.get("memo").toString());
                    }

                    this.save(coinAccount);
                }else{
                    System.out.println("ETH create address debug--->i:create failed");
                }

            }
            jsonResult.setSuccess(true);
            jsonResult.setMsg("创建成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setMsg("创建失败");
        }

        return jsonResult;
    }

    @Override
    public String findAddress(String walletCode) {
        String address = coinAccountDao.findAddress(walletCode);
        updateAddressStatus(address);
        return address;
    }

    @Override
    public void updateAddressStatus(String address) {

        coinAccountDao.updateAddressStatus(address);
    }

    @Override
    public JsonResult findAddressByEffective(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        jsonResult.setCode(Constants.SUCCESS);
        jsonResult.setMsg("成功");
        try{
            jsonResult.setData(coinAccountDao.findAddressNum());
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg("查询失败"+e.getMessage());
            jsonResult.setData(0);
            e.printStackTrace();
        }
        return jsonResult;
    }



}