
package com.batsoft.core.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 封装JsTree结果集
 * 
 * 适合加载全部数据
 * 
 * @author Bat Admin
 *
 */
@Data
public class JsTree implements Serializable{

	/**
	 * id
	 */
	private String id = "";
	/**
	 * 父id
	 */
	private String parentId="";
	/**
	 * text
	 */
	private String text = "";
	/**
	 * icon
	 */
	private String icon = "";
	/**
	 * sort
	 */
	private Integer sort = 0;
	/**
	 * 备用字段如 url等
	 */
	private String remark = "";
	/**
	 * 备用字段
	 */
	private String remark1 = "";
	/**
	 * key
	 */
	private String nodeKey="";
	/**
	 * children
 	 */
	private List<JsTree> children ;

	/**
	 * 以下参数提供 ant design tree 使用
	 */
	private String label="";
	private String value="";
	private String key="";

}
