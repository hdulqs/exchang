package com.batsoft.core.security;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.utils.WebUtils;
import com.sun.mail.iap.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class CsrfInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory
            .getLogger(CsrfInterceptor.class);

    RedisService redisService = (RedisService) SpringContextUtil.getBean("redisService");

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String CsrfToken = CsrfTokenManager.getTokenFromRequest(request);
            if (CsrfToken == null
                    || !CsrfToken.equals(redisService.get(Constants.CACHE_SECURITY_KEY+ ApplicationConfigure.sessionIdCookie+":"+CsrfTokenManager.CSRF_TOKEN_FOR_SESSION_ATTR_NAME+"-"+request.getSession().getId().toString()))) {
                if(!WebUtils.isAjax(request)) {
                    String reLoginUrl = "/login?backurl=" + URLEncoder.encode(getCurrentUrl(request), "utf-8");
                    response.sendRedirect(reLoginUrl);
                }else {
                    JsonResult jsonResult=new JsonResult();
                    jsonResult.setCode(Constants.FAILED);
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg("页面过期请刷新页面");
                    WebUtils.sendJson(response,jsonResult);
                }
                return false;
            }
        }else if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            response.setStatus(200);
            return true;
        }
        return true;
    }

    private String getCurrentUrl(HttpServletRequest request) {
        String currentUrl = request.getRequestURL().toString();
        if (!StringUtils.isEmpty(request.getQueryString())) {
            currentUrl += "?" + request.getQueryString();
        }

        return currentUrl;
    }
}