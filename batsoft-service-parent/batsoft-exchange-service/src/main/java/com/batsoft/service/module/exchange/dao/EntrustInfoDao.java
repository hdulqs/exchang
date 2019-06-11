/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:47
 */

package com.batsoft.service.module.exchange.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.EntrustInfo;
import com.batsoft.model.module.exchange.vo.EntrustInfoVo;
import com.batsoft.model.module.exchange.vo.IndexUsdtTradeAmount;

/**
 * <p>EntrustInfoDao</p>
 *
 * @author: Bat Admin
 * @Date :  2018-04-14 10:22:47
 */
public interface EntrustInfoDao extends BaseDao<EntrustInfo, String> {

	List<EntrustInfo> findPageBySql(Map<String, Object> map);

	Long findPageBySqlTotal(Map<String, Object> map);

	EntrustInfo findById(Map<String, Object> map);

	/**
	 * 通过用户id分页查询
	 *
	 * @param map
	 * @return
	 */
	List<EntrustInfo> findPageByUserId(Map<String, Object> map);

	Long findPageByUserIdTotal(Map<String, Object> map);

	EntrustInfoVo getTotalAmunt(Map<String, Object> map);

	EntrustInfoVo getTotalMoney(Map<String, Object> map);

	/**
	 * 查询交易大厅成交数据
	 * 
	 * @return
	 */
	List<EntrustInfoVo> findByTradePricingCoin(Map<String, Object> map);

	
	/**
	 * 根据时间段查询装换后的USDT数量
	 * 
	 * @param values
	 * 			
	 * @return
	 */
	IndexUsdtTradeAmount findTradeAmountFormUsdt(Map<String, Object> values);


	BigDecimal sumOfBuyAmoutByStartTimeAndId(Map<String, Object> map);

	BigDecimal sumOfSellAmoutByStartTimeAndId(Map<String, Object> map);

	int  deleteById(HashMap<String,Object> map);
}
