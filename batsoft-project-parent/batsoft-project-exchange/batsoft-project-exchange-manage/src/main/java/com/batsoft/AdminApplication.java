/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月10日 下午3:01:08
 */
package com.batsoft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <p> TODO </p> 333
 * @author:         Bat Admin
 * @Date :          2016年12月10日 下午3:01:08 
 */
@EnableDiscoveryClient //该注解能激活Eureka中的DiscoveryClient实现，这样才能实现Controller中对服务信息的输出。
@SpringBootApplication
@ServletComponentScan
@EnableFeignClients
public class AdminApplication  {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminApplication.class);

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("PUT", "DELETE","OPTIONS","GET","POST")
                        .allowedHeaders("*")
                        .exposedHeaders("access-control-allow-headers",
                                "access-control-allow-methods",
                                "access-control-allow-origin",
                                "access-control-max-age",
                                "X-Frame-Options")
                        .allowCredentials(false).maxAge(3600);
            }
        };

    }

    /**
     * 
     * <p> main 支持 run运行 </p>
     * @author:         Bat Admin
     * @param:    @param args
     * @param:    @throws Exception
     * @return: void 
     * @Date :          2016年12月14日 下午12:25:04    
     * @throws:
     */
    public static void main(String[] args) throws Exception {
    	new SpringApplicationBuilder(AdminApplication.class).web(true).run(args);
    }

   
}