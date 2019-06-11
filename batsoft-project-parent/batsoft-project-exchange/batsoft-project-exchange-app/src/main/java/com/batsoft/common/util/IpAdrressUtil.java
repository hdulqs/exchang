package com.batsoft.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class IpAdrressUtil {

    public static String getIpAddress(HttpServletRequest request) {
             String ip = request.getHeader("x-forwarded-for");
              if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                  ip = request.getHeader("Proxy-Client-IP");
                  }
              if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                   }
              if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                       ip = request.getHeader("HTTP_CLIENT_IP");
                  }
             if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
             if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                  }
            return ip;
         }
    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    /**
     * parseURI
     * @param url
     * @return uri
     */
    public static URI stringParse2URI(String url){
        UriComponents b = null;
        if (StringUtils.isNoneEmpty(url)) {
            b = UriComponentsBuilder.fromUriString(url).build();
        }
        return b.toUri();
    }
}
