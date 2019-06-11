package com.batsoft.common.util.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Model
 * 
 * @author simon
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = -6744882162751796931L;

	/** 上一页 **/
	private int prePage = 1;

	/** 下一页 **/
	private int nextPage = 1;

	/** 当前页 **/
	private Integer pageNo;

	/** 每页的记录数;默认10条 **/
	private Integer pageSize = 10;

	/** 总记录数 **/
	private long totalCount;

	/** 总页数 **/
	private int totalPage;

	/** 起始位置，当前页第一条数据在List中的位置，从0开始 **/
	private int start;

	/** 当前页，结束的位置 **/
	private int end;
	
	/** 响应数据 **/
	private List<T> result = new ArrayList<T>();

	public Page() {}

	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getPrePage() {
		prePage = pageNo - 1;
		if (prePage < 1) {
			prePage = 1;
		}
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getNextPage() {
		nextPage = pageNo + 1;
		if (nextPage > totalPage) {
			nextPage = totalPage;
		}
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		if(nextPage > 1000){
			nextPage = 1000;
		}
		this.nextPage = nextPage;
	}

	public int getStart() {
		start = (getPageNo() - 1) * getPageSize();
		if (start < 0) {
			start = 0;
		}
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize != null && pageSize > 50) {
			pageSize = 50;
		}
		this.pageSize = pageSize;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		this.totalPage = (int) (totalCount - 1) / this.pageSize + 1;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
	
	public void setResult(T result) {
		this.result.add(result);
 	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	public int getPageNo() {
		if (pageNo == null || pageNo <= 0) {
			pageNo = 1;
		}
		return pageNo;
	}

	/**
	 * 根据当前页获取到页面分页按钮的开始位置,总页数10页以内从1开始，总页数结束 当前页大于4并且总页数大于10 从当前页减4开始，加5结束
	 * 
	 * @return int
	 */
	public int getPageStartIndex() {
		if (this.pageNo > 4 && this.totalPage > 10) {
			if (this.totalPage - this.pageNo < 5) {
				// 当前页为总页数减5之内时，开始的位置不能再减四，保证显示10条数据
				return this.totalPage - 9;
			}
			return this.pageNo - 4;
		}
		return 1;
	}

	/**
	 * 根据当前页获取到页面分页按钮的结束位置,总页数10页以内从1开始，总页数结束 当前页大于4并且总页数大于10 从当前页减4开始，加5结束
	 * 
	 * @return int
	 */
	public int getPageEndIndex() {
		if (this.totalPage > 10) {
			if (this.pageNo > 4) {
				return this.pageNo + 5 > this.totalPage ? this.totalPage : this.pageNo + 5;
			} else {
				return 10;
			}
		}
		return this.totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 获取任一页第一条数据在数据集的位置.
	 * 
	 * @param pageNo
	 *            从1开始的页号
	 * @param pageSize
	 *            每页记录条数
	 * @return 该页第一条数据
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

	public int getEnd() {
		end = getStart() + getPageSize();
		end = (int) (end > getTotalCount() ? getTotalCount() : end);
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
