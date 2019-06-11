package com.batsoft.engine.trade;

import com.batsoft.model.module.exchange.EntrustIng;
import com.lmax.disruptor.RingBuffer;

/**
 * 消息的生产者。
 * 
 * @author idcomcn
 * @version //idcomcn2017 //19:08 2017-3-12
 */
public class TradeEventProducer {
	private RingBuffer<TradeEvent> ringBuffer;

	public TradeEventProducer(RingBuffer<TradeEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	/**
	 * 添加并发布消息
	 * 
	 * @param value
	 */
	public void onData(EntrustIng value) {
		// Grab the next sequence 获取下一个sequence
		long sequence = ringBuffer.next();
		try {// Get the entry in the Disruptor 获取entry对象
			TradeEvent event = ringBuffer.get(sequence);
			// for the sequence 对应sequence位置上的event
			// Fill with data 填充业务数据
			event.setTrade(value);
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
