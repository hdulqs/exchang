/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月10日 下午3:01:08
 */
package com.batsoft.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p> TODO </p>
 * @author:         Bat Admin
 * @Date :          2016年12月10日 下午3:01:08 
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication   {
    
    private static final Logger logger = LoggerFactory.getLogger(EurekaApplication.class);

    /**
     * 
     * <p> main 支持 run运行</p>
     * @author:         Bat Admin
     * @param:    @param args
     * @param:    @throws Exception
     * @return: void 
     * @Date :          2016年12月14日 下午12:25:04    
     * @throws:
     */
    public static void main(String[] args) throws Exception {
    	new SpringApplicationBuilder(EurekaApplication.class).web(true).run(args);
    }
   
}