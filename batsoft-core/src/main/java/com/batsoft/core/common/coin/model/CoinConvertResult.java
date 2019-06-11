package com.batsoft.core.common.coin.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.batsoft.core.common.enums.MessageEnum;

/**
 * 货币装换
 * 
 * @author simon
 */
public class CoinConvertResult implements Serializable {

	private static final long serialVersionUID = 8379638611738137440L;
	
	/**
	 * 装换后目标货币数量
	 * 
	 */
	private BigDecimal targetCoinAmount;

	/**
	 * USDT 跟人民币的汇率； 栗子： 1USDT = 6.88CNY
	 * 
	 */
	private BigDecimal usdtToCnyRate;
	
	/**
	 * 说明代码
	 * 
	 */
	private String code;
	
	/**
	 * 状态说明
	 * 
	 */
	private String message;
	

	public BigDecimal getTargetCoinAmount() {
		return targetCoinAmount;
	}

	public void setTargetCoinAmount(BigDecimal targetCoinAmount) {
		this.targetCoinAmount = targetCoinAmount;
	}

	public BigDecimal getUsdtToCnyRate() {
		return usdtToCnyRate;
	}

	public void setUsdtToCnyRate(BigDecimal usdtToCnyRate) {
		this.usdtToCnyRate = usdtToCnyRate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setSuccess() {
		this.code = MessageEnum.SUCCESS.getCode();
		this.message = MessageEnum.SUCCESS.getMessage();
	}
	
	public void setFail() {
		this.code = MessageEnum.FAIL.getCode();
		this.message = MessageEnum.FAIL.getMessage();
	}

	public void setFail(MessageEnum message) {
		this.code = message.getCode();
		this.message = message.getMessage();
	}
	
	public boolean isSuccess() {
		if(Objects.equals(this.code, MessageEnum.SUCCESS.getCode())) {
			return true;
		}
		return false;
	}
	
}
