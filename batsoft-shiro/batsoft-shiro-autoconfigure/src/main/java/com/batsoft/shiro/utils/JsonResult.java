
package com.batsoft.shiro.utils;

import java.io.Serializable;

/**
 * 封装json结果集
 * 
 * 
 * 
 * @author Bat Admin
 *
 */
public class JsonResult implements Serializable{

	private Boolean success = false;// 返回是否成功
	private String msg = "";// 返回信息
	private Object data = null;// 返回其他对象信息
	private String code = "";//提示代码

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
