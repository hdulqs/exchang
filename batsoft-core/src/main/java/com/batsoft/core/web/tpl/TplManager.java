package com.batsoft.core.web.tpl;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 模板Service
 */
 public interface TplManager {
	/**
	 * 获得模板列表。根据前缀，用于选择模板。
	 * 
	 * @param prefix
	 *            前缀
	 * @return
	 */
	 List<? extends Tpl> getListByPrefix(String prefix);

	 List<String> getNameListByPrefix(String prefix);

	/**
	 * 获得下级模板列表。根据路径，用于展现下级目录和文件。
	 * 
	 * @param path
	 *            路径
	 * @return
	 */
	 List<? extends Tpl> getChild(String path);

	/**
	 * 保存模板
	 * 
	 * @param name
	 *            模板名称
	 * @param source
	 *            模板内容
	 * @param isDirectory
	 *            是否目录
	 */
	 void save(String name, String source, boolean isDirectory);

	/**
	 * 保存模板
	 * 
	 * @param path
	 * @param file
	 */
	 void save(String path, MultipartFile file);

	/**
	 * 获得模板
	 * 
	 * @param name
	 * @return
	 */
	 Tpl get(String name);

	/**
	 * 更新模板
	 * 
	 * @param name
	 *            模板名称
	 * @param source
	 *            模板内容
	 */
	 void update(String name, String source);

	/**
	 * 修改模板名称或路径
	 * 
	 * @param orig
	 * @param dist
	 */
	 void rename(String orig, String dist);

	/**
	 * 删除模板
	 * 
	 * @param names
	 *            模板名称数组
	 * @return 被删除的模板数量
	 */
	 int delete(String[] names);

}
