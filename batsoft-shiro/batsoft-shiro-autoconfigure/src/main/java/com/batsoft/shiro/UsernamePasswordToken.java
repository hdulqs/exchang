package com.batsoft.shiro;

import lombok.Data;

import javax.servlet.ServletRequest;

/**
 * 用户和密码（包含验证码）令牌类
 * @author Think
 * @version 2013-5-19
 */
@Data
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;
	private boolean mobileLogin;
	private ServletRequest request;
	
	public UsernamePasswordToken() {
		super();
	}

	public UsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha, boolean mobileLogin,ServletRequest request) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.mobileLogin = mobileLogin;
		this.request=request;
	}

	
}