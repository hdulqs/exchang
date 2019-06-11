/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 17:46:20 
*/

package com.batsoft.service.module.exchange.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.exchange.CoinRecharge;
import com.batsoft.model.module.exchange.vo.CoinRechargeVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 
* <p>CoinRechargeDao</p>
* @author: Bat Admin
* @Date :  2018-04-19 17:46:20 
*/
public interface CoinRechargeDao extends  BaseDao<CoinRecharge, String> {

    List<CoinRechargeVo> findList(Map<String,Object> map);

    Long findPageByUserIdTotal(Map<String,Object> map);

    /**
     * 查询充值币种总量
     * @param day  不为空则表示当日总量
     * @return
     */
    List<CoinRechargeVo> getTotleRechargesByCode(@Param("day") String day);

    /**
     * 查询充值币种
     * @return
     */
    List<CoinRechargeVo> getCoins();
}
