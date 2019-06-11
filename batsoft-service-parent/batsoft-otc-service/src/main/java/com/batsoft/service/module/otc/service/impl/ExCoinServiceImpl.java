/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-11-18 13:20:52
 */
package com.batsoft.service.module.otc.service.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.otc.ExCoin;
import com.batsoft.service.module.otc.dao.ExCoinDao;
import com.batsoft.service.module.otc.service.ExCoinService;
import com.batsoft.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> ExCoinServiceImpl </p>
 * @author:
 * @Date :  2017-11-18 13:20:52
 */
@Service("exCoinService")
public class ExCoinServiceImpl extends BaseServiceImpl<ExCoin, String> implements ExCoinService {

    @Autowired
    private ExCoinDao exCoinDao;
    @Autowired
    private RedisService redisService;

    public static final String CACHE_COINS="otc:coins";



    @Override
    public List<ExCoin> findCoins() {
        return exCoinDao.findListCoins();
    }

    @Override
    public String findJsonCoins() {
        String coins=redisService.get(CACHE_COINS);
        if (StringUtils.isEmpty(coins)) {
            coins = JSON.toJSONString(findCoins());
            redisService.set(CACHE_COINS, coins, 0);
        }
        return coins;
    }
}
