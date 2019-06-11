package com.batsoft.core.common.i18n;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Language Convet Initialize Listener
 * 
 * @author simon
 */
@WebListener
public class LanguageConvetInitializeListener extends ApplicationObjectSupport implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// web应用上下文对象
		WebApplicationContext bean = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		
		// 初始化语言转换器
		LocaleMessageSourceService messageService = (LocaleMessageSourceService) bean.getBean("localeMessageSourceService");
		LanguageConvetSingleton.getInstance().initialize(messageService);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

}
