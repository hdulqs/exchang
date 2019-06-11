package com.batsoft.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.batsoft.common.base.BaseController;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.exception.ValidException;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.shiro.UsernamePasswordToken;
import com.batsoft.utils.StringUtils;
import com.google.common.collect.Maps;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/")
@RestController
public class LoginController extends BaseController {

    @Resource
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "登陆")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", dataType = "String",value = "手机号",required = true),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String",value = "密码",required = true),
            @ApiImplicitParam(paramType = "query", name = "validate", dataType = "String",value = "图形验证码",required = true),
            @ApiImplicitParam(paramType = "query", name = "rememberMe", dataType = "Boolean",value = "记住我",required = true),
            @ApiImplicitParam(paramType = "query", name = "redirectUrl", dataType = "String",value = "跳转url",required = false)
    })
    @ResponseBody
    public JsonResult login(@RequestParam(value = "userName") String userName,
                            @RequestParam(value = "password") String password,
                            @RequestParam(value = "validate") String captcha,
                            @RequestParam(value = "rememberMe") Boolean rememberMe,
                            @RequestParam(value = "redirectUrl",required = false) String redirectUrl,
                            HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult(false);
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> map = Maps.newHashMap();
        String tokenId = "";
        if( redirectUrl == null) {
            if(subject.getSession().getAttribute("redirectUrl") != null) {
                redirectUrl = subject.getSession().getAttribute("redirectUrl").toString();
            }
        }
        UserVo userVo = userService.findUserInfo(userName);
        if(userVo == null){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L("msg_user_no_exists"));
            return  jsonResult;
        }
        if(userVo.getStatus() == UserVo.STATUS1.intValue()){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L("msg_user_forbidden"));
            return  jsonResult;
        }
        String shopToken;
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password.toCharArray(), rememberMe, null, captcha, false, request);
            subject.login(token);
            subject.getSession().setAttribute("userName", userName);
            tokenId = subject.getSession().getId().toString();
            Collection<Object> objects = subject.getSession().getAttributeKeys();

            Iterator iterator = objects.iterator();
            while (iterator.hasNext()){
                Object object = iterator.next();
                System.out.println(object+":"+subject.getSession().getAttribute(object));
            }
            //设置缓存
            UserUtils.getByUserName(userName);
            userService.updatUserLogin(request);
            // 针对商城登录token
            shopToken = UUID.randomUUID().toString().replaceAll("-","");
            Map value = new HashMap();
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
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
            return  jsonResult;
        } catch (ValidException e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
            return  jsonResult;
        } catch (IncorrectCredentialsException e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L("msg_login_error"));
            return jsonResult;
        } catch (Exception e) {
            //其他错误，比如锁定，如果想单独处理请单独catch 处理
            jsonResult.setSuccess(false);
            jsonResult.setMsg( e.getMessage());
            return jsonResult;
        }
        jsonResult.setSuccess(true);
        map.put("shopToken", shopToken);
        map.put("token", tokenId);
        map.put("userId",userVo.getId());
        map.put("mobile",userVo.getUserMobile());
        System.out.println(tokenId);
        if(!StringUtils.isNull(redirectUrl)){
            map.put("successURL",redirectUrl);
        }
        jsonResult.setData(map);
        jsonResult.setMsg(Language.L("msg_login_success"));
        return jsonResult;
    }
}
