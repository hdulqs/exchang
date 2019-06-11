/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:47
 */
package com.batsoft.service.module.exchange.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.EntrustInfo;
import com.batsoft.model.module.exchange.vo.EntrustInfoVo;
import com.batsoft.model.module.exchange.vo.IndexUsdtTradeAmount;
import com.batsoft.service.module.exchange.dao.EntrustInfoDao;
import com.batsoft.service.module.exchange.service.EntrustInfoService;
import com.batsoft.utils.DateUtils;

/**
 *
 * @author: Bat Admin
 * @Date :  2018-04-14 10:22:47
 */
@Service("entrustInfoService")
public class EntrustInfoServiceImpl extends BaseServiceImpl<EntrustInfo, String> implements EntrustInfoService {

	@Autowired
	private EntrustInfoDao entrustInfoDao;

	private final String TABLE_NAME = "exchange_entrust_info";

	@Override
	public PageResult findPageBySql(String coin_code,String order_id,int page,int pageSize,Date date) {
		PageResult pageResult = new PageResult();
		int from = pageSize*(page-1);
		String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
		Map<String,Object> mapMap = new HashMap<>();
		mapMap.put("buy_order_id",order_id);
		mapMap.put("coin_code",coin_code);
		mapMap.put("from",from);
		mapMap.put("pageSize",pageSize);
		mapMap.put("tablename",TABLE_NAME + Today);
		List<EntrustInfo> customerAccountRecords = entrustInfoDao.findPageBySql(mapMap);
		Long rows = entrustInfoDao.findPageBySqlTotal(mapMap);
		pageResult.setRows(customerAccountRecords);
		Map<String,Long> map=new HashMap<>();
		map.put("total",rows);
		map.put("pageSize", (long) pageSize);
		map.put("current", (long) page);
		pageResult.setPagination(map);
		return pageResult;
	}


	@Override
	public EntrustInfo findById(String id, Date date) {
		String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
		Map<String,Object> mapMap = new HashMap<>();
		mapMap.put("id",id);
		mapMap.put("tablename",TABLE_NAME+Today);
		return entrustInfoDao.findById(mapMap);
	}




	@Transactional(rollbackFor = Exception.class,timeout = 3,propagation = Propagation.REQUIRED)
	@Override
	public int deleteById(String[] id, Date date) {
		String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
		HashMap<String,Object> mapMap = new HashMap<>();
		mapMap.put("id",id);
		mapMap.put("tablename",TABLE_NAME+Today);
		return entrustInfoDao.deleteById(mapMap);
	}

	/**
	 * 根据userId查询分页
	 *
	 * @return
	 */
	@Override
	public PageResult findPageByUserId(HttpServletRequest request,String user_id,int page,int pageSize,Date date) {
		PageResult pageResult = new PageResult();
		int from = pageSize*(page-1);
		String Today = DateUtils.dateFormatToString(date,DateUtils.TABLES_DAY_FIX);
		Map<String,Object> param = new HashMap<>();
		param.put("user_id", user_id);
		param.put("from", from);
		param.put("pageSize", pageSize);
		param.put("coin_code", request.getParameter("base_asset"));
		param.put("tablename", TABLE_NAME+Today);
		List<EntrustInfo> customerAccountRecords = entrustInfoDao.findPageByUserId(param);
		Long rows = entrustInfoDao.findPageByUserIdTotal(param);
		pageResult.setRows(customerAccountRecords);
		Map<String,Long> map=new HashMap<>();
		map.put("total",rows);
		map.put("pageSize", (long) pageSize);
		map.put("current", (long) page);
		pageResult.setPagination(map);
		return pageResult;
	}

	@Override
	public BigDecimal junjia(String tradeCoinCode, String pricingCoinCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tradeCoinCode", tradeCoinCode);
		map.put("pricingCoinCode", pricingCoinCode);
		map.put("tablename",TABLE_NAME + DateUtils.dateFormatToString(new Date(),DateUtils.TABLES_DAY_FIX));
		EntrustInfoVo monery = entrustInfoDao.getTotalMoney(map);
		EntrustInfoVo amunt = entrustInfoDao.getTotalAmunt(map);
		if (monery != null && amunt != null) {
			return monery.getEntrustTotalMoney().divide(amunt.getEntrustTotalAmunt(),2, BigDecimal.ROUND_HALF_UP);
		}
		return null;
	}


	@Override
	public IndexUsdtTradeAmount findTradeAmountFormUsdt(Date beginTime) {
		String Today = DateUtils.dateFormatToString(beginTime, DateUtils.TABLES_DAY_FIX);
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("tablename",TABLE_NAME + Today);
		IndexUsdtTradeAmount amount = entrustInfoDao.findTradeAmountFormUsdt(values);
		if(amount == null) { amount = new IndexUsdtTradeAmount(); }
		amount.setFeeCoinAmount(amount.getFeeCoinAmount() == null? BigDecimal.ZERO : amount.getFeeCoinAmount());
		amount.setTradeCoinAmount(amount.getTradeCoinAmount() == null? BigDecimal.ZERO : amount.getTradeCoinAmount());
		return amount;
	}

	@Override
	public BigDecimal sumOfBuyAmoutByStartTimeAndId(String id,Date now, Date create_time,String coinCode,String priceCode) {
		String Today = DateUtils.dateFormatToString(now,DateUtils.TABLES_DAY_FIX);
		Map<String,Object> mapMap = new HashMap<>();
		mapMap.put("id",id);
		mapMap.put("create_time",create_time);
		mapMap.put("coinCode",coinCode);
		mapMap.put("priceCode",priceCode);
		mapMap.put("type","buy");
		mapMap.put("tablename",TABLE_NAME + Today);
		return entrustInfoDao.sumOfBuyAmoutByStartTimeAndId(mapMap);
	}

	@Override
	public BigDecimal sumOfSellAmoutByStartTimeAndId(String id,Date now, Date create_time,String coinCode,String priceCode) {
		String Today = DateUtils.dateFormatToString(now,DateUtils.TABLES_DAY_FIX);
		Map<String,Object> mapMap = new HashMap<>();
		mapMap.put("id",id);
		mapMap.put("create_time",create_time);
		mapMap.put("coinCode",coinCode);
		mapMap.put("priceCode",priceCode);
		mapMap.put("type","sell");
		mapMap.put("tablename",TABLE_NAME + Today);
		return entrustInfoDao.sumOfSellAmoutByStartTimeAndId(mapMap);
	}

}
