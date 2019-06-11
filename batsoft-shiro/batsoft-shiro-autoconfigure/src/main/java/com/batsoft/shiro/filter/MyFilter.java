package com.batsoft.shiro.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
public class MyFilter implements Filter {
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
