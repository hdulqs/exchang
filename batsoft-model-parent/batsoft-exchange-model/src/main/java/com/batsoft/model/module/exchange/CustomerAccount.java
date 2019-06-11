package com.batsoft.model.module.exchange;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
 * 货币账户
 * 
 */
@Entity
@ToString
@Table(name = "exchange_customer_account")
public class CustomerAccount extends BaseModel {

	private static final long serialVersionUID = 4518736076521554092L;
	/**
	 * 状态 0:正常
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:账户冻结
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	protected String id;
	/**
	 * 账户账号
	 */
	@Length(max = 50, message = "账户账号长度必须介于1和50之间")
	@Words(field = "账户账号", message = "账户账号包含敏感词")
	@Column(name = "account_number")
	protected String accountNumber;
	/**
	 * 用户id
	 */
	@NotNull(message = "用户id不能为空")
	@Length(max = 64, message = "用户id长度必须介于1和64之间")
	@Words(field = "用户id", message = "用户id包含敏感词")
	@Column(name = "user_id")
	protected String userId;
	
	/**
	 * 用户名
	 */
	@NotNull(message = "用户名不能为空")
	@Length(max = 50, message = "用户名长度必须介于1和50之间")
	@Words(field = "用户名", message = "用户名包含敏感词")
	@Column(name = "user_name")
	protected String userName;
	
	/**
	 * 币种
	 */
	@NotNull(message = "币种不能为空")
	@Length(max = 50, message = "币种长度必须介于1和50之间")
	@Words(field = "币种", message = "币种包含敏感词")
	@Column(name = "coin_code")
	protected String coinCode;

	/**
	 * 币地址
	 */
	@Length(max = 255, message = "币地址长度必须介于1和255之间")
	@Words(field = "币地址", message = "币地址包含敏感词")
	@Column(name = "coin_address")
	protected String coinAddress;
	
	/**
	 * 币地址标签
	 */
	@Length(max = 255, message = "币地址标签长度必须介于1和255之间")
	@Words(field = "币地址标签", message = "币地址标签包含敏感词")
	@Column(name = "memo")
	protected String memo;
	
	/**
	 * 可用金额
	 */
	@Money(point = 15, message = "可用金额金额格式错误")
	@Column(name = "available")
	protected BigDecimal available;
	
	/**
	 * 冻结金额
	 */
	@Money(point = 15, message = "冻结金额金额格式错误")
	@Column(name = "freeze")
	protected BigDecimal freeze;
	/**
	 * 状态
	 */
	@Column(name = "status")
	protected Integer status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public String getCoinAddress() {
		return coinAddress;
	}

	public void setCoinAddress(String coinAddress) {
		this.coinAddress = coinAddress;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public BigDecimal getFreeze() {
		return freeze;
	}

	public void setFreeze(BigDecimal freeze) {
		this.freeze = freeze;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
