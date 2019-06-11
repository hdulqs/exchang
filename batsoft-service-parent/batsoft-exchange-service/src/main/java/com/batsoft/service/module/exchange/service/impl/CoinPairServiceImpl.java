/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:20:19
 */
package com.batsoft.service.module.exchange.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.batsoft.core.cache.motion.KlineSymbol24dataUtil;
import com.batsoft.model.module.exchange.vo.CoinPairVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.CoinPair;
import com.batsoft.model.module.exchange.vo.CoinPairDigits;
import com.batsoft.service.module.exchange.dao.CoinPairDao;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.utils.StringUtils;

/**
 * <p> CoinPairServiceImpl </p>
 * @author: Bat Admin
 * @Date :  2018-04-14 10:20:19
 */
@Service("coinPairService")
public class CoinPairServiceImpl extends BaseServiceImpl<CoinPair, String> implements CoinPairService {

    @Autowired
    private CoinPairDao coinPairDao;

    @Autowired
    private RedisService redisService;
    public static final String CACHE_PREFIX = "exchange:";
    public static final String CACHE_TRADE_COINS = CACHE_PREFIX + "trade_coins:";
    public static final String CACHE_TRADE_AREA_COINS = CACHE_PREFIX + "trade_area_coins";
    public static final String CACHE_TRADE_RECOMMEND_COINS = CACHE_PREFIX + "trade_recommend_coins";   //推荐币key
    public static final String CACHE_TRADE_DIGITS_COINS = CACHE_PREFIX + "trade_digits_coins";   //交易币位数key

    /**
     * 交易对缓冲
     */
    public static final String CACHE_COINPAIR_STR = CACHE_PREFIX + "coin_pair_str";
    @Autowired
    private CoinService coinService;

    @Override
    public boolean hasCoinTrade(String priceSymbol, String tradeSymobl) {
        QueryFilter filter = new QueryFilter(CoinPair.class);
        filter.addFilter("pricingCoinCode_EQ", priceSymbol);
        filter.addFilter("tradeCoinCode_EQ", tradeSymobl);
        CoinPair coinTrade = this.get(filter);
        return !StringUtils.isNull(coinTrade);
    }

    @Override
    public List<CoinPair> findTradeCoins(String priceCoin) {
        QueryFilter filter = new QueryFilter(CoinPair.class);
        filter.addFilter("pricingCoinCode_EQ", priceCoin);
        filter.addFilter("status_EQ", CoinPair.STATUS1);
        return this.find(filter);
    }

    /**
     * 查询推荐币种列表
     * @return
     */
    @Override
    public String findRecomment() {
        QueryFilter filter = new QueryFilter(CoinPair.class);
        filter.addFilter("recommend_EQ", CoinPair.RECOMENT1);
        filter.orderBy("sort asc");
        List<CoinPair> list = this.find(filter);
        String coinpair = JSON.toJSONString(list);
        redisService.set(CACHE_TRADE_RECOMMEND_COINS, coinpair, RedisService.CACHE_TIME);
        return coinpair;
    }

    @Override
    public String findJsonTradeCoins(String priceCoin) {
        String coins = redisService.get(CACHE_TRADE_COINS + priceCoin);
        if (StringUtils.isEmpty(coins)) {
            List<CoinPair> coinPairList = findTradeCoins(priceCoin);
            coins = JSON.toJSONString(coinPairList);
            redisService.set(CACHE_TRADE_COINS + priceCoin, coins, RedisService.CACHE_TIME);
        }
        return coins;
    }

    @Override
    public void updateJsonTradeCoins(String priceCoin) {
        String coins = JSON.toJSONString(findTradeCoins(priceCoin));
        redisService.set(CACHE_TRADE_COINS + priceCoin, coins, RedisService.CACHE_TIME);
    }

    /**
     * 更新交易区数据 --保存全部数据[{"ETH:"[{},{}]},{"USDT":[{},{}]}]
     *
     * @return
     */
    @Override
    public String updateTradeAreaCoins() {
        List<Coin> list = coinService.findPriceCoins();
        Map<String, String> data = new HashMap<>();
        for (Coin coin : list) {
            data.put(coin.getCoinCode(), this.findJsonTradeCoins(coin.getCoinCode()));
        }
        String cacheData = JSON.toJSONString(data);
        //从exchange_coin 表里面查询出名称然后从redis根据名称查询code查询出来
        redisService.set(CACHE_TRADE_AREA_COINS, cacheData, RedisService.CACHE_TIME);
        return cacheData;
    }

    @Override
    public String findTradeAreaCoins() {
        String coins = redisService.get(CACHE_TRADE_AREA_COINS);
        if (StringUtils.isEmpty(coins)) {
            coins = updateTradeAreaCoins();
        }
        return coins;
    }

