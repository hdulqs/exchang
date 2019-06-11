package com.batsoft.model.module.exchange.po;

import com.batsoft.model.module.exchange.po.base.BasePO;

/**
 * 账户交易记录综合查询
 * 
 * @author ppmle
 */
public class CustomerAccountRecordPO extends BasePO {

	private static final long serialVersionUID = -4744594084467287237L;
	
	/**
	 * 用户ID 
	 * 
	 */
	private String customerId;
	
	/**
	 * 流水类型
	 * 
	 */
	private String type;
	

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
