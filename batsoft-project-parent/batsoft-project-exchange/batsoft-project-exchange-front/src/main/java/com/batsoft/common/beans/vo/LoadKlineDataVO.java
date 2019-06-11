package com.batsoft.common.beans.vo;

import java.math.BigDecimal;

import com.batsoft.common.base.BaseVO;

/**
 * Ajax加载K线图数据
 * 
 * @author simon
 */
public class LoadKlineDataVO extends BaseVO {
	
	private static final long serialVersionUID = -8505142832786292965L;

	// 收盘价格
    private BigDecimal close;
    
    // 最高价
    private BigDecimal high;
    
    // 最低价
    private BigDecimal low;
    
    // 开盘价
    private BigDecimal open;
    
    // 交易量
    private BigDecimal volume;

    // 时间戳
    private Long time;

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
    
	public boolean valid() {
		if(close == null || close.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}
		if(high == null || high.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}
		if(low == null || low.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}
		if(open == null || open.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}
		if(volume == null || volume.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}
	    return true; // 有效数据
	}
    
}
