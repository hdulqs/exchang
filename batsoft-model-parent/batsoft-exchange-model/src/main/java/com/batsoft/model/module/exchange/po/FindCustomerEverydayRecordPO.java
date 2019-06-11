package com.batsoft.model.module.exchange.po;

import java.util.Date;

import com.batsoft.model.module.exchange.po.base.BasePO;

/**
 * 查询每人每日usdt交易流水
 * 
 * @author simon
 */
public class FindCustomerEverydayRecordPO extends BasePO {

	private static final long serialVersionUID = 8045279407228824390L;
	
	// 表名称
	private String tableName;
	
	// 交易类型
	private String type;
	
	// 开始时间
	private Date beginTime;
	
	// 结束时间
	private Date endTime;
	
	// 交易币代码
	private String tradeCoinCode;
	
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
	
	public String getTradeCoinCode() {
		return tradeCoinCode;
	}

	public void setTradeCoinCode(String tradeCoinCode) {
		this.tradeCoinCode = tradeCoinCode;
	}

	@Override
	public String toString() {
		return "FindCustomerEverydayRecordPO [type=" + type + ", beginTime=" + beginTime + ", endTime=" + endTime
				+ ", tradeCoinCode=" + tradeCoinCode + "]";
	}
	
}
