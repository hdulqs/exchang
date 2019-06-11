package com.batsoft.engine.trade;

import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.model.module.exchange.EntrustIng;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.lmax.disruptor.EventHandler;

/**
 * 通过实现接口 com.lmax.disruptor.EventHandler<T> 定义事件处理的具体实现。
 *
 * @author idcomcn
 * @version //idcomcn2017 //14:17 2017-3-12
 */
public class TradeEventHandler implements EventHandler<TradeEvent> {


    @Override
    public void onEvent(TradeEvent event, long sequence, boolean endOfBatch)  throws Exception{
         EntrustIngService entrustService;
        try {
            entrustService=(EntrustIngService) SpringContextUtil.getBean("entrustIngService");
            //保存订单
            EntrustIng entrust= event.getEntrust();
            entrustService.save(entrust);
            System.out.println("事件处理完成: " + entrust.toString());
        }catch (Exception e){
            throw new RuntimeException("手动异常");
        }

    }
}
