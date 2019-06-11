/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:16:15 
*/

package com.batsoft.service.module.exchange.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.C2cOrder;
import com.batsoft.model.module.exchange.ExchangeAction;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 
* <p>C2cOrderDao</p>
* @author: LouSir
* @Date :  2018-05-07 09:16:15 
*/
public interface ExchangeActionDao extends  BaseDao<ExchangeAction, String> {

    public List<ExchangeAction> selectActionByCoinCodeAndPriceCode(Map<String,Object> data);

    public List<ExchangeAction> selectLastActionByCoinCodeAndPriceCode(Map<String,Object> data);

    public List<ExchangeAction> selectNextActionByCoinCodeAndPriceCode(Map<String,Object> data);

    public List<ExchangeAction> selectNextAllActionByDate(Map<String,Object> data);

    public int updateActionValide(@Param("valide") Integer valide,@Param("id") String id);

}
