/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-12-08 09:56:42
 */
package com.batsoft.service.module.otc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.otc.ExCoin;
import com.batsoft.model.module.otc.ExFinance;
import com.batsoft.model.module.otc.vo.ExFinanceVo;
import com.batsoft.mq.FanoutExchangeQueue;
import com.batsoft.mq.annotation.MessageCache;
import com.batsoft.mq.pojo.CacheMessage;
import com.batsoft.mq.util.MessageCacheUtil;
import com.batsoft.mq.util.MessageFatalExceptionStrategy;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.otc.dao.ExFinanceDao;
import com.batsoft.service.module.otc.service.ExCoinService;
import com.batsoft.service.module.otc.service.ExFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> ExFinanceServiceImpl </p>
 * @author: Bat Admin
 * @Date :  2017-12-08 09:56:42
 */
@Service("exFinanceService")
@RabbitListener(queues = FanoutExchangeQueue.OTC_ACCOUNT_QUEUE)
@Slf4j
public class ExFinanceServiceImpl extends BaseServiceImpl<ExFinance, String> implements ExFinanceService {
    private static final Logger logger = LoggerFactory.getLogger(ExFinanceServiceImpl.class);

    @Autowired
    private ExCoinService exCoinService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ExFinanceDao exFinanceDao;

    @Override
    public JsonResult saveOneFinance(String userName, String userId, String coin) {
        System.out.println("mq_one============" + userName);
        return null;
    }

    @Override
    public List<ExFinanceVo> findList() {
        User user= UserUtils.getUser();
        return exFinanceDao.findList(user.getId());
    }

    @MessageCache(cacheName="otcfinance",cacheKey="otc_finance_message",messageArgMapper="message")
    @RabbitHandler
    public JsonResult saveMultipleFinanceByMQ(String message) {
        CacheMessage cacheMessage= JSONObject.parseObject(message, CacheMessage.class);
        try{
            User user= JSONObject.parseObject(message, User.class);
            List<ExCoin> list= exCoinService.findCoins();

            for (ExCoin coin:list) {
                ExFinance finance=new ExFinance();
                finance.setSymbol(coin.getSymbol());
                finance.setUserId(user.getId());
                finance.setUserName(user.getUserName());
                save(finance);
            }
            logger.info("otcfinance消息处理成功！" + message);
        }catch(Exception e){
            //失败后交给死信，所以不通过缓存重发
            MessageCacheUtil.remove(cacheMessage.getCacheCorrelationData().getCacheName(),cacheMessage.getCacheCorrelationData().getId());
            //自动发送basic.nack如果requeue为true，重新入队
            //官方原文When a listener throws an exception, it is wrapped in a ListenerExecutionFailedException and, normally the message is rejected and requeued by the broker
            //throw new NullPointerException();
            //自动发送basic.nack如果requeue为false，有死信路由将被路由到死信队列，或直接被丢弃
            //AmqpRejectAndDontRequeueException可以不管requeue的设置，直接不重入队列
            //官方原文the listener can throw an AmqpRejectAndDontRequeueException to conditionally control this behavior
            //Setting defaultRequeueRejected to false will cause messages to be discarded (or routed to a dead letter exchange)
            //SimpleRabbitListenerContainerFactory可以设置defaultRequeueRejected
            //如果是交给ErrorHandle处理的异常，就不需要手动nack
            System.out.println("------------"+!MessageFatalExceptionStrategy.causeIsFatal(e.getCause())+"----------");
            if(!MessageFatalExceptionStrategy.causeIsFatal(e.getCause())){
                //捕获异常后，要使ErrorHandle生效，必须调用e.getCause().getClass()
                logger.info("非致命错误！"+e.getCause().getClass());
                throw new AmqpRejectAndDontRequeueException(e.getMessage());
            }
        }
        return null;
    }

}
