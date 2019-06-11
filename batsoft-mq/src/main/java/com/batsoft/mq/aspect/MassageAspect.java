package com.batsoft.mq.aspect;

import com.batsoft.mq.annotation.MessageCache;
import com.batsoft.mq.pojo.CacheMessage;
import com.batsoft.mq.pojo.MetaMessage;
import com.batsoft.mq.util.CacheCorrelationData;
import com.batsoft.mq.util.MessageCacheUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 消息发送前放入缓存
 *
 * @author Bat Admin
 */
@Order(1)
@Aspect
@Component
public class MassageAspect {

    private static final Logger logger = LoggerFactory.getLogger(MassageAspect.class);

    /**
     * 把消息重新封装到MetaMessage中，MetaMessage包含了原交换机和路由键信息，便于重发
     * 把封装后的MetaMessage放入缓存
     *
     * @param point
     * @throws Throwable
     */
    @Around("execution(* com.batsoft.client..*.send*Message(..))")
    public void aroundSendMessage(ProceedingJoinPoint point) throws Throwable {
        Signature sig = point.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("方法签名不合法");
        }
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method targetMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

        if (targetMethod.isAnnotationPresent(MessageCache.class)) {
            Object[] args = point.getArgs();
            //所有的原始队列，路由键，缓存信息都通过自定义的注释进行处理
            MessageCache cacheAnnotation = targetMethod.getAnnotation(MessageCache.class);
            String cacheName = (String) getSpel(point.getTarget(), targetMethod, args, cacheAnnotation.cacheName());
            String cacheKey = (String) getSpel(point.getTarget(), targetMethod, args, cacheAnnotation.cacheKey());
            ArgIndexMessage message = getMessage(point.getTarget(), targetMethod, args, cacheAnnotation.messageArgMapper());
            String exchange = (String) getSpel(point.getTarget(), targetMethod, args, cacheAnnotation.exchange());
            String routingKey = (String) getSpel(point.getTarget(), targetMethod, args, cacheAnnotation.routingKey());
            MetaMessage metaMessage = new MetaMessage();
            metaMessage.setPayload(message.getPayload());
            metaMessage.setExchange(exchange);
            metaMessage.setRoutingKey(routingKey);
            //加入缓存
            MessageCacheUtil.add(cacheName, cacheKey, metaMessage);
            //替换方法message参数为加入缓存名的Message
            CacheMessage cacheMessage = new CacheMessage();
            CacheCorrelationData correlationData = new CacheCorrelationData(cacheKey, cacheName);
            cacheMessage.setCacheCorrelationData(correlationData);
            cacheMessage.setPayload(message.getPayload());
            args[message.getIndex()] = cacheMessage;
            point.proceed();
        } else {
            logger.info(point.getTarget().getClass().getName() + "," + targetMethod.getName() + "没有使用缓存，在无法找到交换机时会丢失消息！");
        }
    }
	
	/*
	@After("execution(* com.wlf.demo.bussiness..*Recv(..)) && !execution(* com.wlf.demo.bussiness..DeadRecv(..))")
	public void afterConsumeMessage(JoinPoint point) throws Exception{
		Signature sig = point.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("��ע��ֻ�����ڷ���");
        }
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method targetMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        if(targetMethod.isAnnotationPresent(MessageCache.class)){
        	MessageCache cacheAnnotation=targetMethod.getAnnotation(MessageCache.class);
        	String cacheName=cacheAnnotation.value();
        	String bindKey=cacheAnnotation.bindKey();
        	String cacheKey=getCacheKey(targetMethod,bindKey);
        	MessageCacheUtil.remove(cacheName, cacheKey);
        }else{
        	System.out.println(point.getTarget().getClass().getName()+","+targetMethod.getName()+"û��ʹ�û��棬����Ϣ��ʧ�ķ��գ�");
        }
	}
	*/

    /**
     * 解析自定义缓存注释的设置
     * ${field.xxx}从切入点所在类的成员变量中获取ֵ
     * ${arg.xxx}从切入点所在方法的参数中获取ֵ
     *
     * @param target
     * @param method
     * @param args
     * @param value
     * @return
     * @throws Exception
     */
    private Object getSpel(Object target, Method method, Object[] args, String value) throws Exception {
        Object rs = value;
        if (value.matches("^\\$\\{[a-zA-Z0-9\\.]+\\}")) {
            String routingKeySpel = value.split("\\$\\{|\\}")[1];
            String[] spels = routingKeySpel.split("\\.");
            if (spels[0].equals("field")) {
                Field field = target.getClass().getDeclaredField(spels[1]);
                field.setAccessible(true);
                rs = field.get(target);
            } else if (spels[0].equals("arg")) {
                String argName = spels[1];
                ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                for (int i = 0; i < parameterNames.length; i++) {
                    if (parameterNames[i].equals(argName)) {
                        rs = args[i];
                    }
                }
            }
        }
        return rs;
    }

    private ArgIndexMessage getMessage(Object target, Method method, Object[] args, String value) throws Exception {
        ArgIndexMessage argIndexMessage = new ArgIndexMessage();
        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(value)) {
                argIndexMessage.setIndex(i);
                argIndexMessage.setPayload(args[i]);
            }
        }
        return argIndexMessage;
    }

    class ArgIndexMessage {
        private int index;
        private Object payload;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }
    }

}
