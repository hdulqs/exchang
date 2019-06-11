package com.batsoft.mq.util;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class CacheCorrelationData extends CorrelationData{

	private String cacheName;
	
	public CacheCorrelationData(String id) {
		super(id);
	}

	@JSONCreator()
	public CacheCorrelationData(@JSONField(name = "id") String id,@JSONField(name = "cacheName") String cacheName) {
		super(id);
		this.cacheName=cacheName;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	
	@Override
	public String toString() {
		return "CorrelationData [id=" + super.getId() + ",cacheName="+cacheName+"]";
	}
	
}
