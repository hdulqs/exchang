package com.batsoft.client.exchange.common;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.batsoft.mq.EntrustOrderFanoutConfig;

/**
 * 委托订单推送到前台【发布订阅模式 交换机+队列】
 * 
 * @author simon
 */
@Configuration
public class EntrustOrderFanoutQueue {
	
	/**
     *	定义推送添加委托订单交换器
     */
    @Bean
    public FanoutExchange addEntrustOrderFanoutExchange() {
        return new FanoutExchange(EntrustOrderFanoutConfig.ADD_ENTRUST_EXCHANGE_NAME);
    }
    
    /**
     * 	 定义推送添加委托订单匿名队列
     */
    @Bean
    public Queue pushAddEntrustOrderToFront() {
        return new AnonymousQueue();
    }

    /**
     *	把推送添加委托订单队列绑定到扇出（广播）交换器
     *
     * @param addEntrustOrderFanoutExchange 
     * 					交换器
     * @param pushAddEntrustOrderToFront 
     * 					自动删除队列
     * @return Binding
     */
    @Bean
    public Binding bindingPushAddEntrustOrder(FanoutExchange addEntrustOrderFanoutExchange, Queue pushAddEntrustOrderToFront) {
        return BindingBuilder.bind(pushAddEntrustOrderToFront).to(addEntrustOrderFanoutExchange);
    }
    
    //-------------------------------------------------------------------------------------------------------------------------
    
    /**
     *	定义推送取消委托订单交换器
     */
    @Bean
    public FanoutExchange cancelEntrustOrderFanoutExchange() {
        return new FanoutExchange(EntrustOrderFanoutConfig.CANCEL_ENTRUST_EXCHANGE_NAME);
    }
    
    /**
     * 	 定义推送取消委托订单匿名队列
     */
    @Bean
    public Queue pushCancelOrderToFront() {
        return new AnonymousQueue();
    }
    
    /**
     *	把推送取消委托订单队列绑定到扇出（广播）交换器
     *
     * @param cancelEntrustOrderFanoutExchange 
     * 					交换器
     * @param pushCancelOrderToFront 
     * 					自动删除队列
     * @return Binding
     */
    @Bean
    public Binding bindingPushCancelEntrustOrder(FanoutExchange cancelEntrustOrderFanoutExchange, Queue pushCancelOrderToFront) {
        return BindingBuilder.bind(pushCancelOrderToFront).to(cancelEntrustOrderFanoutExchange);
    }

}
