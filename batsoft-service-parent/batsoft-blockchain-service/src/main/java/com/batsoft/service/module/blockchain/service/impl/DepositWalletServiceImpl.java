/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-06-03 10:51:01
 */
package com.batsoft.service.module.blockchain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.blockchain.common.Coin;
import com.batsoft.blockchain.common.WalletResult;
import com.batsoft.blockchain.rpc.BaseRpc;
import com.batsoft.blockchain.rpc.Config;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.blockchain.DepositWallet;
import com.batsoft.model.module.blockchain.ErcCoin;
import com.batsoft.model.module.blockchain.Wallet;
import com.batsoft.service.module.blockchain.dao.DepositWalletDao;
import com.batsoft.service.module.blockchain.service.DepositWalletService;
import com.batsoft.service.module.blockchain.service.ErcCoinService;
import com.batsoft.service.module.blockchain.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p> DepositWalletServiceImpl </p>
 *
 * @author: Bat Admin
 * @Date :  2018-06-03 10:51:01
 */
@Service("depositWalletService")
@Slf4j
public class DepositWalletServiceImpl extends BaseServiceImpl<DepositWallet, String> implements DepositWalletService {

	@Autowired
	private DepositWalletDao depositWalletDao;
	@Autowired
	private WalletService walletService;
	@Autowired
	private ErcCoinService ercCoinService;

