package com.batsoft.model.module.exchange;

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
 * 交易对收藏记录
 * 
 */
@Entity
@ToString
@Table(name = "coin_pair_user_collection")
public class CoinPairUserCollection extends BaseModel {

	private static final long serialVersionUID = 2868195008780168375L;

	
	private String id;
	
	// 交易币
	private String tradeCoinCode;
	
	// 定价币
	private String pricingCoinCode;
	
	// 客户
	private String userId;

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@NotNull(message = "交易币代码不能为空")
	@Length(max = 50, message = "交易币代码长度必须介于1和50之间")
	@Words(field = "交易币代码", message = "交易币代码包含敏感词")
	@Column(name = "trade_coin_code")
	public String getTradeCoinCode() {
		return tradeCoinCode;
	}

	public void setTradeCoinCode(String tradeCoinCode) {
		this.tradeCoinCode = tradeCoinCode;
	}
	
	@NotNull(message = "定价币代码不能为空")
	@Length(max = 50, message = "定价币代码长度必须介于1和50之间")
	@Words(field = "定价币代码", message = "定价币代码包含敏感词")
	@Column(name = "pricing_coin_code")
	public String getPricingCoinCode() {
		return pricingCoinCode;
	}

	public void setPricingCoinCode(String pricingCoinCode) {
		this.pricingCoinCode = pricingCoinCode;
	}
	
	@Length(max = 50, message = "创建人id长度必须介于1和50之间")
	@Words(field = "创建人id", message = "创建人id包含敏感词")
	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
