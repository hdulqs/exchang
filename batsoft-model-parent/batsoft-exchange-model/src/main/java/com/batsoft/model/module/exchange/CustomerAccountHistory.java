package com.batsoft.model.module.exchange;

import java.math.BigDecimal;
import java.util.Date;

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

/**
 * 用户货币账户历史
 * 
 */
@Data
@Entity
@Table(name = "exchange_customer_account_history")
public class CustomerAccountHistory extends BaseModel {

	private static final long serialVersionUID = 4518736076521554092L;

	/**
	 * 状态 0:正常
	 */
	public static final Integer STATUS0 = 0;

	/**
	 * 状态 1:账户冻结
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 账户账号
	 */
	@Length(max = 50, message = "账户账号长度必须介于1和50之间")
	@Words(field = "账户账号", message = "账户账号包含敏感词")
	@Column(name = "account_number")
	private String accountNumber;

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
	@Length(max = 50, message = "币种长度必须介于1和50之间")
	@Words(field = "币种", message = "币种包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;

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
	@Column(name = "memo")
	private String memo;

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

	/**
	 * 警告类型
	 */
	@Column(name = "warning")
	private Integer warning;

	/**
	 * 警告备注
	 */
	@Column(name = "warning_remark")
	private String warningRemark;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
