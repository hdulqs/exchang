/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.exchange.user;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import com.batsoft.core.cache.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.common.validator.MobileCodeToken;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.blockchain.service.CoinAccountService;
import com.batsoft.service.module.exchange.service.CoinRechargeService;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.service.module.exchange.service.CoinWithdrawService;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.service.EntrustHistoryService;
import com.batsoft.service.module.exchange.service.EntrustInfoService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.batsoft.service.module.exchange.service.WithdrawAddressService;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;

import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;

/**
 * <p>UserAccountController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "交易用户管理")
@Controller("ex_userAccountController")
@RequestMapping("/ex/member/account")
public class UserAccountController extends GenericController {

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerAccountService customerAccountService;

	@Autowired
	private CoinService coinService;

	@Autowired
	private CoinWithdrawService coinWithdrawService;

	@Autowired
	private CoinRechargeService coinRechargeService;

	@Autowired
	private WithdrawAddressService withdrawAddressService;

	@Autowired
	private EntrustIngService entrustIngService;

	@Autowired
	private EntrustHistoryService entrustHistoryService;

	@Autowired
	private EntrustInfoService entrustInfoService;

	@Autowired
	private CoinAccountService coinAccountService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private CustomerAccountRecordService customerAccountRecordService;
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

	/**
	 * 我的资产
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "",method = RequestMethod.GET)
	public String index(Model model) {
		return "exchange/member/account/index";
	}


	/**
	 * 委托管理
	 *
	 * @return
	 */
	@RequestMapping(value = "/entrust",method = RequestMethod.GET)
	public String entrust() {
		return "exchange/member/account/entrust";
	}

	/**
	 * 委托管理数据查询
	 *
	 * @return
	 */
	@ApiOperation(value = "当前委托")
	@RequestMapping(value = "/entrustList",method = RequestMethod.POST)
	@ResponseBody
	public PageResult entrustList(HttpServletRequest request) {
		PageResult pageResult = entrustIngService.findPageByUserId(request);
		return pageResult;
	}

	/**
	 * 历史委托管理
	 *
	 * @return
	 */
	@RequestMapping(value = "/history_entrust",method = RequestMethod.GET)
	public String history_entrust() {
		return "exchange/member/account/history_entrust";
	}

	/**
	 * 委历史委托管理查询
	 *
	 * @return
	 */
	@ApiOperation(value = "历史委托")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "页码", name = "page", paramType = "query", dataType = "String", required = false,defaultValue = "1"),
			@ApiImplicitParam(value = "分页数", name = "pageSize", paramType = "query", dataType = "String", required = false,defaultValue = "10"),
			@ApiImplicitParam(value = "查询的日期", name = "date", paramType = "query", dataType = "String", required = false,defaultValue = "new Date()"),
	})
	@RequestMapping(value = "/history_entrustList",method = RequestMethod.POST)
	@ResponseBody
	public PageResult history_entrustList(HttpServletRequest request) {
		int page = 1;
		int pageSize = 10;
		String pageStr = request.getParameter("page");
		if(pageStr != null){
			page = Integer.valueOf(pageStr);
		}
		String pageSizeStr = request.getParameter("pageSize");
		if(pageSizeStr != null){
			pageSize = Integer.valueOf(pageSize);
		}
		String dateStr = request.getParameter("date");
		Date date ;
		if(dateStr != null){
			date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
		}else{
			date =  new Date();
		}
		String userId = UserUtils.getUser().getId();
		PageResult pageResult = entrustHistoryService.findPageByUserId( request,userId, page, pageSize, date);
		return pageResult;
	}

	/**
	 * 历史成交管理
	 *
	 * @return
	 */
	@RequestMapping(value = "/history_trade",method = RequestMethod.GET)
	public String history_trade() {
		return "exchange/member/account/history_trade";
	}

	/**
	 * 历史成交管理查询
	 *
	 * @return
	 */
	@RequestMapping(value = "/history_tradeList",method = RequestMethod.POST)
	@ApiOperation(value = "历史成交")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "页码", name = "page", paramType = "query", dataType = "String", required = false,defaultValue = "1"),
			@ApiImplicitParam(value = "分页数", name = "pageSize", paramType = "query", dataType = "String", required = false,defaultValue = "10"),
			@ApiImplicitParam(value = "查询的日期", name = "date", paramType = "query", dataType = "String", required = false,defaultValue = "new Date()"),
	})
	@ResponseBody
	public PageResult history_tradeList(HttpServletRequest request) {
		int page = 1;
		int pageSize = 10;
		String pageStr = request.getParameter("page");
		if(pageStr != null){
			page = Integer.valueOf(pageStr);
		}
		String pageSizeStr = request.getParameter("pageSize");
		if(pageSizeStr != null){
			pageSize = Integer.valueOf(pageSize);
		}
		String dateStr = request.getParameter("date");
		Date date ;
		if(dateStr != null){
			date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
		}else{
			date =  new Date();
		}

		String userId = UserUtils.getUser().getId();
		PageResult pageResult = entrustInfoService.findPageByUserId(request, userId, page, pageSize, date);
		return pageResult;
	}

	/**
	 * 交易管理
	 *
	 * @return
	 */
	@RequestMapping(value = "/trade",method = RequestMethod.GET)
	public String trade() {
		return "exchange/member/account/trade";
	}

	/**
	 * 币账户提现地址管理
	 *
	 * @return
	 */
	@RequestMapping(value = "/address",method = RequestMethod.GET)
	public String address() {
		return "exchange/member/account/address";
	}

	/**
	 * 资金充值
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deposit",method = RequestMethod.GET)
	public String deposit(Model model, HttpServletRequest request) throws Exception{
		String coinCode = request.getParameter("coin_code");
		String allowRecharge = request.getParameter("allowRecharge");
		if(StringUtils.isNotEmpty(allowRecharge)&&allowRecharge!=null&&"0".equals(allowRecharge)){
			return "exchange/member/account/index";
		}
		if (StringUtils.isEmpty(coinCode)) {
			coinCode = ApplicationConfigure.defaultCoin;
		}
		model.addAttribute("coinCode", coinCode);
		return "exchange/member/account/deposit";
	}

	/**
	 * 获取具体币种充值数据
	 *
	 * @return
	 */
	@ApiOperation(value = "账户对应的币种的余额")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易对", name = "coin_code", paramType = "path", dataType = "String")
	})
	@RequestMapping(value = "/findCoinAccount/{coinCode}",method = RequestMethod.GET)
	@ResponseBody
	public CustomerAccountVo findCoinAccount(@PathVariable String coinCode) {
		return customerAccountService.findCoinAccount(UserUtils.getUser().getId(), coinCode);
	}

	/**
	 * 充币提币记录
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/money_info",method = RequestMethod.GET)
	public String money_info(Model model) {
		return "exchange/member/account/money_info";
	}

	/**
	 * 资金提现
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/withdraw",method = RequestMethod.GET)
	public String withdraw(Model model, HttpServletRequest request)throws  Exception {
		String coinCode = request.getParameter("coin_code");
		String allowWithdraw = request.getParameter("allowWithdraw");
		if(StringUtils.isNotEmpty(allowWithdraw)&&allowWithdraw!=null&&"0".equals(allowWithdraw)){
			return "exchange/member/account/index";
		}
		if (StringUtils.isEmpty(coinCode)) {
			coinCode = ApplicationConfigure.defaultCoin;
		}
		model.addAttribute("coinCode", coinCode);
		return "exchange/member/account/withdraw";
	}

	@RequestMapping(value = "/withdraw_history",method = RequestMethod.GET)
	public String withDrawHistory(Model model, HttpServletRequest request)throws  Exception {
		return "exchange/member/account/withdraw_history";
	}

	/**
	 * 资金明细
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detail",method = RequestMethod.GET)
	public String detail(Model model) {
		return "exchange/member/account/detail";
	}

	/**
	 * /**
	 * 银行卡管理
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/card",method = RequestMethod.GET)
	public String card(Model model) {
		return "exchange/member/account/card";
	}



	/**
	 * 获取具体币种提币数据
	 *
	 * @return
	 */
	@ApiOperation(value = "账户提币初始化")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易对", name = "coinCode", paramType = "query", dataType = "String")
	})
	@RequestMapping(value = "/findWithdrawData/{coinCode}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findWithdrawData(@PathVariable String coinCode) {
		UserVo userVo = UserUtils.getUser();
		Map<String, Object> data = new HashMap<>();
		data.put("coin", coinService.findWithdrawData(coinCode));
		CustomerAccountVo account = customerAccountService.findCoinAccount(userVo.getId(), coinCode);
		if (account == null) {
			account = new CustomerAccountVo();
		}
		data.put("account", account);
		data.put("address", withdrawAddressService.findAddresList(coinCode,userVo.getId()));
		data.put("openGoogleAuth", userVo.getOpenGoogleAuth());
		return data;
	}

	/**
	 * 获取币种地址
	 *
	 * @return
	 */
	@ApiOperation(value = "获取提币地址")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易对", name = "coinCode", paramType = "path", dataType = "String")
	})
	@RequestMapping(value = "/findAddress/{coinCode}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findAddress(@PathVariable String coinCode) {
		Map<String, Object> data = new HashMap<>();
		String address = "";

		//从exchange_coin_account 查询coin_address 地址
		JSONObject ret = customerAccountService.findCoinAddress(UserUtils.getUser().getId(), coinCode);

		// 非 ERC20 代币
		if (!(Boolean) ret.get("ERC") || ret.get("address") == null) {
			address = coinAccountService.findAddress(coinCode);//从blockchain_coin_account 更加coinCode差处理
			if (address != null) {
				//保存地址
				customerAccountService.updateAccountAddress(UserUtils.getUser().getId(), coinCode, address);
			}
		}

		// ERC 20 代币
		if ((Boolean) ret.get("ERC") && ret.get("address") == null) {
			// 直接获取 eth 地址
			address = coinAccountService.findAddress("ETH");
			if (address != null) {
				// 保存代币地址
				customerAccountService.updateAccountAddress(UserUtils.getUser().getId(), coinCode, address);

				if (!coinCode.equals("ETH")) {
					// 同时保存以太地址
					customerAccountService.updateAccountAddress(UserUtils.getUser().getId(), "ETH", address);
				}
			}
		}

		if ((Boolean) ret.get("ERC") && ret.get("address") != null) {
			address = ret.get("address").toString();
		}
		data.put("address", address);
		data.put("coinCode", coinCode);
		return data;
	}


	/**
	 * 查询某个币种的可用数量
	 *
	 * @param coinCode
	 * @return
	 */
	@ApiOperation(value = "查询某个币种的可用数量")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易对", name = "coinCode", paramType = "path", dataType = "String")
	})
	@RequestMapping(value = "/bal/{coinCode}",method = RequestMethod.GET)
	@ResponseBody
	public BigDecimal bal(@PathVariable String coinCode) {

		BigDecimal money = customerAccountService.findCoinAccount(UserUtils.getUser().getId(), coinCode).getAvailable();

		return money;
	}

	/**
	 * 我的资产 --个人中心--下架币种
	 *
	 * @return
	 */
	@ApiOperation(value = "查询当前用户下架的币种")
	@RequestMapping(value = "/financeUnder",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult financeUnder() {
		JsonResult jsonResult = new JsonResult();
		try {
			jsonResult.setData(customerAccountService.findList(Coin.STATUS2));
			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}

	/**
	 * 我的资产 --个人中心--正常币种
	 *
	 * @return
	 */
	@ApiOperation(value = "查询当前用户正常的币种")
	@RequestMapping(value = "/financeData",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult financeData() {
		JsonResult jsonResult = new JsonResult();
		BigDecimal total = new BigDecimal(0);
		Jedis jedis = jedisClient.getJedis();
		List<CustomerAccountVo> accounts = customerAccountService.findCoinAccounts(UserUtils.getUser().getId());
		try {
			List<CustomerAccountVo> customerAccountVos = RedisUserUtil.getAllCoinsAccount(UserUtils.getUser().getId());
			for (CustomerAccountVo accountVo:customerAccountVos){
				CustomerAccountVo customerAccountVo =  accounts.stream().filter(new Predicate<CustomerAccountVo>() {
					@Override
					public boolean test(CustomerAccountVo customerAccountVo) {
						if(customerAccountVo.getCoinCode() != null && customerAccountVo.getCoinCode().equals(accountVo.getCoinCode())){
							return true;
						}
						return false;
					}
				}).findFirst().get();
				accountVo.setAllowWithdraw(customerAccountVo.getAllowWithdraw());
				accountVo.setAllowRecharge(customerAccountVo.getAllowRecharge());
				accountVo.setCoinLogo(customerAccountVo.getCoinLogo());
			}
			JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(customerAccountVos));
			DecimalFormat decimalFormat = new DecimalFormat(Constants.MONEY_FORMAT);
			for (Object o : jsonArray) {
				JSONObject jo = (JSONObject) o;
				if(jo.get("coinCode")!=null && jo.get("available") !=null && jo.get("freeze")!=null){
					BigDecimal totalMoney = new BigDecimal(jo.get("available").toString()).add(new BigDecimal(jo.get("freeze").toString()));
					BigDecimal estimatedValue = totalMoney.multiply(RedisUserUtil.covertToUSDT(jedis,jo.get("coinCode").toString(),"USDT")); //改币种的预估值 （保留两位小数）
					total = total.add(estimatedValue);
					jo.put("estimatedValue", decimalFormat.format(estimatedValue));
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("total",total.setScale(2,BigDecimal.ROUND_HALF_UP));
			map.put("customerAccountVos", jsonArray);
			jsonResult.setData(map);
			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setSuccess(false);
		} finally {
			jedisClient.close(jedis);
		}
		return jsonResult;
	}


	/**
	 * 我的资产 --交易大厅
	 *
	 * @return
	 */
	@ApiOperation(value = "我的资产")
	@RequestMapping(value = "/asset",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult account() {
		JsonResult jsonResult = new JsonResult();
		try {
			JSONArray jsonArray = new JSONArray();
			List<CustomerAccount> list = RedisUserUtil.getAllCoinsAccount();
			if (list != null && !list.isEmpty()) {
				for (CustomerAccount customerAccount : list) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("coinCode", customerAccount.getCoinCode());
					jsonObject.put("available", customerAccount.getAvailable());
					jsonObject.put("freeze", customerAccount.getFreeze());
					jsonObject.put("totalMoney", customerAccount.getAvailable().add(customerAccount.getFreeze()));
					jsonArray.add(jsonObject);
				}
			}
			jsonResult.setData(jsonArray);
			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}

	/**
	 * 提币申请
	 *
	 * @param tag              标签
	 * @param address          转币地址
	 * @param memo             备注
	 * @param amt              提币数量
	 * @param symbol           币种
	 * @param valid_code       验证码
	 * @param tradePwd         交易密码
	 * @param googleCode       google认证码
	 * @return
	 */
	@ApiOperation(value = "提币申请")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "标签", name = "tag", paramType = "query", dataType = "String"),
			@ApiImplicitParam(value = "转币地址", name = "address", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "备注", name = "memo", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "提币数量", name = "amt", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "币种", name = "symbol", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "手机验证码", name = "mobileCode", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "交易密码", name = "tradePwd", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "google认证码", name = "googleCode", paramType = "query", dataType = "String"),
	})
	@RequestMapping(value = "/withdrawCoin", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult withdrawCoin(@RequestParam(value = "address") String address,
	                                @RequestParam(value = "memo") String memo,
	                                @RequestParam(value = "amt") String amt,
	                                @RequestParam(value = "symbol") String symbol,
	                                @RequestParam(value = "mobileCode") String valid_code,
	                                @RequestParam(value = "tradePwd") String tradePwd,
	                                @RequestParam(value = "googleCode",required = false) String googleCode) {
		JsonResult jsonResult = new JsonResult();
		try {
			String[] addressArray = address.split(" ");
			address = addressArray[addressArray.length-1];
			User user = UserUtils.getUser();
			if(user.getOpenGoogleAuth() == 1) {
				//验证goodleCode
				if (googleCode == null) {
					jsonResult.setSuccess(false);
					jsonResult.setMsg(Language.L("input_google_code"));
					return  jsonResult;
				}else{
					if (!userService.checkGoogleAuth(user.getGoogleCode(), googleCode)) {
						jsonResult.setSuccess(false);
						jsonResult.setMsg(Language.L("google_invalid"));
						return jsonResult;
					}
				}
			}
			//1.校验验证码是否正确
			MobileCodeToken mobileCodeToken = new MobileCodeToken(valid_code, UserUtils.getUser().getUserMobile(), false, null);
			mobileCodeToken.isValid();
			//验证码单次有效，遇到异常删除验证码
			//2.校验交易密码是否正确
			PasswordHelper passwordHelper = new PasswordHelper();
			String tradePassWord = passwordHelper.encryString(tradePwd,UserUtils.getUser().getSalt());
			if(!tradePassWord.equals(UserUtils.getUser().getTradePassword())){
				jsonResult.setSuccess(false);
				jsonResult.setCode(Constants.FAILED);
				jsonResult.setMsg(Language.L_Failed("msg_trade_password_error",true));
				return jsonResult;
			}
		}catch (Exception e){
			jsonResult.setSuccess(false);
			jsonResult.setMsg(e.getMessage());
			return jsonResult;
		}
		//校验提币规则并且保存提币历史
		return coinWithdrawService.saveWithdrawOrder( address,  memo, amt, symbol);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(value = "转币地址", name = "address", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "备注", name = "memo", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "提币数量", name = "amt", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "币种", name = "symbol", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "验证码", name = "valid_code", paramType = "query", dataType = "String",required = true),
			@ApiImplicitParam(value = "验证类型", name = "valid_type", paramType = "query", dataType = "String"),
	})
	@RequestMapping(value = "/withdrawOrder", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult withdrawOrder(@RequestParam(value = "address", required = true) String address,
									@RequestParam(value = "memo", required = true) String memo,
									@RequestParam(value = "amt", required = true) String amt,
									@RequestParam(value = "symbol", required = true) String symbol,
									@RequestParam(value = "valid_code", required = true) String valid_code,
									@RequestParam(value = "valid_type", required = true) String valid_type) {
		boolean googleAuth = false;
		boolean mobileAuth = false;
		JsonResult jsonResult = new JsonResult();
		if (!StringUtils.isEmpty(valid_type) && !StringUtils.isEmpty(valid_code)) {
			if (VALID_TYPE_GOOGLE.equals(valid_type)) {
				googleAuth = userService.checkGoogleAuth(UserUtils.getUser().getGoogleCode(), valid_code);
			} else {
				// 需要实现哦...
				mobileAuth = false;
			}
		}
//        googleAuth = true;
		if (googleAuth || mobileAuth) {
			jsonResult = coinWithdrawService.saveWithdrawOrder(address, memo, amt, symbol);
		} else {
			jsonResult.setMsg(Language.L("msg_code_error"));
			jsonResult.setSuccess(false);
			jsonResult.setCode(JsonResult.ResultCode.FAILE);
		}
		return jsonResult;
	}

	/**
	 * 充值提现历史订单 提现数获取
	 *
	 * @return
	 */
	@ApiOperation(value = "体现历史")
	@RequestMapping(value = "/withdrawInfo",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult withdrawInfo(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page,@RequestParam(name = "pageSize",required = false,defaultValue = "10") Integer pageSize) {
		User user = UserUtils.getUser();
		JsonResult jsonResult = new JsonResult();
		try {
			PageResult pageResult = coinWithdrawService.findList(page,pageSize,user.getId());
			jsonResult.setData(pageResult);
			jsonResult.setSuccess(true);
			jsonResult.setMsg(Language.L_Success(""));
			jsonResult.setCode(Constants.SUCCESS);
		} catch (Exception e) {
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setMsg(Language.L_Failed(""));
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}

	/**
	 * 充值提现历史订单 充值数获取
	 *
	 * @return
	 */
	@ApiOperation(value = "当前用户充值历史")
	@RequestMapping(value = "/rechargeInfo",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult rechargeInfo(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page,@RequestParam(name = "pageSize",required = false,defaultValue = "10") Integer pageSize) {
		User user = UserUtils.getUser();
		JsonResult jsonResult = new JsonResult();
		try {
			PageResult pageResult = coinRechargeService.findList(page,pageSize,user.getId());
			jsonResult.setData(pageResult);
			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}
	
	/**
	 * 查询交易挖矿流水
	 * 
	 * @param dateStr
	 * @param page
	 * @param pageSize
	 */
	@ResponseBody
	@ApiOperation(value = "挖矿交易流水")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "页码", name = "page", paramType = "query", dataType = "String", required = false,defaultValue = "1"),
			@ApiImplicitParam(value = "分页数", name = "pageSize", paramType = "query", dataType = "String", required = false,defaultValue = "10"),
			@ApiImplicitParam(value = "查询的日期", name = "date", paramType = "query", dataType = "String", required = false,defaultValue = "new Date()"),
	})
	@RequestMapping(value = { "/tradeMiningRecord" }, method = RequestMethod.POST)
	public JsonResult tradeMiningRecord(@RequestParam(name = "date") String dateStr,@RequestParam(name = "page",required = false,defaultValue = "1") Integer page,@RequestParam(name = "pageSize",required = false,defaultValue = "10") Integer pageSize) {
		JsonResult jsonResult = new JsonResult(false);
		CustomerAccountRecord param = new CustomerAccountRecord();
		param.setType(CustomerAccountRecord.TRADE_MINING);
		param.setCustomerId(UserUtils.getUser().getId());
		Date date ;
		if(dateStr!=null){
			date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
		}else{
			date =  new Date();
		}
		jsonResult.setSuccess(true);
		jsonResult.setData(customerAccountRecordService.findPage(param,  page, pageSize, date));
		// 封装查询参数
		return jsonResult;
	}
	

}
