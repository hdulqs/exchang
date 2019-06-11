/**
 * Copyright:   北京互融时代软件有限公司
 * @author:       SHANGXL
 * @version:      V1.0 
 * @Date:        2018年5月21日 下午1:35:02
 */
package com.batsoft.blockchain.common;

/**
 * @Desc 币种类型
 * @author: shangxl
 * @Date : 2018年5月21日 下午1:35:02
 */
public enum Coin {

	BTC("比特币", "BTC"),
	LTC("莱特币", "LTC"),
	QTUM("量子链", "QTUM"),
	ETH("以太坊", "ETH"),
	ETC("以太经典", "ETC"),
	NEO("小蚁币", "NEO"),
	XRP("瑞波币", "XRP"),
	GXS("公信宝", "GXS"),
	BT("BT","BT"),
	USDT("USDT","USDT"),
	MT("MT","MT");

	/**
	 * 币种名称
	 */
	String name;
	/**
	 * 币种类型
	 */
	String type;

	Coin(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
