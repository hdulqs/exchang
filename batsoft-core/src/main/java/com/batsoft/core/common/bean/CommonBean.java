/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月9日 下午9:20:12
 */
package com.batsoft.core.common.bean;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.validation.Validator;
import java.util.Locale;

/**
 * <p> TODO</p>
 * @author: Bat Admin
 * @Date :          2017年1月9日 下午9:20:12 
 */
@Configuration
public class CommonBean {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "validator")
    public Validator validator() {
        Validator v = new LocalValidatorFactoryBean();
        return v;
    }

    @Bean(name = "localeResolver")

    public LocaleResolver localeResolver() {

        CookieLocaleResolver slr = new CookieLocaleResolver();

        //设置默认区域,

        slr.setDefaultLocale(Locale.CHINA);

        slr.setCookieMaxAge(3600);//设置cookie有效期.
        slr.setCookieName("lang");



        return slr;

    }


}
