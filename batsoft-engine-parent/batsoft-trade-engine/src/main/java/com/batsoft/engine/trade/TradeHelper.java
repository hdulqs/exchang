package com.batsoft.engine.trade;

import com.batsoft.model.module.exchange.EntrustIng;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TradeHelper {

	/**
	 * ringBuffer的容量，必须是2的N次方
	 */
	private static final int BUFFER_SIZE = 1024 * 1024;

	private Disruptor<TradeEvent> disruptor = null;
	private TradeEventProducer producer = null;

	@SuppressWarnings("unchecked")
	public TradeHelper() {
		disruptor = new Disruptor<TradeEvent>(new TradeEventFactory(), BUFFER_SIZE, DaemonThreadFactory.INSTANCE,
				ProducerType.SINGLE, new YieldingWaitStrategy());

		 /*disruptor = new Disruptor(new TradeEventFactory(), BUFFER_SIZE,
				Executors.defaultThreadFactory());*/


		disruptor.handleEventsWith(new TradeEventHandler());

		 //Pipeline
		RingBuffer<TradeEvent> ringBuffer = disruptor.start();

		producer = new TradeEventProducer(ringBuffer);

	}

	public void onData(EntrustIng value) {
		producer.onData(value);
	}

	public void shutdown() {
		disruptor.shutdown();
	}
}
