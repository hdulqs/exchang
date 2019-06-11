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
import com.batsoft.model.module.exchange.vo.C2cUserVo;

import java.math.BigDecimal;

/**
* <p>C2cOrderService</p>
* @author: LouSir
* @Date :  2018-05-07 09:16:15 
*/
public interface C2cOrderService  extends BaseService<C2cOrder, String>{

    /**
     * 插入c2c交易订单记录
     * @param c2cOrder
     * @return buytype购买类型
     */
    JsonResult addTransactionOrder(C2cOrder c2cOrder,int buytype);

    /**
     * 查询对应币种的买卖记录
     * @param coinCode
     * @param type
     * @param start
     * @param limit
     * @return
     */
    JsonResult findUserOrders(String coinCode,Integer type,int start,int limit);

    /**
     * 查询代币买卖自定义价格
     * @param coinCode 币种
     * @param type  买卖类型
     * @return
     */
    BigDecimal findPriceByCoinCode(String coinCode, String type);

    /**
     * 查询用户c2c交易记录
     * @param coinCode  币种
     * @param opeationState 状态
     * @param type  业务类型
     * @param start
     * @param limit
     * @return
     */
    JsonResult findOrderByUserId(String coinCode,Integer opeationState,Integer type,Integer start,Integer limit);

    /**
     * c2c订单审核通过方法
     * @param id
     * @return
     */
    JsonResult c2cPass(String id);

    /**
     * c2c订单审核拒绝方法
     * @param id
     * @return
     */
    JsonResult c2cRefuse(String id);

    /**
     * 根据订单号获得商户信息
     * @param id
     * @return
     */
    C2cUserVo findC2cUserVoById(String id);
}
