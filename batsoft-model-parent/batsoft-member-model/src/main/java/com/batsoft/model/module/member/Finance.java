/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-17 18:52:28
 */

package com.batsoft.model.module.member;

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
 * 账户余额
 * 
 */
@Entity
@Data
@ToString
@Table(name = "member_finance")
public class Finance extends BaseModel {

	private static final long serialVersionUID = -200536435067091571L;
	
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
	 * 货币名称
	 */

	@NotNull(message = "货币名称不能为空")
	@Length(max = 100, message = "货币名称长度必须介于1和100之间")
	@Words(field = "货币名称", message = "货币名称包含敏感词")
	@Column(name = "currency_name")
	private String currencyName;
	
	/**
	 * 币种类型
	 */
	@Length(max = 20, message = "币种类型长度必须介于1和20之间")
	@Words(field = "币种类型", message = "币种类型包含敏感词")
	@Column(name = "currency_code")
	private String currencyCode;
	
	/**
	 * 可用金额
	 */
	@Money(point = 15, message = "可用金额金额格式错误")
	@Column(name = "available_money")
	private BigDecimal availableMoney;
	
	/**
	 * 冻结金额
	 */
	@Money(point = 15, message = "冻结金额金额格式错误")
	@Column(name = "freeze_money")
	private BigDecimal freezeMoney;
	/**
	 * 币种logo
	 */

	@Length(max = 255, message = "币种logo长度必须介于1和255之间")
	@Words(field = "币种logo", message = "币种logo包含敏感词")
	@Column(name = "currency_logo")
	private String currencyLogo;
	/**
	 * 用户id
	 */

	@NotNull(message = "用户id不能为空")
	@Length(max = 64, message = "用户id长度必须介于1和64之间")
	@Words(field = "用户id", message = "用户id包含敏感词")
	@Column(name = "user_id")
	private String userId;
	/**
	 * 用户真实姓名
	 */

	@Length(max = 64, message = "用户真实姓名长度必须介于1和64之间")
	@Words(field = "用户真实姓名", message = "用户真实姓名包含敏感词")
	@Column(name = "user_real_name")
	private String userRealName;
	/**
	 * 用户手机号
	 */

	@Length(max = 64, message = "用户手机号长度必须介于1和64之间")
	@Words(field = "用户手机号", message = "用户手机号包含敏感词")
	@Column(name = "user_mobile")
	private String userMobile;
	/**
	 * 状态
	 */

	@Column(name = "status")
	private Integer status;

}
