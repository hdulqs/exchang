package com.batsoft.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.utils.CoinPairConfigUtil;
import com.batsoft.service.KlineBusService;
import com.batsoft.service.module.exchange.service.KlineService;
import com.batsoft.utils.gson.GsonSingleton;

@Service(value = "klineBusService")
public class KlineBusServiceImpl implements KlineBusService {

	@Resource
	private KlineService klineService;

	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();
	
	private CoinPairConfigUtil decimalUtil = new CoinPairConfigUtil();
	
	
	

}
