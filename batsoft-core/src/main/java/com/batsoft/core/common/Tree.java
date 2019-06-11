
package com.batsoft.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装Tree结果集
 * 
 * 适合Ajax异步加载
 * 
 * @author Bat Admin
 *
 */
@Data
public class Tree implements Serializable{

	private String id = "";// id
	private String text = "";// text
	private String icon = "";// icon
	private String nodeKey="";//key
	private boolean children ;// children

}
