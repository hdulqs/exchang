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
 * OTC项目订单
 *
 */
@Entity
@Data
@ToString
@Table(name = "otc_project_order")
public class ProjectOrder extends BaseModel {

	private static final long serialVersionUID = -6042841879884545180L;

	/**
	 * 交易类型 0:出售
	 */
	public static final Integer ORDERTYPE0 = 0;
	
	/**
	 * 交易类型 1:购买
	 */
	public static final Integer ORDERTYPE1 = 1;
	
	/**
	 * 状态 0:未付款
	 */
	public static final Integer STATUS0 = 0;
	
	/**
	 * 状态 1:已付款
	 */
	public static final Integer STATUS1 = 1;
	
	/**
	 * 状态 2:已完成
	 */
	public static final Integer STATUS2 = 2;
	
	/**
	 * 状态 3:已取消
	 */
	public static final Integer STATUS3 = 3;
	
	/**
	 * 状态 4:申诉中
	 */
	public static final Integer STATUS4 = 4;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;
	
	/**
	 * 订单号
	 */
	@NotNull(message = "订单号不能为空")
	@Length(max = 128, message = "订单号长度必须介于1和128之间")
	@Words(field = "订单号", message = "订单号包含敏感词")
	@Column(name = "orderNum")
	private String orderNum;
	
	/**
	 * 项目id
	 */
	@NotNull(message = "项目id不能为空")
	@Length(max = 64, message = "项目id长度必须介于1和64之间")
	@Words(field = "项目id", message = "项目id包含敏感词")
	@Column(name = "projectId")
	private String projectId;
	/**
	 * 交易币种
	 */
	@NotNull(message = "交易币种不能为空")
	@Length(max = 20, message = "交易币种长度必须介于1和20之间")
	@Words(field = "交易币种", message = "交易币种包含敏感词")
	@Column(name = "orderCoin")
	private String orderCoin;
	
	/**
	 * 交易类型
	 */
	@NotNull(message = "交易类型不能为空")
	@Column(name = "orderType")
	private Integer orderType;
	
	/**
	 * 交易数量
	 */
	@NotNull(message = "交易数量不能为空")
	@Money(point = 15, message = "交易数量金额格式错误")
	@Column(name = "orderCount")
	private BigDecimal orderCount;
	
	/**
	 * 交易金额
	 */
	@NotNull(message = "交易金额不能为空")
	@Money(point = 15, message = "交易金额金额格式错误")
	@Column(name = "orderMoney")
	private BigDecimal orderMoney;
	
	/**
	 * 币种
	 */
	@Length(max = 10, message = "币种长度必须介于1和10之间")
	@Words(field = "币种", message = "币种包含敏感词")
	@Column(name = "currency")
	private String currency;
	
	/**
	 * 交易手续费
	 */
	@NotNull(message = "交易手续费不能为空")
	@Money(point = 15, message = "交易手续费金额格式错误")
	@Column(name = "orderFee")
	private BigDecimal orderFee;
	
	/**
	 * 用户
	 */
	@NotNull(message = "用户不能为空")
	@Length(max = 50, message = "用户长度必须介于1和50之间")
	@Words(field = "用户", message = "用户包含敏感词")
	@Column(name = "userName")
	private String userName;
	
	/**
	 * 用户id
	 */
	@NotNull(message = "用户id不能为空")
	@Length(max = 64, message = "用户id长度必须介于1和64之间")
	@Words(field = "用户id", message = "用户id包含敏感词")
	@Column(name = "userId")
	private String userId;
	
	/**
	 * 付款备注
	 */
	@Length(max = 20, message = "付款备注长度必须介于1和20之间")
	@Words(field = "付款备注", message = "付款备注包含敏感词")
	@Column(name = "orderRemarkNum")
	private String orderRemarkNum;
	
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

}
