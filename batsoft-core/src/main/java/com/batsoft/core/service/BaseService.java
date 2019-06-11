/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月13日 下午4:47:49
 */
package com.batsoft.core.service;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.model.SaveResult;

import java.io.Serializable;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2016年12月13日 下午4:47:49 
 */
 public interface BaseService<T extends Serializable, PK extends Serializable> {

	/**
	 * 保存一个对象
	 * 
	 * @param t
	 * @return
	 */
	public SaveResult save(T t);

	/**
	 * 保存一批对象
	 * 
	 * @param l
	 */
	public void saveAll(List<T> l);
	
	
	/**
	 * 根据主键删除对象
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param t
	 * @return: void 
	 * @Date :          2016年3月17日 下午5:19:44   
	 * @throws:
	 */
	public boolean delete(PK pk);
	
	
	/**
	 * 按条件删除
	 * 删除对象
	 * 
	 * @param filter
	 */
	public boolean delete(QueryFilter filter);

	/**
	 * 修改一个对象
	 * 
	 * @param t
	 */
	public int update(T t);

	/**
	 * 获得一个对象
	 * 
	 * @param pk
	 *            主键
	 * @return
	 */
	public T get(PK pk);
	/**
	 * 修改一个对象,更新null值
	 * 
	 * @param t
	 */
	public void updateNull(T t);
	/**
	 * 获得第一个对象
	 * 
	 * @param filter
	 * @return
	 */
	public T get(QueryFilter filter);

	

	/**
	 * 查找所有
	 * 
	 * @return
	 */
	public List<T> findAll();
	

	/**
	 * 查找所有符合条件的
	 * 
	 * @param filter
	 * @return
	 */
	public List<T> find(QueryFilter filter);
	
	

	/**
	 * 统计所有
	 * 
	 * @return
	 */
	public Long getCount(T t);
	

	/**
	 * 统计符合条件的
	 * 
	 * @param filter
	 * @return
	 */
	public Long getCount(QueryFilter filter);
	
	
	/**  单表分页方法
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param filter
	 * @return: void 
	 * @Date :          2016年3月17日 上午10:46:29   
	 * @throws:
	 */
	public PageResult findPage(QueryFilter filter);

	/**
	 * 创建返回JsonResult
	 * @param flag 成功或失败标识
	 * @param msg  提示信息
	 * @return
	 */
	JsonResult createJsonResult(boolean flag, String msg);

}