    @Override
    public String findRecommentSession() {
        String coins = redisService.get(CACHE_TRADE_RECOMMEND_COINS);
        if (StringUtils.isEmpty(coins) || "[]".equals(coins)) {
            coins = findRecomment();
        }
        List<CoinPair> list = JSONArray.parseArray(coins, CoinPair.class);
        List<CoinPairVO> coinPairVOList=new ArrayList<>();
        for (CoinPair coinPair : list ) {
            CoinPairVO coinPairVO = new CoinPairVO();
            BeanUtils.copyProperties(coinPair,coinPairVO);
            KlineSymbol24dataUtil dataUtil = new KlineSymbol24dataUtil(coinPairVO.getTradeCoinCode()+"_"+coinPairVO.getPricingCoinCode());
            coinPairVO.setRate(dataUtil.getValue(KlineSymbol24dataUtil.RATE).toString());
            coinPairVO.setTradingVolume(dataUtil.getValue(KlineSymbol24dataUtil.AMOUT).toString());
            coinPairVOList.add(coinPairVO);
        }
        return JSON.toJSONString(coinPairVOList);
    }

    /**
     * 查询定价币和交易币的比例
     * @return
     */
    @Override
    public String findMapTradeCoins() {
        String coins = findTradeAreaCoins();
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(coins)) {
            JSONObject json = JSON.parseObject(coins);

            if (!json.isEmpty()) {
                Set<String> set = json.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (json.containsKey(key)) {
                        String coinJsons = json.getString(key);
                        List<CoinPair> list = JSONObject.parseArray(coinJsons, CoinPair.class);//把字符串转换成集合
                        if (list != null && list.size() > 0) {
                            Map<String, BigDecimal> map0 = new HashMap<String, BigDecimal>();
                            for (CoinPair p : list) {
                                map0.put(p.getTradeCoinCode(), BigDecimal.ZERO);
                            }
                            map.put(key, map0);
                        }

                    }
                }
            }
        }
        return JSON.toJSONString(map);
    }

    /**
     * 查询交易币小数定价规则
     */
    @Override
    public String findCoinPairDigit() {
        String digits = redisService.get(CACHE_TRADE_DIGITS_COINS);
        if (StringUtils.isEmpty(digits)) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<CoinPair> list = this.findAll();
            if (list != null && list.size() > 0) {
                for (CoinPair coinPair : list) {
                    CoinPairDigits digit = new CoinPairDigits(coinPair);
                    map.put(coinPair.getTradeCoinCode() + "_" + coinPair.getPricingCoinCode(), digit);
                }
            }
            digits = JSON.toJSONString(map);
            redisService.set(CACHE_TRADE_DIGITS_COINS, digits, RedisService.CACHE_TIME);
        }
        return digits;
    }

    /**
     * 查询交易币小数定价规则
     */
    @Override
    public void findCoinPairDigitInit() {
        String digits = redisService.get(CACHE_TRADE_DIGITS_COINS);
        Map<String, Object> map = new HashMap<String, Object>();
        List<CoinPair> list = this.findAll();
        if (list != null && list.size() > 0) {
            for (CoinPair coinPair : list) {
                CoinPairDigits digit = new CoinPairDigits(coinPair);
                map.put(coinPair.getTradeCoinCode() + CHS.underline.getValue() + coinPair.getPricingCoinCode(), digit);
            }
        }
        digits = JSON.toJSONString(map);
        redisService.set(CACHE_TRADE_DIGITS_COINS, digits, RedisService.CACHE_TIME);
    }

    @Override
    public List<String> findCoinPairStr() {
        List<String> coinPairList = (List<String>) redisService.getObject(CACHE_COINPAIR_STR);
        if (coinPairList == null) {
            coinPairList = this.updateCoinPairStr();
            redisService.setObject(CACHE_COINPAIR_STR, coinPairList, RedisService.CACHE_TIME);
        }
        return coinPairList;
    }

    @Override
    public List<String> updateCoinPairStr() {

        List<String> coinPairList = new ArrayList<>();
        List<CoinPair> list = findAll();
        for (CoinPair coin : list) {
            coinPairList.add(coin.getTradeCoinCode() + "_" + coin.getPricingCoinCode());
        }

        redisService.setObject(CACHE_COINPAIR_STR, coinPairList, RedisService.CACHE_TIME);

        return coinPairList;
    }

    /**
     * 推荐设置管理
     * @param PKS
     * @param type 0  --取消推荐  else  推荐设置
     * @return
     */
    @Override
    public JsonResult recommendCoins(String[] PKS, Integer type) {
        JsonResult jsonResult = new JsonResult();
        try {
            for (String PK : PKS) {
                CoinPair coinPair = this.get(PK);
                if (coinPair != null) {
                    coinPair.setRecommend(type);
                    this.update(coinPair);
                }
                //更新缓存
                String coins = findRecomment();
                redisService.set(CACHE_TRADE_RECOMMEND_COINS, coins, RedisService.CACHE_TIME);
                jsonResult.setSuccess(true);
                jsonResult.setCode(Constants.SUCCESS);
                jsonResult.setData(coins);
                jsonResult.setMsg("设置成功");
            }
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg("系统有误-"+e.getMessage());
        }
        return jsonResult;
    }

}
