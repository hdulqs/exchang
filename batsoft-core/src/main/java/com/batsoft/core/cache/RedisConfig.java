package com.batsoft.core.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;


import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Order(1)
@Configuration
public class RedisConfig {

	@Value("${spring.redis.database}")
	int database = 0;
	
	@Value("${spring.redis.host}")
	String host = "127.0.0.1";
	
	@Value("${spring.redis.port}")
	int port = 6379;
	
	@Value("${spring.redis.password}")
	String password = "";
	
	@Value("${spring.redis.pool.max-active}")
	int maxactive = 8;
	
	@Value("${spring.redis.pool.max-wait}")
	int maxwait = -1;
	
	@Value("${spring.redis.pool.max-idle}")
	int maxidle = 8;
	
	@Value("${spring.redis.pool.min-idle}")
	int minidle = 0;
	
	@Value("${spring.redis.timeout}")
	int timeout = 0;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(minidle);
		config.setMaxIdle(maxidle);
		config.setMaxWaitMillis(maxwait);
		factory.setDatabase(database);
		factory.setHostName(host);
		factory.setPort(port);
		factory.setPassword(password);
		factory.setTimeout(60000);
		factory.setPoolConfig(config);
		return factory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new RedisObjectSerializer());
		return template;
	}

	@Bean
	public JedisPool jedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(50000);
		config.setMaxIdle(maxidle);
		config.setMaxWaitMillis(1000 * 100);
		config.setTestOnBorrow(true);
		JedisPool jedisPool = new JedisPool(config, host, port, 100000, password);
		return jedisPool;
	}
	
}
