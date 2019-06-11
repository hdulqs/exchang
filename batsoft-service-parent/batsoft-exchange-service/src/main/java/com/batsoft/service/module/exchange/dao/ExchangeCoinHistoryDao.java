package com.batsoft.service.module.exchange.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.ExchangeCoinHistory;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ExchangeCoinHistoryDao extends BaseDao<ExchangeCoinHistory,String> {

    ExchangeCoinHistory findByCoinCodeAndDate(Map<String,Object> map);

    BigDecimal  getTotalByCoinCode(HashMap<String,Object> map);

    List<ExchangeCoinHistory> getListByDate(Map<String,Object> map);
}
