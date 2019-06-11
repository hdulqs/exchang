/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月1日 下午11:18:55
 */
package com.batsoft.app.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.batsoft.core.cache.RedisService;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.system.manage.AppUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.exception.ValidException;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.system.auth.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.shiro.UsernamePasswordToken;
import com.google.common.collect.Maps;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author: Bat Admin v
 * @Date : 2017年1月1日 下午11:18:55
 */
@Controller("main")
@RequestMapping("/")
public class Main extends GenericController {

	@Value("${shiro.success-url}")
	private String successURL = "";

	@Autowired
	private AppConfigService configService;

	/**
	 * 登陆类型--账号登陆 account
	 */
	private static final String LOGIN_TYPE_ACCOUNT = "account";
	/**
	 * 登陆类型--手机登陆 mobile
	 */
	private static final String LOGIN_TYPE_MOBILE = "mobile";

	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("name", "index");
		return "index";
	}
	@Autowired
	private RedisService redisService;

	@RequestMapping(value = "/loginCheck")
	@ResponseBody
	public Map<String, Object> loginCheck(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password, @RequestParam(value = "validCode") String validCode,
			@RequestParam(value = "rememberMe") Boolean rememberMe, HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		Map<String, Object> map = Maps.newHashMap();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password.toCharArray(), rememberMe, null, validCode, false, request);
		try {
			subject.login(token);
			subject.getSession().setAttribute("userName", userName);
		} catch (UnknownAccountException e) {
			map.put("success", false);
			map.put("type", LOGIN_TYPE_ACCOUNT);
			map.put("msg", "用户名/密码错误");
			return map;
		} catch (ValidException e) {
			map.put("success", false);
			map.put("type", LOGIN_TYPE_ACCOUNT);
			map.put("msg", e.getMessage());
			return map;
		} catch (IncorrectCredentialsException e) {
			map.put("success", false);
			map.put("type", LOGIN_TYPE_ACCOUNT);
			map.put("msg", "用户名/密码错误");
			return map;
		} catch (Exception e) {
			// 其他错误，比如锁定，如果想单独处理请单独catch 处理
			e.printStackTrace();
			map.put("success", false);
			map.put("type", LOGIN_TYPE_ACCOUNT);
			map.put("msg", "其他错误:" + e.getMessage());
			return map;
		}
		map.put("success", true);
		map.put("userName", userName);
		map.put("msg", "登陆成功");
		map.put("type", LOGIN_TYPE_ACCOUNT);
		map.put("successURL", successURL);
		map.put("permissions", UserUtils.getPermission());
		return map;
	}

	/**
	 * 用户登出操作
	 *
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult logout() {
		JsonResult ret = new JsonResult();
		AppUser user = UserUtils.getUser();
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		//读取商城的redis token，如果有那么从redis删除
		Object tokenKey =  redisService.getObject("userToken:"+ user.getUserName());
		if(tokenKey!=null){
			redisService.delRedisByKey(tokenKey.toString());
			redisService.delRedisByKey("userToken:"+user.getUserName());
		}
		ret.setCode(Constants.SUCCESS);
		ret.setMsg("退出成功！");
		ret.setSuccess(true);
		return ret;
	}

	@RequestMapping("/mq")
	@ResponseBody
	public JsonResult mq() throws Exception {
		JsonResult ret = new JsonResult();
		ret.setCode(JsonResult.ResultCode.SUCCESS);
		ret.setMsg("消息發送成功！");
		return ret;
	}

	@RequestMapping("/globInfo")
	@ResponseBody
	public JSONObject globInfo() {
		JSONObject data = new JSONObject();
		data.put("copyright", configService.findValueByKey(AppConfig.MANAGECOPYRIGHT));
		data.put("webSiteTitle", configService.findValueByKey(AppConfig.WEBSITTITLE));
		data.put("manageLogo", configService.findValueByKey(AppConfig.MANAGELOGO));
		data.put("fileHost", configService.findValueByKey(AppConfig.FILEHOST));
		return data;
	}

}
