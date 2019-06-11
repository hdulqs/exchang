/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 17:46:20
 */

package com.batsoft.model.module.exchange;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import com.batsoft.utils.annotation.Money;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * 数字货币充值记录
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_coin_recharge")
public class CoinRecharge extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2099646572931072440L;
	/**
	 * 状态 0:等待中
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:成功
	 */
	public static final Integer STATUS1 = 1;
	/**
	 * 状态 2:失败
	 */
	public static final Integer STATUS2 = 2;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 转入地址
	 */
	@NotNull(message = "转入地址不能为空")
	@Length(max = 255, message = "转入地址长度必须介于1和255之间")
	@Words(field = "转入地址", message = "转入地址包含敏感词")
	@Column(name = "in_address")
	private String inAddress;
	/**
	 * 接受地址
	 */
	@NotNull(message = "接受地址不能为空")
	@Length(max = 255, message = "接受地址长度必须介于1和255之间")
	@Words(field = "接受地址", message = "接受地址包含敏感词")
	@Column(name = "to_address")
	private String toAddress;
	/**
	 * 币种代码
	 */
	@NotNull(message = "币种代码不能为空")
	@Length(max = 50, message = "币种代码长度必须介于1和50之间")
	@Words(field = "币种代码", message = "币种代码包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;
	/**
	 * 转入数量
	 */
	@Money(point = 15, message = "转入数量金额格式错误")
	@Column(name = "coin_count")
	private BigDecimal coinCount;
	/**
	 * 矿工费
	 */
	@Money(point = 15, message = "矿工费金额格式错误")
	@Column(name = "fee")
	private BigDecimal fee;
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 交易单号
	 */
	@NotNull(message = "交易单号不能为空")
	@Length(max = 150, message = "交易单号长度必须介于1和150之间")
	@Words(field = "交易单号", message = "交易单号包含敏感词")
	@Column(name = "tx_order")
	private String txOrder;
	/**
	 * 用户id
	 */
	@NotNull(message = "用户id不能为空")
	@Length(max = 64, message = "用户id长度必须介于1和64之间")
	@Words(field = "用户id", message = "用户id包含敏感词")
	@Column(name = "user_id")
	private String userId;
	/**
	 * 币账户对应id
	 */
	@Length(max = 64, message = "币账户对应id长度必须介于1和64之间")
	@Words(field = "币账户对应id", message = "币账户对应id包含敏感词")
	@Column(name = "account_id")
	private String accountId;
	/**
	 * 用户名
	 */
	@Length(max = 50, message = "用户名长度必须介于1和50之间")
	@Words(field = "用户名", message = "用户名包含敏感词")
	@Column(name = "user_name")
	private String userName;
	/**
	 * 用户手机号
	 */
	@Length(max = 20, message = "用户手机号长度必须介于1和20之间")
	@Words(field = "用户手机号", message = "用户手机号包含敏感词")
	@Column(name = "user_mobile")
	private String userMobile;
	/**
	 * 用户邮箱
	 */
	@Length(max = 255, message = "用户邮箱长度必须介于1和255之间")
	@Words(field = "用户邮箱", message = "用户邮箱包含敏感词")
	@Column(name = "user_email")
	private String userEmail;
	/**
	 * 备注
	 */
	@Length(max = 255, message = "备注长度必须介于1和255之间")
	@Words(field = "备注", message = "备注包含敏感词")
	@Column(name = "remark")
	private String remark;

}
