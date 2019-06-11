/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:02
 */

package com.batsoft.model.module.exchange;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
 * 账户货币冻结记录
 * 
 */
@Entity
@ToString
@Table(name = "exchange_customer_account_freeze")
public class CustomerAccountFreeze extends BaseModel {
    
	private static final long serialVersionUID = -900120563908084801L;

	/**
     * 委托
     */
    public static final String ENTRUST = "entrust";

    /**
     * 撤销
     */
    public static final String CANCEL = "cancel";

    /**
     * C2C
     */
    public static final String C2C = "c2c";
    /**
     * 充值
     */
    public static final String RECHARGE = "recharge";
    /**
     * 提现
     */
    public static final String WITHDRAW = "withdraw";


    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 账户id
     */
    @Length(max = 50, message = "账户id长度必须介于1和50之间")
    @Words(field = "账户id", message = "账户id包含敏感词")
    @Column(name = "account_id")
    private String accountId;
    
    /**
     * 客户id
     */
    @Length(max = 50, message = "客户id长度必须介于1和50之间")
    @Words(field = "客户id", message = "客户id包含敏感词")
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 账户号
     */
    @Length(max = 50, message = "账户号长度必须介于1和50之间")
    @Words(field = "账户号", message = "账户号包含敏感词")
    @Column(name = "account_number")
    private String accountNumber;

    /**
     * 币代码
     */
    @Length(max = 50, message = "币代码长度必须介于1和50之间")
    @Words(field = "币代码", message = "币代码包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;
    /**
     * 冻结类型
     */

    @Length(max = 50, message = "冻结类型长度必须介于1和50之间")
    @Words(field = "冻结类型", message = "冻结类型包含敏感词")
    @Column(name = "freeze_type")
    private String freezeType;
    /**
     * 订单id
     */

    @Length(max = 10, message = "订单id长度必须介于1和10之间")
    @Words(field = "订单id", message = "订单id包含敏感词")
    @Column(name = "order_id")
    private String orderId;
    /**
     * 冻结金额
     */

    @Money(point = 15, message = "冻结金额金额格式错误")
    @Column(name = "freeze_money")
    private BigDecimal freezeMoney;
    /**
     * 订单说明
     */

    @Length(max = 50, message = "订单说明长度必须介于1和50之间")
    @Words(field = "订单说明", message = "订单说明包含敏感词")
    @Column(name = "remark")
    private String remark;

    /**
     * 用户名
     */
    @Transient
    private String userName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public String getFreezeType() {
		return freezeType;
	}

	public void setFreezeType(String freezeType) {
		this.freezeType = freezeType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    

}
