package com.batsoft.model.module.exchange.po;

import java.math.BigDecimal;
import java.util.Date;

import com.batsoft.model.module.exchange.po.base.BasePO;

/**
 * 校验指定交易记录是否存在
 * 
 * @author simon
 */
public class FindCustomerAccountRecordExistPO extends BasePO {

	private static final long serialVersionUID = -4450156441104325608L;
	
	// 表名
	private String tableName;
	
	// 交易类型
	private String type;
	
	// 开始时间
	private Date beginTime;
	
	// 结束时间
	private Date endTime;
	
	// 客户ID
	private String customerId;
	
	// 交易金额
	private BigDecimal money;
	
	// 交易货币代码
	private String coinCode;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	
}
