/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 17:51:09
*/

package com.batsoft.service.module.exchange.service;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.exchange.CoinWithdraw;
import com.batsoft.model.module.exchange.vo.CoinWithdrawVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>CoinWithdrawService</p>
* @author: Bat Admin
* @Date :  2018-04-19 17:51:09
*/
public interface CoinWithdrawService  extends BaseService<CoinWithdraw, String>{
    /**
     * 生成充币订单
     * @param address
     * @param memo
     * @param amt
     * @param symbol
     * @return
     */
    JsonResult saveWithdrawOrder(String address,  String memo, String amt, String symbol);

    PageResult findList(Integer page,Integer pageSize,String userId);

    /**
     * 后台提现审核
     * @param status
     * @return
     */
    JsonResult withdrawReview(String[] pks,Integer status);

    /**
     * 查询提币未审核总量
     * @return
     */
    JsonResult getTotleMoneyByUnaudited();

    /**
     * 查询各币种提现总量
     * @param type=day 统计当日
     * @return
     */
    JsonResult getTotleWithdrawsByCode(String type);

    /**
     * 查询各币种排序
     * @param limit  每个币种查询条数
     * @return
     */
    JsonResult getWithdrawsBySort(Integer limit);
}
