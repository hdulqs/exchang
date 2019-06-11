package com.batsoft.client.system.aop;

import com.batsoft.core.model.SaveResult;
import com.batsoft.model.module.log.MemberLogin;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.log.service.MemberLoginService;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 用户登录切面日志
 * @author LouSir
 */
@Aspect
@Component
@Slf4j
public class MemberLogAop {

    @Autowired
    private MemberLoginService memberloginService;
    @Autowired
    private UserService userService;
    public String memberLoginId = null;

    /**
     * 对com.batsoft.app.system.Main loginCheck方法记录日志
     */
    @Pointcut("execution(* com.batsoft.client.system.Main.loginCheck(..))")
    public void webLog(){
        log.info("进入登录切面方法");
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        System.out.println( "进入doBefore切面");
        memberLoginId = null;
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String username = request.getParameter("userName");
        MemberLogin memberLogin = new MemberLogin();
        if(StringUtils.isNotEmpty(username)){
            User user = userService.findUser(username);
            if(user!=null){
                memberLogin.setUserId(user.getId());
            }
        }
        memberLogin.setLoginTime(new Date());
        try {
            memberLogin.setIp(IpUtil.getIpAddr(request));   //request.getRemoteAddr()
            memberLogin.setAddress("");
            //memberLogin.setAddress(IpUtil.getIpAdress(IpUtil.getIpAddr(request), "utf-8"));
        }catch (Exception e ){
            memberLogin.setAddress("网络异常");
            e.printStackTrace();
        }
        memberLogin.setUrl(request.getRequestURL().toString());
        memberLogin.setMethod(request.getMethod());
        memberLogin.setMediaType("");
        memberLogin.setRequestMethod(request.getMethod());
        memberLogin.setStatus(0);
        memberLogin.setParams(Arrays.toString(joinPoint.getArgs()));
        SaveResult result = memberloginService.save(memberLogin);
        if(result!=null){
            //返回log表ID after使用
            memberLoginId = result.getId();
        }
        // 记录下请求内容
//        log.info("URL1111 : " + request.getRequestURL().toString());
//        log.info("HTTP_METHOD 222 : " + request.getMethod());
//        log.info("IP : " + request.getRemoteAddr());
//        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret",pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable{
        //{msg=登陆成功, success=true, successURL=http://127.0.0.1:8011/}
        System.out.println("切面  after");
        // 处理完请求，返回内容
        log.info("RESPONSE : " + ret);
        MemberLogin memberLogin = memberloginService.get(memberLoginId);
        if(memberLogin!=null&&ret!=null){
            memberLogin.setCallbackParams(ret.toString());
            memberLogin.setCallbackNum(1);
            Map<String,Object> retMap = (Map<String, Object>) ret;
            if(retMap.containsKey("success")){
                if((boolean)retMap.get("success")){
                    memberLogin.setStatus(1);
                }else{
                    memberLogin.setStatus(1);
                }
            }
            memberLogin.setRemark(retMap.get("msg").toString());
            memberloginService.update(memberLogin);
        }
    }



}
