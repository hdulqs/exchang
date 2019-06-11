/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Yuan Zhicheng
 * @version:      V1.0 
 * @Date:        2015�?�?6�?上午11:04:39
 */
package com.batsoft.blockchain.common;

import java.io.Serializable;


public class WalletResult implements Serializable{
	/**
	 * 返回是否成功
	 */
	private Boolean success = false;
	/**
	 * 返回信息
	 */
	private String msg = "";
	/**
	 * 返回其他对象信息
	 */
	private Object data = null;
	/**
	 * 提示代码
	 */
	private String code = "";

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

	/**
	 * <p> TODO</p>
	 * @return:     String
	 */
	public String getCode() {
		return code;
	}

	/** 
	 * <p> TODO</p>
	 * @return: String
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
