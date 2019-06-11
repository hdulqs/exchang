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

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.batsoft.shiro.filter.FormSignInFilter;
import com.batsoft.shiro.filter.MyShiroFilterFactoryBean;
import com.batsoft.shiro.filter.ShiroFilterCustomizer;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.CipherService;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import javax.servlet.Filter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({ShiroProperties.class, ShiroSessionProperties.class, ShiroSignInProperties.class,
        ShiroCookieRememberMeProperties.class, ShiroCookieIdProperties.class})
@Import(ShiroManager.class)
public class ShiroAutoConfiguration {
    protected static final long MILLIS_PER_SECOND = 1000;
    @Autowired
    private ShiroProperties properties;
    @Autowired
    private ShiroSignInProperties signInProperties;
    @Autowired
    private ShiroSessionProperties shiroSessionProperties;
    @Autowired
    private ShiroCookieRememberMeProperties shiroCookieProperties;
    @Autowired
    private ShiroCookieIdProperties shiroCookieIdProperties;
    @Autowired(required = false)
    private CipherService cipherService;

    @Autowired(required = false)
    private Serializer<PrincipalCollection> serializer;

    @Autowired(required = false)
    private Collection<SessionListener> listeners;

    @Autowired(required = false)
    private ShiroFilterCustomizer shiroFilterCustomizer;

    @Bean(name = "mySessionDAO")
    public SessionDAO MySessionDAO() {

        Class<?> sessionDaoClass = properties.getSessionDaoClass();
        SessionDAO mySessionDAO = (SessionDAO) BeanUtils.instantiate(sessionDaoClass);
        //	mySessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return mySessionDAO;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie(shiroCookieIdProperties.getName());
       // sessionIdCookie.setName(shiroCookieIdProperties.getName());
         sessionIdCookie.setMaxAge(shiroCookieIdProperties.getMaxAge());
        sessionIdCookie.setHttpOnly(shiroCookieProperties.isHttpOnly());
        sessionIdCookie.setPath("/");
        sessionIdCookie.setDomain("");
        return sessionIdCookie;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie rememberMeCookie = new SimpleCookie(shiroCookieProperties.getName());
        rememberMeCookie.setMaxAge(shiroCookieProperties.getMaxAge());
        rememberMeCookie.setValue(shiroCookieProperties.getValue());
        rememberMeCookie.setVersion(shiroCookieProperties.getVersion());
        rememberMeCookie.setHttpOnly(shiroCookieProperties.isHttpOnly());
        rememberMeCookie.setSecure(shiroCookieProperties.isSecure());
        rememberMeCookie.setPath("/");
        rememberMeCookie.setDomain("");
        return rememberMeCookie;
    }

    @Bean(name = "rememberMeManager")
    @ConditionalOnMissingBean(RememberMeManager.class)
    public RememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie);
        cookieRememberMeManager.setCipherService(cipherService);
        if (shiroCookieProperties.getCipherKey() != null) {
            cookieRememberMeManager.setCipherKey(shiroCookieProperties.getCipherKey().getBytes());
        } else {
            if (shiroCookieProperties.getEncryptionCipherKey() != null) {
                cookieRememberMeManager.setEncryptionCipherKey(shiroCookieProperties.getEncryptionCipherKey().getBytes());
            }
            if (shiroCookieProperties.getDecryptionCipherKey() != null) {
                cookieRememberMeManager.setDecryptionCipherKey(shiroCookieProperties.getDecryptionCipherKey().getBytes());
            }
        }
        //cookieRememberMeManager.setSerializer(serializer);
        return cookieRememberMeManager;
    }

    @Bean(name = "sessionManager")
    @DependsOn(value = {"cacheManager", "mySessionDAO", "sessionIdCookie"})
    public DefaultWebSessionManager sessionManager(CacheManager cacheManager, SessionDAO mySessionDAO, SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(cacheManager);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionDAO(mySessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
       // listeners = new HashSet<SessionListener>();
       // listeners.add(new MySessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setGlobalSessionTimeout(shiroSessionProperties.getGlobalSessionTimeout() * MILLIS_PER_SECOND);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        return sessionManager;
    }

    /**
     * 密码校验
     *
     * @param cacheManager
     * @return
     */
    @Bean(name = "credentialsMatcher")
    @DependsOn("cacheManager")
    @ConditionalOnMissingBean
    public CredentialsMatcher credentialsMatcher(CacheManager cacheManager) {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
        credentialsMatcher.setHashAlgorithmName(properties.getHashAlgorithmName());
        credentialsMatcher.setHashIterations(properties.getHashIterations());
        credentialsMatcher.setStoredCredentialsHexEncoded(properties.isStoredCredentialsHexEncoded());
        credentialsMatcher.setRetryMax(properties.getRetryMax());
        return credentialsMatcher;
    }

    /**
     * 登录过滤器
     *
     * @return
     */
    public FormSignInFilter formSignInFilter() {
        FormSignInFilter filter = new FormSignInFilter();
        filter.setLoginUrl(properties.getLoginUrl());
        filter.setSuccessUrl(properties.getSuccessUrl());
        filter.setUsernameParam(signInProperties.getUserParam());
        filter.setPasswordParam(signInProperties.getPasswordParam());
        filter.setRememberMeParam(signInProperties.getRememberMeParam());
        return filter;
    }

    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    public Realm realm(CredentialsMatcher credentialsMatcher, CacheManager cacheManager) {
        Class<?> realmClass = properties.getRealmClass();
        Realm realm = (Realm) BeanUtils.instantiate(realmClass);
        if (realm instanceof AuthenticatingRealm) {
            ((AuthenticatingRealm) realm).setCredentialsMatcher(credentialsMatcher);
            ((AuthenticatingRealm) realm).setCacheManager(cacheManager);
        }
        return realm;
    }

    @Bean(name = "shiroFilter")
    @DependsOn(value = {"securityManager"})
    @ConditionalOnMissingBean
    public MyShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultSecurityManager securityManager, Realm realm, DefaultWebSessionManager sessionManager, RememberMeManager rememberMeManager) {
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rememberMeManager);
        MyShiroFilterFactoryBean shiroFilter = new MyShiroFilterFactoryBean();

        Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
        filterMap.put("authc", formSignInFilter());

        Map<String, Filter> filterClasses = instantiateFilterClasses(properties.getFilters());
        if (filterClasses != null) {
            filterMap.putAll(filterClasses);
        }

        if (shiroFilterCustomizer != null) {
            filterMap = shiroFilterCustomizer.customize(filterMap);
        }

        shiroFilter.setFilters(filterMap);




        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl(properties.getLoginUrl());
        shiroFilter.setSuccessUrl(properties.getSuccessUrl());
        shiroFilter.setUnauthorizedUrl(properties.getUnauthorizedUrl());
        shiroFilter.setFilterChainDefinitionMap(properties.getFilterChainDefinitions());
        return shiroFilter;
    }


    private Map<String, Filter> instantiateFilterClasses(Map<String, Class<? extends Filter>> filters) {
        Map<String, Filter> filterMap = null;
        if (filters != null) {
            filterMap = new LinkedHashMap<String, Filter>();
            for (String name : filters.keySet()) {
                Class<? extends Filter> clazz = filters.get(name);
                Filter f = BeanUtils.instantiate(clazz);
                filterMap.put(name, f);
            }
        }
        return filterMap;
    }
}
