package com.batsoft.model.module.exchange.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 数据格式 "["GTO_ETH","0.000749","18.00","0.000750","699.00","-0.000002","-0.0027
 * 24h涨跌","0.000749 最新价","1130.11 24h 交易量","0.000869 24h最高","0.000735
 * 24h最低","1459708.34","3.78"]"
 */
public class TickerVo implements Serializable {

	private static final long serialVersionUID = -2914771221496666628L;

	private String priceCode;
	private String coinCode;
	private String logo;
	/**
	 * 交易对
	 */
	private String coinPair;
	/**
	 * 24h最新价格
	 */
	private BigDecimal last = new BigDecimal(0);
	/**
	 * 倒数第二笔24h最新价格
	 */
	private BigDecimal preLast = new BigDecimal(0);
	
	/**
	 * 换算成人名币价值
	 * 
	 */
	private BigDecimal duihuan = new BigDecimal(0);
	/**
	 * 24h最高价
	 */
	private BigDecimal high = new BigDecimal(0);
	/**
	 * 24h最低价
	 */
	private BigDecimal low = new BigDecimal(0);

	/**
	 * 24h交易量
	 */
	private BigDecimal volume = new BigDecimal(0);
	
	/**
	 * 24h涨跌
	 */
	private BigDecimal rate = new BigDecimal(0);

	public String getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCoinPair() {
		return coinPair;
	}

	public void setCoinPair(String coinPair) {
		this.coinPair = coinPair;
	}

	public BigDecimal getLast() {
		return last;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	public BigDecimal getPreLast() {
		return preLast;
	}

	public void setPreLast(BigDecimal preLast) {
		this.preLast = preLast;
	}

	public BigDecimal getDuihuan() {
		return duihuan;
	}

	public void setDuihuan(BigDecimal duihuan) {
		this.duihuan = duihuan;
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

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

}
