/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:22:21 
*/
package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.EntrustHistory;
import com.batsoft.service.module.exchange.dao.EntrustHistoryDao;
import com.batsoft.service.module.exchange.service.EntrustHistoryService;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p> EntrustHistoryServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-14 10:22:21 
*/
@Service("entrustHistoryService")
public class EntrustHistoryServiceImpl extends BaseServiceImpl<EntrustHistory, String> implements EntrustHistoryService{

	@Autowired
	private EntrustHistoryDao entrustHistoryDao;

    private final String TABLE_NAME = "exchange_entrust_history";

    @Override
    public EntrustHistory findById(String id, Date date) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename",TABLE_NAME+Today);
        return entrustHistoryDao.findById(mapMap);
    }

    @Transactional(rollbackFor = Exception.class,timeout = 3,propagation = Propagation.REQUIRED)
    @Override
    public int deleteById(String[] id, Date date) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        HashMap<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename",TABLE_NAME+Today);
        return entrustHistoryDao.deleteById(mapMap);
    }

    @Override
    public PageResult findPageBySql(String account_id, String customer_id,String entrustPrice,String entrustAmout,String entrustState,String category, int page, int pageSize, Date date) {
        PageResult pageResult = new PageResult();
        int from = pageSize*(page-1);
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("customer_id",customer_id);
        mapMap.put("account_id",account_id);
        mapMap.put("entrustPrice",entrustPrice);
        mapMap.put("entrustAmout",entrustAmout);
        mapMap.put("entrustState",entrustState);
        mapMap.put("category",category);
        mapMap.put("from",from);
        mapMap.put("pageSize",pageSize);
        mapMap.put("tablename",TABLE_NAME+Today);
        List<EntrustHistory> customerAccountRecords = entrustHistoryDao.findPageBySql(mapMap);
        Long rows = entrustHistoryDao.findPageBySqlTotal(mapMap);
        pageResult.setRows(customerAccountRecords);
        Map<String,Long> map = new HashMap<>();
        map.put("total",rows);
        map.put("pageSize", (long) pageSize);
        map.put("current", (long) page);
        pageResult.setPagination(map);
        return pageResult;
    }

    @Override
    public BigDecimal sumOfBuyAmoutByStartTimeAndId(String id, Date date,Date create_time,String coinCode,String priceCode) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename",TABLE_NAME+Today);
        mapMap.put("create_time",create_time);
        mapMap.put("coinCode",coinCode);
        mapMap.put("priceCode",priceCode);
        mapMap.put("type","buy");
        return entrustHistoryDao.sumOfAmoutByStartTimeAndId(mapMap);
    }

    @Override
    public BigDecimal sumOfSellAmoutByStartTimeAndId(String id, Date date,Date create_time,String coinCode,String priceCode) {
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("id",id);
        mapMap.put("tablename",TABLE_NAME+Today);
        mapMap.put("create_time",create_time);
        mapMap.put("coinCode",coinCode);
        mapMap.put("priceCode",priceCode);
        mapMap.put("type","sell");
        return entrustHistoryDao.sumOfAmoutByStartTimeAndId(mapMap);
    }

    /**
     * 根据USERid分页查询
     * @return
     */
    @Override
    public PageResult findPageByUserId(HttpServletRequest request,String userId, int page, int pageSize, Date date) {
        PageResult pageResult = new PageResult();
        int from = pageSize*(page-1);
        String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
        Map<String,Object> mapMap = new HashMap<>();
        mapMap.put("user_id",userId);
        if(request.getParameter("base_asset") !=null && StringUtils.isNotEmpty(request.getParameter("base_asset"))){
            mapMap.put("coin_code",request.getParameter("base_asset"));
        }
        if(request.getParameter("quote_asset") !=null && StringUtils.isNotEmpty(request.getParameter("quote_asset"))){
            mapMap.put("price_code",request.getParameter("quote_asset"));
        }
        mapMap.put("from",from);
        mapMap.put("pageSize",pageSize);
        mapMap.put("tablename","exchange_entrust_history"+Today);
        List<EntrustHistory> customerAccountRecords = entrustHistoryDao.findPageByUserId(mapMap);
        Long rows = entrustHistoryDao.findPageByUserIdTotal(mapMap);
        pageResult.setRows(customerAccountRecords);
        Map<String,Long> map = new HashMap<>();
        map.put("total",rows);
        map.put("pageSize", (long) pageSize);
        map.put("current", (long) page);
        pageResult.setPagination(map);
        return pageResult;
    }


}
