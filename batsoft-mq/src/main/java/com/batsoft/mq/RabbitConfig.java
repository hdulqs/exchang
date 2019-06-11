package com.batsoft.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	// BTC_USDT
	public static final String BTC_USDT = "BTC_USDT";
	
	// ETH_USDT
	public static final String ETH_USDT = "ETH_USDT";
	
	// MT_BT
	public static final String MT_BT = "MT_BT";
	
	// CPT_BT
	public static final String CPT_BT = "CPT_BT";
	
	// JT_BT
	public static final String JT_BT = "JT_BT";
	
	// BT_USDT
	public static final String BT_USDT = "BT_USDT";
	
	// MTT_BT
	public static final String MTT_BT = "MTT_BT";
	
	// HAT_BT
	public static final String HAT_BT = "HAT_BT";
	
	@Bean
    public Queue BTC_USDT() {
        return new Queue(BTC_USDT);
    }
	
	@Bean
    public Queue ETH_USDT() {
        return new Queue(ETH_USDT);
    }
	
	@Bean
    public Queue MT_BT() {
        return new Queue(MT_BT);
    }
	
	@Bean
    public Queue CPT_BT() {
        return new Queue(CPT_BT);
    }
	
	@Bean
    public Queue JT_BT() {
        return new Queue(JT_BT);
    }
	
	@Bean
    public Queue BT_USDT() {
        return new Queue(BT_USDT);
    }
	
	@Bean
    public Queue MTT_BT() {
        return new Queue(MTT_BT);
    }
	
	@Bean
    public Queue HAT_BT() {
        return new Queue(HAT_BT);
    }
	
	// --------------------------------------------------------------------------------------------------
	
	// 执行SQL队列
	public static final String SQL_QUEUE = "sql";
	
	@Bean
    public Queue sql() {
        return new Queue(SQL_QUEUE);
    }
	
	// --------------------------------------------------------------------------------------------------
	
	// 更新RedisAcction
	public static final String REDIS_ACCOUNT_QUEUE = "redisAccount";
	
	@Bean
    public Queue redisAccount() {
        return new Queue(REDIS_ACCOUNT_QUEUE);
    }
	
	
	// --------------------------------------------------------------------------------------------------
	
	// 注册初始化队列
	public static final String REGISTER_INIT_QUEUE = "registerInit";
	
	
	@Bean
    public Queue registerInit() {
        return new Queue(REGISTER_INIT_QUEUE);
    }
	
	
	// --------------------------------------------------------------------------------------------------
	
	// 后台给货币账户加币通道
	public static final String ADD_COIN_INIT_QUEUE = "addCoinInit";
    
    @Bean
    public Queue addCoinInit() {
        return new Queue(ADD_COIN_INIT_QUEUE);
    }
    
    // --------------------------------------------------------------------------------------------------
    
    // 取消委托订单
    public static final String CANCEL = "cancel";
    
    @Bean
    public Queue cancel() {
    	return new Queue(CANCEL);
    }
    
    // 取消全部委托订单
    public static final String CANCEL_ALL = "cancelAll";
    
    @Bean
    public Queue cancelAll() {
    	return new Queue(CANCEL_ALL);
    }
    
}
