/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:47
 */

package com.batsoft.model.module.exchange;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
 * 委托交易成交信息
 * 
 */
@Entity
@ToString
@Table(name = "exchange_entrust_info")
public class EntrustInfo extends BaseModel {

	private static final long serialVersionUID = 8221732671355585333L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 成交货币
	 */
	@Length(max = 50, message = "成交货币长度必须介于1和50之间")
	@Words(field = "成交货币", message = "成交货币包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;

	/**
	 * 交易币代码
	 *
	 */
	@NotNull(message = "交易币代码不能为空")
	@Length(max = 50, message = "交易币代码长度必须介于1和50之间")
	@Words(field = "交易币代码", message = "交易币代码包含敏感词")
	@Column(name = "trade_coin_code")
	private String tradeCoinCode;

	/**
	 * 定价币代码
	 */
	@NotNull(message = "定价币代码不能为空")
	@Length(max = 50, message = "定价币代码长度必须介于1和50之间")
	@Words(field = "定价币代码", message = "定价币代码包含敏感词")
	@Column(name = "pricing_coin_code")
	private String pricingCoinCode;

	/**
	 *
	 *
	 */
	@NotNull(message = "主动交易类型")
	@Column(name = "type")
	private String type;

	/**
	 * 成交价格
	 */
	@Money(point = 15, message = "成交价格金额格式错误")
	@Column(name = "entrust_price")
	private BigDecimal entrustPrice;

	/**
	 * 成交数量
	 */
	@Money(point = 15, message = "成交数量金额格式错误")
	@Column(name = "entrust_amout")
	private BigDecimal entrustAmout;

	/**
	 * 成交时间
	 */
	@Column(name = "entrust_time")
	private Date entrustTime;

	/**
	 * 卖方账户
	 */
	@Length(max = 50, message = "卖方账户长度必须介于1和50之间")
	@Words(field = "卖方账户", message = "卖方账户包含敏感词")
	@Column(name = "sell_account_id")
	private String sellAccountId;

	/**
	 * 买方账户
	 */
	@Length(max = 50, message = "买方账户长度必须介于1和50之间")
	@Words(field = "买方账户", message = "买方账户包含敏感词")
	@Column(name = "buy_account_id")
	private String buyAccountId;

	/**
	 * 卖方客户
	 */
	@Length(max = 50, message = "卖方客户长度必须介于1和50之间")
	@Words(field = "卖方客户", message = "卖方客户包含敏感词")
	@Column(name = "sell_customer_id")
	private String sellCustomerId;

	/**
	 * 买方客户
	 */
	@Length(max = 50, message = "买方客户长度必须介于1和50之间")
	@Words(field = "买方客户", message = "买方客户包含敏感词")
	@Column(name = "buy_customer_id")
	private String buyCustomerId;

	/**
	 * 备注
	 */
	@Length(max = 50, message = "备注长度必须介于1和50之间")
	@Words(field = "备注", message = "备注包含敏感词")
	@Column(name = "remark")
	private String remark;

	/**
	 * 买费率
	 *
	 */
	@Column(name = "buy_rate")
	private BigDecimal buyRate;

	/**
	 * 卖费率
	 *
	 */
	@Column(name = "sell_rate")
	private BigDecimal sellRate;

	/**
	 * 买手续费
	 *
	 */
	@Column(name = "buy_fee")
	private BigDecimal buyFee;

	/**
	 * 卖手续费
	 *
	 */
	@Column(name = "sell_fee")
	private BigDecimal sellFee;

	/**
	 * 买订单号
	 *
	 */
	@Column(name = "buy_order_id")
	private String buyOrderId;

	/**
	 *
	 *
	 */
	@Column(name = "platformCoin")
	private String platformCoin;

	@Column(name = "buyPlatformCoin")
	private String buyPlatformCoin;

	/**
	 * 定价币相对于USDT实时汇率
	 * 
	 */
	@Column(name = "real_time_usdt_rate")
	private BigDecimal realTimeUsdtRate;

	@Transient
	private String buyUserName;

	@Transient
	private String sellUserName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}

	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}

	public BigDecimal getEntrustAmout() {
		return entrustAmout;
	}

	public void setEntrustAmout(BigDecimal entrustAmout) {
		this.entrustAmout = entrustAmout;
	}

	public Date getEntrustTime() {
		return entrustTime;
	}

	public void setEntrustTime(Date entrustTime) {
		this.entrustTime = entrustTime;
	}

	public String getSellAccountId() {
		return sellAccountId;
	}

	public void setSellAccountId(String sellAccountId) {
		this.sellAccountId = sellAccountId;
	}

	public String getBuyAccountId() {
		return buyAccountId;
	}

	public void setBuyAccountId(String buyAccountId) {
		this.buyAccountId = buyAccountId;
	}

	public String getSellCustomerId() {
		return sellCustomerId;
	}

	public void setSellCustomerId(String sellCustomerId) {
		this.sellCustomerId = sellCustomerId;
	}

	public String getBuyCustomerId() {
		return buyCustomerId;
	}

	public void setBuyCustomerId(String buyCustomerId) {
		this.buyCustomerId = buyCustomerId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(BigDecimal buyRate) {
		this.buyRate = buyRate;
	}

	public BigDecimal getSellRate() {
		return sellRate;
	}

	public void setSellRate(BigDecimal sellRate) {
		this.sellRate = sellRate;
	}

	public BigDecimal getBuyFee() {
		return buyFee;
	}

	public void setBuyFee(BigDecimal buyFee) {
		this.buyFee = buyFee;
	}

	public BigDecimal getSellFee() {
		return sellFee;
	}

	public void setSellFee(BigDecimal sellFee) {
		this.sellFee = sellFee;
	}

	public String getBuyOrderId() {
		return buyOrderId;
	}

	public void setBuyOrderId(String buyOrderId) {
		this.buyOrderId = buyOrderId;
	}

	public String getPlatformCoin() {
		return platformCoin;
	}

	public void setPlatformCoin(String platformCoin) {
		this.platformCoin = platformCoin;
	}

	public String getBuyPlatformCoin() {
		return buyPlatformCoin;
	}

	public void setBuyPlatformCoin(String buyPlatformCoin) {
		this.buyPlatformCoin = buyPlatformCoin;
	}

	public BigDecimal getRealTimeUsdtRate() {
		return realTimeUsdtRate;
	}

	public void setRealTimeUsdtRate(BigDecimal realTimeUsdtRate) {
		this.realTimeUsdtRate = realTimeUsdtRate;
	}

	public String getBuyUserName() {
		return buyUserName;
	}

	public void setBuyUserName(String buyUserName) {
		this.buyUserName = buyUserName;
	}

	public String getSellUserName() {
		return sellUserName;
	}

	public void setSellUserName(String sellUserName) {
		this.sellUserName = sellUserName;
	}

}
