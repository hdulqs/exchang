package com.batsoft.common.beans.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.batsoft.common.base.BaseVO;

/**
 * 获取全部交易对
 * 
 * @author simon
 */
public class CoinsAreaVO extends BaseVO {

	private static final long serialVersionUID = -2996322219064033980L;

	private Boolean isCollect;

	private String collectionId;

	// 交易币
	private String tradeCoinCode;

	// 定价币
	private String pricingCoinCode;

	// CNY价值
	private BigDecimal evaluateCny;

	// 最高价
	private BigDecimal high;

	// 最低价
	private BigDecimal low;

	// 最新价格
	private BigDecimal colse;

	// 倒数第二价格
	private BigDecimal preLast;

	// 实时汇率
	private BigDecimal rate;

	// 交易币总额
	private BigDecimal volume;

	// 活动开始时间
	private Date actionTime;
	
	// 交易币LOGO
	private String tradeCoinLogo;

	public Boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Boolean isCollect) {
		this.isCollect = isCollect;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
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

	public BigDecimal getEvaluateCny() {
		return evaluateCny;
	}

	public void setEvaluateCny(BigDecimal evaluateCny) {
		this.evaluateCny = evaluateCny;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getColse() {
		return colse;
	}

	public void setColse(BigDecimal colse) {
		this.colse = colse;
	}

	public BigDecimal getPreLast() {
		return preLast;
	}

	public void setPreLast(BigDecimal preLast) {
		this.preLast = preLast;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getTradeCoinLogo() {
		return tradeCoinLogo;
	}

	public void setTradeCoinLogo(String tradeCoinLogo) {
		this.tradeCoinLogo = tradeCoinLogo;
	}

}
