/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2017-12-08 09:56:42
 */

package com.batsoft.model.module.otc;

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
 * OTC货币余额
 * 
 */
@Entity
@Data
@ToString
@Table(name = "otc_ex_finance")
public class ExFinance extends BaseModel {

	private static final long serialVersionUID = 5369368806799611524L;
	
	/**
	 * 状态 0:正常
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:冻结
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;
	
	/**
	 * 用户id
	 */
	@NotNull(message = "用户id不能为空")
	@Length(max = 64, message = "用户id长度必须介于1和64之间")
	@Words(field = "用户id", message = "用户id包含敏感词")
	@Column(name = "user_id")
	private String userId;
	
	/**
	 * 用户名
	 */
	@NotNull(message = "用户名不能为空")
	@Length(max = 50, message = "用户名长度必须介于1和50之间")
	@Words(field = "用户名", message = "用户名包含敏感词")
	@Column(name = "user_name")
	private String userName;
	
	/**
	 * 币种
	 */
	@NotNull(message = "币种不能为空")
	@Length(max = 50, message = "币种标识长度必须介于1和50之间")
	@Words(field = "币种标识", message = "币种标识包含敏感词")
	@Column(name = "symbol")
	private String symbol;
	
	/**
	 * 币地址
	 */
	@Length(max = 255, message = "币地址长度必须介于1和255之间")
	@Words(field = "币地址", message = "币地址包含敏感词")
	@Column(name = "coin_address")
	private String coinAddress;
	
	/**
	 * 币地址标签
	 */
	@Length(max = 255, message = "币地址标签长度必须介于1和255之间")
	@Words(field = "币地址标签", message = "币地址标签包含敏感词")
	@Column(name = "coin_address_tag")
	private String coinAddressTag;
	
	/**
	 * 可用金额
	 */
	@Money(point = 15, message = "可用金额金额格式错误")
	@Column(name = "available")
	private BigDecimal available;
	
	/**
	 * 冻结金额
	 */
	@Money(point = 15, message = "冻结金额金额格式错误")
	@Column(name = "freeze")
	private BigDecimal freeze;
	
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

}
