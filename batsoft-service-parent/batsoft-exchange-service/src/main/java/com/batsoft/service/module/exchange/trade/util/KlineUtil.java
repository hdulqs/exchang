package com.batsoft.service.module.exchange.trade.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.motion.KlineSymbol24dataUtil;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.coin.CoinUtil;
import com.batsoft.core.common.coin.model.CoinConvertResult;
import com.batsoft.core.common.constant.KlineTimeNodeConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.model.module.exchange.vo.KlineVo;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.model.TradeEntrustInfo;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;

import redis.clients.jedis.Jedis;

public class KlineUtil {
	
	private static JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

    /**
     * 2018 0509 18:09:03
     * 15min===2018-05-09 18:00:00
     * 30min===2018-05-09 18:00:00
     * 6hour===2018-05-09 18:00:00
     * 5min===2018-05-09 18:05:00
     * 4hour===2018-05-09 16:00:00
     * 1min===2018-05-09 18:09:00
     * 1hour===2018-05-09 18:00:00
     * 2hour===2018-05-09 18:00:00
     * 12hour===2018-05-09 12:00:00
     * 1day===2018-05-09 00:00:00
     * 1week===2018-05-07 00:00:00
     *
     * @param tradeEntrust
     * @param orders
     */
    public static void calculateTimeZoneData(TradeEntrust tradeEntrust, List<TradeEntrustInfo> orders) {
        if (orders != null && !orders.isEmpty()) {
            Jedis jedis = jedisClient.getJedis(JedisDataSourceSignleton.DB1);
            try {
            	String symbol = tradeEntrust.getTradeCoinCode() + CHS.underline.getValue() + tradeEntrust.getPricingCoinCode();
                String klineTimeZoneData = String.format(RedisKeyConstant.KLINE_TIME_ZONE_DATA, symbol);
                String klineTimeZoneDataLast = String.format(RedisKeyConstant.KLINE_TIME_ZONE_DATA_LAST, symbol);
                //委托时间
                Date entrustDate = new Date(tradeEntrust.getEntrustTime());
                //最新价
                BigDecimal newPrice = orders.get(orders.size() - 1).getEntrustPrice();
                //成交量
                BigDecimal amout = new BigDecimal(0);
                for (TradeEntrustInfo info : orders) {
                    amout = amout.add(info.getEntrustAmout());
                }
                String[] zoneKey = KlineTimeNodeConstant.convertToArray();
                //获取时区
                Map<String, Date> periodMap = DateUtils.getPeriodMap(entrustDate);
                for (String zk : zoneKey) {
                    String hget = jedis.hget(klineTimeZoneData, zk);
                    if (StringUtils.isEmpty(hget)) {//如果为空，则创建
                        KlineVo kline = new KlineVo();
                        kline.setClose(newPrice);
                        kline.setOpen(newPrice);
                        kline.setHigh(newPrice);
                        kline.setLow(newPrice);
                        kline.setVolume(amout);
                        //时区时间
                        kline.setCreatedDate(periodMap.get(zk).getTime());
                        kline.setDateToString(DateUtils.dateFormatToString(periodMap.get(zk), "yyyy-MM-dd HH:mm:ss"));
                        jedis.hset(klineTimeZoneData, zk, JSON.toJSONString(kline));
                    } else {//不为空
                        KlineVo kline = JSON.parseObject(hget, KlineVo.class);
                        if (periodMap.get(zk).getTime() == kline.getCreatedDate()) {//在一个时区
                            kline.setClose(newPrice);
                            //比高
                            if (kline.getHigh().compareTo(newPrice) < 0) {
                                kline.setHigh(newPrice);
                            }
                            //比低
                            if (kline.getLow().compareTo(newPrice) > 0) {
                                kline.setLow(newPrice);
                            }
                            kline.setVolume(kline.getVolume().add(amout));
                            jedis.hset(klineTimeZoneData, zk, JSON.toJSONString(kline));
                        } else {//不在一个时区
                            //当前区更新到老区
                            jedis.hset(klineTimeZoneDataLast, zk, JSON.toJSONString(kline));
                            
                            //创建最新新区覆盖老区数据
                            kline = new KlineVo();
                            kline.setClose(newPrice);
                            kline.setOpen(newPrice);
                            kline.setHigh(newPrice);
                            kline.setLow(newPrice);
                            kline.setVolume(amout);
                            kline.setCreatedDate(periodMap.get(zk).getTime());
                            kline.setDateToString(DateUtils.dateFormatToString(periodMap.get(zk), "yyyy-MM-dd HH:mm:ss"));
                            jedis.hset(klineTimeZoneData, zk, JSON.toJSONString(kline));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                jedisClient.close(jedis);
            }
        }
    }

    /**
     * 24小时数据
     * 
     * @param orders
     */
    public static void data24hour(List<TradeEntrustInfo> orders) {
        // 24小时之前的界线
        if (orders != null && orders.size() > 0) {
            TradeEntrustInfo order = orders.get(orders.size() - 1);
            long splitTime = DateUtils.addDayToDate(new Date(), -1).getTime();
            // 24小时成交订单
        	String symbol = order.getTradeCoinCode() + CHS.underline.getValue() + order.getPricingCoinCode();
            String klineSymbol24order = String.format(RedisKeyConstant.KLINE_SYMBOL_24ORDER, order.getTradeCoinCode(), order.getPricingCoinCode());
            
            // 统计24小时数据
            BigDecimal entrustPrice = order.getEntrustPrice();
            Long entrustTime = order.getEntrustTime().getTime();
            BigDecimal entrustAmout = BigDecimal.ZERO;
            BigDecimal entrustVol = BigDecimal.ZERO;
            for (TradeEntrustInfo info : orders) {
            	entrustAmout = entrustAmout.add(info.getEntrustAmout());
            	entrustVol = entrustVol.add(info.getEntrustAmout().multiply(info.getEntrustPrice()));
            }
            JSONObject mapParam = new JSONObject();
            mapParam.put("entrustPrice", entrustPrice);
            mapParam.put("entrustAmout", entrustAmout);
            mapParam.put("entrustTime", entrustTime);
            mapParam.put("entrustVol", entrustVol);
            jedisClient.lpush(JedisDataSourceSignleton.DB1, klineSymbol24order, mapParam.toJSONString());
            
            //出队总额 //出队总量
            BigDecimal sumCacheVol = BigDecimal.ZERO;
            BigDecimal sumCacheAmout = BigDecimal.ZERO;
            BigDecimal highPrice = BigDecimal.ZERO;
            BigDecimal lowPrice = BigDecimal.valueOf(Integer.MAX_VALUE);
            List<String> listSymbol24OrderData = jedisClient.lrange(JedisDataSourceSignleton.DB1, klineSymbol24order, 0, -1);
            if (listSymbol24OrderData != null && listSymbol24OrderData.size() > 0) {
                int length = listSymbol24OrderData.size() - 1;
                for (int i = length; i > -1; i--) {
                    JSONObject recordVal = JSON.parseObject(listSymbol24OrderData.get(i));
                    long longValue = recordVal.getLongValue("entrustTime");
                    if (longValue < splitTime) {
                    	//如果成交时间不在24小时界线内，从队尾删除数据
                    	jedisClient.rpop(JedisDataSourceSignleton.DB1, klineSymbol24order);
                    }else {
                    	sumCacheVol = sumCacheVol.add(recordVal.getBigDecimal("entrustVol"));
                        sumCacheAmout = sumCacheAmout.add(recordVal.getBigDecimal("entrustAmout"));
                        
                        // 高价
                        BigDecimal recordEntrustPrice = recordVal.getBigDecimal("entrustPrice");
                        if (recordEntrustPrice.compareTo(highPrice) > 0) {
                        	highPrice = recordEntrustPrice;
                        }
                        
                        // 低价
                        if(lowPrice.compareTo(recordEntrustPrice) > 0) {
                        	lowPrice = recordEntrustPrice;
                        }
                    }
                }
            }
            
            // 倒数第二笔最新价
            String klineSymbolOrderKey = String.format(RedisKeyConstant.KLINE_S_ORDER, symbol);
            BigDecimal preNewPrice = BigDecimal.ZERO;
            String entrustInfoJson = jedisClient.lindex(JedisDataSourceSignleton.DB1, klineSymbolOrderKey, 1);
            if(StringUtils.isNotBlank(entrustInfoJson)) {
            	TradeEntrustInfo traEntrustInfoDTO = JSON.parseObject(entrustInfoJson, TradeEntrustInfo.class);
            	preNewPrice = traEntrustInfoDTO.getEntrustPrice();
            }
            
            // 设置24H数据
            KlineSymbol24dataUtil dataUtil = new KlineSymbol24dataUtil(symbol);
            dataUtil.setValue(sumCacheVol.toPlainString(), KlineSymbol24dataUtil.VOL);
            dataUtil.setValue(sumCacheAmout.toPlainString(), KlineSymbol24dataUtil.AMOUT);
            dataUtil.setValue(entrustPrice.toPlainString(), KlineSymbol24dataUtil.NEW_PRICE);
            dataUtil.setValue(preNewPrice.toPlainString(), KlineSymbol24dataUtil.PRE_NEW_PRICE);
            dataUtil.setValue(highPrice.toPlainString(), KlineSymbol24dataUtil.HIGH_PRICE);
            dataUtil.setValue(lowPrice.toPlainString(), KlineSymbol24dataUtil.LOW_PRICE);
            
            CoinUtil coinUtil = new CoinUtil();
            CoinConvertResult convertResult = coinUtil.convetUSDT(sumCacheVol, order.getPricingCoinCode());
            
            // 转换成USDT总额
            BigDecimal usdtVol = convertResult.getTargetCoinAmount();
            dataUtil.setValue(usdtVol.toPlainString(), KlineSymbol24dataUtil.USDT_VOL);
            
            // 转换成CNY总额
            BigDecimal cnyVol = usdtVol.multiply(convertResult.getUsdtToCnyRate());
            dataUtil.setValue(cnyVol.toPlainString(), KlineSymbol24dataUtil.CNY_VOL);
            
            // 开盘价格
            String openPriceKey = String.format(RedisKeyConstant.KLINE_S_OPENPRICE, symbol);
            String openPrice = jedisClient.get(JedisDataSourceSignleton.DB1, openPriceKey);
            
            // 计算24H涨跌幅
            BigDecimal rate = BigDecimal.ZERO;
            BigDecimal bigOpenPrice = new BigDecimal(openPrice);
            if(StringUtils.isNotBlank(openPrice) && bigOpenPrice.compareTo(BigDecimal.ZERO) > 0) {
            	BigDecimal diffPrice = entrustPrice.subtract(bigOpenPrice);
            	BigDecimal finalRate = diffPrice.divide(bigOpenPrice, 4, BigDecimal.ROUND_HALF_UP);
        		rate = finalRate.multiply(new BigDecimal(100)).setScale(2);
            }
            dataUtil.setValue(rate.toPlainString(), KlineSymbol24dataUtil.RATE);     
        }
    }
    
}
