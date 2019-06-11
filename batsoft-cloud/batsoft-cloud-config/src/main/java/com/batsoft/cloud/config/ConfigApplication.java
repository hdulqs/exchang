/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月16日 下午4:28:50
 */
package com.batsoft.cloud.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <p> TODO</p>
 * @author:         Bat Admin 1
 * @Date :          2016年12月16日 下午4:28:50 
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {
	 /**
     * <p> main 支持 run运行</p>
     * 
     * @author:         Bat Admin
     * @param:    @param args
     * @param:    @throws Exception
     * @return: void 
     * @Date :          2016年12月14日 下午12:25:04
     * @throws:
     */
    public static void main(String[] args) throws Exception {
    	new SpringApplicationBuilder(ConfigApplication.class).web(true).run(args);
    }
}
