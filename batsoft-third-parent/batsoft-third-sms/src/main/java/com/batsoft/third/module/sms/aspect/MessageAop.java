package com.batsoft.third.module.sms.aspect;

import com.batsoft.core.common.JsonResult;
import com.batsoft.model.module.log.MessageSend;
import com.batsoft.service.module.log.service.MessageSendService;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;


/**
 * 用户登录切面日志
 *
 * @author LouSir
 */
@Aspect
@Component
@Slf4j
public class MessageAop {

    @Autowired
    private MessageSendService messageSendService;

    /**
     * 对com.batsoft.third.module.sms.SendService send的请求及回调进行切面
     */
    @Pointcut("execution(* com.batsoft.third.module.sms.SendService.send(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        log.info("进入MessageAop.doBefore");
    }

    @AfterReturning(pointcut="webLog()",returning="returnValue")
    public void doAfterReturning(JoinPoint point, Object returnValue) {
        /*System.out.println("@AfterReturning：模拟日志记录功能...");
        System.out.println("@AfterReturning：目标方法为：" +
                point.getSignature().getDeclaringTypeName() +
                "." + point.getSignature().getName());
        System.out.println("@AfterReturning：参数为：" +
                Arrays.toString(point.getArgs()));
        System.out.println("@AfterReturning：返回值为：" + returnValue);
        System.out.println("@AfterReturning：被织入的目标对象为：" + point.getTarget());*/
        try {
            //保存短信日志管理
            Object[] objects = point.getArgs();
            String areaCode = "";
            String mobile = "";
            String datas = "";
            String tplCode = "";
            if(objects.length==4){
                areaCode = objects[0]==null?"":objects[0].toString();
                mobile = objects[1]==null?"":objects[1].toString();
                datas = objects[2]==null?"":objects[2].toString();
                tplCode = objects[3]==null?"":objects[3].toString();
            }
            MessageSend messageSend = new MessageSend();
            messageSend.setCellphone(areaCode+mobile);
            messageSend.setSendTime(new Date());
            messageSend.setSmsContent(datas+"|"+tplCode);
            String providerL = point.getSignature().getDeclaringTypeName();
            String provider = providerL!=null?providerL.substring(providerL.lastIndexOf(".")+1,providerL.length()-1):"";
            messageSend.setSmsProvider(provider);
            JsonResult jsonResult = (JsonResult)returnValue;
            if(jsonResult!=null){
                if(StringUtils.isNotEmpty(jsonResult.getCode())){
                    if("0".equals(jsonResult.getCode())){
                        messageSend.setStatus(1);
                    }else {
                        messageSend.setStatus(0);
                    }
                }else{
                    messageSend.setStatus(0);
                }
                messageSend.setMsg(jsonResult.getMsg());
            }
            messageSendService.save(messageSend);
        }catch (Exception e){
            log.info("-----------短信切面保存报错---------");
            e.printStackTrace();
        }
    }
}
