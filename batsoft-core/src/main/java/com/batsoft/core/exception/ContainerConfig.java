/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月12日 下午8:48:40
 */
package com.batsoft.core.exception;


import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * <p> 异常处理容器</p>
 * @author:         Bat Admin
 * @Date :          2016年12月12日 下午8:48:40 
 */
@Configuration
public class ContainerConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new MyCustomizer();
    }

    private static class MyCustomizer implements EmbeddedServletContainerCustomizer {
        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
            container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
            container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/401"));
            container.addErrorPages(new ErrorPage(java.lang.Throwable.class,"/error"));
           
            
            
        }
    }
}
