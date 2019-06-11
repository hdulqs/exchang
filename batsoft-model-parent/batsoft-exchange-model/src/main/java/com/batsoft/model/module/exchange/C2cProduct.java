/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-17 19:59:28
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

import lombok.Data;
import lombok.ToString;

/**
 * 
 * 法币交易产品设定
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_c2c_product")
public class C2cProduct extends BaseModel {

	private static final long serialVersionUID = 3888722897506834776L;
	
	/**
	 * 是否有效 0:否
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 是否有效 1:是
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	private String id;
	
	/**
	 * 币种编码
	 */
	@NotNull(message = "币种编码不能为空")
	@Length(max = 30, message = "币种编码长度必须介于1和30之间")
	@Words(field = "币种编码", message = "币种编码包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;
	
	/**
	 * 最小交易金额
	 */
	@NotNull(message = "最小交易金额不能为空")
	@Money(point = 15, message = "最小交易金额金额格式错误")
	@Column(name = "min_number")
	private BigDecimal minNumber;
	
	/**
	 * 最大交易金额
	 */
	@NotNull(message = "最大交易金额不能为空")
	@Money(point = 15, message = "最大交易金额金额格式错误")
	@Column(name = "max_number")
	private BigDecimal maxNumber;
	
	/**
	 * 购买单价
	 */
	@NotNull(message = "购买单价不能为空")
	@Column(name = "buy_price")
	private BigDecimal buyPrice;
	
	/**
	 * 卖出单价
	 */
	@NotNull(message = "卖出单价不能为空")
	@Column(name = "sell_price")
	private BigDecimal sellPrice;
	
	/**
	 * 是否有效
	 */
	@Column(name = "status")
	private Integer status;
	
	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

}
