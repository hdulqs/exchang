package com.batsoft.service.module.exchange.service;

import java.util.List;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.CoinPairUserCollection;

/**
 * Created with IDEA
 * description:
 * author:liquanyu
 * Date:2018/8/21
 * Time:22:42
 */
public interface CoinPairUserCollectionService extends BaseService<CoinPairUserCollection, String> {

	/**
	 * 查询用户提币地址
	 * @param
	 * @return
	 */
	List<CoinPairUserCollection> findCoinPairUserCollectionList( String userId);
	
	int deleteByKeyId( String id,String userId);
	
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
	int deleteByCoinCode(String tradeCoinCode,String pricingCoinCode, String userId);

}
