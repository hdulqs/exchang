/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-07 09:22:51
 */

package com.batsoft.model.module.member;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 绑定银行卡记录
 * 
 */
@Entity
@Data
@ToString
@Table(name = "member_bankcard")
public class Bankcard extends BaseModel {

	private static final long serialVersionUID = -2502882467323713440L;
	
	/**
	 * 是否有效 0:无效
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 是否有效 1:有效
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * id
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 用户ID
	 */

	@NotNull(message = "用户ID不能为空")
	@Length(max = 64, message = "用户ID长度必须介于1和64之间")
	@Words(field = "用户ID", message = "用户ID包含敏感词")
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
	 * 银行ID
	 */

	@Length(max = 64, message = "银行ID长度必须介于1和64之间")
	@Words(field = "银行ID", message = "银行ID包含敏感词")
	@Column(name = "bank_id")
	private String bankId;
	/**
	 * 银行卡号
	 */

	@NotNull(message = "银行卡号不能为空")
	@Length(max = 50, message = "银行卡号长度必须介于1和50之间")
	@Words(field = "银行卡号", message = "银行卡号包含敏感词")
	@Column(name = "bank_card")
	private String bankCard;
	/**
	 * 银行名称
	 */

	@NotNull(message = "银行名称不能为空")
	@Length(max = 100, message = "银行名称长度必须介于1和100之间")
	@Words(field = "银行名称", message = "银行名称包含敏感词")
	@Column(name = "bank_name")
	private String bankName;
	/**
	 * 支行名称
	 */

	@Length(max = 100, message = "支行名称长度必须介于1和100之间")
	@Words(field = "支行名称", message = "支行名称包含敏感词")
	@Column(name = "branch_name")
	private String branchName;
	/**
	 * 开户省
	 */

	@Length(max = 30, message = "开户省长度必须介于1和30之间")
	@Words(field = "开户省", message = "开户省包含敏感词")
	@Column(name = "province")
	private String province;
	/**
	 * 开户市
	 */

	@Length(max = 30, message = "开户市长度必须介于1和30之间")
	@Words(field = "开户市", message = "开户市包含敏感词")
	@Column(name = "city")
	private String city;
	/**
	 * 是否有效
	 */

	@Column(name = "status")
	private Integer status;

}
