/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月10日 下午3:47:04
 */
package com.batsoft.core.web.controller;

import com.batsoft.utils.WebUtils;
import com.github.pagehelper.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * <p> TODO</p>
 * @author: Bat Admin
 * @Date :          2017年1月10日 下午3:47:04 
 */
@Controller("changeLanguage")
@RequestMapping("/")
public class ChangeLanguage {

    @RequestMapping("/lan/{lan}")
    public String changeLanauage(HttpServletRequest request, HttpServletResponse response, @PathVariable  String lan) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        String[] l=lan.split("-");
        if ( l.length>1) {
            localeResolver.setLocale(request, response, new Locale(l[0], l[1]));
        } else {
            localeResolver.setLocale(request, response, new Locale(l[0]));
        }

       /* CookieLocaleResolver test=new CookieLocaleResolver();
        LocaleContext context=test.resolveLocaleContext(request);
        context.getLocale().getLanguage();
        localeResolver.resolveLocale(request).getLanguage();*/
        return "redirect:"+ WebUtils.returnUrl(request);
    }

}
