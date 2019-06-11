package com.batsoft.shiro.filter;


import com.batsoft.shiro.UsernamePasswordToken;
import com.batsoft.shiro.utils.JsonResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This guy is lazy, nothing left.
 *
 * @author Bat Admin
 */
public class FormSignInFilter extends FormAuthenticationFilter {

    //对应页面 name 属性
    public static final String DEFAULT_CAPTCHA_PARAM = "validCode";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    public String getCaptchaParam() {

        return captchaParam;

    }

    protected String getCaptcha(ServletRequest request) {

        return WebUtils.getCleanParam(request, getCaptchaParam());

    }

    @Override
    protected AuthenticationToken createToken(
            ServletRequest request, ServletResponse response) {

        String username = getUsername(request);

        String password = getPassword(request);

        String captcha = getCaptcha(request);

        boolean rememberMe = isRememberMe(request);

        String host = getHost(request);

        return new UsernamePasswordToken(username,
                password.toCharArray(), rememberMe, host, captcha, false,request);

    }

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Subject subject = getSubject(request, response);

        JsonResult ret=new JsonResult();
        if (subject.getPrincipal() == null) {
            if (com.batsoft.utils.WebUtils.isAjax(httpRequest)) {
                ret.setCode("401.1");
                ret.setMsg("您尚未登录或登录超时！");
                ret.setData(getLoginUrl());
                ret.setSuccess(false);
                com.batsoft.utils.WebUtils.sendJson(httpResponse, ret);
            } else {
                //返回401 状态错误 js 根据状态处理1
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                //跳转页面
                //super.redirectToLogin(request, response);
            }
        } else {
            if (com.batsoft.utils.WebUtils.isAjax(httpRequest)) {
                ret.setCode("401.2");
                ret.setMsg("您没有足够的权限执行该操作!");
                ret.setSuccess(false);
                com.batsoft.utils.WebUtils.sendJson(httpResponse, ret);
            } else {
               /* String unauthorizedUrl = WebUtils.getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(401);
                }*/

                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }
}
