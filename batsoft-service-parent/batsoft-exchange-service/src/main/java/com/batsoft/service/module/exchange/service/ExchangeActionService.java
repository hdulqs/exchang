/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:16:15 
*/

package com.batsoft.service.module.exchange.service;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.C2cOrder;
import com.batsoft.model.module.exchange.ExchangeAction;
import com.batsoft.model.module.exchange.vo.C2cUserVo;
import javassist.bytecode.ExceptionsAttribute;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* <p>C2cOrderService</p>
* @author: LouSir
* @Date :  2018-05-07 09:16:15 
*/
public interface ExchangeActionService extends BaseService<ExchangeAction, String>{

    /**
     * 获取活动信息
     * @param coinCode
     * @param priceCode
     * @return
     */
    List<ExchangeAction> getAction(String coinCode, String priceCode, Date nowDate);

    List<ExchangeAction> getLastAction(String  coinCode, String priceCode,Date nowDate);

    /**
     * 获取下一个活动
     * @param coinCode
     * @param priceCode
     * @return
     */
    List<ExchangeAction> getNextAction(String coinCode, String priceCode, Date nowDate);

    /**
     * 读取还为开始的活动
     * @param nowDate
     * @return
     */
    List<ExchangeAction> getNextAllAction(Date nowDate);

    /**
     * 活动开始更新
     * @param exchangeAction
     * @return
     */
    int updateActionStatus(ExchangeAction exchangeAction);

}
