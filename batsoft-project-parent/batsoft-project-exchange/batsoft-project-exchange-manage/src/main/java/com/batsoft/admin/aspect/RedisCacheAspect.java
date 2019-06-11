/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月11日 上午8:51:45
 */
package com.batsoft.admin.aspect;


import com.batsoft.model.module.system.cache.AppCache;
import com.batsoft.service.module.system.service.cache.AppCacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2016年12月11日 上午8:51:45 
 */
@Slf4j
@Aspect
@Component
public class RedisCacheAspect {

    @Autowired
    private AppCacheService appCacheService;
    @Pointcut("execution(public * com.batsoft.core.cache.RedisService.set*(..))")
    public void redisCache(){}

    @AfterReturning(returning = "ret", pointcut = "redisCache()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        // 记录下请求内容
        Object[] ARGS=joinPoint.getArgs();
        String cacheKey=ARGS[0].toString();
        String cacheValue=ARGS[1].toString();
        boolean valueIsString=ARGS[1] instanceof  String;
        int cacheTime=Integer.valueOf(ARGS[2].toString());

        // 只保存永久缓存
        if(cacheTime==AppCache.CACHETYPE0){
        AppCache appCache=new AppCache();
        appCache.setCacheKey(cacheKey);
        appCache.setCacheValue(cacheValue);
        appCache.setCacheTime(cacheTime);
            if(valueIsString){
                appCache.setCacheValueType(AppCache.CACHEVALUETYPE0);
            }else{
                appCache.setCacheValueType(AppCache.CACHEVALUETYPE1);
            }
            appCache.setCacheType(AppCache.CACHETYPE0);
            appCacheService.saveOrUpdateCache(appCache);
        }


    }
}
