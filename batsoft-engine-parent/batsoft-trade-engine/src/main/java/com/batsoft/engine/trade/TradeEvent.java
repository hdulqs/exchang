package com.batsoft.engine.trade;

import com.batsoft.model.module.exchange.EntrustIng;

/**
 * 事件(Event)就是通过 Disruptor 进行交换的数据类型。
 * 
 * @author idcomcn
 * @version //idcomcn2017 //14:15 2017-3-12
 *
 */
public class TradeEvent {
	private EntrustIng entrust;

	public EntrustIng getEntrust() {
		return entrust;
	}

	public void setTrade(EntrustIng value) {
		this.entrust=value;
	}
}
