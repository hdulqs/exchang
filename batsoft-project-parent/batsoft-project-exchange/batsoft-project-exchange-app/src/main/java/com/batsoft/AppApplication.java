package com.batsoft;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * App项目程序入口
 * 
 * @author simon
 */
@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class AppApplication {
    /**
     * Run Application
     * 
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	new SpringApplicationBuilder(AppApplication.class).web(true).run(args);
    }
    
}