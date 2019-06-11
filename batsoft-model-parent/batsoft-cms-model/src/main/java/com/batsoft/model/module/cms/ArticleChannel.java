/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 10:21:01
 */

package com.batsoft.model.module.cms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
 * 引导文章
 * 
 */
@Entity
@ToString
@Table(name = "cms_article_channel")
public class ArticleChannel extends BaseModel {

	private static final long serialVersionUID = 7236393230381520060L;
	
	/**
	 * 是否首页展示 0:否
	 */
	public static final Integer INDEXSHOW0 = 0;
	/**
	 * 是否首页展示 1:是
	 */
	public static final Integer INDEXSHOW1 = 1;
	/**
	 * 状态 0:禁用
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:可用
	 */
	public static final Integer STATUS1 = 1;

	/**
	 * id
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 文章类别
	 */

	@NotNull(message = "文章类别不能为空")
	@Length(max = 100, message = "文章类别长度必须介于1和100之间")
	@Words(field = "文章类别", message = "文章类别包含敏感词")
	@Column(name = "name")
	private String name;
	/**
	 * 类型Key
	 */

	@NotNull(message = "类型Key不能为空")
	@Length(max = 50, message = "类型Key长度必须介于1和50之间")
	@Words(field = "类型Key", message = "类型Key包含敏感词")
	@Column(name = "type_key")
	private String typeKey;
	/**
	 * 是否首页展示
	 */

	@NotNull(message = "是否首页展示不能为空")
	@Column(name = "index_show")
	private Integer indexShow;
	/**
	 * 所属父类
	 */

	@Length(max = 64, message = "所属父类长度必须介于1和64之间")
	@Words(field = "所属父类", message = "所属父类包含敏感词")
	@Column(name = "parentId")
	private String parentId;
	/**
	 * 状态
	 */

	@NotNull(message = "状态不能为空")
	@Column(name = "status")
	private Integer status;
	/**
	 * 分类描述
	 */

	@Words(field = "分类描述", message = "分类描述包含敏感词")
	@Column(name = "description")
	private String description;
	/**
	 * 图标路径
	 */

	@Length(max = 100, message = "图标路径长度必须介于1和100之间")
	@Words(field = "图标路径", message = "图标路径包含敏感词")
	@Column(name = "icon")
	private String icon;
	/**
	 * 排序
	 */

	@Column(name = "sort")
	private Integer sort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public Integer getIndexShow() {
		return indexShow;
	}

	public void setIndexShow(Integer indexShow) {
		this.indexShow = indexShow;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
