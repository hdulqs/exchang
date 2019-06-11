package com.batsoft;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ApiConfiguration extends WebMvcConfigurerAdapter {
    /**
     * 设置默认首页
     *
     * @param registry viewControllerRegistry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/api").setViewName("redirect:/swagger-ui.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

}
