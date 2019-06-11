package com.batsoft.blockchain.rpc;


import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.batsoft.blockchain.common.Coin;
import com.batsoft.blockchain.common.WalletResult;
import com.batsoft.blockchain.rpc.impl.bitcore.BitcoreRpc;
import com.batsoft.blockchain.rpc.impl.geth.GethRpc;
import com.batsoft.utils.StringUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Desc 钱包api
 * @author: shangxl
 * @Date : 2018年5月11日 下午2:42:17
 */
public class BaseRpc {
	private BitcoreRpc bitcoreRpc;
	private GethRpc gethRpc;

	/**
	 * 创建钱包
	 *
	 * @param walletType
	 * @param config
	 * @return
	 */
	public WalletResult createAddress(String walletType, Config config) {
		if (walletType.equals(Coin.BTC.getType())) {
			if (bitcoreRpc == null) {
				bitcoreRpc = new BitcoreRpc(config);
			}
			return bitcoreRpc.createAddress("");
		}

		if (walletType.equals(Coin.ETH.getType()) || walletType.equals(Coin.ETC.getType())) {
			String password = StringUtils.createRandom(false, 32);
			if (gethRpc == null) {
				gethRpc = new GethRpc(config);
			}
			return gethRpc.createAddress(password);
		}

		return null;
	}

	/**
	 * 基于BTC 转账
	 *
	 * @param config
	 * @return
	 */
	public WalletResult sendBtcChain(Config config, String from, String to, String amount, String lockPass) {

		if (bitcoreRpc == null) {
			bitcoreRpc = new BitcoreRpc(config);
		}
		return bitcoreRpc.send(from, to, Double.valueOf(amount), lockPass);

	}

	public WalletResult sendEthChain(Config config, String from, String to, String amount, BigDecimal gas_price, BigDecimal gas_limit, String password, String contractAddress, int decimals) throws Exception {

		if (gethRpc == null) {
			gethRpc = new GethRpc(config);
		}

		BigDecimal gasPrice = Convert.toWei(gas_price, Convert.Unit.GWEI);


		BigDecimal money = Convert.toWei(new BigDecimal(amount), Convert.Unit.ETHER);
		if (config.getCoinCode().equals(Coin.ETH.getType()) || config.getCoinCode().equals(Coin.ETC.getType())) {
			return gethRpc.send(from, null, gasPrice.toBigInteger(), gas_limit.toBigInteger(), to, money.toBigInteger(), "", password);
		} else {
			return gethRpc.transferERC20Token(from, to, new BigDecimal(amount), password, contractAddress,decimals);
		}


	}

	/**
	 * 查询区块确认数
	 *
	 * @param walletType
	 * @param config
	 * @param hash
	 * @return
	 */
	public BigInteger findConfirm(String walletType, Config config, String hash) {
		if (walletType.equals(Coin.BTC.getType())) {
			if (bitcoreRpc == null) {
				bitcoreRpc = new BitcoreRpc(config);
			}
			WalletResult result = bitcoreRpc.getRawTransaction(hash);

			Bitcoin.RawTransaction tx = (Bitcoin.RawTransaction) result.getData();
			if (result.getSuccess()) {
				return new BigInteger(String.valueOf(tx.confirmations()));
			}
		}

		if (walletType.equals(Coin.ETH.getType()) || walletType.equals(Coin.ETC.getType())) {
			String password = StringUtils.createRandom(false, 32);
			if (gethRpc == null) {
				gethRpc = new GethRpc(config);
			}
			WalletResult result = gethRpc.getTransactionByHash(hash);

			org.web3j.protocol.core.methods.response.Transaction transaction = (org.web3j.protocol.core.methods.response.Transaction) result.getData();

			if (result.getSuccess()) {
				return transaction.getTransactionIndex();
			}
		}

		return BigInteger.ZERO;
	}


	/**
	 * 获取基于btc链开发的钱包区块数据
	 *
	 * @param config
	 * @return
	 */
	public WalletResult findBtcBlockTransaction(Config config) {


		if (bitcoreRpc == null) {
			bitcoreRpc = new BitcoreRpc(config);
		}
		return bitcoreRpc.listTransactions("*", 10, 0);
	}

	/**
	 * 查询基于ETH 链开发的钱包区块数据
	 *
	 * @param config
	 * @param number 区块高度
	 * @return
	 */

	public WalletResult findEthBlockTransaction(Config config, BigInteger number) {

		if (gethRpc == null) {
			gethRpc = new GethRpc(config);
		}
		return gethRpc.getBlockByNumbe(number, true);
	}

	/**
	 * 查询基于ETH 链开发的钱包区块数据
	 *
	 * @param config
	 * @return
	 */

	public WalletResult findEthBlockNumber(Config config) {

		if (gethRpc == null) {
			System.out.println("==========" + config.getWalletCode());
			gethRpc = new GethRpc(config);
		}
		return gethRpc.getBlockHigh();
	}

	/**
	 * 获取区块高度
	 *
	 * @param walletType
	 * @param config
	 * @return
	 */
	public WalletResult findBlockHigh(String walletType, Config config) {
		WalletResult result = new WalletResult();
		result.setSuccess(false);

		if (walletType.equals(Coin.BTC.getType())) {
			if (bitcoreRpc == null) {
				bitcoreRpc = new BitcoreRpc(config);
			}
			result = bitcoreRpc.getBlockHigh();
		}
		if (walletType.equals(Coin.ETH.getType())) {
			if (gethRpc == null) {
				gethRpc = new GethRpc(config);
			}
			result = gethRpc.getBlockHigh();
		}

		return result;

	}

}
