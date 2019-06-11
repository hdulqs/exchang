/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2017-11-18 13:20:52
 */

package com.batsoft.model.module.otc;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * <p>
 * ExCoin
 * </p>
 * 
 * @author: Yuan Zhicheng
 * @Date : 2017-11-18 13:20:52
 */
@Entity
@Data
@ToString
@Table(name = "otc_ex_coin")
public class ExCoin extends BaseModel {
	
	private static final long serialVersionUID = -5333490028081074422L;
	
	/**
	 * 状态 0:禁用
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:正常
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * 允许充值 0:否
	 */
	public static final Integer ALLOWRECHARGE0 = 0;
	/**
	 * 允许充值 1:是
	 */
	public static final Integer ALLOWRECHARGE1 = 1;
	/**
	 * 允许提现 0:否
	 */
	public static final Integer ALLOWDEPOSIT0 = 0;
	/**
	 * 允许提现 1:是
	 */
	public static final Integer ALLOWDEPOSIT1 = 1;
	
	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;
	
	/**
	 * 币种名称
	 */
	@NotNull(message = "币种名称不能为空")
	@Length(max = 50, message = "币种名称长度必须介于1和50之间")
	@Words(field = "币种名称", message = "币种名称包含敏感词")
	@Column(name = "coin_name")
	private String coinName;
	
	/**
	 * 币种代码
	 */
	@NotNull(message = "币种代码不能为空")
	@Length(max = 50, message = "币种代码长度必须介于1和50之间")
	@Words(field = "币种代码", message = "币种代码包含敏感词")
	@Column(name = "symbol")
	private String symbol;
	
	/**
	 * 币种logo
	 */
	@NotNull(message = "币种logo不能为空")
	@Length(max = 255, message = "币种logo长度必须介于1和255之间")
	@Words(field = "币种logo", message = "币种logo包含敏感词")
	@Column(name = "coin_logo")
	private String coinLogo;
	
	/**
	 * 充值节点确认个数
	 */
	@NotNull(message = "充值节点确认个数不能为空")
	@Column(name = "coin_confirm")
	private Integer coinConfirm;
	
	/**
	 * 买手续费
	 */
	@Money(point = 15, message = "买手续费金额格式错误")
	@Column(name = "buy_fee")
	private BigDecimal buyFee;
	
	/**
	 * 卖手续费
	 */
	@Money(point = 15, message = "卖手续费金额格式错误")
	@Column(name = "sell_fee")
	private BigDecimal sellFee;

	/**
	 * 提现手续费
	 */
	@Money(point = 8, message = "提现手续费金额格式错误")
	@Column(name = "withdraw_fee")
	private BigDecimal withdrawFee;
	
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 允许充值
	 */
	@Column(name = "allow_recharge")
	private Integer allowRecharge;

	/**
	 * 允许提现
	 */
	@Column(name = "allow_deposit")
	private Integer allowDeposit;

	/**
	 * 币种介绍
	 */
	@Length(max = 255, message = "币种介绍长度必须介于1和255之间")
	@Words(field = "币种介绍", message = "币种介绍包含敏感词")
	@Column(name = "coin_remark")
	private String coinRemark;
	
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空")
	@Column(name = "sort")
	private Integer sort;
}
