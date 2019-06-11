package com.batsoft.common.util.result;

import java.io.Serializable;

import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.core.common.enums.MessageEnum;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 响应封装对象
 * 
 * @author simon
 */
public class ResultData<T> implements Serializable {

	private static final long serialVersionUID = 9007526256209475169L;
	
	// 返回是否成功
	private Boolean success = true;
	
	// 响应码
	private String code = MessageEnum.SUCCESS.getCode();
	
	// 响应描述
	private String msg = MessageEnum.SUCCESS.getMessage();
	
	// 响应结果
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData(Class<T> clazz) {
		if(data == null) {
			try { data = clazz.newInstance(); } catch (Exception e) { e.printStackTrace(); }
		}
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public String toJson() {
		return GsonSingleton.getInstance().toJson(this);
	}
	
	public boolean isSuccess() {
		return "SUCCESS".equalsIgnoreCase(getCode());
	}
	
	public void setSuccessful() {
		this.setCode(MessageEnum.SUCCESS.getCode());
		this.setMsg(MessageEnum.SUCCESS.getMessage());
	}
	
	public void setFailed() {
		this.setSuccess(false);
		this.setCode(MessageEnum.FAIL.getCode());
		this.setMsg(MessageEnum.FAIL.getMessage());
	}
	
	public void setMessage(ModuleMessageEnum message) {
		this.setCode(message.getCode());
		this.setMsg(message.getMessage());
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
}
