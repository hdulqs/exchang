package com.batsoft.core.conf;

import com.batsoft.core.security.CsrfInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 2017/9/19.
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

    /**
     * web 安全攔截器
     * @return
     */
    @Bean
    public CsrfInterceptor getCsrfInterceptor() {
        return new CsrfInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       // InterceptorRegistration addInterceptor = registry.addInterceptor(getCsrfInterceptor());

        // 排除配置
        //addInterceptor.excludePathPatterns("/error");

        // 拦截配置
      //  addInterceptor.addPathPatterns("/member/**/save**");
        //addInterceptor.addPathPatterns("/member/**/add**");
       // addInterceptor.addPathPatterns("/member/**/update**");
       // addInterceptor.addPathPatterns("/member/**/edit**");
    }

}