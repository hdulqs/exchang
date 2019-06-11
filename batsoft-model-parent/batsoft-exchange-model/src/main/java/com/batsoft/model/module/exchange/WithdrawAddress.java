/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/

package com.batsoft.model.module.exchange;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;

import lombok.Data;
import lombok.ToString;

/**
 * 提现地址
 * 
 */
@Data
@Entity
@ToString
@Table(name = "exchange_withdraw_address")
public class WithdrawAddress extends BaseModel {

	private static final long serialVersionUID = -4641405969678308238L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 提币地址
	 */
	@NotNull(message = "提币地址不能为空")
	@Length(max = 255, message = "提币地址长度必须介于1和255之间")
	@Words(field = "提币地址", message = "提币地址包含敏感词")
	@Column(name = "coin_address")
	private String coinAddress;

	/**
	 * memo地址
	 */
	@Length(max = 255, message = "memo地址长度必须介于1和255之间")
	@Words(field = "memo地址", message = "memo地址包含敏感词")
	@Column(name = "memo")
	private String memo;

	/**
	 * 币种代码
	 */
	@NotNull(message = "币种代码不能为空")
	@Length(max = 50, message = "币种代码长度必须介于1和50之间")
	@Words(field = "币种代码", message = "币种代码包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;

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
