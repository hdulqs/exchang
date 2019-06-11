/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:48
 */
package com.batsoft.service.module.exchange.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.dao.CustomerAccountRecordDao;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.trade.util.SqlUtil;
import com.batsoft.utils.BeansUtil;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;

/**
 * <p> CustomerAccountRecordServiceImpl </p>
 * @author: Bat Admin
 * @Date :  2018-04-14 10:21:48
 */
@Service("customerAccountRecordService")
public class CustomerAccountRecordServiceImpl extends BaseServiceImpl<CustomerAccountRecord, String> implements CustomerAccountRecordService {

    @Autowired
    private CustomerAccountRecordDao customerAccountRecordDao;
    
    @Autowired
    private RabbitMqSender rabbitMqSender;


    //查询方法
    @Override
    public PageResult findPageBySql(String customer_id,String account_id,int page,int pageSize,Date date) {
        PageResult pageResult = new PageResult();
        int from = pageSize*(page-1);
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("customer_id",customer_id);
        mapMap.put("account_id",account_id);
        mapMap.put("from",from);
        mapMap.put("pageSize",pageSize);
        mapMap.put("tablename","exchange_customer_account_record"+Today);
        List<CustomerAccountRecord> customerAccountRecords = customerAccountRecordDao.findPageBySql(mapMap);
        Long rows = customerAccountRecordDao.findPageBySqlTotal(mapMap);
        pageResult.setRows(customerAccountRecords);
        Map<String,Long> map=new HashMap<>();
        map.put("total",rows);
        map.put("pageSize", (long) pageSize);
        map.put("current", (long) page);
        pageResult.setPagination(map);
        return pageResult;
    }

    @Override
    public CustomerAccountRecord findById(String id, Date date) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename","exchange_customer_account_record"+Today);
        return customerAccountRecordDao.findById(mapMap);
    }
    @Transactional(rollbackFor = Exception.class,timeout = 3,propagation = Propagation.REQUIRED)
    @Override
    public int deleteById(String[] id, Date date) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        HashMap<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename","exchange_customer_account_record"+Today);
        return customerAccountRecordDao.deleteById(mapMap);
    }

    @Override
    public void saveAccountRecord(String type, String accountId, String userId, String coinCode, String amount) {
        String tableDate = DateUtils.dateFormatToString(new Date(),DateUtils.TABLES_DAY_FIX);
        StringBuffer sql = new StringBuffer("");
        //实际流水增加
        CustomerAccountRecord record = new CustomerAccountRecord();
        record.setType(type);
        record.setAccountId(accountId);
        record.setCustomerId(userId);
        record.setCoinCode(coinCode);
        record.setOrderId(StringUtils.createOrderNum());
        record.setMoney(new BigDecimal(amount));
        sql.append(SqlUtil.createSql(record,tableDate));
        //同步sql消息
        rabbitMqSender.toSql(sql.toString());
    }
    
    
    @Override
	public void saveAccountRecord(String type, String userId, String coinCode, BigDecimal money, String tradeCoinCode, String pricingCoinCode, String remark,String tableDate) {

        StringBuffer sql = new StringBuffer();
        CustomerAccountRecord record = new CustomerAccountRecord();
        record.setType(type);
        record.setCustomerId(userId);
        record.setCoinCode(coinCode);
        record.setOrderId(StringUtils.createOrderNum());
        record.setMoney(money);
        record.setTradeCoinCode(tradeCoinCode);
        record.setPricingCoinCode(pricingCoinCode);
        record.setRemark(remark);
        sql.append(SqlUtil.createSql(record,tableDate));
        rabbitMqSender.toSql(sql.toString());
	}
    
    
    @Override
    public BigDecimal sumMoneyByTypeCoinCode(String type, String coinCode, Date date) {
        Map<String, Object> values = new HashMap<String, Object>();
        String table = BeansUtil.findBeanTableValue(CustomerAccountRecord.class);
        String today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        values.put("type", type);
        values.put("coinCode", coinCode);
        values.put("tablename", table + today);
        BigDecimal money = customerAccountRecordDao.findMoneyByTypeCoinCode(values);
        return money == null ? BigDecimal.ZERO : money;
    }

	@Override
	public PageResult findPage(CustomerAccountRecord param, int page,int pageSize,Date date) {
        PageResult pageResult = new PageResult();
        int from = pageSize * (page-1);
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        // 调用Dao层接口
        HashMap<String,Object> mapMap = new HashMap<>();
		// 客户ID
		if(StringUtils.isNotBlank(param.getCustomerId())) {
            mapMap.put("customerId",param.getCustomerId());
		}
		// 交易类型
		if(StringUtils.isNotBlank(param.getType())) {
            mapMap.put("type",param.getType());
		}

        mapMap.put("from", from);
        mapMap.put("pageSize", pageSize);
        mapMap.put("tablename", "exchange_customer_account_record" + Today);
        List<CustomerAccountRecord> list = customerAccountRecordDao.findPage(mapMap);
        Long rows = customerAccountRecordDao.findPageToalRows(mapMap);
        pageResult.setRows(list);
        pageResult.setTotal(rows);
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
		// 返回
		return pageResult;
	}
	
}
