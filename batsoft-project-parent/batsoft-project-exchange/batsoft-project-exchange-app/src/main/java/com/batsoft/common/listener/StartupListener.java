package com.batsoft.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * init Data Listener
 * 
 * @author simon
 */
@WebListener
public class StartupListener extends ApplicationObjectSupport implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		@SuppressWarnings("unused")
		WebApplicationContext bean = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}
	
}
