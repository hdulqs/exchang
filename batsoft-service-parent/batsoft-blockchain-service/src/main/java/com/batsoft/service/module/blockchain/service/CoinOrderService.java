/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-23 23:07:19 
*/

package com.batsoft.service.module.blockchain.service;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.blockchain.CoinOrder;

import java.util.List;

/**
* <p>CoinOrderService</p>
* @author: Bat Admin
* @Date :  2018-05-23 23:07:19 
*/
public interface CoinOrderService  extends BaseService<CoinOrder, String>{
    /**
     * 同步区块并且记账
     * 用于定时获取转账记录
     * @param address  ETH 的自己地址
     */
    void findBlockTraction(String address);

    /**
     * 是否已经存在交易
     * @param hash
     * @return
     */
    int hasHash(String hash);


    /**
     * 查询未记账订单
     * @return
     */
    List<CoinOrder> findCoinOrders();

    /**
     * 确认是否到账
     */
    void findConfirm();
}
