/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.common.beans.vo.CoinBalanceVO;
import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.common.util.ModuleAssert;
import com.batsoft.common.util.result.ResultData;
import com.batsoft.core.annotation.CheckLogin;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.exchange.service.CoinRechargeService;
import com.batsoft.service.module.exchange.service.CoinWithdrawService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.service.EntrustHistoryService;
import com.batsoft.service.module.exchange.service.EntrustInfoService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.DateUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>UserAccountController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "交易用户管理")
@Controller("ex_userAccountController")
@RequestMapping("/api/ex/member/account")
public class UserAccountController extends GenericController {

	@Autowired
	private CustomerAccountService customerAccountService;

	@Autowired
	private CoinWithdrawService coinWithdrawService;

	@Autowired
	private CoinRechargeService coinRechargeService;


	@Autowired
	private EntrustIngService entrustIngService;

	@Autowired
	private EntrustHistoryService entrustHistoryService;

	@Autowired
	private EntrustInfoService entrustInfoService;

	/**
	 * 委托管理数据查询
	 *
	 * @return
	 */
	@ApiOperation(value = "当前委托")
	@RequestMapping(value = "/entrustList",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult<PageResult> entrustList(HttpServletRequest request) {
		JsonResult<PageResult> resultData = new JsonResult<>();
		PageResult pageResult = entrustIngService.findPageByUserId(request);
		resultData.setSuccess(Boolean.TRUE);
		resultData.setData(pageResult);
		resultData.setCode(Constants.SUCCESS);
		return resultData;
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
	@RequestMapping(value = "/history_entrustList",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult<PageResult> history_entrustList(HttpServletRequest request) {
		JsonResult resultData = new JsonResult();
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
			date =   DateUtils.parse(dateStr, DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
		}else{
			date =  new Date();
		}
		String userId = UserUtils.getUser().getId();
		PageResult pageResult = entrustHistoryService.findPageByUserId(request, userId, page, pageSize, date);
		resultData.setSuccess(Boolean.TRUE);
		resultData.setCode(Constants.SUCCESS);
		resultData.setData(pageResult);
		return resultData;
	}

	/**
	 * 历史成交管理查询
	 *
	 * @return
	 */
	@RequestMapping(value = "/history_tradeList",method = RequestMethod.GET)
	@ApiOperation(value = "历史成交")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "页码", name = "page", paramType = "query", dataType = "String", required = false,defaultValue = "1"),
			@ApiImplicitParam(value = "分页数", name = "pageSize", paramType = "query", dataType = "String", required = false,defaultValue = "10"),
			@ApiImplicitParam(value = "查询的日期", name = "date", paramType = "query", dataType = "String", required = false,defaultValue = "new Date()"),
	})
	@ResponseBody
	public JsonResult<PageResult> history_tradeList(HttpServletRequest request) {
		JsonResult resultData=new JsonResult();
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
			date =   DateUtils.parse(dateStr, DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
		}else{
			date =  new Date();
		}
		String userId = UserUtils.getUser().getId();
		PageResult pageResult = entrustInfoService.findPageByUserId(request, userId, page, pageSize, date);
		resultData.setSuccess(Boolean.TRUE);
		resultData.setCode(Constants.SUCCESS);
		resultData.setData(pageResult);
		return resultData;
	}


	/**
	 * 获取具体币种充值数据
	 *
	 * @return
	 */
	/*@ApiOperation(value = "账户对应的币种的余额")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "交易对", name = "coin_code", paramType = "path", dataType = "String")
	})
	@RequestMapping(value = "/findCoinAccount/{coinCode}",method = RequestMethod.GET)
	@ResponseBody
	public CustomerAccountVo findCoinAccount(@PathVariable String coinCode) {
		return customerAccountService.findCoinAccount(UserUtils.getUser().getId(), coinCode);
	}*/

	
	/**
	 * 获取具体币种提币数据
	 *
	 * @return
	 */
	/*@ApiOperation(value = "账户提币初始化")
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
		data.put("address", withdrawAddressService.findAddresList(coinCode));
		data.put("openGoogleAuth", userVo.getOpenGoogleAuth());
		return data;
	}*/


	/**
	 * 提现记录订单 提现数获取
	 *
	 * @return
	 */
	@ApiOperation(value = "提现记录")
	@RequestMapping(value = "/withdrawInfo",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult<PageResult> withdrawInfo(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page, @RequestParam(name = "pageSize",required = false,defaultValue = "10") Integer pageSize) {

		User user = UserUtils.getUser();
		JsonResult jsonResult = new JsonResult();
		PageResult pageResult = coinWithdrawService.findList(page,pageSize,user.getId());
		jsonResult.setData(pageResult);
		jsonResult.setSuccess(Boolean.TRUE);
		jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setMsg(Language.L_Success(""));
		return jsonResult;
	}

	/**
	 * 充值提现历史订单 充值数获取
	 *
	 * @return
	 */
	@ApiOperation(value = "当前用户充值记录")
	@RequestMapping(value = "/rechargeInfo",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult rechargeInfo(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page, @RequestParam(name = "pageSize",required = false,defaultValue = "10") Integer pageSize) {
		JsonResult jsonResult = new JsonResult();
		User user = UserUtils.getUser();
		PageResult pageResult = coinRechargeService.findList(page,pageSize,user.getId());
		jsonResult.setData(pageResult);
		jsonResult.setCode(Constants.SUCCESS);
		jsonResult.setSuccess(Boolean.TRUE);
		return jsonResult;
	}
	
	/**
	 * 查询某个币种的可用数量
	 *
	 * @param coinCode
	 * @return
	 */
	@ApiOperation(value = "查询某个币种的可用数量")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "货币代码", name = "coinCode", paramType = "path", dataType = "String")
	})
	@CheckLogin
	@ResponseBody
	@RequestMapping(value = "/balance/{coinCode}",method = RequestMethod.GET)
	public String balance(@PathVariable String coinCode, ResultData<CoinBalanceVO> result) {
		UserVo user = UserUtils.getUser();
		ModuleAssert.notNull(user, ModuleMessageEnum.NO_LOGIN);
		
		CoinBalanceVO resultVo = result.getData(CoinBalanceVO.class);
		CustomerAccountVo account = customerAccountService.findCoinAccount(user.getId(), coinCode);
		BigDecimal balance =  account.getAvailable();
		resultVo.setBalance(balance.setScale(3, BigDecimal.ROUND_DOWN));
		resultVo.setCoin(coinCode);
		return result.toJson();
	}

}
