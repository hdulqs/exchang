package com.batsoft.core.common;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装分页结果集 page 从1开始
 * 
 * @author Bat Admin
 *
 */

@SuppressWarnings("serial")
public class PageResult implements Serializable {

	private Integer page;// 要查找第几页
	
	private Integer pageSize;// 每页显示多少条
	
	private Long total;// 总记录数
	
	@SuppressWarnings("rawtypes")
	private List rows;// 结果集
	
	private Map<String,Long> pagination;
	
	public Map<String, Long> getPagination() {
		return pagination;
	}

	public void setPagination(Map<String, Long> pagination) {
		this.pagination = pagination;
	}
	
	public PageResult(){
		super();
	}
	
	/**
	 * 分页对象封装
	 * @param page
	 * @param filter
	 */
	public PageResult(Page page, QueryFilter filter){
		//----------------------分页查询底部外壳------------------------------
		//设置分页数据
		this.setRows(page.getResult());
		// 设置总记录数
		this.setTotal(page.getTotal());
		this.setPage(filter.getPage());
		this.setPageSize(filter.getPageSize());

		Map<String,Long> map=new HashMap<>();
		map.put("total",page.getTotal());
		map.put("pageSize",Long.valueOf(filter.getPageSize()));
		map.put("current",Long.valueOf(filter.getPage()));

		this.setPagination(map);
		//----------------------分页查询底部外壳------------------------------

	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}



	/**
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @return: Long
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @return: Long
	 */
	public void setTotal(Long total) {
		this.total = total;
	}


	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}


	@Override
	public String toString() {
		return "PageResult [ page=" + page + ", pageSize="
				+ pageSize + ", recordsTotal="
				+ total + ",  rows=" + rows + "]";
	}

}
