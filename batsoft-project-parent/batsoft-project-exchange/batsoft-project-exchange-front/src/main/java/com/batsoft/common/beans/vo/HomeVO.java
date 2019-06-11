package com.batsoft.common.beans.vo;

import com.batsoft.common.base.BaseVO;

/**
 * 首页响应
 * 
 * @author simon
 */
public class HomeVO extends BaseVO {

	private static final long serialVersionUID = 7496826553560909073L;
	
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
