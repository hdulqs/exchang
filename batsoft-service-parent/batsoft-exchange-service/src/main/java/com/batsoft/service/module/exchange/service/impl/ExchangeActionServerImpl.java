package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.ExchangeAction;
import com.batsoft.service.module.exchange.dao.ExchangeActionDao;
import com.batsoft.service.module.exchange.service.ExchangeActionService;
import com.batsoft.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("exchangeActionService")
public class ExchangeActionServerImpl extends BaseServiceImpl<ExchangeAction,String> implements ExchangeActionService {
    @Autowired
    private ExchangeActionDao exchangeActionDao;

    @Override
    public List<ExchangeAction> getAction(String coinCode, String priceCode, Date now) {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("date", now);
        hashMap.put("coinCode",coinCode);
        hashMap.put("priceCode",priceCode);
        return exchangeActionDao.selectActionByCoinCodeAndPriceCode(hashMap);
    }

    @Override
    public List<ExchangeAction> getLastAction(String coinCode, String priceCode, Date nowDate) {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("date", nowDate);
        hashMap.put("coinCode",coinCode);
        hashMap.put("priceCode",priceCode);
        return exchangeActionDao.selectLastActionByCoinCodeAndPriceCode(hashMap);
    }

    @Override
    public List<ExchangeAction> getNextAction(String coinCode, String priceCode, Date now) {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("date", DateUtils.dateFormat(now,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD));
        hashMap.put("coinCode",coinCode);
        hashMap.put("priceCode",priceCode);
        return exchangeActionDao.selectNextActionByCoinCodeAndPriceCode(hashMap);
    }

    @Override
    public List<ExchangeAction> getNextAllAction(Date nowDate) {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("date", nowDate);
        return exchangeActionDao.selectNextAllActionByDate(hashMap);
    }

    @Override
    public int updateActionStatus(ExchangeAction exchangeAction) {
        return exchangeActionDao.updateActionValide(exchangeAction.getValide(),exchangeAction.getId());
    }




}
