/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月9日 下午12:00:31
 */
package com.batsoft.shiro.filter;


import com.batsoft.shiro.utils.JsonResult;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年1月9日 下午12:00:31 
 */
public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    public  HttpServletRequest request;

    @Override  
      public Class getObjectType() {  
        return MySpringShiroFilter.class;  
      } 

    @Override
    protected AbstractShiroFilter createInstance() throws Exception {

    	org.apache.shiro.mgt.SecurityManager securityManager = (org.apache.shiro.mgt.SecurityManager) getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        }

        if (!(securityManager instanceof WebSecurityManager)) {
            String msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        }
        FilterChainManager manager = createFilterChainManager();

        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);

        return new MySpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }

    private static final class MySpringShiroFilter extends AbstractShiroFilter {  

        protected MySpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {  
          super();  
          if (webSecurityManager == null) {  
            throw new IllegalArgumentException("WebSecurityManager property cannot be null.");  
          }  
          setSecurityManager(webSecurityManager);  
          if (resolver != null) {  
            setFilterChainResolver(resolver);  
          }  
        }  

        @Override  
        protected ServletResponse wrapServletResponse(HttpServletResponse orig, ShiroHttpServletRequest request) {  
          return new MyShiroHttpServletResponse(orig, getServletContext(), request);
        }  
    }


}
