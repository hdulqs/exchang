package com.batsoft.model.module.blockchain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
* 
* <p>CoinApiLog</p>
* @author: Bat Admin
* @Date :  2018-05-22 14:21:20 
*/

@Entity
@ToString
@Table(name="blockchain_coin_api_log")
public class CoinApiLog extends BaseModel {

	private static final long serialVersionUID = -2291432324360577286L;

	/**
    * id
    */
    @Id
    @Column(name="id")
    private String id;
        
    /**
    * 请求ip
    */
    @NotNull(message="请求ip不能为空")
    @Length(max = 16, message = "请求ip长度必须介于1和16之间")
    @Words(field = "请求ip", message = "请求ip包含敏感词")
    @Column(name="request_ip")
    private String requestIp;
    
    /**
    * 请求用户
    */
    @NotNull(message="请求用户不能为空")
    @Length(max = 30, message = "请求用户长度必须介于1和30之间")
    @Words(field = "请求用户", message = "请求用户包含敏感词")
    @Column(name="request_user")
    private String requestUser;
    
    /**
    * 请求方法
    */
    @NotNull(message="请求方法不能为空")
    @Length(max = 20, message = "请求方法长度必须介于1和20之间")
    @Words(field = "请求方法", message = "请求方法包含敏感词")
    @Column(name="request_method")
    private String requestMethod;
    
    /**
    * 请求参数
    */
    @Words(field = "请求参数", message = "请求参数包含敏感词")
    @Column(name="request_parameter")
    private String requestParameter;
    
    /**
    * 返回状态
    */
    @Length(max = 20, message = "返回状态长度必须介于1和20之间")
    @Words(field = "返回状态", message = "返回状态包含敏感词")
    @Column(name="request_state")
    private String requestState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getRequestUser() {
		return requestUser;
	}

	public void setRequestUser(String requestUser) {
		this.requestUser = requestUser;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestParameter() {
		return requestParameter;
	}

	public void setRequestParameter(String requestParameter) {
		this.requestParameter = requestParameter;
	}

	public String getRequestState() {
		return requestState;
	}

	public void setRequestState(String requestState) {
		this.requestState = requestState;
	}

}
