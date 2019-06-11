package com.batsoft.core.cache;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.batsoft.core.cache.redis.RedisCacheManager;

import redis.clients.jedis.JedisPool;

/**
 * Redis Handler Util Initialize 
 * 
 * @author simon
 */
@WebListener
public class RedisSignletonInitializeListener extends ApplicationObjectSupport implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// web应用上下文对象
		WebApplicationContext bean = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		
		// Redis分布式缓存
		RedisCacheManager redisCacheManager = (RedisCacheManager) bean.getBean("redisCacheManager");
		RedisCacheSignleton.getInstance().initialize(redisCacheManager);
		
		// Jedis client
		JedisPool jedisPool = (JedisPool) bean.getBean("jedisPool");
		JedisDataSourceSignleton.getInstance().initialize(jedisPool);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
