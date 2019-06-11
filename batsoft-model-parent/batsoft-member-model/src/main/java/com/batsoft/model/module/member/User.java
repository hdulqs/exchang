package com.batsoft.model.module.member;

import java.math.BigDecimal;
import java.util.Date;
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
 * 用户
 * 
 */
@Entity
@Data
@ToString
@Table(name = "member_user")
public class User extends BaseModel {

	private static final long serialVersionUID = 1774848389673932803L;

	/**
	 * 是否开启google认证 不开启
	 */
	public static final Integer OPENGOOGLEAUTH0 = 0;
	/**
	 * 是否开启google认证 开启
	 */
	public static final Integer OPENGOOGLEAUTH1 = 1;
	/**
	 * 状态 0:正常
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:禁用
	 */
	public static final Integer STATUS1 = 1;
	/**
	 * 证件类型 0:身份证
	 */
	public static final Integer USERCARDTYPE0 = 0;
	/**
	 * 证件类型 1:护照
	 */
	public static final Integer USERCARDTYPE1 = 1;
	/**
	 * 证件类型 2:其它
	 */
	public static final Integer USERCARDTYPE2 = 2;
	/**
	 * 是否实名 0:未实名
	 */
	public static final Integer REALSTATE_NORMAL = 0;
	/**
	 * 是否实名 1:已实名
	 */
	public static final Integer REALSTATE_SUCCESS = 1;

	/**
	 * 是否实名 2:审核中
	 */
	public static final Integer REALSTATE_WAITING = 2;

	/**
	 * 实名认证失败 3.认证失败
	 */
	public static final Integer REALSTATE_FAILE = 3;

	/**
	 * 用户id
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 用户昵称
	 */

	@Length(max = 30, message = "用户昵称长度必须介于1和30之间")
	@Words(field = "用户昵称", message = "用户昵称包含敏感词")
	@Column(name = "user_nick")
	private String userNick;
	/**
	 * 用户名
	 */

	@NotNull(message = "用户名不能为空")
	@Length(max = 50, message = "用户名长度必须介于1和50之间")
	@Words(field = "用户名", message = "用户名包含敏感词")
	@Column(name = "user_name")
	private String userName;
	/**
	 * 登录密码
	 */

	@NotNull(message = "登录密码不能为空")
	@Length(max = 32, message = "登录密码长度必须介于1和32之间")
	@Words(field = "登录密码", message = "登录密码包含敏感词")
	@Column(name = "password")
	private String password;
	/**
	 * 密码盐
	 */

	@Length(max = 32, message = "密码盐长度必须介于1和32之间")
	@Words(field = "密码盐", message = "密码盐包含敏感词")
	@Column(name = "salt")
	private String salt;
	/**
	 * 真实姓名
	 */

	@Length(max = 50, message = "真实姓名长度必须介于1和50之间")
	@Words(field = "真实姓名", message = "真实姓名包含敏感词")
	@Column(name = "real_name")
	private String realName;
	/**
	 * 手机区域号
	 */

	@Length(max = 10, message = "手机区域号长度必须介于1和10之间")
	@Words(field = "手机区域号", message = "手机区域号包含敏感词")
	@Column(name = "area_code")
	private String areaCode;
	/**
	 * 手机号
	 */

	@Length(max = 15, message = "手机号长度必须介于1和15之间")
	@Words(field = "手机号", message = "手机号包含敏感词")
	@Column(name = "user_mobile")
	private String userMobile;
	/**
	 * 邮箱
	 */

	@Length(max = 20, message = "邮箱长度必须介于1和20之间")
	@Words(field = "邮箱", message = "邮箱包含敏感词")
	@Column(name = "user_email")
	private String userEmail;
	/**
	 * 状态
	 */

	@Column(name = "status")
	private Integer status;
	/**
	 * 证件类型
	 */

	@Column(name = "user_card_type")
	private Integer userCardType;
	/**
	 * 证件号
	 */

	@Length(max = 30, message = "证件号长度必须介于1和30之间")
	@Words(field = "证件号", message = "证件号包含敏感词")
	@Column(name = "user_card_number")
	private String userCardNumber;
	/**
	 * 生日
	 */

	@Column(name = "user_birthday")
	private Long userBirthday;
	/**
	 * 头像
	 */

