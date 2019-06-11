/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:02
 */
package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.CustomerAccountFreeze;
import com.batsoft.service.module.exchange.dao.CustomerAccountFreezeDao;
import com.batsoft.service.module.exchange.service.CustomerAccountFreezeService;
import com.batsoft.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> CustomerAccountFreezeServiceImpl </p>
 * @author: Bat Admin
 * @Date :  2018-04-14 10:21:02
 */
@Service("customerAccountFreezeService")
public class CustomerAccountFreezeServiceImpl extends BaseServiceImpl<CustomerAccountFreeze, String> implements CustomerAccountFreezeService {

    @Autowired
    private CustomerAccountFreezeDao customerAccountFreezeDao;


    @Override
    public PageResult findPageBySql(String account_id,String coin_code,int page, int pageSize, Date date) {
        PageResult pageResult = new PageResult();
        int from = pageSize*(page-1);
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("account_id",account_id);
        mapMap.put("coin_code",coin_code);
        mapMap.put("from",from);
        mapMap.put("pageSize",pageSize);
        mapMap.put("tablename","exchange_customer_account_freeze"+Today);
        List<CustomerAccountFreeze> customerAccountRecords = customerAccountFreezeDao.findPageBySql(mapMap);
        Long rows = customerAccountFreezeDao.findPageBySqlTotal(mapMap);
        pageResult.setRows(customerAccountRecords);
        Map<String,Long> map=new HashMap<>();
        map.put("total",rows);
        map.put("pageSize", (long) pageSize);
        map.put("current", (long) page);
        pageResult.setPagination(map);
        return pageResult;
    }

    @Override
    public CustomerAccountFreeze findById(String id, Date date) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename","exchange_customer_account_freeze"+Today);
        return customerAccountFreezeDao.findById(mapMap);
    }

    @Override
    public int saveObject(CustomerAccountFreeze freeze, Date date) {
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("account_id",freeze.getAccountId());
        mapMap.put("coin_code",freeze.getCoinCode());
        mapMap.put("create_time",freeze.getCreateTime());
        mapMap.put("update_time",new Date());
        mapMap.put("customer_id",freeze.getCustomerId());
        mapMap.put("freeze_type",freeze.getFreezeType());
        mapMap.put("order_id",freeze.getOrderId());
        mapMap.put("freeze_money",freeze.getFreezeMoney());
        mapMap.put("user_name",freeze.getUserName());
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        mapMap.put("tablename","exchange_customer_account_freeze"+Today);
        return  customerAccountFreezeDao.saveObject(mapMap);
    }
    @Transactional(rollbackFor = Exception.class,timeout = 3,propagation = Propagation.REQUIRED)
    @Override
    public int deleteById(String[] id, Date date) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        HashMap<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename","exchange_customer_account_freeze"+Today);
        return customerAccountFreezeDao.deleteById(mapMap);
    }
}
