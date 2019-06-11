package com.batsoft.core.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;

/**
 * 让静态变量保存到Spring ApplicationContext
 * 可在任何代码任何地方任何时候中取出ApplicaitonContext
 */
@Component
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    /***
     * 根据一个bean的id获取配置文件中相应的bean
     */
    public static Object getBean(String beanId) throws BeansException {
        if (applicationContext.containsBean(beanId)) {
            return  applicationContext.getBean(beanId);
        }
        return null;
    }

    /***
     * 根据一个bean的类型获取配置文件中相应的bean
     */
    public static <T> T getBeanByClass(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static ApplicationContext getApplicationContext() {
        return SpringContextUtil.applicationContext;
    }

    /**
     * 获取项目根路径
     * @return
     */
    public static String getPath() {
        return ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }
    /**
     * 获取资源文件数组
     * @param path
     * @return
     * @throws IOException
     */
    public static Resource[] findResourceArr (String path) {
        Resource[] resources=null;
        try{
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + path);
        }catch (Exception e){
            log.error("findResourceArr err:"+e.getMessage());
        }
        return resources;
    }
}