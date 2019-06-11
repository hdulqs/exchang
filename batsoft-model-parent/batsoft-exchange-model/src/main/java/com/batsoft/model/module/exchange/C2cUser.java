/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-07 09:21:46
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
 * 发布交易匹配信息
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_c2c_user")
public class C2cUser extends BaseModel {

	private static final long serialVersionUID = -7985969068905573067L;

	/**
	 * 是否显示 0:隐藏
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 是否显示 1:显示
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * 类型
	 */
	public static final String TYPE1 = "A类";

	public static final String TYPE2 = "B类";

	public static final String TYPE3 = "C类";
	/**
	 * id
	 */

	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 商户号
	 */
	@NotNull(message = "商户号不能为空")
	@Length(max = 50, message = "商户号长度必须介于1和50之间")
	@Words(field = "商户号", message = "商户号包含敏感词")
	@Column(name = "user_name")
	private String userName;

	/**
	 * 银行开户名
	 */
	@Length(max = 255, message = "银行开户名长度必须介于1和255之间")
	@Words(field = "银行开户名", message = "银行开户名包含敏感词")
	@Column(name = "true_name")
	private String trueName;

	/**
	 * 手机号码
	 */
	@Length(max = 30, message = "手机号码长度必须介于1和30之间")
	@Words(field = "手机号码", message = "手机号码包含敏感词")
	@Column(name = "telephone")
	private String telephone;

	/**
	 * 商户证件号
	 */
	@Length(max = 50, message = "商户证件号长度必须介于1和50之间")
	@Words(field = "商户证件号", message = "商户证件号包含敏感词")
	@Column(name = "user_card")
	private String userCard;

	/**
	 * 银行ID
	 */
	@Length(max = 64, message = "银行ID长度必须介于1和64之间")
	@Words(field = "银行ID", message = "银行ID包含敏感词")
	@Column(name = "bank_id")
	private String bankId;

	/**
	 * 银行名称
	 */
	@NotNull(message = "银行名称不能为空")
	@Length(max = 255, message = "银行名称长度必须介于1和255之间")
	@Words(field = "银行名称", message = "银行名称包含敏感词")
	@Column(name = "bank_name")
	private String bankName;
	/**
	 * 支行名称
	 */

	@Length(max = 255, message = "支行名称长度必须介于1和255之间")
	@Words(field = "支行名称", message = "支行名称包含敏感词")
	@Column(name = "branch_name")
	private String branchName;
	/**
	 * 银行卡号
	 */

	@NotNull(message = "银行卡号不能为空")
	@Length(max = 50, message = "银行卡号长度必须介于1和50之间")
	@Words(field = "银行卡号", message = "银行卡号包含敏感词")
	@Column(name = "card_number")
	private String cardNumber;
	/**
	 * 币种名称
	 */

	@Length(max = 30, message = "币种名称长度必须介于1和30之间")
	@Words(field = "币种名称", message = "币种名称包含敏感词")
	@Column(name = "coin_name")
	private String coinName;

	/**
	 * 币种代码
	 */
	@Length(max = 30, message = "币种代码长度必须介于1和30之间")
	@Words(field = "币种代码", message = "币种代码包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;

	/**
	 * 币种ID
	 */
	@Length(max = 64, message = "币种ID长度必须介于1和64之间")
	@Words(field = "币种ID", message = "币种ID包含敏感词")
	@Column(name = "coin_id")
	private String coinId;

	/**
	 * 商户类型
	 */
	@Column(name = "type")
	private String type;

	@NotNull(message = "币账户类型不能为空")
	@Column(name = "coin_number")
	private BigDecimal coinNumber;

	/**
	 * 对人民币折算比例
	 */
	@Money(point = 15, message = "对人民币折算比例金额格式错误")
	@Column(name = "coin_proportion")
	private BigDecimal coinProportion;

	/**
	 * 是否显示
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 备注
	 */
	@Length(max = 255, message = "备注长度必须介于1和255之间")
	@Words(field = "备注", message = "备注包含敏感词")
	@Column(name = "remark")
	private String remark;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

}
