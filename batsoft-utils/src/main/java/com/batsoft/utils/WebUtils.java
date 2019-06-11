package com.batsoft.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/5/1.
 */
public class WebUtils {
    private final static String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" };
    public static void sendJson( HttpServletResponse response,Object o) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSON.toJSONString(o));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 是否是ajax請求
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && "XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否移动端
     * @param request
     * @return
     */
    public static boolean isMobile(HttpServletRequest request) {
        String ua=request.getHeader("User-Agent");
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : agent) {
                    if (ua.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 跳转页面到上一级
     * @param request
     * @return
     */
    public static String returnUrl(HttpServletRequest request) {
        String url  = request.getHeader("Referer");
        if(StringUtils.isEmpty(url)){
            return "/";
        }else{
            return url;
        }
    }
}
