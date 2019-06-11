/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:22:21 
*/

package com.batsoft.service.module.exchange.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.EntrustHistory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
* 
* <p>EntrustHistoryDao</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:22:21 
*/
public interface EntrustHistoryDao extends  BaseDao<EntrustHistory, String> {

    /**
     * 分页查询记录
     * @param map
     * @return
     */
    List<EntrustHistory> findPageBySql(Map<String, Object> map);

    Long findPageBySqlTotal(Map<String, Object> map);

    EntrustHistory findById(Map<String, Object> map);


    BigDecimal sumOfAmoutByStartTimeAndId(Map<String, Object> map);



    /**
     * 通过用户id分页查询
     * @param map
     * @return
     */
    List<EntrustHistory> findPageByUserId(Map<String, Object> map);

    Long findPageByUserIdTotal(Map<String, Object> map);

    /**
     * 通过订单号查询成交历史记录是否存在
     * 
     * @return
     */
    BigInteger findByOrderIdExist(Map<String, Object> map);

    int  deleteById(Map<String, Object> map);
}
