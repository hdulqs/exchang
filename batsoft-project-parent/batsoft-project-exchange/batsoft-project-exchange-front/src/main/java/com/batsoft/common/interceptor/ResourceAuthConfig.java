package com.batsoft.common.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 鉴权拦截器
 * 
 * @author simon
 */
@Configuration
public class ResourceAuthConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		// 注册鉴权拦截器
        registry.addInterceptor(new RegisterAuthInterceptor()).addPathPatterns("/**");
        
        // 短信鉴权拦截器
        registry.addInterceptor(new SmsAuthInterceptor()).addPathPatterns("/**");
    }
	
}
