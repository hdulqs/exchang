
package com.batsoft.core.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 封装json结果集
 * 
 * 
 * 
 * @author Bat Admin
 *
 */
@ApiModel(value = "返回结果类")
public class JsonResult<T> implements Serializable {

	private static final long serialVersionUID = -2212846892798108717L;
	@ApiModelProperty(value = "返回是否成功")
	private Boolean success = false;// 返回是否成功
	@ApiModelProperty(value = "返回信息")
	private String msg = "";// 返回信息
	@ApiModelProperty(value = "返回数据对象")
	private T data = null;// 返回其他对象信息
	@ApiModelProperty(value = "返回提示代码")
	private String code =  ResultCode.FAILE.getCode();

	public JsonResult(){
		super();
	}
	public JsonResult(Boolean isSuccess){
		if(isSuccess){
			this.success = true;
			this.code = ResultCode.SUCCESS.getCode();
		}else{
			this.success = false;
			this.code = ResultCode.FAILE.getCode();
		}
	}

	public  enum ResultCode{
		NO_LOGIN("1001","未登录"),
		SUCCESS("8888","成功"),
		FAILE("0000","失败错误码"),
		OTHERS("1002","其他");
		private String code;
		private String codeStr;
		ResultCode(String code,String codeStr){
			this.code = code;
			this.codeStr = codeStr;
		}
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getCodeStr() {
			return codeStr;
		}

		public void setCodeStr(String codeStr) {
			this.codeStr = codeStr;
		}

		@Override
		public String toString() {
			return this.code;
		}
	}
	public JsonResult(Boolean success, String msg){
		this.success = success;
		this.msg = msg;
	}
	
	public JsonResult(Boolean success, String msg, ResultCode code){
		this.success = success;
		this.msg = msg;
		this.code = code.getCode();
	}
	
	public JsonResult(Boolean success, String msg, ResultCode code, T data){
		this.success = success;
		this.msg = msg;
		this.code = code.getCode();
		this.data = data;
	}

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

	public void setData(T data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(ResultCode code) {
		this.code = code.getCode();
	}

	
}
