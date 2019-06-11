/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:20:38 
*/

package com.batsoft.service.module.exchange.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.CustomerAccountHistory;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 
* <p>CustomerAccountDao</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:20:38 
*/
public interface CustomerAccountHistoryDao extends  BaseDao<CustomerAccountHistory, String> {

    CustomerAccountHistory getRecordSum(HashMap<String,Object> map);

    CustomerAccountHistory getFreezeSum(HashMap<String,Object> map);

    int updateDateById(CustomerAccountHistory customerAccountHistory);

    int insertBatch(List<CustomerAccountHistory> list);
}
