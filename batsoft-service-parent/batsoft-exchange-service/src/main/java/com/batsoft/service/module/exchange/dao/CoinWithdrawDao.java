/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 16:15:19 
*/

package com.batsoft.service.module.exchange.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.exchange.CoinWithdraw;
import com.batsoft.model.module.exchange.vo.CoinWithdrawVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* 
* <p>CoinWithdrawDao</p>
* @author: Bat Admin
* @Date :  2018-04-25 16:15:19 
*/
public interface CoinWithdrawDao extends  BaseDao<CoinWithdraw, String> {

    /**
     * 查询用户提现列表
     * @param map
     * @return
     */
    List<CoinWithdrawVo> findList(Map<String,Object> map);

    Long findListTotal(Map<String,Object> map);

    /**
     * 查询用户当日/所有有效提现的总金额
     * @param userId
     * @param type  day 表示天  查询所有可以不传
     * @return
     */
    BigDecimal getTotleMoneyByDays(@Param("userId") String userId,@Param("type") String type,@Param("coinCode") String coinCode);

    /**
     *查询未提现总量
     */
    BigDecimal getTotleMoneyByUnaudited();

    /**
     * 获取各币种提现总金额
     * @return
     */
    List<CoinWithdrawVo> getTotleWithdrawsByCode(@Param("day") String day);

    /**
     * 提现币种列表
     * @return
     */
    List<CoinWithdrawVo> getCoins();
}
