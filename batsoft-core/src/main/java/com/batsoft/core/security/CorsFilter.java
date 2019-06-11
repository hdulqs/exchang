package com.batsoft.core.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;


public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers","Content-Type, x-requested-with, X-Custom-Header");
        if(request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}
}
