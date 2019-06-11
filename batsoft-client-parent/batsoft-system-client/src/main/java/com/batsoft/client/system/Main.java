/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月1日 下午11:18:55
 */
package com.batsoft.client.system;


import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.batsoft.client.system.model.MiningJobResult;
import com.batsoft.client.system.util.CommonUtil;
import com.batsoft.client.system.util.IpAdrressUtil;
import com.batsoft.core.annotation.RegisterAuth;
import com.batsoft.core.annotation.SmsAuth;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.common.validator.MobileCodeToken;
import com.batsoft.core.common.validator.ValidCodeToken;
import com.batsoft.core.exception.ValidException;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.util.MessageUtil;
import com.batsoft.service.module.member.service.PromotionAwardService;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.shiro.UsernamePasswordToken;
import com.batsoft.third.module.sms.MessageService;
import com.batsoft.utils.IpUtil;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.WebUtils;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * <p> TODO</p>
 *
 * @author: Bat Admin v
 * @Date :          2017年1月1日 下午11:18:55
 */
@Api(description = "main 主controller")
@Controller("main")
@Slf4j
@RequestMapping("/")
public class Main extends GenericController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private AppConfigService appConfigService;
	@Autowired
	private UserService userService;
	@Autowired
	private CoinPairService coinPairService;
	@Value("${shiro.success-url}")
	private String successURL = "";
	@Value("${shiro.h5-success-url}")
	private String h5SuccessURL = "";
	@Autowired
	private TemplateProperties templateProperties;
	@Autowired
	private RedisService redisService;
	@Qualifier(value = "remoteRestTemplate")
	private RestTemplate remoteRestTemplate = new RestTemplate();
	@Autowired
	private CustomerAccountService customerAccountService;
	@Autowired
	private RabbitMqSender rabbitMqSender;
	@Autowired
	private CustomerAccountRecordService customerAccountRecordService;

	@Autowired
	private PromotionAwardService promotionAwardService;

	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public String register(Model model,HttpServletRequest request) {
		if(request.getParameter("prometorCode") !=null ){
			model.addAttribute("prometorCode",request.getParameter("prometorCode"));
		}
		return templateProperties.getRegister();
	}


	@RequestMapping(value = "/helpcenter",method = RequestMethod.GET)
	public String helpcenter(Model model) {
		return templateProperties.getHelpcenter();
	}

	@RequestMapping(value = "/privacys",method = RequestMethod.GET)
	public String privacy(Model model) {
		return templateProperties.getPrivacys();
	}

	@RequestMapping(value = "/handlingfee",method = RequestMethod.GET)
	public String getHandlingfee(Model model) {
		return templateProperties.getHandlingfee();
	}

	@RequestMapping(value = "/conditions",method = RequestMethod.GET)
	public String getConditions(Model model) {
		return templateProperties.getConditions();
	}

	@RequestMapping(value = "/agreement",method = RequestMethod.GET)
	public String agreement(Model model) {
		return templateProperties.getAgreement();
	}


	@RequestMapping(value = "/deprecated",method = RequestMethod.GET)
	public String deprecated(Model model) {
		return templateProperties.getDeprecated();
	}

	@RequestMapping(value = "/introduce",method = RequestMethod.GET)
	public String commercial(Model model) {
		return templateProperties.getCommercial();
	}

	/**
	 * 忘记密码
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/forgot",method = RequestMethod.GET)
	public String forgot(Model model) {
		return templateProperties.getForgot();
	}

	/**
	 * 跳转至登陆页面
	 *
	 * @return
	 *
	 */
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String login(HttpServletRequest request,Model model) {
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute("redirectUrl", WebUtils.returnUrl(request));
		if (subject.isAuthenticated()) {
			return "redirect:" + WebUtils.returnUrl(request);
		}
		String redirectUrl = request.getParameter("redirectUrl");
		if(redirectUrl!=null&& !"".equals(redirectUrl)){
			model.addAttribute("redirectUrl",redirectUrl);
		}
		return templateProperties.getLogin();
	}

	/**
	 * H5 注册页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/h5/register",method = RequestMethod.GET)
	public String h5Register(Model model) {
		return templateProperties.getH5Register();
	}

	/**
	 * 结束页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/IGO_JT",method = RequestMethod.GET)
	public String description(Model model) {
		return templateProperties.getDescription();
	}


	@RequestMapping(value = "/SHOPPING_GUIDE",method = RequestMethod.GET)
	public String shopping_guide(Model model) {
		return templateProperties.getShopping_guide();
	}


	/**
	 *
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/BT",method = RequestMethod.GET)
	public String advertisement(Model model) {
		return templateProperties.getAdvertisement();
	}

	/**
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/referral",method = RequestMethod.GET)
	public String referraladvertisement(Model model) {
		return templateProperties.getReferral();
	}
	/**
	 * 跳转至H5登陆页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/h5/login",method = RequestMethod.GET)
	public String h5Login(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute("redirectUrl", WebUtils.returnUrl(request));
		if (subject.isAuthenticated()) {
			return "redirect:" + WebUtils.returnUrl(request);
		}
		return templateProperties.getH5Login();
	}

	@RequestMapping(value = "",method = RequestMethod.GET)
	public String index(Model model) {
		String tradeCoins = coinPairService.findMapTradeCoins();
		String digits = coinPairService.findCoinPairDigit();
		model.addAttribute("tradeCoins", tradeCoins);
		model.addAttribute("digits", digits);
		return templateProperties.getIndex();
	}

	@ApiOperation(value = "登陆")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userName", dataType = "String",value = "手机号",required = true),
			@ApiImplicitParam(paramType = "query", name = "password", dataType = "String",value = "密码",required = true),
			@ApiImplicitParam(paramType = "query", name = "validate", dataType = "String",value = "图形验证码",required = true),
			@ApiImplicitParam(paramType = "query", name = "rememberMe", dataType = "Boolean",value = "记住我",required = true),
			@ApiImplicitParam(paramType = "query", name = "redirectUrl", dataType = "String",value = "跳转url",required = false)
	})
	@RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginCheck(@RequestParam(value = "userName") String userName,
	                                      @RequestParam(value = "password") String password,
	                                      @RequestParam(value = "validate",required = false) String captcha,
	                                      @RequestParam(value = "rememberMe") Boolean rememberMe,
	                                      @RequestParam(value = "redirectUrl",required = false) String redirectUrl,
	                                      HttpServletRequest request,
										  HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		Map<String, Object> map = Maps.newHashMap();
		if( redirectUrl == null) {
			if(subject.getSession().getAttribute("redirectUrl") != null) {
				redirectUrl = subject.getSession().getAttribute("redirectUrl").toString();
			}
		}
		UserVo userVo = userService.findUserInfo(userName);
		if(userVo.getStatus() == UserVo.STATUS1.intValue()){
			map.put("success", false);
			map.put("msg", Language.L("msg_user_forbidden"));
			return  map;
		}
		String shopToken;
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(userName, password.toCharArray(), rememberMe, null, captcha, false, request);
			subject.login(token);
			subject.getSession().setAttribute("userName", userName);
			//设置缓存
			UserUtils.getByUserName(userName);
			userService.updatUserLogin(request);
			// 针对商城登录token
			shopToken = UUID.randomUUID().toString().replaceAll("-","");
			Map value = new HashMap(1);
			value.put("phone",userName);
			//读取商城的redis token，如果有那么从redis删除
			Object tokenKey =  redisService.getObject("userToken:"+userName);
			if(tokenKey!=null){
				redisService.delRedisByKey("userToken:"+userName);
			}
			redisService.setValueWithExpiresForShop(shopToken, value, 7*24*60*60);
			//设置存储的用户商城那边的token的到redis
			redisService.setValueWithExpiresForShop("userToken:"+userName,shopToken,7*24*60*60);
		} catch (UnknownAccountException e) {
			map.put("success", false);
			map.put("msg", e.getMessage());
			return map;
		} catch (ValidException e) {
			map.put("success", false);
			map.put("msg", e.getMessage());
			return map;
		} catch (IncorrectCredentialsException e) {
			map.put("success", false);
			map.put("msg", Language.L("msg_login_error"));
			return map;
		} catch (Exception e) {
			//其他错误，比如锁定，如果想单独处理请单独catch 处理
			map.put("success", false);
			map.put("msg", e.getMessage());
			e.printStackTrace();
			return map;
		}
		map.put("success", true);
		map.put("shopToken", shopToken);
		map.put("msg", Language.L("msg_login_success"));
		if (StringUtils.isEmpty(redirectUrl)) {
			map.put("successURL",  successURL);
		} else {
			map.put("successURL",  redirectUrl);
		}
		return map;
	}


	/**
	 * 用户登出操作
	 *
	 * @return
	 */
	@ApiOperation(value = "用户退出",notes = "退出登陆")
	@RequestMapping(value = "/logouts", method = RequestMethod.GET)
	public String logout() {
		UserVo appUser = UserUtils.getUser();
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		//读取商城的redis token，如果有那么从redis删除
		Object tokenKey =  redisService.getObject("userToken:"+appUser.getUserName());
		if(tokenKey!=null){
			redisService.delRedisByKey("userToken:"+appUser.getUserName());
			redisService.delRedisByKey(tokenKey.toString());
		}
		return  "redirect:/login";
	}


	/**
	 * 基于手机号的用户注册
	 *
	 * @param userName   用户名 =手机号
	 * @param password   密码
	 * @param mobileCode 短信验证码
	 * @param areaCode   地区号 0086
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RegisterAuth
	@RequestMapping(value = "/reg",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户注册")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userName", dataType = "String",value = "手机号",required = true),
			@ApiImplicitParam(paramType = "query", name = "password", dataType = "String",value = "密码",required = true),
			@ApiImplicitParam(paramType = "query", name = "mobileCode", dataType = "String",value = "手机验证码",required = true),
			@ApiImplicitParam(paramType = "query", name = "areaCode", dataType = "String",value = "地区码",required = false),
			@ApiImplicitParam(paramType = "query", name = "tradePwd", dataType = "String",value = "交易密码",required = true),
			@ApiImplicitParam(paramType = "query", name = "promoterCode", dataType = "String",value = "推广人码",required = false)
	})
    public JsonResult reg(@RequestParam(value = "userName") String userName,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "mobileCode") String mobileCode,
                          @RequestParam(value = "areaCode",required = false) String areaCode,
                          @RequestParam(value = "tradePwd") String tradePwd,
                          @RequestParam(value = "promoterCode",required = false) String promoterCode,
                          HttpServletRequest request)  {
        JsonResult jsonResult = new JsonResult(true);
        Boolean hasUser = userService.hasUser(userName);
        if(hasUser){
            return new JsonResult(Boolean.FALSE,Language.L("msg_user_exists"), JsonResult.ResultCode.FAILE);
        }

        jsonResult = userService.saveUserByMobile(userName, password, mobileCode, areaCode,tradePwd,promoterCode ,request);
        if(jsonResult.getSuccess()){
            try {
                String mallUrl = appConfigService.findValueByKey(AppConfig.MALLURL);
                String mallPort = appConfigService.findValueByKey(AppConfig.MALLPORT);
                String shopPrefix = mallUrl + mallPort;
                URI uri = IpAdrressUtil.stringParse2URI(shopPrefix + "/mall/backend/access/addCustomerUserFormBttmall");
                HttpHeaders headers = new HttpHeaders();
                MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
                headers.setContentType(type);
                JSONObject jsonObj = JSONObject.fromObject(jsonResult.getData());
                HttpEntity<String> req = new HttpEntity<String>(jsonObj.toString(),headers);
                ResponseEntity<String> remoteResponse =	remoteRestTemplate.exchange(uri, HttpMethod.POST,req,String.class);
                com.alibaba.fastjson.JSONObject resultJson = com.alibaba.fastjson.JSONObject.parseObject(remoteResponse.getBody());
                if (resultJson.isEmpty() || !Boolean.valueOf(resultJson.get("success").toString())) {
                    jsonResult.setMsg(Language.L_Failed("login.register_error_contact_customer_service"));
                    log.error("用户注册发送给商户端注册失败，用户id["+((User)jsonResult.getData()).getId()+"]");
                }
            } catch (Exception e){
                e.printStackTrace();
                jsonResult.setMsg(Language.L_Failed("login.register_error_contact_customer_service"));
				log.error("用户注册发送给商户端注册失败，用户id["+((User)jsonResult.getData()).getId()+"]",e);
            }
        }
        Map<String, String> data = new HashedMap();
        if (WebUtils.isMobile(request)) {
            data.put("loginUrl", "/login");
        } else {
            data.put("loginUrl", "/login");
        }
        jsonResult.setData(data);
        return jsonResult;
    }



    @ApiOperation(value = "/promoterCode",notes = "获取推荐码")
	@ApiImplicitParams(
			{
					@ApiImplicitParam( name ="mobile",paramType = "query",dataType = "String",required = false,value = "推广人手机号")
			})
	@RequestMapping(value = "/promoterCode",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getUserPromoterCode(@RequestParam(name = "mobile",required = false) String mobileStr){
		JsonResult jsonResult = new JsonResult(false);
		User user;
		if(mobileStr != null){
			user = userService.findUserInfo(mobileStr);
		}else {
	      	user = UserUtils.getUser();
		}
		if(user == null || user.getId() == null){
			jsonResult.setMsg(Language.L("msg_no_login"));
			jsonResult.setSuccess(false);
			return  jsonResult;
		}
		if(user.getPromoterCode() == null || StringUtils.isNull(user.getPromoterCode())){
			//生成自己的推广码
			char[] heads = new char[2];
			for (int i=0;i<heads.length;i++){
				int a = 'a'+(int)(Math.random()*26);
				heads[i] = (char) a;
			}
			String headcode = new String(heads).toUpperCase();
			String mobile = user.getUserMobile();
			if(mobile != null && mobile.length() > 4) {
				user.setPromoterCode(headcode.concat(mobile.substring(mobile.length() - 4, mobile.length())));
			}else{
				user.setPromoterCode(mobile);
			}
			userService.updateUser(user);
		}
		jsonResult.setSuccess(true);
		jsonResult.setData(user.getPromoterCode());
		return jsonResult;
	}

	@ApiOperation(value = "/promoterList",notes = "获取推荐列表")
	@RequestMapping(value = "/promoterList",method = RequestMethod.GET)
	@ApiImplicitParams(
			{
					@ApiImplicitParam( name ="mobile",paramType = "query",dataType = "String",required = false,value = "推广人手机号"),
					@ApiImplicitParam( name ="page",paramType = "query",dataType = "int",required = false,value = "页码 1开始"),
					@ApiImplicitParam( name ="pageSize",paramType = "query",dataType = "int",required = false,value = "页大小 默认10")
			})
	@ResponseBody
	public JsonResult getUserPromotionList(@RequestParam(name = "mobile",required = false) String mobileStr, @RequestParam(name = "page",required = false,defaultValue = "1") int page,
										   @RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize) {
		JsonResult jsonResult = new JsonResult(false);
		User user;
		if(mobileStr != null){
			user = userService.findUser(mobileStr);
		}else {
			user = UserUtils.getUser(false);
		}
		if(user == null){
			jsonResult.setSuccess(false);
			jsonResult.setMsg(Language.L("msg_user_no_exists"));
			return 	jsonResult;
		}
		jsonResult.setData(promotionAwardService.findPrototionAward(page,pageSize,user));
		jsonResult.setSuccess(true);
		return jsonResult;
	}


	/**
	 * 短信验证码发送
	 *
	 * @param validate
	 * @return
	 */
	@RequestMapping(value = "/sendAuthMessageCode",method = RequestMethod.POST)
	@ApiOperation(value = "短信验证码发送")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "areaCode", dataType = "String",value = "地区码",required = false),
			@ApiImplicitParam(paramType = "query", name = "validate", dataType = "String",value = "图形码",required = true)
	})
	@SmsAuth
	@ResponseBody
	public JsonResult sendCode(
							   @RequestParam(value = "areaCode",required = false) String areaCode,
							   @RequestParam(value = "validate", required = false) String validate,
							   HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		Map<String, Object> data = new HashedMap();
		UserVo userVo = UserUtils.getUser(false);
		if(userVo == null || userVo.getId() == null){
			jsonResult.setSuccess(false);
			jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
			jsonResult.setMsg(Language.L_Failed("msg_no_login"));
		}
		try {
			// 图形验证码校验
			ValidCodeToken validCodeToken = new ValidCodeToken(validate, false, request);
			validCodeToken.isValid();
			Map<String, Object> retData = new HashedMap();
			String codeTimeOut = appConfigService.findValueByKey(AppConfig.MOBILECODETIMEOUT);
			retData.put("codeTimeOut", codeTimeOut);
			String host = IpUtil.getIpAddr(request);
			JsonResult ret = messageService.sendCode(areaCode, userVo.getUserMobile(),host, data);
			jsonResult.setSuccess(ret.getSuccess());
			jsonResult.setMsg(ret.getMsg());
			jsonResult.setData(retData);
		} catch (ValidException e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg(e.getMessage());
		} catch (Exception e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg(e.getMessage());
		}
		return jsonResult;
	}

    /**
	 * 短信验证码发送
	 *
	 * @param mobile
	 * @param validate
	 * @return
	 */
	@RequestMapping(value = "/sendCode",method = RequestMethod.GET)
	@ApiOperation(value = "短信验证码发送")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String",value = "手机号",required = true),
			@ApiImplicitParam(paramType = "query", name = "areaCode", dataType = "String",value = "地区码",required = false),
			@ApiImplicitParam(paramType = "query", name = "validate", dataType = "String",value = "图形码",required = true)
	})
	@SmsAuth
	@ResponseBody
	public JsonResult sendCode(@RequestParam(value = "mobile", required = true) String mobile,
	                           @RequestParam(value = "areaCode",required = false) String areaCode,
	                           @RequestParam(value = "validate", required = false) String validate,
	                           HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		Map<String, Object> data = new HashedMap();

		try {
			// 图形验证码校验
			ValidCodeToken validCodeToken = new ValidCodeToken(validate, false, request);
			validCodeToken.isValid();
			Map<String, Object> retData = new HashedMap();
			String codeTimeOut = appConfigService.findValueByKey(AppConfig.MOBILECODETIMEOUT);
			retData.put("codeTimeOut", codeTimeOut);
			String host = IpUtil.getIpAddr(request);
			JsonResult ret = messageService.sendCode(areaCode, mobile,host, data);
//			jsonResult.setCode(ret.getCode());
			jsonResult.setSuccess(ret.getSuccess());
			jsonResult.setMsg(ret.getMsg());
			jsonResult.setData(retData);
		} catch (ValidException e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg(e.getMessage());
		} catch (Exception e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 忘记密码 - 修改密码
	 *
	 * @param userName 用户名 =手机号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "忘记密码")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userName", dataType = "String",value = "用户名",required = true),
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String",value = "验证码",required = true),
			@ApiImplicitParam(paramType = "query", name = "type", dataType = "String",value = "验证码类型",required = true),
			@ApiImplicitParam(paramType = "query", name = "password", dataType = "String",value = "新密码",required = true)
	})
	@RequestMapping(value = "/forgotPassword",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult forgotPassword(@RequestParam(value = "userName") String userName,
	                                 @RequestParam(value = "code") String code,
	                                 @RequestParam(value = "type") String type,
	                                 @RequestParam(value = "password") String password,
	                                 HttpServletRequest request) throws Exception {
		JsonResult jsonResult = new JsonResult();
		//通过用户名查询用户是否存在
		User user = userService.findUser(userName);
		if (user != null) {
			boolean googleAuth = false;
			boolean mobileAuth = false;
			if (!StringUtils.isEmpty(type) && !StringUtils.isEmpty(code)) {
				if (VALID_TYPE_GOOGLE.equals(type)) {
					if (StringUtils.isNotEmpty(user.getGoogleCode())) {
						googleAuth = userService.checkGoogleAuth(user.getGoogleCode(), code);
					} else {
						jsonResult.setSuccess(false);
						jsonResult.setCode(JsonResult.ResultCode.FAILE);
						jsonResult.setMsg(Language.L("google_unbound"));  //用户不存在
						return jsonResult;
					}
				} else {
					try {
						// 手机验证码校验
						MobileCodeToken mobileCodeToken = new MobileCodeToken(code, userName, false, request);
						mobileCodeToken.isValid();
						mobileAuth = true;
					}catch (Exception e){
						jsonResult.setSuccess(false);
						jsonResult.setMsg(e.getMessage());
						return jsonResult;
					}
				}
			}
			if (googleAuth || mobileAuth) {
				jsonResult = userService.addPassword(user, password);
			} else {
				jsonResult.setMsg(Language.L("msg_code_error"));
				jsonResult.setSuccess(false);
				jsonResult.setCode(JsonResult.ResultCode.FAILE);
			}
		} else {
			jsonResult.setSuccess(false);
			jsonResult.setCode(JsonResult.ResultCode.FAILE);
			jsonResult.setMsg(Language.L("msg_code_error"));  //用户不存在
		}

		Map<String, String> data = new HashedMap();
		if (WebUtils.isMobile(request)) {
			data.put("loginUrl", "/h5/login");
		} else {
			data.put("loginUrl", "/login");
		}
		jsonResult.setData(data);
		return jsonResult;
	}
	@ApiOperation(value = "查询用户账户信息")
	@ApiImplicitParam(paramType = "body", name = "param", dataType = "Map",value = "hash值包括用户token,phone",required = true)
	@RequestMapping(value = "/queryUserFinanceData",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult queryUserFinanceData(@RequestBody Map param){
		JsonResult result = new JsonResult();
		HashMap map = (HashMap) redisService.getShopObject(String.valueOf(param.get("token")));
		result.setSuccess(Boolean.TRUE);
		JsonResult chkMapNotNull = CommonUtil.chkMapNotNull(map,"Token Err","403");
		if (chkMapNotNull.getSuccess()) {
			return chkMapNotNull;
		}
		User user = userService.findUser(String.valueOf(map.get("phone")));
		JsonResult chkUserExist = CommonUtil.chkUserExist(user,"Token Err","403");
		if (chkUserExist.getSuccess()){
			return chkUserExist;
		}
		result.setData(customerAccountService.findCoinAccount(user.getId(),String.valueOf(param.get("coinSymbol"))));
		return result;
	}

	/**
	 * 挖矿的逻辑加账号余额和减冻结余额
	 * @return
	 */
	@ApiOperation(value = "购物挖矿的逻辑加账号余额和减冻结余额")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "param", dataType = "Map", value = "hash值包括data,账户挖矿数据", required = true)
	})
	@RequestMapping(value = "/modifyUserFinanceDataByMining",method = RequestMethod.POST)
	@ResponseBody
	public synchronized JsonResult  modifyUserFinanceDataByMining(@RequestBody Map param,HttpServletRequest request ) {
		//TODO  不安全接口
		System.out.println("com.batsoft.client.system.Main.modifyUserFinanceDataByMining:IP:"+IpUtil.getIpAddr(request));
		JsonResult result = new JsonResult();
		String ip = appConfigService.findValueByKey("Job_Service_Ip",false);
		String requstIp = IpUtil.getIpAddr(request);
		if(ip.contains(",")){
			List<String> ips = Arrays.asList(ip.split(","));
			if(!ips.contains(requstIp)){
				result.setSuccess(false);
				result.setMsg("访问无效");
				return result;
			}
		} else {
			if (!ip.equals(requstIp)) {
				result.setSuccess(false);
				result.setMsg("访问无效");
				return result;
			}
		}
		if(param.get("data") == null){
			result.setSuccess(false);
			result.setMsg("数据为空");
			return result;
		}
		String jsonData = "";
		jsonData = param.get("data").toString();
		System.out.println(" mining shop 挖矿接收数据:jsonData:"+jsonData);
		List<MiningJobResult> jobResults = JSON.parseArray(jsonData,MiningJobResult.class);
		for(MiningJobResult miningJobResult:jobResults) {
			UserVo user = userService.findUserInfo(miningJobResult.getPhone());
			CustomerAccountVo customerAccountVo = customerAccountService.findCoinAccount(user.getId(), miningJobResult.getSymbol());
			BigDecimal money = new BigDecimal(miningJobResult.getMiningBit()).setScale(5,BigDecimal.ROUND_HALF_UP);
			System.out.println(" mining shop 发送redis的数据 对象CustomerAccountVo："+customerAccountVo.toString());
			rabbitMqSender.toRedisAccount(
					MessageUtil.addCoinAvailable(customerAccountVo.getUserId(), customerAccountVo.getCoinCode(),
							new BigDecimal(miningJobResult.getMiningBit()).multiply(new BigDecimal("1"))));
			customerAccountRecordService.saveAccountRecord(
					CustomerAccountRecord.SHOPPING_MINING, customerAccountVo.getId(), customerAccountVo.getUserId(),
					customerAccountVo.getCoinCode(), String.valueOf(money));
		}
		result.setSuccess(true);
		return result;
	}

	@ApiOperation(value = "用户购物下单，修改用户账户信息")
	@ApiImplicitParam(paramType = "body", name = "param", dataType = "Map",value = "hash值包括用户token,phone,payPassword",required = true)
	@RequestMapping(value = "/modifyUserFinanceData",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult modifyUserFinanceData(@RequestBody Map param,HttpServletRequest request){
	    //TODO  不安全接口
		JsonResult result = new JsonResult();
		String requstIp = IpUtil.getIpAddr(request);
		String ip = appConfigService.findValueByKey("Shop_Service_Ip",false);
		System.out.println("com.batsoft.client.system.Main.modifyUserFinanceData:IP:"+IpUtil.getIpAddr(request));
		if(ip.contains(",")){
			List<String> ips = Arrays.asList(ip.split(","));
			if(!ips.contains(requstIp)){
				result.setSuccess(false);
				result.setMsg("访问无效");
				return result;
			}
		}else {
			if (!ip.equals(requstIp)) {
				result.setSuccess(false);
				result.setMsg("访问无效");
				return result;
			}
		}
		boolean mining = false;
		if(param.get("mining")!=null){
			mining = Boolean.valueOf(param.get("mining").toString());
		}else{
			throw new NullPointerException("mining parameter is null");
		}
		HashMap redisData = (HashMap) redisService.getShopObject(String.valueOf(param.get("token")));
		result.setSuccess(Boolean.TRUE);
		JsonResult chkMapNotNull = CommonUtil.chkMapNotNull(redisData,"Token Err","403");
		if (chkMapNotNull.getSuccess()) {
			return chkMapNotNull;
		}
		UserVo user = userService.findUserInfo(String.valueOf(redisData.get("phone")));
		JsonResult chkUserExist = CommonUtil.chkUserExist(user,"Token Err","403");
		if (chkUserExist.getSuccess()){
			return chkUserExist;
		}
		// 1.判断交易密码
		PasswordHelper passwordHelper = new PasswordHelper();
		String inputPwd = passwordHelper.encryString(String.valueOf(param.get("payPassword")),user.getSalt());
		String userTradePwd = StringUtils.isNotBlank(user.getTradePassword()) ? user.getTradePassword() : "";
		if(StringUtils.isEmpty(userTradePwd)){
			result.setSuccess(Boolean.FALSE);
			result.setCode(JsonResult.ResultCode.FAILE);
			result.setMsg("请先设置支付密码");
			return result;
		}
		if (!inputPwd.equals(userTradePwd)) {
			result.setSuccess(Boolean.FALSE);
			result.setCode(JsonResult.ResultCode.FAILE);
			result.setMsg("支付密码错误");
			return result;
		}
		// 2.判断余额
		CustomerAccountVo customerAccountVo = customerAccountService.findCoinAccount(user.getId(),String.valueOf(param.get("coinSymbol")));
		BigDecimal availableMoney = new BigDecimal(param.get("availableMoney").toString()).setScale(16);
		if (customerAccountVo != null && availableMoney.compareTo(customerAccountVo.getAvailable()) <= 0){
			rabbitMqSender.toRedisAccount(
					MessageUtil.addCoinAvailable(customerAccountVo.getUserId(), customerAccountVo.getCoinCode(),
							availableMoney.multiply(new BigDecimal("-1"))));
			if(mining) {
			}
			customerAccountRecordService.saveAccountRecord(
					CustomerAccountRecord.SHOPPING,customerAccountVo.getId(),customerAccountVo.getUserId(),
						customerAccountVo.getCoinCode(),String.valueOf(availableMoney.add(new BigDecimal(-1))));
		} else {
			result.setSuccess(Boolean.FALSE);
			result.setCode(JsonResult.ResultCode.FAILE);
			result.setMsg("余额不足");
			return result;
		}
		return result;
	}
}
