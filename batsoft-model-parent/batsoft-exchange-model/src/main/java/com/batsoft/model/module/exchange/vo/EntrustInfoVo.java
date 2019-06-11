package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.EntrustInfo;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @aouthor LouSir
 * @date 2018/5/24 16:58
 */
@ToString
public class EntrustInfoVo extends EntrustInfo {
	/**
	 * 交易类型
	 */
	private String typeStr;
	/**
	 * 交易额
	 */
	private BigDecimal totleMoney;

	/**
	 * 卖方委托单号
	 */
	private String sellOrderId;

	private BigDecimal entrustTotalAmunt;
	private BigDecimal entrustTotalMoney;

	public String getTypeStr() {
		String typeValue = "";
		switch (super.getType()) {
			case "buy":
				typeValue = "买入";
				break;
			case "sell":
				typeValue = "卖出";
				break;
			default:
				typeValue = "买入";
				break;
		}
		return typeValue;
	}

	public BigDecimal getTotleMoney() {
		return ((super.getEntrustPrice() == null ? BigDecimal.ZERO : super.getEntrustPrice())
				.multiply(super.getEntrustAmout() == null ? BigDecimal.ZERO : super.getEntrustAmout()));
	}

	public BigDecimal getEntrustTotalAmunt() {
		return entrustTotalAmunt;
	}

	public void setEntrustTotalAmunt(BigDecimal entrustTotalAmunt) {
		this.entrustTotalAmunt = entrustTotalAmunt;
	}

	public BigDecimal getEntrustTotalMoney() {
		return entrustTotalMoney;
	}

	public void setEntrustTotalMoney(BigDecimal entrustTotalMoney) {
		this.entrustTotalMoney = entrustTotalMoney;
	}

	public String getSellOrderId() {
		return sellOrderId;
	}

	public void setSellOrderId(String sellOrderId) {
		this.sellOrderId = sellOrderId;
	}
}
