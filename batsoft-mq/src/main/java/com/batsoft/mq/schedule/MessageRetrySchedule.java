package com.batsoft.mq.schedule;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.mq.pojo.CacheMessage;
import com.batsoft.mq.pojo.MetaMessage;
import com.batsoft.mq.util.AttactMessageFilter;
import com.batsoft.mq.util.CacheCorrelationData;
import com.batsoft.mq.util.MessageCacheManager;
import com.batsoft.mq.util.MessageCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 
 * 确认机制confirm返回nack后的信息在此统一重发
 * 
 * @author Bat Admin
 *
 */
@Component
public class MessageRetrySchedule {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageRetrySchedule.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 
	 * 重发
	 * 
	 */
	@Scheduled(fixedRate = 10000)
	public void sendMessage() {
		Map<String,Map<String,Object>> caches= MessageCacheManager.instance().getAll();
		caches.forEach((cacheName,cache)->{
			cache.forEach((key,message)->{
				MetaMessage metaMessage=(MetaMessage) message;
				if(AttactMessageFilter.instance().isAttact(metaMessage.getRoutingKey())){
					logger.debug("不合法的路由键，可能是外部攻击！"+metaMessage.getRoutingKey());
					MessageCacheUtil.remove(cacheName, key);
					return;
				}
				//重发尝试5次
				if(metaMessage.getCount()==6){
					logger.info("消息无法找到交换机,message:"+message+"exchange:"+metaMessage.getExchange()+",routingKeys:"+metaMessage.getRoutingKey());
					logger.info("通知管理员并写入数据库(注意去重)");
					MessageCacheUtil.remove(cacheName, key);
					return;
				}
				CacheCorrelationData correlationId = new CacheCorrelationData(metaMessage.getPayload().toString(),cacheName);
				logger.info("消息重发！exchange:"+metaMessage.getExchange()+",routingKeys:"+metaMessage.getRoutingKey()+",correlationData:"+correlationId.toString());
				CacheMessage cacheMessage=new CacheMessage();
				CacheCorrelationData correlationData = new CacheCorrelationData(key,cacheName);
	        	cacheMessage.setCacheCorrelationData(correlationData);
				cacheMessage.setPayload(metaMessage.getPayload());	
				Message msg=new Message(JSONObject.toJSONString(cacheMessage).getBytes(),MessagePropertiesBuilder.newInstance().setContentType("text/x-json").build());
				rabbitTemplate.send(metaMessage.getExchange(), metaMessage.getRoutingKey(), msg, correlationId);
				metaMessage.setCount(metaMessage.getCount()+1);
			});
		});
	}
	
}
