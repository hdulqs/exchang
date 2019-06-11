package com.batsoft.common.util;

import java.io.Serializable;

import com.batsoft.common.enums.ResponseStatusCodeEnum;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * Api数据响应对象
 * 
 * @author simon
 *
 * @param <T>
 * 			通用类型
 */
public class ApiResultData<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 当前时间戳
	private String date = String.valueOf(System.currentTimeMillis());
	
	// 状态代码
	private String status;
	
	// 响应数据
	private T data;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getData() {
		return data;
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
	
	public void setSuccessful() {
		this.status = ResponseStatusCodeEnum.SUCCESS.getValue();
	}
	
	public void setFailed() {
		this.status = ResponseStatusCodeEnum.FAIL.getValue();
	}
	
	public String toJson() {
		return GsonSingleton.getInstance().toJson(this);
	}
	
}
