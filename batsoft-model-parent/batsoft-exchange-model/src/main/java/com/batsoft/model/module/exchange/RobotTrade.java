/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-29 23:10:22
 */

package com.batsoft.model.module.exchange;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;

/**
 * 自动下单机器人配置
 * 
 */
@Entity
@Table(name = "exchange_robot_trade")
public class RobotTrade extends BaseModel {

	private static final long serialVersionUID = 7115992550473664308L;

	/**
	 * 是否三方数据 0:否
	 */
	public static final Integer FROMTHIRD0 = 0;

	/**
	 * 是否三方数据 1:是
	 */
	public static final Integer FROMTHIRD1 = 1;

	/**
	 * 运行状态 0:停止
	 */
	public static final Integer STATUS0 = 0;

	/**
	 * 运行状态 1:启动
	 */
	public static final Integer STATUS1 = 1;

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
	@Length(max = 100, message = "交易对长度必须介于1和100之间")
	@Words(field = "交易对", message = "交易对包含敏感词")
	@Column(name = "coin_pair")
	private String coinPair;

	/**
	 * 下单用户
	 */
	// @NotNull(message = "下单用户不能为空")
	@Length(max = 32, message = "下单用户长度必须介于1和32之间")
	@Words(field = "下单用户", message = "下单用户包含敏感词")
	@Column(name = "user_id")
	private String userId;

	/**
	 * 下单用户
	 */
	@NotNull(message = "下单用户不能为空")
	@Length(max = 50, message = "下单用户长度必须介于1和50之间")
	@Words(field = "下单用户", message = "下单用户包含敏感词")
	@Column(name = "user_name")
	private String userName;

	/**
	 * 最大下单数量
	 */
	@Money(point = 15, message = "最大下单数量金额格式错误")
	@Column(name = "entrust_num_max")
	private BigDecimal entrustNumMax;

	/**
	 * 最小下单数量
	 */
	@Money(point = 15, message = "最小下单数量金额格式错误")
	@Column(name = "entrust_num_min")
	private BigDecimal entrustNumMin;

	/**
	 * 下单时间最大
	 */
	@Column(name = "entrust_time_max")
	private Integer entrustTimeMax;

	/**
	 * 下单时间最小
	 */
	@Column(name = "entrust_time_min")
	private Integer entrustTimeMin;

	/**
	 * 最大下单价格
	 */
	@Money(point = 15, message = "最大下单价格金额格式错误")
	@Column(name = "entrust_price_max")
	private BigDecimal entrustPriceMax;

	/**
	 * 最小下单价格
	 */
	@Money(point = 15, message = "最小下单价格金额格式错误")
	@Column(name = "entrust_price_min")
	private BigDecimal entrustPriceMin;

	/**
	 * 基础价格 修改为 步长
	 */
	@Money(point = 15, message = "步长金额格式错误")
	@Column(name = "base_price")
	private BigDecimal basePrice;

	/**
	 * 是否三方数据
	 */
	@Column(name = "from_third")
	private Integer fromThird;

	/**
	 * api地址
	 */
	@Length(max = 100, message = "api地址长度必须介于1和100之间")
	@Words(field = "api地址", message = "api地址包含敏感词")
	@Column(name = "third_api")
	private String thirdApi;

	/**
	 * 运行状态
	 */
	@Column(name = "status")
	private Integer status;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getEntrustNumMax() {
		return entrustNumMax;
	}

	public void setEntrustNumMax(BigDecimal entrustNumMax) {
		this.entrustNumMax = entrustNumMax;
	}

	public BigDecimal getEntrustNumMin() {
		return entrustNumMin;
	}

	public void setEntrustNumMin(BigDecimal entrustNumMin) {
		this.entrustNumMin = entrustNumMin;
	}

	public Integer getEntrustTimeMax() {
		return entrustTimeMax;
	}

	public void setEntrustTimeMax(Integer entrustTimeMax) {
		this.entrustTimeMax = entrustTimeMax;
	}

	public Integer getEntrustTimeMin() {
		return entrustTimeMin;
	}

	public void setEntrustTimeMin(Integer entrustTimeMin) {
		this.entrustTimeMin = entrustTimeMin;
	}

	public BigDecimal getEntrustPriceMax() {
		return entrustPriceMax;
	}

	public void setEntrustPriceMax(BigDecimal entrustPriceMax) {
		this.entrustPriceMax = entrustPriceMax;
	}

	public BigDecimal getEntrustPriceMin() {
		return entrustPriceMin;
	}

	public void setEntrustPriceMin(BigDecimal entrustPriceMin) {
		this.entrustPriceMin = entrustPriceMin;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public Integer getFromThird() {
		return fromThird;
	}

	public void setFromThird(Integer fromThird) {
		this.fromThird = fromThird;
	}

	public String getThirdApi() {
		return thirdApi;
	}

	public void setThirdApi(String thirdApi) {
		this.thirdApi = thirdApi;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RobotTrade [id=" + id + ", coinPair=" + coinPair + ", userId=" + userId + ", userName=" + userName
				+ ", entrustNumMax=" + entrustNumMax + ", entrustNumMin=" + entrustNumMin + ", entrustTimeMax="
				+ entrustTimeMax + ", entrustTimeMin=" + entrustTimeMin + ", entrustPriceMax=" + entrustPriceMax
				+ ", entrustPriceMin=" + entrustPriceMin + ", basePrice=" + basePrice + ", fromThird=" + fromThird
				+ ", thirdApi=" + thirdApi + ", status=" + status + "]";
	}

}
