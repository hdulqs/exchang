package com.batsoft.model.module.log;

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
 * 短信发送记录
 * 
 */
@Entity
@Data
@ToString
@Table(name = "log_message_send")
public class MessageSend extends BaseModel {

	private static final long serialVersionUID = -4625163291468970735L;
	
	/**
	 * 发送状态 0:失败
	 */
	public static final Integer STATUS0 = 0;
	
	/**
	 * 发送状态 1:成功
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * ID
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 短信方
	 */

	@Length(max = 100, message = "短信方长度必须介于1和100之间")
	@Words(field = "短信方", message = "短信方包含敏感词")
	@Column(name = "sms_provider")
	private String smsProvider;
	/**
	 * 短信类型
	 */

	@Length(max = 25, message = "短信类型长度必须介于1和25之间")
	@Words(field = "短信类型", message = "短信类型包含敏感词")
	@Column(name = "sms_type")
	private String smsType;
	/**
	 * 短信内容
	 */

	@Words(field = "短信内容", message = "短信内容包含敏感词")
	@Column(name = "sms_content")
	private String smsContent;
	/**
	 * 发送时间
	 */

	@NotNull(message = "发送时间不能为空")
	@Column(name = "send_time")
	private Date sendTime;
	/**
	 * 电话号码
	 */

	@Length(max = 255, message = "电话号码长度必须介于1和255之间")
	@Words(field = "电话号码", message = "电话号码包含敏感词")
	@Column(name = "cellphone")
	private String cellphone;
	/**
	 * 发送状态
	 */

	@Column(name = "status")
	private Integer status;

	/**
	 * 返回信息
	 */
	@Column(name = "msg")
	private String msg;

}
