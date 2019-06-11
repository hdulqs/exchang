package com.batsoft.client.exchange.common;

import com.batsoft.model.module.exchange.ExchangeAction;

import java.util.Date;
import java.util.List;

public class ExchangeActionUtil {
    /**
     * 获取今天最合适的活动
     * @param actionList
     * @param now
     * @return
     */
    public static  ExchangeAction getNowExchangeAction(List<ExchangeAction> actionList,List<ExchangeAction> yesterDayActionList, Date now){
        for(int i=0;i<actionList.size();i++){
                ExchangeAction exchangeAction = actionList.get(i);
                if (exchangeAction.getEndTime().compareTo(now) >=0) {
                    return exchangeAction;
                } else {
                    continue;
                }
        }
        //说明今天没有数据返回明天的第一个活动
        if(!yesterDayActionList.isEmpty()){
            return yesterDayActionList.get(0);
        }
        return null;
    }

}
