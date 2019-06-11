package com.batsoft.service.module.exchange.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.CoinPairUserCollection;
import com.batsoft.service.module.exchange.dao.CoinPairUserCollectionDao;
import com.batsoft.service.module.exchange.service.CoinPairUserCollectionService;

/**
 * Created with IDEA
 * description:
 * author:liquanyu
 * Date:2018/8/21
 * Time:23:21
 */
@Service("CoinPairUserCollectionService")
public class CoinPairUserCollectionServiceImpl extends BaseServiceImpl<CoinPairUserCollection, String> implements CoinPairUserCollectionService {
	@Autowired
	private CoinPairUserCollectionDao coinPairUserCollectionDao;

	@Override
	public List<CoinPairUserCollection> findCoinPairUserCollectionList(String userId) {

		return coinPairUserCollectionDao.findCoinPairUserCollectionList(userId);
	}

	@Override
	public int deleteByKeyId(String id,String userId) {
		return coinPairUserCollectionDao.deleteByKeyId(id,userId);
	}

	@Override
	public int deleteByCoinCode(String tradeCoinCode, String pricingCoinCode, String userId) {
		return coinPairUserCollectionDao.deleteByCoinCode(tradeCoinCode, pricingCoinCode, userId);
	}
}
