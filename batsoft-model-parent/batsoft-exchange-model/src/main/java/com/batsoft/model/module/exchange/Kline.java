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
 * K线图数据
 * 
 */
@Entity
@ToString
@Table(name = "exchange_kline")
public class Kline extends BaseModel {

	private static final long serialVersionUID = -3325247184694565326L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 交易对
	 */
	@NotNull(message = "交易对不能为空")
	@Length(max = 10, message = "交易对长度必须介于1和10之间")
	@Words(field = "交易对", message = "交易对包含敏感词")
	@Column(name = "coin_pair")
	private String coinPair;

	/**
	 * 时区
	 */
	@NotNull(message = "时区不能为空")
	@Length(max = 10, message = "时区长度必须介于1和10之间")
	@Words(field = "时区", message = "时区包含敏感词")
	@Column(name = "kline_time")
	private String klineTime;

	/**
	 * K线数据
	 */
	@Words(field = "K线数据", message = "K线数据包含敏感词")
	@Column(name = "kline_data")
	private String klineData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoinPair() {
		return coinPair;
	}

	public void setCoinPair(String coinPair) {
		this.coinPair = coinPair;
	}

	public String getKlineTime() {
		return klineTime;
	}

	public void setKlineTime(String klineTime) {
		this.klineTime = klineTime;
	}

	public String getKlineData() {
		return klineData;
	}

	public void setKlineData(String klineData) {
		this.klineData = klineData;
	}

}
