/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:16:15 
*/

package com.batsoft.service.module.exchange.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.exchange.C2cOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 
* <p>C2cOrderDao</p>
* @author: LouSir
* @Date :  2018-05-07 09:16:15 
*/
public interface C2cOrderDao extends  BaseDao<C2cOrder, String> {

    public List<C2cOrder> findUserOrders(@Param("coinCode") String coinCode,
                                         @Param("type") Integer type,
                                         @Param("start") Integer start,
                                        @Param("limit") Integer limit);

    /**
     * 按状态查询客户订单号
     * @param userId
     * @param coinCode
     * @param opeationState
     * @param type
     * @param start
     * @param limit
     * @return
     */
    public List<C2cOrder> findOrderByUserId(@Param("userId") String userId,
                                            @Param("coinCode") String coinCode,
                                            @Param("opeationState") Integer opeationState,
                                             @Param("type") Integer type,
                                             @Param("start") Integer start,
                                             @Param("limit") Integer limit);
}
