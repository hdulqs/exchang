package com.batsoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.batsoft.common.base.BaseController;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CoinPairUserCollectionService;
import com.batsoft.service.module.exchange.service.ExchangeActionService;

/**
 * 首页
 * 
 * @author simon
 */
@Controller
public class HomeController extends BaseController {
	
	@Autowired
	private CoinPairService coinPairService;
	
	@Autowired
	private CoinPairUserCollectionService coinPairUserCollectionService;
	
	@Autowired
	private ExchangeActionService exchangeActionService;
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	
}
