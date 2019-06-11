package com.batsoft.core.common.enums;

/**
 * 货币代码
 * 
 * @author simon
 */
public enum CoinEnum {
	
	/**
	 * 比特币
	 * 
	 */
	BTC("比特币", "BTC"),
	
	/**
	 * 莱特币
	 * 
	 */
	LTC("莱特币", "LTC"),
	
	/**
	 * 量子链
	 * 
	 */
	QTUM("量子链", "QTUM"),
	
	/**
	 * 以太坊
	 * 
	 */
	ETH("以太坊", "ETH"),
	
	/**
	 * 以太经典
	 * 
	 */
	ETC("以太经典", "ETC"),
	
	/**
	 * 小蚁币
	 * 
	 */
	NEO("小蚁币", "NEO"),
	
	/**
	 * 瑞波币
	 * 
	 */
	XRP("瑞波币", "XRP"),
	
	/**
	 * 公信宝
	 * 
	 */
	GXS("公信宝", "GXS"),
	
	/**
	 * BT
	 * 
	 */
	BT("BT","BT"),
	
	/**
	 * USDT
	 * 
	 */
	USDT("USDT","USDT"),
	
	/**
	 * MT
	 * 
	 */
	MT("MT","MT"), 
	
	/**
	 * MTT
	 * 
	 */
	MTT("MTT", "MTT"),
	
	/**
	 * CNY
	 * 
	 */
	CNY("CNY", "CNY"),
	
	;

	private CoinEnum(String name, String code) {
		this.name = name;
		this.code = this.name;
	}
	
	/**
	 * 币种名称
	 * 
	 */
	private String name;
	
	/**
	 * 币种代码
	 * 
	 */
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
