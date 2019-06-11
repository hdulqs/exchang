/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:23:08 
*/

package com.batsoft.service.module.exchange.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.exchange.EntrustIng;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* 
* <p>EntrustIngDao</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:23:08 
*/
public interface EntrustIngDao extends  BaseDao<EntrustIng, String> {

    List<EntrustIng> findPageBySql(Map<String, Object> map);

    /**
     * 通过用户id分页查询
     * @param map
     * @return
     */
    List<EntrustIng> findPageByUserId(Map<String, Object> map);

    BigDecimal sumOfAmoutByStartTimeAndId(Map<String, Object> map);

    /**
     * 根据用户ID分组查询交易对代码
     * 
     * @param userId
     * @return
     */
	List<String> findCoinPairGroupByUserId(String userId);
    
    
    
}
