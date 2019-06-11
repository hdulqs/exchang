package com.batsoft.common.beans.vo;

import com.batsoft.common.base.BaseVO;

/**
 * 收藏状态
 * 
 * @author simon
 */
public class CollectStateVO extends BaseVO {

	private static final long serialVersionUID = -5869771870005548276L;
	
	private boolean state = false;

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
	
}
