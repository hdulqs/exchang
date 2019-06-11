package com.batsoft.engine.trade;

import com.lmax.disruptor.EventFactory;

/**
 * <pre>
 * 一个 Event 实例实际上被用作一个“数据槽”， 发布者发布前，
 * 先从 RingBuffer 获得一个 Event 的实例， 然后往 Event
 * 实例中填充数据，之后再发布到 RingBuffer 中， 
 * 之后由 Consumer 获得该 Event 实例并从中读取数据。
 * </pre>
 * 
 * @author idcomcn
 *
 */
public class TradeEventFactory implements EventFactory<TradeEvent> {
	public TradeEvent newInstance() {
		return new TradeEvent();
	}
}
