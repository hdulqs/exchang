package com.batsoft.app.member.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.batsoft.core.common.PageResult;
import com.batsoft.model.module.exchange.EntrustHistory;
import com.batsoft.model.module.member.User;
import com.batsoft.utils.DateUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.batsoft.app.member.service.UserBusService;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.member.Bankcard;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.service.TradeService;
import com.batsoft.service.module.member.service.BankcardService;
import com.batsoft.service.module.member.service.UserService;

@Service(value = "userBusService")
public class UserBusServiceImpl implements UserBusService {
	
	@Resource
	private EntrustIngService entrustIngService;
	
	@Resource
	private CustomerAccountService customerAccountService;
	
	@Resource
	private BankcardService bankcardService;
	
	@Resource
	private UserService userService;
	
	@Resource(name = "tradeRedisService")
	private TradeService tradeService;
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	@Override
	public void removeEntrustIng(String userId) {
		List<String> coinPairList = entrustIngService.findCoinPairGroupByUserId(userId);
		for(String coinPair : coinPairList) {
			String entrustingKey = String.format(RedisKeyConstant.USER_ENTRUSTING_S_S, coinPair, userId);
	        Map<String, String> resultMap = jedisClient.hgetall(JedisDataSourceSignleton.DB1, entrustingKey);
	        if(resultMap != null && resultMap.size() > 0) {
		        for(String key : resultMap.keySet()) {
		        	TradeEntrust redisEntrust = JSON.parseObject(resultMap.get(key), TradeEntrust.class);
		        	tradeService.cancel(userId, coinPair, redisEntrust.getOrderId());
		        }
	        }
		}
	}



	@Override
	public void removeCustomerAccount(String userId) {
		// remove Redis里面的数据
		String key = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
		jedisClient.del(JedisDataSourceSignleton.DB1, key);
		
		// remove data
		QueryFilter filter = new QueryFilter(CustomerAccount.class);
		filter.addFilter("user_id=", userId);
		customerAccountService.delete(filter);
	}

	@Override
	public void removeUserBankcard(String userId) {
		QueryFilter filter = new QueryFilter(Bankcard.class);
		filter.addFilter("user_id=", userId);
		bankcardService.delete(filter);
	}
	
	@Override
	public void removeUser(String userId) {
		userService.delete(userId);
	}

	@Override
	public PageResult findPageBySql(int page,int pageSize,HashMap mapMap) {
		PageResult pageResult = new PageResult();
		int from = pageSize*(page-1);
		mapMap.put("from",from);
		mapMap.put("pageSize",pageSize);
		List<User> customerAccountRecords = userService.findUnKYCUser(mapMap);
		Long rows = userService.findUnKYCUserTotal(mapMap);
		pageResult.setRows(customerAccountRecords);
		Map<String,Long> map = new HashMap<>();
		map.put("total",rows);
		map.put("pageSize", (long) pageSize);
		map.put("current", (long) page);
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(rows);
		pageResult.setPagination(map);
		return pageResult;
	}

}
