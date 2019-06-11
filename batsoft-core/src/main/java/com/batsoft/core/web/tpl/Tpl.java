package com.batsoft.core.web.tpl;

import java.util.Date;

/**
 * 模板接口
 */
 public interface Tpl {
	/**
	 * 获得模板完整名称，是文件的唯一标识。
	 * 
	 * @return
	 */
	 String getName();

	/**
	 * 获得路径，不包含文件名的路径。
	 * 
	 * @return
	 */
	 String getPath();

	/**
	 * 获得模板名称，不包含路径的文件名。
	 * 
	 * @return
	 */
	 String getFilename();

	/**
	 * 获得模板内容
	 * 
	 * @return
	 */
	 String getSource();

	/**
	 * 获得最后修改时间（毫秒）
	 * 
	 * @return
	 */
	 long getLastModified();

	/**
	 * 获得最后修改时间（日期）
	 * 
	 * @return
	 */
	 Date getLastModifiedDate();

	/**
	 * 获得文件大小，单位bytes
	 * 
	 * @return
	 */
	 long getLength();

	/**
	 * 获得文件大小，单位K bytes
	 * 
	 * @return
	 */
	 int getSize();

	/**
	 * 是否目录
	 * 
	 * @return
	 */
	 boolean isDirectory();
}
