package com.batsoft.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布订阅模式 交换机+队列
 */
@Configuration
public class FanoutExchangeQueue {
	
	public static final String REGISTER_SUCCESS_EXCHANGE = "register_success_exchange";
	
	public static final String EXCHANGE_ACCOUNT_QUEUE = "exchange_account_queue";
	
    public static final String OTC_ACCOUNT_QUEUE = "otc_account_queue";
    
    
    /**
     * 注册成功交换机
     * @return
     */
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(REGISTER_SUCCESS_EXCHANGE);
    }
    
    /**
     * 交易所币账户队列
     * @return
     */
    @Bean
    public Queue exchangeAccountMessage() {
        return new Queue(EXCHANGE_ACCOUNT_QUEUE);
    }
    
    /**
     * OTC币账户队列
     * @return
     */
    @Bean
    public Queue otcAccountMessage() {
        return new Queue(OTC_ACCOUNT_QUEUE);
    }

    @Bean
    Binding bindingExExchange(Queue exchangeAccountMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(exchangeAccountMessage).to(fanoutExchange);
    }
    
    @Bean
    Binding bindingOtcExchange(Queue otcAccountMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(otcAccountMessage).to(fanoutExchange);
    }


}
