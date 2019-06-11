package com.batsoft.service.module.exchange.trade.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.service.TradeAccountService;
import com.batsoft.service.module.exchange.trade.util.SqlUtil;
import com.batsoft.utils.gson.GsonSingleton;
import com.google.common.reflect.TypeToken;

/**
 * About Account
 * 
 * @author simon
 */
@SuppressWarnings("serial")
@Service(value = "tradeAccountService")
public class TradeAccountServiceImpl implements TradeAccountService {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();
	
	@Resource
    private JdbcTemplate jdbcTemplate;
	
	@Resource
	private CustomerAccountService customerAccountService;

	@Override
	public List<CustomerAccount> listByUserId(String userId) {
		List<CustomerAccount> result = new ArrayList<CustomerAccount>();
		String key = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
    	String dataSource = jedisClient.get(JedisDataSourceSignleton.DB1, key);
        if (!StringUtils.isEmpty(dataSource)) {
            result = gsonClient.fromJson(dataSource, new TypeToken<ArrayList<CustomerAccount>>() {}.getType());
        }
		return result;
	}

	@Override
	public CustomerAccount getCustomerAccountByUserId(String userId, String coinCode) {
		List<CustomerAccount> listCustomerAccount = listByUserId(userId);
		for(CustomerAccount account : listCustomerAccount) {
			if(Objects.equals(account.getCoinCode(), coinCode) || coinCode.equalsIgnoreCase(account.getCoinCode())) {
				return account;
			}
		}
		return null;
	}
	
	@Override
	public void setCoinAccountToRedis(String userId, CustomerAccount coinAccount) {
		List<CustomerAccount> dataSource = listByUserId(userId);
		if(dataSource != null && dataSource.size() > 0) {
			boolean ident = true;
			for(CustomerAccount account : dataSource) {
				if(Objects.equals(account.getCoinCode(), coinAccount.getCoinCode())) {
					account.setAvailable(coinAccount.getAvailable());
					account.setFreeze(coinAccount.getFreeze());
					ident = false;
				}
			}
			if(ident) {// 匹配不到该货币代码，设置成新货币
				dataSource.add(coinAccount);
			}
			String key = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
			jedisClient.set(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(dataSource));
		}
	}

	@Override
	public CustomerAccount updateCoinAvailable(String userId, String coinCode, BigDecimal amount) {
		if(amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}
		CustomerAccount coinAccount = getCustomerAccountByUserId(userId, coinCode);
		if(coinAccount != null && coinAccount.getAvailable() != null) {
			coinAccount.setAvailable(coinAccount.getAvailable().add(amount));
			this.setCoinAccountToRedis(userId, coinAccount);
		}else {
			coinAccount = customerAccountService.findCoinAccount(userId, coinCode);
		}
		// 执行更新
		if(coinAccount != null && coinAccount.getAvailable() != null) {
			jdbcTemplate.batchUpdate(SqlUtil.updateAccountAvailableSql(userId, coinCode, amount));
		}
		return coinAccount;
	}

	@Override
	public CustomerAccount updateCoinFreeze(String userId, String coinCode, BigDecimal amount) {
		if(amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}
		CustomerAccount coinAccount = getCustomerAccountByUserId(userId, coinCode);
		if(coinAccount != null && coinAccount.getFreeze() != null) {
			coinAccount.setFreeze(coinAccount.getFreeze().add(amount));
			this.setCoinAccountToRedis(userId, coinAccount);
		}else {
			coinAccount = customerAccountService.findCoinAccount(userId, coinCode);
		}
		// 执行更新
		if(coinAccount != null && coinAccount.getFreeze() != null) {
			jdbcTemplate.batchUpdate(SqlUtil.updateAccountFreezeSql(userId, coinCode, amount));
		}
		return coinAccount;
	}

	@Override
	public CustomerAccount updateCoinAmount(String userId, String coinCode, BigDecimal available, BigDecimal freeze) {
		if(BigDecimal.ZERO.compareTo(available) == 0 && BigDecimal.ZERO.compareTo(freeze) == 0) {
			return null;
		}
		CustomerAccount coinAccount = getCustomerAccountByUserId(userId, coinCode);
		if(coinAccount != null && coinAccount.getAvailable() != null && BigDecimal.ZERO.compareTo(available) != 0) {
			coinAccount.setAvailable(coinAccount.getAvailable().add(available));
		}
		if(coinAccount != null && coinAccount.getFreeze() != null && BigDecimal.ZERO.compareTo(freeze) != 0) {
			coinAccount.setFreeze(coinAccount.getFreeze().add(freeze));
		}
		if(coinAccount != null) {
			this.setCoinAccountToRedis(userId, coinAccount);
		}else {
			coinAccount = customerAccountService.findCoinAccount(userId, coinCode);
		}
		// 执行更新
		if(coinAccount != null && BigDecimal.ZERO.compareTo(available) != 0) {
			jdbcTemplate.batchUpdate(SqlUtil.updateAccountAvailableSql(userId, coinCode, available));
		}
		if(coinAccount != null && BigDecimal.ZERO.compareTo(freeze) != 0) {
			jdbcTemplate.batchUpdate(SqlUtil.updateAccountFreezeSql(userId, coinCode, freeze));
		}
		return coinAccount;
	}

}
