package com.batsoft.staticassets; /**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月10日 下午3:01:08
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * <p> TODO </p>
 * @author:         Bat Admin
 * @Date :          2016年12月10日 下午3:01:0812
 */

@SpringBootApplication

public class StaticApplication  {
    
    private static final Logger logger = LoggerFactory.getLogger(StaticApplication.class);

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
    
    /**
     * <p> main 支持 run运行 2 </p>
     * @author:         Bat Admin
     * @param:    @param args
     * @param:    @throws Exception
     * @return: void 
     * @Date :          2016年12月14日 下午12:25:04    
     * @throws:
     */
    public static void main(String[] args) throws Exception {
    	new SpringApplicationBuilder(StaticApplication.class).web(true).run(args);
    }
   
}