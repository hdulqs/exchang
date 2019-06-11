package com.batsoft.mq.bussiness;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.mq.pojo.CacheMessage;
import com.batsoft.mq.util.CacheCorrelationData;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 
 * 死信统一分情况外处理消费端无法消费的消息，另也处理老版本immediate的情况，即没有消费者的情况
 * 
 * @author Bat Admin
 *
 */
@Component
@RabbitListener(queues = "dlx_queue")
public class DeadRecv {
	
	private static final Logger logger = LoggerFactory.getLogger(DeadRecv.class);
	
	private Map<String,Integer> expiredCounter=new ConcurrentSkipListMap<String,Integer>();

	private final String EXPIRED="expired";
	private final String REJECTED="rejected";
	private final String MAXLEN="maxlen";
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@RabbitHandler
    public void receive(@Header("x-death") List<Map<String,Object>> ds,String message, Channel channel) {
		try{
			logger.info("死信！"+message);
			CacheMessage cacheMessage=JSONObject.parseObject(message,CacheMessage.class);
			Map<String,Object> params=ds.stream()
										.findFirst()
										.get();
			String reason=(String) params.get("reason");
			String queue=(String) params.get("queue");
			String originalExpiration=(String) params.get("original-expiration"); 
			String exchange=(String) params.get("exchange"); 
			List<String> routingKeys=(List<String>) params.get("routing-keys");
			String key=routingKeys.stream().findFirst().get();
			logger.debug("reason:"+reason+
			           	 ",queue:"+queue+
			           	 ",originalExpiration:"+originalExpiration+
			           	 ",exchange:"+exchange+
			           	 ",routingKeys:"+key);
			
			CacheCorrelationData correlationId = new CacheCorrelationData(message,cacheMessage.getCacheCorrelationData().getCacheName());
			Message msg=new Message(message.getBytes(),
					MessagePropertiesBuilder.newInstance()
											.setContentType("text/x-json")
											.build()
			);
			if(reason.equals(EXPIRED)){
				//五次重发的机制，不是一味的重发
				int count=Optional.ofNullable(expiredCounter.get(cacheMessage.getCacheCorrelationData().getId())).orElse(0);
				logger.debug("重发详情：key="+cacheMessage.getCacheCorrelationData().getId()+",count="+count);
				if(count==6){
					expiredCounter.remove(cacheMessage.getCacheCorrelationData().getId());
					logger.debug("消息"+message+"无法被消费端处理，queue:"+queue+"exchange:"+exchange+"routingKeys:"+key);
					logger.debug("通知管理员并写入数据库(注意去重)");
					return;
				}
				count++;
				expiredCounter.put(cacheMessage.getCacheCorrelationData().getId(), count);
				
				//消息重发
				Optional.ofNullable(originalExpiration).ifPresent(e->{
					Message expirationMsg=new Message(message.getBytes(),
											MessagePropertiesBuilder.newInstance()
																	.setExpiration(originalExpiration)
																	.setContentType("text/x-json")
																	.build()
					);
					rabbitTemplate.send(exchange, key, expirationMsg, correlationId);
				});
				rabbitTemplate.send(exchange, key, msg, correlationId);
			}
			else if(reason.equals(REJECTED)){
				//由于致命错误，为了防止负载均衡的轮询策略，路由到其他消费者，造成大量消费者瘫痪，直接丢弃该消息
				logger.info("消息被拒绝！"+message);
				logger.info("通知管理员并写入数据库(注意去重)");
			}
			else if(reason.equals(MAXLEN)){
				logger.info("队列已满，消息转发！"+queue);
				//利用负载均衡的轮询策略，重发消息
				rabbitTemplate.send(exchange, key, msg, correlationId);
			}
		}catch(Exception e){
			logger.info("死信处理出错！"+message);
			logger.info("通知管理员并写入数据库(注意去重)");
			e.printStackTrace();
			throw new AmqpRejectAndDontRequeueException(e.getMessage());
		}
    }
	
}
