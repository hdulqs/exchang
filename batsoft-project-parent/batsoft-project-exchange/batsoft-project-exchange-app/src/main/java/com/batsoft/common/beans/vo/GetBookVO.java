package com.batsoft.common.beans.vo;

import java.util.ArrayList;
import java.util.List;

import com.batsoft.common.base.BaseVO;

/**
 * Get委托单
 * 
 * @author simon
 */
public class GetBookVO extends BaseVO {

	private static final long serialVersionUID = -4878213197085093495L;
	
	// 买单
	private List<String[]> buy = new ArrayList<String[]>();
	
	// 卖单
	private List<String[]> sell = new ArrayList<String[]>();

	public List<String[]> getBuy() {
		return buy;
	}

	public void setBuy(List<String[]> buy) {
		this.buy = buy;
	}

	public List<String[]> getSell() {
		return sell;
	}

	public void setSell(List<String[]> sell) {
		this.sell = sell;
	}
	
}
