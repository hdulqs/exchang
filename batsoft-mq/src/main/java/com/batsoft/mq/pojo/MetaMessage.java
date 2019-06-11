package com.batsoft.mq.pojo;

public class MetaMessage {

	private Object payload;
	private String exchange;
	private String routingKey;
	private int count;
	
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
