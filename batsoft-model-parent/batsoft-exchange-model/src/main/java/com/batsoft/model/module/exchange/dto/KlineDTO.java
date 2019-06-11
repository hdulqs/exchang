package com.batsoft.model.module.exchange.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Kline DataNode DTO
 * 
 * @author simon
 */
public class KlineDTO implements Serializable {

	private static final long serialVersionUID = -7212421490050564037L;
	
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
	
	public boolean verify() {
		if(this.close == null || this.close.compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}
		if(this.high == null || this.high.compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}
		if(this.low == null || this.low.compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}
		if(this.open == null || this.open.compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}
		if(this.volume == null || this.volume.compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}
		return true;
	}
}
