/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:24:31 
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
 * Banner
 * </p>
 * 
 * @author: Bat Admin
 * @Date : 2018-04-14 10:24:31
 */
@Entity
@ToString
@Table(name = "cms_banner")
public class Banner extends BaseModel {

	private static final long serialVersionUID = 3537766581002346854L;

	/**
	 * 是否显示 0:隐藏
	 */
	public static final Integer DISPLAY0 = 0;

	/**
	 * 是否显示 1:显示
	 */
	public static final Integer DISPLAY1 = 1;

	/**
	* 
	*/

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * Banner名称
	 */

	@NotNull(message = "Banner名称不能为空")
	@Length(max = 100, message = "Banner名称长度必须介于1和100之间")
	@Words(field = "Banner名称", message = "Banner名称包含敏感词")
	@Column(name = "name")
	private String name;
	/**
	 * 链接地址
	 */

	@Length(max = 255, message = "链接地址长度必须介于1和255之间")
	@Words(field = "链接地址", message = "链接地址包含敏感词")
	@Column(name = "url")
	private String url;
	/**
	 * banner图片
	 */

	@NotNull(message = "banner图片不能为空")
	@Length(max = 255, message = "banner图片长度必须介于1和255之间")
	@Words(field = "banner图片", message = "banner图片包含敏感词")
	@Column(name = "image")
	private String image;
	/**
	 * 是否显示
	 */

	@Column(name = "display")
	private Integer display;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
