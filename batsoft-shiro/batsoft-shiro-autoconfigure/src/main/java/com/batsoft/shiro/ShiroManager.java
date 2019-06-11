/*
 *    Copyright 2010-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.batsoft.shiro;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

/**
 * Shiro Config Manager.
 * 
 * @author Boya
 */
@EnableConfigurationProperties(ShiroSessionProperties.class)
public class ShiroManager {
	
	@Autowired
	private ShiroSessionProperties shiroSessionProperties;

	/**
	 * 保证实现了Shiro内部lifecycle函数的bean执行
	 */
	@Bean(name = "lifecycleBeanPostProcessor")
	@ConditionalOnMissingBean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean(name = "defaultAdvisorAutoProxyCreator")
	@ConditionalOnMissingBean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;

	}

	/**
	 * (基于内存的)用户授权信息Cache
	 */
	@Bean(name="cacheManager")
	public EhCacheManager getEhCacheManager() {
		EhCacheManager em = new EhCacheManager();
		em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return em;
	}
	@Bean(name = "securityManager")
	@ConditionalOnMissingBean
	public DefaultSecurityManager securityManager(CacheManager cacheManager) {
		DefaultSecurityManager sm = new DefaultWebSecurityManager();
		sm.setCacheManager(cacheManager);
		return sm;
	}



	@Bean
	@ConditionalOnMissingBean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager);
		return new AuthorizationAttributeSourceAdvisor();
	}
}
