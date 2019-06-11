package com.batsoft.service.module.exchange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.CoinPairUserCollection;

/**
 * Created with IDEA
 * description:
 * author:liquanyu
 * Date:2018/8/21
 * Time:22:47
 */
public interface CoinPairUserCollectionDao extends BaseDao<CoinPairUserCollection, String> {
	
	List<CoinPairUserCollection> findCoinPairUserCollectionList(@Param("userId") String userId);

	int deleteByKeyId(@Param("id") String id,@Param("userId") String userId);
	
	/**
	 * 使用货币代码删除收藏记录
	 * 
	 * @param tradeCoinCode
	 * 				交易币
	 * @param pricingCoinCode
	 * 				定价币
	 * @param userId
	 * 				用户ID
	 * @return
	 */
	int deleteByCoinCode(@Param("tradeCoinCode") String tradeCoinCode,@Param("pricingCoinCode") String pricingCoinCode,@Param("userId") String userId);
}
