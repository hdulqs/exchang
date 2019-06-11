/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:47
 */

package com.batsoft.service.module.exchange.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.EntrustInfo;
import com.batsoft.model.module.exchange.vo.IndexUsdtTradeAmount;

/**
 * <p>EntrustInfoService</p>
 *
 * @author: Bat Admin
 * @Date :  2018-04-14 10:22:47
 */
public interface EntrustInfoService extends BaseService<EntrustInfo, String> {

	/**
	 * sql分页
	 *
	 * @param order_id 委托单号
	 * @param coin_code 成交类型
	 * @return
	 */
	PageResult findPageBySql(String coin_code,String order_id,int page,int pageSize,Date date);



	int  deleteById(String[] id, Date date);
	/**
	 * 根据USERid分页查询
	 *
	 * @param
	 * @return
	 */
	EntrustInfo findById(String id,Date date);

	/**
	 * 根据USERid分页查询
	 *
	 * @return
	 */
	PageResult findPageByUserId(HttpServletRequest request,String userid,int page,int pageSize,Date date);

	BigDecimal sumOfBuyAmoutByStartTimeAndId(String id, Date now,Date create_time,String coinCode,String priceCode);

	BigDecimal sumOfSellAmoutByStartTimeAndId(String id,Date now, Date create_time,String coinCode,String priceCode);

	/**
	 * 查询均价
	 * @param tradeCoinCode
	 * 				交易币
	 * @param pricingCoinCode
	 * 				定价币
	 * @return
	 */
	BigDecimal junjia(String tradeCoinCode, String pricingCoinCode);

	
	/**
	 * 根据时间段查询装换后的USDT数量
	 * 
	 * @param beginTime
	 * 			开始时间
	 * @return
	 */
	IndexUsdtTradeAmount findTradeAmountFormUsdt(Date beginTime);
	

}
