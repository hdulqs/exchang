/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:23:08 
*/

package com.batsoft.service.module.exchange.service;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.exchange.EntrustIng;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* <p>EntrustIngService</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:23:08 
*/
public interface EntrustIngService  extends BaseService<EntrustIng, String>{

    /**
     * 初始化委托单到redis中
     */
    void ininRedis();

    /**
     * 登录初始化当前委托数据，和历史委托数据到redis中
     * 规则1：如果当前委托和历史委托不为空则不初始化。以redis持久化为主
     * 规则2：历史委托只初始化当日历史委托数据
     * @param userId
     */
    void flushRedisEntrustByCustomerId(String userId);

    /**
     * sql 分页
     * @param request
     * @return
     */
    PageResult findPageBySql(HttpServletRequest request);

    BigDecimal sumOfBuyAmoutByStartTimeAndId(String id, Date create_time,String coinCode,String priceCode);

    BigDecimal sumOfSellAmoutByStartTimeAndId(String id, Date create_time,String coinCode,String priceCode);

    /**
     * 根据USERid分页查询
     * @param request
     * @return
     */
    PageResult findPageByUserId(HttpServletRequest request);


    /**
     * 查询该用户所有订单
     * @return
     */
    List<EntrustIng> findByUserId();
    
    /**
     * 根据用户ID分组查询交易对代码
     * 
     * @param userId
     * @return
     */
    List<String> findCoinPairGroupByUserId(String userId);
}
