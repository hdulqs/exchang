package com.batsoft.blockchain.rpc.impl.geth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.blockchain.common.WalletResult;
import com.batsoft.blockchain.rpc.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * @Desc 以太坊节点接口
 * @author: shangxl
 * @Date :          2018年5月14日 上午10:25:25
 */
@Slf4j
public class GethRpc {

	private JsonRpc2_0Admin client;
	private Web3jService web3jService;
	private Web3j web3j;
	private BigInteger gasPrice = Convert.toWei("10", Convert.Unit.GWEI).toBigInteger();
	private BigInteger gasLimit =  new BigInteger("90000");
	private String keystorePath="/root/.ethereum/keystore/";
	public GethRpc(Config config) {
		String protocol = config.getProtocol();
		String ip = config.getIp();
		String port = config.getPort();
		if (StringUtils.isNotEmpty(protocol) && StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(port)) {
			String url = protocol + "://" + ip + ":" + port;
			System.out.println("获取eth的jsonrpc连接===url=" + url);
			web3jService = new HttpService(url);
			web3j = Web3j.build(new HttpService(url));
			this.client = (JsonRpc2_0Admin) Admin.build(web3jService);
		} else {
			System.out.println("protocol=" + protocol);
			System.out.println("ip=" + ip);
			System.out.println("port=" + port);
		}
	}