	@Length(max = 255, message = "头像长度必须介于1和255之间")
	@Words(field = "头像", message = "头像包含敏感词")
	@Column(name = "user_avatar")
	private String userAvatar;
	/**
	 * qq
	 */

	@Length(max = 100, message = "qq长度必须介于1和100之间")
	@Words(field = "qq", message = "qq包含敏感词")
	@Column(name = "user_qq")
	private String userQq;
	/**
	 * 登录次数
	 */

	@Column(name = "login_num")
	private Integer loginNum;
	/**
	 * 当前登录时间
	 */

	@Column(name = "login_time")
	private Date loginTime;
	/**
	 * 上次登录时间
	 */

	@Column(name = "old_login_time")
	private Date oldLoginTime;
	/**
	 * 当前登录ip
	 */

	@Length(max = 20, message = "当前登录ip长度必须介于1和20之间")
	@Words(field = "当前登录ip", message = "当前登录ip包含敏感词")
	@Column(name = "login_ip")
	private String loginIp;
	/**
	 * 上次登录ip
	 */

	@Length(max = 20, message = "上次登录ip长度必须介于1和20之间")
	@Words(field = "上次登录ip", message = "上次登录ip包含敏感词")
	@Column(name = "old_login_ip")
	private String oldLoginIp;
	/**
	 * 证件证明照片
	 */

	@Length(max = 255, message = "证件证明照片长度必须介于1和255之间")
	@Words(field = "证件证明照片", message = "证件证明照片包含敏感词")
	@Column(name = "user_card_front")
	private String userCardFront;
	/**
	 * 证件反面照片
	 */

	@Length(max = 255, message = "证件反面照片长度必须介于1和255之间")
	@Words(field = "证件反面照片", message = "证件反面照片包含敏感词")
	@Column(name = "user_card_back")
	private String userCardBack;

	/**
	 * 是否实名 身份认证状态 0 未审批 1审批成功 2审核中 3审批失败
	 */
	@Column(name = "real_state")
	private Integer realState;

	/**
	 * 审批失败备注信息
	 */
	@Column(name = "real_state_remark")
	@Words(field = "身份证审批备注信息", message = "审批失败的备注信息")
	private String realStateRemark;

	/**
	 * 交易密码
	 */

	@Length(max = 255, message = "交易密码长度必须介于1和255之间")
	@Words(field = "交易密码", message = "交易密码包含敏感词")
	@Column(name = "trade_password")
	private String tradePassword;
	/**
	 * google 验证码
	 */

	@Length(max = 20, message = "google 验证码长度必须介于1和20之间")
	@Words(field = "google 验证码", message = "google 验证码包含敏感词")
	@Column(name = "google_code")
	private String googleCode;
	/**
	 * 手持证件照
	 */

	@Length(max = 255, message = "手持证件照长度必须介于1和255之间")
	@Words(field = "手持证件照", message = "手持证件照包含敏感词")
	@Column(name = "user_card_all")
	private String userCardAll;
	/**
	 * 开启google认证
	 */

	@Column(name = "open_google_auth")
	private Integer openGoogleAuth;
	/**
	 * 被信任人数
	 */

	@Column(name = "trustNum")
	private Integer trustNum;
	/**
	 * 交易次数
	 */

	@Column(name = "exchangeNum")
	private Integer exchangeNum;
	/**
	 * 备注或简介
	 */

	@Length(max = 255, message = "备注或简介长度必须介于1和255之间")
	@Words(field = "备注或简介", message = "备注或简介包含敏感词")
	@Column(name = "remark")
	private String remark;

	/**
	 * 国家
	 */
	@Length(max = 255, message = "国家或地区长度必须介于1和50之间")
	@Words(field = "国家", message = "国家或地区包含敏感词")
	@Column(name = "country")
	private String country;

	/**
	 * 性别 0 男 1女
	 */
	@Column(name = "sex")
	private Integer sex;

	/**
	 * 推广人的码
	 */
	@Column(name = "promoter_code")
	private String promoterCode;

	/**
	 * 父推广人码
	 */
	@Column(name = "promoter_parent_code")
	private String promoterParentCode;

	/**
	 * 推广奖励
	 */
	@Column(name = "promotion_award")
	private BigDecimal promotionAward;
}
