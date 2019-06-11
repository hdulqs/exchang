package com.batsoft.model.module.log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;

import lombok.Data;
import lombok.ToString;

/**
 * API请求日志
 * 
 */
@Entity
@Data
@ToString
@Table(name = "log_api")
public class Api extends BaseModel {

	private static final long serialVersionUID = 2506600639208952459L;
	
	/**
	 * 状态 0:接收成功
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:回调成功
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * id
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 请求地址
	 */

	@Length(max = 255, message = "请求地址长度必须介于1和255之间")
	@Words(field = "请求地址", message = "请求地址包含敏感词")
	@Column(name = "url")
	private String url;
	/**
	 * ip地址
	 */

	@Length(max = 50, message = "ip地址长度必须介于1和50之间")
	@Words(field = "ip地址", message = "ip地址包含敏感词")
	@Column(name = "ip")
	private String ip;
	/**
	 * 请求方法
	 */

	@Length(max = 255, message = "请求方法长度必须介于1和255之间")
	@Words(field = "请求方法", message = "请求方法包含敏感词")
	@Column(name = "method")
	private String method;
	/**
	 * 请求类型（application/json等等）
	 */

	@Length(max = 255, message = "请求类型（application/json等等）长度必须介于1和255之间")
	@Words(field = "请求类型（application/json等等）", message = "请求类型（application/json等等）包含敏感词")
	@Column(name = "media_type")
	private String mediaType;
	/**
	 * 请求方法（get/post）
	 */

	@Length(max = 10, message = "请求方法（get/post）长度必须介于1和10之间")
	@Words(field = "请求方法（get/post）", message = "请求方法（get/post）包含敏感词")
	@Column(name = "request_method")
	private String requestMethod;
	/**
	 * 回调参数
	 */

	@Words(field = "回调参数", message = "回调参数包含敏感词")
	@Column(name = "callback_params")
	private String callbackParams;
	/**
	 * 回调次数
	 */

	@Column(name = "callback_num")
	private Integer callbackNum;
	/**
	 * 来源(app/pc)
	 */

	@Length(max = 50, message = "来源(app/pc)长度必须介于1和50之间")
	@Words(field = "来源(app/pc)", message = "来源(app/pc)包含敏感词")
	@Column(name = "request_from")
	private String requestFrom;
	/**
	 * 执行时间MS
	 */

	@Column(name = "execute_time")
	private Long executeTime;
	/**
	 * 状态
	 */

	@Column(name = "status")
	private Integer status;
	/**
	 * 请求参数
	 */

	@Words(field = "请求参数", message = "请求参数包含敏感词")
	@Column(name = "params")
	private String params;
	/**
	 * 备注
	 */

	@Words(field = "备注", message = "备注包含敏感词")
	@Column(name = "remark")
	private String remark;

}