	/**
	 * @Desc 根据transactionHash获取交易收据
	 * @author: SHANGXL
	 * @param: @param transactionHash
	 * @param: @return
	 * @return: TransactionReceipt
	 * @Date :  	2018年5月28日 上午11:49:28
	 */
	public TransactionReceipt getTransactionReceipt(String transactionHash) {
		TransactionReceipt receipt = new TransactionReceipt();
		try {
			receipt = this.client.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return receipt;
	}

	/**
	 * 转币
	 * gasPrice 单位wei
	 * nonce 默认null
	 * data 默认 ""
	 */
	public WalletResult send(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data, String password) {
		WalletResult result = new WalletResult();
		Transaction transaction = new Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
		try {
			if (this.client != null) {
				client.personalUnlockAccount(from, password);
				log.info("解锁账户+++++++++");
				String txId = this.client.personalSendTransaction(transaction, password).send().getTransactionHash();
				List<Object> attributes = new ArrayList(3);
				attributes.add(from);
//				attributes.add(password);
				Request<?, PersonalUnlockAccount> a = new Request("personal_lockAccount", attributes, web3jService, Response.class);
				System.out.println("锁住账户+++++++++++++++++" + JSON.toJSONString(a));
				if (StringUtils.isNotEmpty(txId)) {
					result.setData(txId);
					result.setSuccess(true);
					log.info("提币转出hash====" + txId);

				}
			} else {
				System.out.println("client is null");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 创建地址
	 */
	public WalletResult createAddress(String password) {
		WalletResult result = new WalletResult();
		try {
			System.out.println("ETH create address debug---> call rpc start");
			NewAccountIdentifier account = this.client.personalNewAccount(password).send();
			System.out.println("ETH create address debug---> call rpc end "+account.getAccountId());
			String address = account.getAccountId();
			if (StringUtils.isNotEmpty(address)) {
				JSONObject data = new JSONObject();
				data.put("address", address);
				data.put("password", password);
				result.setData(data);
				result.setSuccess(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 根据地址查询余额
	 * @author: SHANGXL
	 * @param: @param address
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午5:20:06
	 */
	public WalletResult getBalance(String address) {
		WalletResult result = new WalletResult();
		try {
			if (this.client != null) {
				EthGetBalance ethBalance = this.client.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
				BigInteger balance = ethBalance.getBalance();
				if (balance != null) {
					result.setMsg(balance.toString());
					result.setSuccess(true);
				}
			} else {
				System.out.println("client is null");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("url格式错误");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc unlock address
	 * @author: SHANGXL
	 * @param: @param address
	 * @param: @param password
	 * @param: @return
	 * @return: Boolean
	 * @Date :  	2018年5月14日 下午5:42:36
	 */
	public WalletResult unlockAccount(String address, String password) {
		WalletResult result = new WalletResult();
		try {
			if (this.client != null) {
				PersonalUnlockAccount account = this.client.personalUnlockAccount(address, password).send();
				if (account.accountUnlocked() != null) {
					result.setMsg(account.accountUnlocked().toString());
					result.setSuccess(true);
				}
			} else {
				System.out.println("client is null");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * @Desc 根据区块高度查询交易记录
	 * @author: SHANGXL
	 * @param: @param number
	 * @param: @param isdetail
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午5:57:26
	 */
	public WalletResult getBlockByNumbe(BigInteger number, boolean isdetail) {
		WalletResult result = new WalletResult();
		DefaultBlockParameter blockNumber = DefaultBlockParameter.valueOf(number);
		try {
			EthBlock.Block block = this.client.ethGetBlockByNumber(blockNumber, isdetail).send().getBlock();
			if (block != null) {
				@SuppressWarnings("rawtypes")
				List<EthBlock.TransactionResult> transactions = block.getTransactions();
				if (transactions != null && transactions.size() > 0) {
					result.setData(transactions);
					result.setSuccess(true);
				}
			}
		} catch (NullPointerException e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			System.out.println("geth连接错误");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 获取区块高度
	 * @author: SHANGXL
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午6:01:31
	 */
	public WalletResult getBlockHigh() {
		WalletResult result = new WalletResult();
		try {
			BigInteger blockNumber = this.client.ethBlockNumber().send().getBlockNumber();
			System.out.println("========blockNumber" + blockNumber);
			result.setData(blockNumber);
			result.setSuccess(true);
		} catch (ConnectException e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			System.out.println("geth连接错误");
		} catch (NullPointerException e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 根据hash查询交易记录
	 * @author: SHANGXL
	 * @param: @param hash
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午6:14:32
	 */
	public WalletResult getTransactionByHash(String hash) {
		WalletResult result = new WalletResult();
		try {
			org.web3j.protocol.core.methods.response.Transaction transaction = this.client.ethGetTransactionByHash(hash).send().getTransaction().get();
			if (transaction != null) {
				result.setData(transaction);
				result.setSuccess(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 获取coinBase地址
	 * @author: SHANGXL
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月14日 下午6:17:50
	 */
	public WalletResult getCoinBase() {
		WalletResult result = new WalletResult();
		try {
			String coinbase = this.client.ethCoinbase().send().getAddress();
			if (StringUtils.isNotEmpty(coinbase)) {
				result.setMsg(coinbase);
				result.setSuccess(true);
			}
		} catch (ConnectException e) {
			System.out.println("geth连接错误");
		} catch (NoRouteToHostException e) {
			System.out.println("eth-getBasecoin 钱包接口不通");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("url格式错误");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 查询代币的余额
	 * @author: SHANGXL
	 * @param: @param address
	 * @param: @param contractAddress
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月15日 下午4:46:33
	 */
	public WalletResult getTokenBalance(String address, String contractAddress) {
		WalletResult result = new WalletResult();
		Function fn = new Function("balanceOf", Arrays.asList(new Address(address)), Collections.<TypeReference<?>>emptyList());
		String data = FunctionEncoder.encode(fn);
		Transaction transaction = new Transaction(null, null, null, null, contractAddress, null, data);
		try {
			String balanceWeiStr = this.client.ethCall(transaction, DefaultBlockParameterName.LATEST).send().getValue();
			if (StringUtils.isNotEmpty(balanceWeiStr)) {
				BigInteger balanceWei = Numeric.decodeQuantity(balanceWeiStr);
				result.setMsg(balanceWei.toString());
				result.setSuccess(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Desc 以太坊代币转币
	 * @author: SHANGXL
	 * @param: @param from
	 * @param: @param nonce 默认为null
	 * @param: @param gasPrice 默认null
	 * @param: @param gasLimit 默认null
	 * @param: @param to
	 * @param: @param value
	 * @param: @param password
	 * @param: @return
	 * @return: JsonResult
	 * @Date :  	2018年5月15日 下午5:07:56
	 */
	public WalletResult sendTransactionToken(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger amount, String password, String contractAddress) {
		WalletResult result = new WalletResult();
		try {
			WalletResult unlock_result = this.unlockAccount(from, password);
			System.out.println("解锁账户" + unlock_result);
			if (unlock_result.getSuccess()) {

				List<Type> inputParameters = new ArrayList<>();
				List<TypeReference<?>> outputParameters = new ArrayList<>();

				Address tAddress = new Address(to);

				Uint256 value = new Uint256(amount);
				inputParameters.add(tAddress);
				inputParameters.add(value);

				TypeReference<Bool> typeReference = new TypeReference<Bool>() {
				};
				outputParameters.add(typeReference);

				Function function = new Function("transfer", inputParameters, outputParameters);

				String data = FunctionEncoder.encode(function);


				EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
						from, DefaultBlockParameterName.LATEST).sendAsync().get();
				nonce = ethGetTransactionCount.getTransactionCount();
				System.out.println("获取nonce" + JSON.toJSONString(nonce) + "");
				Transaction transaction = Transaction.createFunctionCallTransaction(from, nonce, gasPrice, gasLimit, contractAddress, data);
				System.out.println("transaction" + JSON.toJSONString(transaction));

				EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
				System.out.println("代币转出结果" + JSON.toJSONString(ethSendTransaction));
				List<Object> attributes = new ArrayList(3);
				attributes.add(from);
				Request<?, PersonalUnlockAccount> a = new Request("personal_lockAccount", attributes, web3jService, Response.class);
				System.out.println("锁住账户+++++++++++++++++" + JSON.toJSONString(a));
				result.setMsg(JSON.toJSONString(ethSendTransaction));
				result.setData(ethSendTransaction.getTransactionHash());
				result.setSuccess(true);
				result.setCode("看data信息不看sussces這個屬性");

			} else {
				return unlock_result;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}
	public WalletResult transferERC20Token(String from, String to, BigDecimal value, String password, String contractAddress,int decimals) throws Exception {
		WalletResult result = new WalletResult();
        //加载转账所需的凭证，用私钥
        //Credentials credentials = Credentials.create(privateKey);
        String keystoreFilename=getKeystoreFilenameFromPath(keystorePath,from);
        Credentials credentials = WalletUtils.loadCredentials(password, keystorePath+keystoreFilename);
        BigDecimal transferAmount=value.multiply(BigDecimal.TEN.pow(decimals));
        //创建RawTransaction交易对象
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(transferAmount.toBigInteger())),
                Arrays.asList(new TypeReference<Type>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        //获取nonce，交易笔数
        BigInteger nonce=getNonce(from);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,
                gasPrice,
                gasLimit,
				contractAddress, encodedFunction);


        //签名Transaction，这里要对交易做签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        if(ethSendTransaction!=null&&ethSendTransaction.getError()!=null){
            System.out.println(ethSendTransaction.getError().getMessage());
        }
        String txHash=ethSendTransaction.getTransactionHash();
        System.out.println("txHash="+txHash);
		result.setMsg(JSON.toJSONString(ethSendTransaction));
		result.setData(txHash);
		result.setSuccess(true);
		result.setCode("看data信息不看sussces這個屬性");
        return result;
    }
	public String getKeystoreFilenameFromPath(String keystorePath,String address){
		File path=new File(keystorePath);
		String keystoreFilename=null;
		if(path.isDirectory()){
			for(File file:path.listFiles()){
				if(file.isFile()){
					if(file.getName().endsWith(address.substring(2))||file.getName().endsWith(address.substring(2)+".json")){
						keystoreFilename=file.getName();
						break;
					}
				}
			}
		}
		return keystoreFilename;
	}
	/**
	 * 获取nonce，交易笔数
	 *
	 * @param from
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private BigInteger getNonce(String from) throws ExecutionException, InterruptedException {
		EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
		BigInteger nonce = transactionCount.getTransactionCount();
		System.out.println("transfer nonce : " + nonce);
		return nonce;
	}

}
