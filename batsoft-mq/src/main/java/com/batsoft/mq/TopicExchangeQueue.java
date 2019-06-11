package com.batsoft.mq;

import com.batsoft.mq.util.AttactMessageFilter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 发布订阅模式 交换机+队列
 */
@Configuration
public class TopicExchangeQueue {
    public static final int X_MESSAGE_TTL = 6000;
    public static final String ROUNTING_KEY_PREFIX = "batsoft.bussiness";

    public static final String ORDER_SAVE_ROUTING_KEY = "order.save";

    public static final String FIANANCE_SAVE_ROUTING_KEY = "finance.save";
    /**
     * 账户路由
     */
    public static final String FINANCE_ROUNTING="batsoft.bussiness.finance.*";
    /**
     * 死信交换机
     */
    public static final String DLX_EXCHANGE = "dlx_exchange";
    /**
     * 死信队列
     */
    public static final String DLX_QUEUE="dlx_queue";


    /**
     * 业务交换机
     */
    public static final String BUSINESS_EXCHANGE = "business_exchange";
    /**
     *
     * 业务交换机direct方式
     *
     */

    /**
     * 账户生成队列（人民币账户、币账户等）
     */
    public static final String FINANCE_QUEUE="finance_queue";

    static {
        AttactMessageFilter.init(Arrays.asList(new String[]{ORDER_SAVE_ROUTING_KEY,FIANANCE_SAVE_ROUTING_KEY}));
    }

    /*
    @Bean
    public DirectExchange bussinessExchange() {
        return new DirectExchange(BUSINESS_EXCHANGE);
    }
    */

    /**
     * 业务交换机tipic方式
     */
   // @Bean
    public TopicExchange bussinessExchange() {
        return new TopicExchange(BUSINESS_EXCHANGE);
    }

    /**
     *
     * 死信交换机direct方式
     *
     * @return
     */
    /*
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }
    */

    /**
     * 死信交换机topic方式
     *
     * @return
     */
    //@Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    /**
     * 账户业务队列
     *
     * @return
     */
   // @Bean
    public Queue queue() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x-dead-letter-exchange", DLX_EXCHANGE);
        params.put("x-message-ttl", X_MESSAGE_TTL);
        return new Queue(FINANCE_QUEUE, true, false, false, params);
    }

    /**
     * 死信队列
     *
     * @return
     */
   // @Bean
    public Queue dlxQueue() {
        Map<String, Object> params = new HashMap<String, Object>();
        Queue queue = new Queue(DLX_QUEUE, true, false, false, params);
        return queue;
    }

    /**
     * 账户业务绑定
     *
     * @return
     */
   // @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(bussinessExchange()).with(FINANCE_ROUNTING);
    }

    /**
     * 死信队列绑定
     *
     * @return
     */
  //  @Bean
    public Binding dlxBinding() {
        //topic的方式
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(FINANCE_ROUNTING);
    }

    /**
     *
     * 手动发送ack可以使用该方法，因为可以调用底层channel
     *
     * @return
     */
    /*
    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(10);
        container.setConcurrentConsumers(3);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //����ȷ��ģʽ�ֹ�ȷ��
        container.setMessageListener(new ChannelAwareMessageListener(){

			public void onMessage(Message message, Channel channel) throws Exception {
				byte[] body = message.getBody();
                System.out.println("receive msg : " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //ȷ����Ϣ�ɹ�����
			}

        });

        return container;
    }
    */




}