	@Override
	public JsonResult saveCoinAddress(String coinCode) {
		JsonResult jsonResult = new JsonResult();
		try {
			QueryFilter filter1 = new QueryFilter(DepositWallet.class);
			filter1.addFilter("coinCode_EQ", coinCode);
			DepositWallet deposit_Wallet = this.get(filter1);

			if (deposit_Wallet == null) {
				Wallet wallet = new Wallet();
				BaseRpc baseRpc = new BaseRpc();
				WalletResult walletResult = new WalletResult();
				DepositWallet depositWallet = new DepositWallet();

				QueryFilter filter = new QueryFilter(Wallet.class);
				filter.addFilter("walletCode_EQ", coinCode);
				filter.addFilter("status_EQ", Wallet.STATUS1);
				wallet = walletService.get(filter);

				Config config = new Config(wallet.getWalletType(), coinCode, wallet.getRpcProtocol(), wallet.getRpcIp(), wallet.getRpcPort(), wallet.getRpcAccount(), wallet.getRpcPassword());

				walletResult = baseRpc.createAddress(wallet.getWalletType(), config);
				if (walletResult.getSuccess()) {
					JSONObject jsonObject = (JSONObject) walletResult.getData();
					depositWallet.setWalletType(wallet.getWalletType());
					depositWallet.setCoinCode(coinCode);
					depositWallet.setWalletAddress(jsonObject.get("address").toString());
					depositWallet.setLockPassword(jsonObject.get("password").toString());
					depositWallet.setFee(new BigDecimal(0));
					depositWallet.setGasPrice(new BigDecimal(0));
					depositWallet.setGasLimit(new BigDecimal(0));
					depositWallet.setWalletAmount(new BigDecimal(0));
					if (jsonObject.get("memo") != null) {
						depositWallet.setMemo(jsonObject.get("memo").toString());
					}

					this.save(depositWallet);
				}


				jsonResult.setSuccess(true);
				jsonResult.setMsg("创建成功");
			} else {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("已存在该钱包提币地址不可重复生成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setSuccess(false);
			jsonResult.setMsg("创建失败");
		}

		return jsonResult;
	}

	@Override
	public JsonResult sendBtcChainCoin(String coinCode, String toAddress, String amount) {
		JsonResult jsonResult = new JsonResult();
		DepositWallet depositWallet = new DepositWallet();

		QueryFilter filter = new QueryFilter(DepositWallet.class);
		filter.addFilter("coinCode_EQ", coinCode);
		depositWallet = this.get(filter);
		if (depositWallet != null) {

			Wallet wallet = new Wallet();
			BaseRpc baseRpc = new BaseRpc();
			WalletResult walletResult = new WalletResult();

			QueryFilter filter1 = new QueryFilter(Wallet.class);
			filter1.addFilter("walletCode_EQ", coinCode);
			filter1.addFilter("status_EQ", Wallet.STATUS1);
			wallet = walletService.get(filter1);

			Config config = new Config(wallet.getWalletType(), coinCode, wallet.getRpcProtocol(), wallet.getRpcIp(), wallet.getRpcPort(), wallet.getRpcAccount(), wallet.getRpcPassword());

			log.info("");
			walletResult = baseRpc.sendBtcChain(config, depositWallet.getWalletAddress(), toAddress, amount, depositWallet.getLockPassword());

			if (walletResult.getSuccess()) {
				jsonResult.setSuccess(true);
				jsonResult.setData(walletResult.getData().toString());
				log.info("转账成功=====" + walletResult.getData().toString());
			}

		}

		return jsonResult;
	}

	@Override
	public JsonResult sendEthChainCoin(String coinCode, String toAddress, String amount) {
		JsonResult jsonResult = new JsonResult();
		DepositWallet depositWallet = new DepositWallet();

		QueryFilter filter = new QueryFilter(DepositWallet.class);
		filter.addFilter("coinCode_EQ", "ETH");
		depositWallet = this.get(filter);
		if (depositWallet != null) {

			Wallet wallet = new Wallet();
			BaseRpc baseRpc = new BaseRpc();
			WalletResult walletResult = new WalletResult();

			QueryFilter filter1 = new QueryFilter(Wallet.class);
			filter1.addFilter("walletCode_EQ", "ETH");
			filter1.addFilter("status_EQ", Wallet.STATUS1);
			wallet = walletService.get(filter1);

			QueryFilter filter2 = new QueryFilter(ErcCoin.class);
			filter2.addFilter("coinCode_EQ", coinCode);
			ErcCoin ercCoin = ercCoinService.get(filter2);

			String coinAddress = "";
			if (ercCoin != null) {
				coinAddress = ercCoin.getCoinAddress();
			}
			Config config = new Config(wallet.getWalletType(), coinCode, wallet.getRpcProtocol(), wallet.getRpcIp(), wallet.getRpcPort(), wallet.getRpcAccount(), wallet.getRpcPassword());
			try {
				walletResult = baseRpc.sendEthChain(config, depositWallet.getWalletAddress(), toAddress, amount, depositWallet.getGasPrice(), depositWallet.getGasLimit(), depositWallet.getLockPassword(), coinAddress,Integer.parseInt(ercCoin.getCoinDecimals()));

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			if (walletResult.getSuccess()) {
				jsonResult.setSuccess(true);
				jsonResult.setData(walletResult.getData().toString());
				jsonResult.setMsg("转账成功");
				log.info("转账成功=====" + walletResult.getData().toString());
			} else {
				jsonResult.setSuccess(false);
				jsonResult.setMsg("调用钱包错误");
			}
		}


		return jsonResult;
	}

	@Override
	public JsonResult sendCoin(JSONObject data) {

		JsonResult jsonResult = new JsonResult();

		String coinCode = data.get("coinCode").toString();
		String toAddress = data.get("toAddress").toString();
		String amount = data.get("amount").toString();

		try {
			QueryFilter filter = new QueryFilter(Wallet.class);
			filter.addFilter("walletCode_EQ", coinCode);
			Wallet wallet = this.walletService.get(filter);

			if (wallet != null) {
				if (wallet.getWalletType().equals(Coin.BTC.getType())) {
					jsonResult = sendBtcChainCoin(coinCode, toAddress, amount);
				} else if (wallet.getWalletType().equals(Coin.ETH.getType())) {
					jsonResult = sendEthChainCoin(coinCode, toAddress, amount);
				}
			} else {
				QueryFilter filter1 = new QueryFilter(ErcCoin.class);
				filter1.addFilter("coinCode_EQ", coinCode);
				ErcCoin ercCoin = ercCoinService.get(filter1);
				if (ercCoin != null) {
					if (ercCoin.getBlock().equals(Coin.ETH.getType())) {
						jsonResult = sendEthChainCoin(coinCode, toAddress, amount);
					}
				}

			}
		} catch (Exception e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg("转币异常" + e.getMessage());
			e.printStackTrace();
		}

		return jsonResult;
	}


}
