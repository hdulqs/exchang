package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.ExchangeCoinHistory;
import com.batsoft.service.module.exchange.dao.ExchangeCoinHistoryDao;
import com.batsoft.service.module.exchange.service.ExchangeCoinHistoryService;
import com.batsoft.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("exchangeCoinHistoryService")
public class ExchangeCoinHistoryServiceImpl extends BaseServiceImpl<ExchangeCoinHistory,String> implements ExchangeCoinHistoryService {

    @Autowired
    ExchangeCoinHistoryDao exchangeCoinHistoryDao;

    @Override
    public ExchangeCoinHistory findByCoinCodeAndDate(String coinCode, Date date) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("coin_code",coinCode);
        hashMap.put("time", DateUtils.dateFormatToString(date,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD));
        ExchangeCoinHistory result = exchangeCoinHistoryDao.findByCoinCodeAndDate(hashMap);
        return result ;
    }


    @Override
    public BigDecimal sumOfTotalByCoinCode(String coinCode) {
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("coin_code",coinCode);
        BigDecimal result = exchangeCoinHistoryDao.getTotalByCoinCode(hashMap);
        return result == null ? BigDecimal.ZERO:result;
    }

    @Override
    public List<ExchangeCoinHistory> getListByDate(Date date) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("time",DateUtils.dateFormat(date,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD));
        return exchangeCoinHistoryDao.getListByDate(hashMap);
    }
}
