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
 * 客户交易记录
 * 
 */
@Entity
@ToString
@Table(name = "exchange_customer_account_record")
public class CustomerAccountRecord extends BaseModel {

	private static final long serialVersionUID = -2755165503858564034L;

	/**
	 * 委托
	 */
	public static final String ENTRUST = "entrust";

	/**
	 * 手续费
	 */
	public static final String HANDFEE = "handfee";

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
	 * 商城消费
	 */
	public static final String SHOPPING = "shopping";

	/**
	 * 商城消费挖坑
	 */
	public static final String SHOPPING_MINING = "shopping_mining";

	/**
	 * 交易挖矿
	 *
	 */
	public static final String TRADE_MINING = "trade_mining";

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
	 * 账户账号
	 */
	@Length(max = 50, message = "账户账号长度必须介于1和50之间")
	@Words(field = "账户账号", message = "账户账号包含敏感词")
	@Column(name = "account_number")
	private String accountNumber;

	/**
	 * 货币代码
	 */
	@Length(max = 50, message = "货币代码长度必须介于1和50之间")
	@Words(field = "货币代码", message = "货币代码包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;

	/**
	 * 流水类型
	 */
	@Length(max = 50, message = "流水类型长度必须介于1和50之间")
	@Words(field = "流水类型", message = "流水类型包含敏感词")
	@Column(name = "type")
	private String type;

	/**
	 * 订单id
	 */
	@Length(max = 10, message = "订单id长度必须介于1和10之间")
	@Words(field = "订单id", message = "订单id包含敏感词")
	@Column(name = "order_id")
	private String orderId;

	/**
	 * 流水金额
	 */
	@Money(point = 15, message = "流水金额金额格式错误")
	@Column(name = "money")
	private BigDecimal money;

	/**
	 * 订单说明
	 */
	@Length(max = 50, message = "订单说明长度必须介于1和50之间")
	@Words(field = "订单说明", message = "订单说明包含敏感词")
	@Column(name = "remark")
	private String remark;

	/**
	 * usdt数量
	 * 
	 */
	@Money(point = 15, message = "USDT金额格式错误")
	@Column(name = "usdt_amount")
	private BigDecimal usdtAmount;

	/**
	 * 交易币代码
	 * 
	 */
	@Length(max = 32, message = "交易币长度需要小于32个字符")
	@Column(name = "trade_coin_code")
	private String tradeCoinCode;

	/**
	 * 定价币代码
	 * 
	 */
	@Length(max = 32, message = "定价币长度需要小于32个字符")
	@Column(name = "pricing_coin_code")
	private String pricingCoinCode;

	/**
	 * 交易币实时价格
	 * 
	 */
	@Money(point = 15, message = "交易币实时价格金额格式错误")
	@Column(name = "trade_coin_real_time_price")
	private BigDecimal tradeCoinRealTimePrice;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getUsdtAmount() {
		return usdtAmount;
	}

	public void setUsdtAmount(BigDecimal usdtAmount) {
		this.usdtAmount = usdtAmount;
	}

	public String getTradeCoinCode() {
		return tradeCoinCode;
	}

	public void setTradeCoinCode(String tradeCoinCode) {
		this.tradeCoinCode = tradeCoinCode;
	}

	public String getPricingCoinCode() {
		return pricingCoinCode;
	}

	public void setPricingCoinCode(String pricingCoinCode) {
		this.pricingCoinCode = pricingCoinCode;
	}

	public BigDecimal getTradeCoinRealTimePrice() {
		return tradeCoinRealTimePrice;
	}

	public void setTradeCoinRealTimePrice(BigDecimal tradeCoinRealTimePrice) {
		this.tradeCoinRealTimePrice = tradeCoinRealTimePrice;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

}
