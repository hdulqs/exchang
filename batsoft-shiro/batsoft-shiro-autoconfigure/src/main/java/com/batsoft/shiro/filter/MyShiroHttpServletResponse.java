/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月9日 上午11:51:14
 */
package com.batsoft.shiro.filter;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年1月9日 上午11:51:14 
 */

public class MyShiroHttpServletResponse extends ShiroHttpServletResponse{


	/**
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param wrapped
	 * @param:    @param context
	 * @param:    @param request
	 */
	public MyShiroHttpServletResponse(HttpServletResponse wrapped,
			ServletContext context, ShiroHttpServletRequest request) {
		super(wrapped, context, request);
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	 protected String toEncoded(String url, String sessionId) {

	        if ((url == null) || (sessionId == null)) {
			return (url);
		}

	        String path = url;
	        String query = "";
	        String anchor = "";
	        int question = url.indexOf('?');
	        if (question >= 0) {
	            path = url.substring(0, question);
	            query = url.substring(question);
	        }
	        int pound = path.indexOf('#');
	        if (pound >= 0) {
	            anchor = path.substring(pound);
	            path = path.substring(0, pound);
	        }
	        StringBuilder sb = new StringBuilder(path);
	        if (sb.length() > 0) { // session id param can't be first.
	           /* sb.append(";");
	            sb.append("sessionId");
	            sb.append("=");
	            sb.append(sessionId);*/
	        }
	        sb.append(anchor);
	        sb.append(query);
	        return (sb.toString());

	    }

}
