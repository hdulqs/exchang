/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:21:02 
*/

package com.batsoft.service.module.exchange.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.CustomerAccountFreeze;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* 
* <p>CustomerAccountFreezeDao</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:21:02 
*/
public interface CustomerAccountFreezeDao extends  BaseDao<CustomerAccountFreeze, String> {

    List<CustomerAccountFreeze> findPageBySql(Map<String, Object> map);

    Long findPageBySqlTotal(Map<String, Object> map);

    int saveObject(Map<String, Object> map);

    CustomerAccountFreeze findById(Map<String, Object> map);

    int  deleteById(Map<String, Object> map);

    BigDecimal getFreezeSum(Map<String,Object> map);

}
