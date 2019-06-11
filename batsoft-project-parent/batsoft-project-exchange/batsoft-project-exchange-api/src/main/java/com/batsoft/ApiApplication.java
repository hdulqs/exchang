package com.batsoft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 项目启动类
 * 
 * @author simon
 */
@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class ApiApplication {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ApiApplication.class);
	
	/**
	 * 启动类
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
    	
    	new SpringApplicationBuilder(ApiApplication.class).web(true).run(args);
    }
    
}