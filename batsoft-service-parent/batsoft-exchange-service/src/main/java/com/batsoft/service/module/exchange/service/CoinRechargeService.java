/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 17:46:20 
*/

package com.batsoft.service.module.exchange.service;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.exchange.CoinRecharge;
import com.batsoft.model.module.exchange.vo.CoinRechargeVo;

import java.util.List;

/**
* <p>CoinRechargeService</p>
* @author: Bat Admin
* @Date :  2018-04-19 17:46:20 
*/
public interface CoinRechargeService  extends BaseService<CoinRecharge, String> {

    PageResult findList(Integer page, Integer pageSize, String userId);

    /**
     * 查询充值币种总量
     *
     * @param type 不为空则表示当日总量
     * @return
     */
    JsonResult getTotleRechargesByCode(String type);

    /**
     * 查询充值记录排行
     *
     * @param limit 条数
     * @return
     */
    JsonResult getRechargesBySort(Integer limit);

    void saveOrder( String accountId, String userId, String coinCode, String amount, String fromAddress,String toAddress, String hash);
}
