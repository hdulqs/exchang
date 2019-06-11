package com.batsoft.common.beans.bo;

import com.batsoft.common.base.BaseBP;

/**
 * 首页
 * 
 * @author simon
 */
public class HomeBP extends BaseBP {

	private static final long serialVersionUID = -2935183415967321516L;
	
	// 手机号码
	private String mobile;
	
	// 登录密码
	private String password;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
