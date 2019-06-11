package com.batsoft.blockchain.rpc.impl.bitcore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.batsoft.blockchain.common.WalletResult;
import com.batsoft.blockchain.rpc.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * @Desc 比特币接口
 * @author: shangxl
 * @Date :          2018年5月11日 下午2:46:09
 */
@Slf4j
public class BitcoreRpc {
	/**
	 * jsonrpc链接
	 */
	private static BitcoinJSONRPCClient client;

	public BitcoreRpc(Config config) {

		String protocol = config.getProtocol();
		String ip = config.getIp();
		String port = config.getPort();
		String rpcuser = config.getRpcuser();
		String rpcpassword = config.getRpcpassword();
//		Authenticator.setDefault(new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(rpcuser, rpcpassword.toCharArray());
//			}
//		});
		if (StringUtils.isNotEmpty(protocol) && StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(port) && StringUtils.isNotEmpty(rpcuser) && StringUtils.isNotEmpty(rpcpassword)) {
			String rpcUrl = protocol + "://" + rpcuser + ":" + rpcpassword + "@" + ip + ":" + port + "/";
			System.out.println("获取rpc链接，ip=" + ip);
			try {
				URL url=new URL(rpcUrl);
				this.client = new BitcoinJSONRPCClient(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("rpc链接中，有无效参数，请检查：");
			System.out.println("protocol=" + protocol);
			System.out.println("ip=" + ip);
			System.out.println("port=" + port);
			System.out.println("rpcuser=" + rpcuser);
			System.out.println("rpcpassword=" + rpcpassword);
		}

	}

	/* (non-Javadoc)
	 * @see com.api.BaseApi#send(java.lang.String, java.lang.String, java.lang.Double, java.lang.String)
	 */
	public WalletResult send(String from, String toAdress, Double amount, String lockPass) {

		WalletResult result = new WalletResult();

		boolean validate = false;
		try {
			validate = validateAddress(from).isValid();
			log.info("验证 from地址" + from + "地址是否可用" + validate);
			if (validate) {
				log.info("try query total balance");
				String balance = client.query("getbalance") + "";
				log.info("client getbalance="+":"+balance);
				String sendResult=client.sendToAddress(toAdress,amount);
				log.info("BTC send success="+":"+sendResult);
				String remainBalance = client.query("getbalance") + "";
				log.info("remainBalance="+":"+remainBalance);
				result.setSuccess(true);
				result.setData(sendResult);
			} else {
				result.setSuccess(false);
				result.setMsg("地址无效");
				log.info("地址无效");
			}
		} catch (BitcoinException e) {
			result.setSuccess(false);
			result.setMsg("钱包接口返回错误");
			log.info("钱包接口返回错误");
			e.printStackTrace();
		} catch (NullPointerException e) {
			log.info("client为空");
			result.setMsg("client为空");
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}

	public Bitcoin.AddressValidationResult validateAddress(String address) {
		Bitcoin.AddressValidationResult addressValidationResult = null;
		try {
			addressValidationResult = client.validateAddress(address);
		} catch (BitcoinException e) {
			log.info("钱包接口返回错误");
			e.printStackTrace();
		} catch (NullPointerException e) {
			log.info("client为空");
			e.printStackTrace();
		}
		return addressValidationResult;
	}

	/* (non-Javadoc)
	 * @see com.api.BaseApi#ceateAddress(java.lang.String)
	 */
	public WalletResult createAddress(String password) {
		WalletResult result = new WalletResult();
		String address;
		try {
			address = client.getNewAddress();
			if (StringUtils.isNotEmpty(address)) {
				JSONObject data = new JSONObject();
				data.put("address", address);
				data.put("password", password);
				result.setData(data);
				result.setSuccess(true);
			}
		} catch (BitcoinException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 查询余额
	 * @author: SHANGXL
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午2:22:07
	 */
	public WalletResult getBalance() {
		WalletResult result = new WalletResult();
		try {
			Double balance = client.getBalance();
			if (balance != null) {
				result.setData(balance.toString());
				result.setSuccess(true);
			}
		} catch (BitcoinException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 查询区块高度
	 * @author: SHANGXL
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午2:22:07
	 */
	public WalletResult getBlockHigh() {
		WalletResult result = new WalletResult();
		try {
			int high = client.getBlockCount();
			result.setData(high);
			result.setSuccess(true);
		} catch (BitcoinException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 查询交易记录
	 * @author: SHANGXL
	 * @param: @param account 账户
	 * @param: @param count 数量
	 * @param: @param from 从第几条开始 一般填 0
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午4:40:11
	 */
	public WalletResult listTransactions(String account, int count, int from) {
		WalletResult result = new WalletResult();
		try {
			List<Transaction> list = client.listTransactions(account, count, from);
			if (list != null && list.size() > 0) {
				result.setData(list);
				result.setSuccess(true);
			}
		} catch (BitcoinException e) {
			e.printStackTrace();
		}
		return result;
	}

	public WalletResult getRawTransaction(String txId) {
		WalletResult result = new WalletResult();
		try {

			result.setSuccess(true);
			result.setData(client.getRawTransaction(txId));
		} catch (BitcoinException e) {
			result.setSuccess(false);
			log.info("钱包接口返回错误");
		} catch (NullPointerException e) {
			result.setSuccess(false);
			log.info("client为空");
		}
		return result;
	}


	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.setProtocol("http");
		config.setIp("47.244.33.192");
		config.setPort("8332");
		//config.setCoinCode("BTC");
		config.setRpcuser("bttmall");
		config.setRpcpassword("Pjrwy9x4WiOs");
		BitcoreRpc rpc = new BitcoreRpc(config);
		Bitcoin.AddressValidationResult a = rpc.validateAddress("CWbTtJ51KK1StD6jKojCsrzHUiYe8gyzoN");
//		System.out.println(JSON.toJSONString(a));
		//System.out.println(JSON.toJSONString(rpc.getBlockHigh()));
//		System.out.println("钱包中所有钱" + client.getBalance());
//		//System.out.println(JSON.toJSONString("钱包中的所有账户信息"+client.query("listaccounts")));
//
//		System.out.println(JSON.toJSONString("钱包中的所有地址"+client.query("listaddressgroupings")));
//		System.out.println(JSON.toJSONString("解锁钱包10s" + client.query("walletpassphrase", "123456789", 10)));
//		//CQDNAuCqHaNMAVirQ9UCeX4ouVCkey2z6n      Cc51CYrtgzrgDrvNPFvYiMzJh3z9Ux1YEn
////		//System.out.println(JSON.toJSONString("转账"+client.sendFrom("CKTbzBf94kYoccTFAHSZfD5zDGrXgzhc1k", "Cc51CYrtgzrgDrvNPFvYiMzJh3z9Ux1YEn", 2.000000000000000)));
//		System.out.println(JSON.toJSONString("转账" + client.query("sendtoaddress", "CQDNAuCqHaNMAVirQ9UCeX4ouVCkey2z6n", 6.2000000000000000)));
//		System.out.println(JSON.toJSONString("锁住钱包" + client.query("walletlock")));
	}
}
