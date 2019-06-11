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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p> TODO </p>
 * @author:         Bat Admin
 * @Date :          2016年12月10日 下午3:01:08 
 */
@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class FrontApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(FrontApplication.class);
	
    /**
     * <p> main 支持 run运行</p>
     * @author:         Bat Admin
     * @param:    @param args
     * @param:    @throws Exception
     * @return:   void 
     * @Date :          2016年12月14日 下午12:25:04
     * @throws:
     */
    public static void main(String[] args) throws Exception {
    	new SpringApplicationBuilder(FrontApplication.class).web(true).run(args);
    }


}