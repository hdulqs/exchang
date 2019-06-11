/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-28 10:34:53
 */

package com.batsoft.model.module.cms;

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

/**
 *
 * <p>
 * Single
 * </p>
 * 
 * @author: LouSir
 * @Date : 2018-05-28 10:34:53
 */
@Entity
@ToString
@Table(name = "cms_single")
public class Single extends BaseModel {

	private static final long serialVersionUID = 8842284982249390888L;
	
	/**
	 * 是否显示 0:隐藏
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 是否显示 1:显示
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * id
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 简标题
	 */

	@NotNull(message = "简标题不能为空")
	@Length(max = 50, message = "简标题长度必须介于1和50之间")
	@Words(field = "简标题", message = "简标题包含敏感词")
	@Column(name = "nav")
	private String nav;

	/**
	 * 文章标识
	 */
	@Length(max = 30, message = "文章标识长度必须介于1和30之间")
	@Column(name = "single_key")
	private String singleKey;
	/**
	 * 文章题目
	 */

	@Length(max = 100, message = "文章题目长度必须介于1和100之间")
	@Words(field = "文章题目", message = "文章题目包含敏感词")
	@Column(name = "title")
	private String title;
	/**
	 * 文章内容
	 */

	@NotNull(message = "文章内容不能为空")
	@Words(field = "文章内容", message = "文章内容包含敏感词")
	@Column(name = "single_form")
	private String singleForm;
	/**
	 * 是否显示
	 */

	@Column(name = "status")
	private Integer status;
	
	/**
	 * logo
	 */
	@Length(max = 255, message = "logo长度必须介于1和255之间")
	@Words(field = "logo", message = "logo包含敏感词")
	@Column(name = "logo_path")
	private String logoPath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNav() {
		return nav;
	}

	public void setNav(String nav) {
		this.nav = nav;
	}

	public String getSingleKey() {
		return singleKey;
	}

	public void setSingleKey(String singleKey) {
		this.singleKey = singleKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSingleForm() {
		return singleForm;
	}

	public void setSingleForm(String singleForm) {
		this.singleForm = singleForm;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

}
