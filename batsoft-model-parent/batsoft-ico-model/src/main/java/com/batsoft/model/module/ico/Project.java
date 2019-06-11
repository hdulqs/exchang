/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Lucl
 * @version: V1.0
 * @Date: 2017-08-22 14:13:21
 */

package com.batsoft.model.module.ico;

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
 * <p>
 * Project
 * </p>
 *
 * @author: Lucl
 * @Date : 2017-08-22 14:13:21
 */
@Entity
@Data
@ToString
@Table(name = "ico_project")
public class Project extends BaseModel {

	private static final long serialVersionUID = 2535517344778184821L;
	
	/**
	 * 项目阶段 0:未启动
	 */
	public static final Integer PROJECTLEVEL0 = 0;
	/**
	 * 项目阶段 1:开发中
	 */
	public static final Integer PROJECTLEVEL1 = 1;
	/**
	 * 项目阶段 2:已上市
	 */
	public static final Integer PROJECTLEVEL2 = 2;
	/**
	 * 项目阶段 3:已盈利
	 */
	public static final Integer PROJECTLEVEL3 = 3;
	/**
	 * 融资阶段 0:天使轮
	 */
	public static final Integer FINANCINGPHASE0 = 0;
	/**
	 * 融资阶段 1:A轮
	 */
	public static final Integer FINANCINGPHASE1 = 1;
	/**
	 * 融资阶段 2:B轮
	 */
	public static final Integer FINANCINGPHASE2 = 2;
	/**
	 * 融资阶段 3:C轮
	 */
	public static final Integer FINANCINGPHASE3 = 3;
	/**
	 * 融资阶段 4:D轮
	 */
	public static final Integer FINANCINGPHASE4 = 4;
	/**
	 * 融资阶段 5:未融资
	 */
	public static final Integer FINANCINGPHASE5 = 5;
	/**
	 * 币种 BTC:BTC
	 */
	public static final String CURRENCYBTC = "BTC";
	/**
	 * 币种 ETH:ETH
	 */
	public static final String CURRENCYETH = "ETH";
	/**
	 * 币种 ETC:ETC
	 */
	public static final String CURRENCYETC = "ETC";
	/**
	 * 投资金额是否限制 0:否
	 */
	public static final Integer IVESTLIMIT0 = 0;
	/**
	 * 投资金额是否限制 1:是
	 */
	public static final Integer IVESTLIMIT1 = 1;
	/**
	 * 名额限制 0:否
	 */
	public static final Integer NUMLIMIT0 = 0;
	/**
	 * 名额限制 1:是
	 */
	public static final Integer NUMLIMIT1 = 1;
	/**
	 * 支持限制 0:否
	 */
	public static final Integer SUPPORTLIMIT0 = 0;
	/**
	 * 支持限制 1:是
	 */
	public static final Integer SUPPORTLIMIT1 = 1;
	/**
	 * 是否包邮 0:否
	 */
	public static final Integer FREEMAIL0 = 0;
	/**
	 * 是否包邮 1:是
	 */
	public static final Integer FREEMAIL1 = 1;
	/**
	 * 目标金额 0:不限
	 */
	public static final String TARGETAMOUNT0 = "0";
	/**
	 * 目标金额 1:限制
	 */
	public static final String TARGETAMOUNT1 = "1";
	/**
	 * 审核状态 0:申请中
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 审核状态 2:审核通过
	 */
	public static final Integer STATUS2 = 2;
	/**
	 * 审核状态 3:拒绝
	 */
	public static final Integer STATUS3 = 3;

	/**
	 * id
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 项目名称
	 */

	@NotNull(message = "项目名称不能为空")
	@Length(max = 50, message = "项目名称长度必须介于1和50之间")
	@Words(field = "项目名称", message = "项目名称包含敏感词")
	@Column(name = "projectName")
	private String projectName;
	/**
	 * 项目阶段
	 */

	@Column(name = "projectLevel")
	private Integer projectLevel;
	/**
	 * 真实姓名
	 */

	@NotNull(message = "真实姓名不能为空")
	@Length(max = 30, message = "真实姓名长度必须介于1和30之间")
	@Words(field = "真实姓名", message = "真实姓名包含敏感词")
	@Column(name = "truename")
	private String truename;
	/**
	 * 国籍
	 */

