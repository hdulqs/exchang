package com.batsoft.shiro.filter;

import com.batsoft.shiro.utils.JsonResult;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/20.
 */


public class AjaxAuthorizationFilter extends RolesAuthorizationFilter {


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        System.out.print("AjaxAuthorizationFilter====================");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Subject subject = getSubject(request, response);

        JsonResult ret=new JsonResult();
        if (subject.getPrincipal() == null) {
            if (com.batsoft.utils.WebUtils.isAjax(httpRequest)) {
                ret.setMsg("您尚未登录或登录超时！");
                ret.setSuccess(false);
                com.batsoft.utils.WebUtils.sendJson(httpResponse, ret);
            } else {
                //返回401 状态错误 js 根据状态处理
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                //跳转页面
               // saveRequestAndRedirectToLogin(request, response);
            }
        } else {
            if (com.batsoft.utils.WebUtils.isAjax(httpRequest)) {
                ret.setMsg("您没有足够的权限执行该操作!");
                ret.setSuccess(false);
                com.batsoft.utils.WebUtils.sendJson(httpResponse, ret);
            } else {
                String unauthorizedUrl = getUnauthorizedUrl();
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            }
        }
        return false;
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {

        System.out.println("isAccessAllowed======="+((HttpServletRequest)request).getRequestURL());
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            // no roles specified, so nothing to check - allow access.
            return true;
        }

        Set<String> roles = CollectionUtils.asSet(rolesArray);
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}