	@Length(max = 30, message = "国籍长度必须介于1和30之间")
	@Words(field = "国籍", message = "国籍包含敏感词")
	@Column(name = "nationality")
	private String nationality;
	/**
	 * 详细地址
	 */

	@Length(max = 255, message = "详细地址长度必须介于1和255之间")
	@Words(field = "详细地址", message = "详细地址包含敏感词")
	@Column(name = "adress")
	private String adress;
	/**
	 * 团队人数
	 */

	@Column(name = "teamNum")
	private Integer teamNum;
	/**
	 * 融资阶段
	 */

	@Column(name = "financingPhase")
	private Integer financingPhase;
	
	/**
	 * 项目简介
	 */
	@Words(field = "项目简介", message = "项目简介包含敏感词")
	@Column(name = "projectBrief")
	private String projectBrief;
	
	/**
	 * 宣传视频
	 */
	@Length(max = 100, message = "宣传视频长度必须介于1和100之间")
	@Words(field = "宣传视频", message = "宣传视频包含敏感词")
	@Column(name = "propagandaUrl")
	private String propagandaUrl;
	
	/**
	 * 项目介绍
	 */
	@Words(field = "项目介绍", message = "项目介绍包含敏感词")
	@Column(name = "projectIntroduction")
	private String projectIntroduction;
	
	/**
	 * ico细则
	 */
	@Words(field = "ico细则", message = "ico细则包含敏感词")
	@Column(name = "icoDetails")
	private String icoDetails;
	
	/**
	 * 团队介绍
	 */
	@Words(field = "团队介绍", message = "团队介绍包含敏感词")
	@Column(name = "teamIntroduction")
	private String teamIntroduction;
	
	/**
	 * 币种
	 */
	@NotNull(message = "币种不能为空")
	@Length(max = 20, message = "币种长度必须介于1和20之间")
	@Words(field = "币种", message = "币种包含敏感词")
	@Column(name = "currency")
	private String currency;
	
	/**
	 * 投资金额是否限制
	 */
	@Column(name = "ivestLimit")
	private Integer ivestLimit;
	
	/**
	 * 投资数额
	 */
	@NotNull(message = "投资数额不能为空")
	@Money(point = 15, message = "投资数额金额格式错误")
	@Column(name = "amount")
	private BigDecimal amount;
	
	/**
	 * 回报说明
	 */
	@Length(max = 255, message = "回报说明长度必须介于1和255之间")
	@Words(field = "回报说明", message = "回报说明包含敏感词")
	@Column(name = "repayment")
	private String repayment;
	
	/**
	 * 名额限制
	 */
	@Column(name = "numLimit")
	private Integer numLimit;
	
	/**
	 * 支持限制
	 */
	@Column(name = "supportLimit")
	private Integer supportLimit;
	
	/**
	 * 是否包邮
	 */
	@Column(name = "freeMail")
	private Integer freeMail;
	
	/**
	 * 回报时间(天)--预计ico结束？天内回报
	 */
	@Column(name = "backTime")
	private Integer backTime;
	
	/**
	 * ICO天数
	 */
	@Column(name = "icoDays")
	private Integer icoDays;
	
	/**
	 * 联系人
	 */
	@Length(max = 30, message = "联系人长度必须介于1和30之间")
	@Words(field = "联系人", message = "联系人包含敏感词")
	@Column(name = "contactPerson")
	private String contactPerson;
	
	/**
	 * 联系电话
	 */
	@Length(max = 30, message = "联系电话长度必须介于1和30之间")
	@Words(field = "联系电话", message = "联系电话包含敏感词")
	@Column(name = "telphone")
	private String telphone;
	
	/**
	 * 开始时间(申请多少天开始)
	 */
	@Column(name = "startTime")
	private Integer startTime;
	
	/**
	 * 目标金额
	 */
	@Length(max = 25, message = "目标金额长度必须介于1和25之间")
	@Words(field = "目标金额", message = "目标金额包含敏感词")
	@Column(name = "targetAmount")
	private String targetAmount;
	
	/**
	 * 审核状态
	 */
	@Column(name = "status")
	private Integer status;
	
	/**
	 * 创建人Id
	 */
	@Column(name = "createId")
	private Long createId;
	
	/**
	 * 创建人
	 */
	@Length(max = 30, message = "创建人长度必须介于1和30之间")
	@Words(field = "创建人", message = "创建人包含敏感词")
	@Column(name = "creator")
	private String creator;

}
