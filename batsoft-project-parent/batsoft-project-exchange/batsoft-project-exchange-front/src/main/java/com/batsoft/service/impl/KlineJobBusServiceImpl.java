package com.batsoft.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.batsoft.common.config.WebSocketSubscibeConfig;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.constant.KlineTimeNodeConstant;
import com.batsoft.model.module.exchange.dto.KlineDTO;
import com.batsoft.model.module.exchange.vo.KlineVo;
import com.batsoft.service.KlineJobBusService;
import com.batsoft.service.module.exchange.trade.service.TradeKlineService;
import com.batsoft.socketjs.Response;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.gson.GsonSingleton;

@Service(value = "klineJobBusService")
public class KlineJobBusServiceImpl implements KlineJobBusService {
	
	private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
	
	private GsonSingleton gsonClient = GsonSingleton.getInstance();
	
	@Resource
	private TradeKlineService tradeKlineService;
	
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public void updateKlineNode(String timeType, Date currentTime) {
        //date有可能是存在是59秒的问题，可能是spring Scheduled的bug也可能是在方面里面new date的缘故现在把两个情况都进行处理
        Date thisDate = null;
        if(DateUtils.dateFormatToString(currentTime, "ss").equals("59")){
            thisDate = DateUtils.dateFormat(currentTime,"yyyy-MM-dd HH:mm:00");
        }else if(DateUtils.dateFormatToString(currentTime, "ss").equals("00")){
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentTime);
            cal.add(Calendar.MINUTE, -1);
            thisDate = DateUtils.dateFormat(cal.getTime(),"yyyy-MM-dd HH:mm:00");
        }else{
            thisDate = DateUtils.dateFormat(currentTime, "yyyy-MM-dd HH:mm:00");
        }
        
        Map<String, Date> periodMap = DateUtils.getPeriodMap(thisDate);
        Map<String, String> coinpair = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
        if (coinpair != null && !coinpair.isEmpty()) {
            Set<String> set = coinpair.keySet();
            Iterator<String> coinPairIterator = set.iterator();
            while (coinPairIterator.hasNext()) {
                String coinPair = coinPairIterator.next();
                KlineDTO klineDto = new KlineDTO();
                
                // 1.最后节点中有记录
                long zktime = periodMap.get(timeType).getTime();
                String klineTimeZoneLastKey = String.format(RedisKeyConstant.KLINE_TIME_ZONE_DATA_LAST, coinPair);
                String lastData = jedisClient.hget(JedisDataSourceSignleton.DB1, klineTimeZoneLastKey, timeType);
                if (!StringUtils.isEmpty(lastData)) { // 最后一个节点数据有数据
                	KlineVo klineVo = JSON.parseObject(lastData, KlineVo.class);
                	if(zktime == klineVo.getCreatedDate()) {	// 匹配节点时间
                		klineDto.setTime(thisDate.getTime());
                		klineDto.setOpen(klineVo.getOpen());
                		klineDto.setClose(klineVo.getClose());
                        klineDto.setHigh(klineVo.getHigh());
                        klineDto.setLow(klineVo.getLow());
                        klineDto.setVolume(klineVo.getVolume());
                     }
                }
                
                // 2.上一个节点没有数据？，从实时数据中获取
                if(klineDto == null || klineDto.getTime() == null){
                	String klineTimeZoneKey = String.format(RedisKeyConstant.KLINE_TIME_ZONE_DATA, coinPair);
                	String thisData = jedisClient.hget(JedisDataSourceSignleton.DB1, klineTimeZoneKey, timeType);
                    if (!StringUtils.isEmpty(thisData)) {// 从实时节点拿数据
                        KlineVo klineVo = JSON.parseObject(thisData, KlineVo.class);
                        if(zktime == klineVo.getCreatedDate()) {
                            klineDto.setTime(thisDate.getTime());
                    		klineDto.setOpen(klineVo.getOpen());
                    		klineDto.setClose(klineVo.getClose());
                            klineDto.setHigh(klineVo.getHigh());
                            klineDto.setLow(klineVo.getLow());
                            klineDto.setVolume(klineVo.getVolume());
                        }
                    }
                }
                
                // 3.匹配不到数据，生成占位数据
                if(klineDto == null || klineDto.getTime() == null) {
                	klineDto.setTime(thisDate.getTime());
            		klineDto.setOpen(BigDecimal.ZERO);
            		klineDto.setClose(BigDecimal.ZERO);
                    klineDto.setHigh(BigDecimal.ZERO);
                    klineDto.setLow(BigDecimal.ZERO);
                    klineDto.setVolume(BigDecimal.ZERO);
                }
                
                // 填充数据到缓存中
            	String klineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, coinPair, timeType);
                String lastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, 0);
                KlineDTO lastKlineDto = gsonClient.fromJson(lastValJson, KlineDTO.class);
                if(lastKlineDto == null || klineDto.getTime() > lastKlineDto.getTime()) {
                	Long length = jedisClient.llen(JedisDataSourceSignleton.DB0, klineKey);
                	if(length != null && length > 259200) { // K线图数量巨大，从尾部删除一个数据
                		jedisClient.rpop(JedisDataSourceSignleton.DB0, klineKey);
                	}
                    // 将数据保存到Redis
                    jedisClient.lpush(JedisDataSourceSignleton.DB0, klineKey, gsonClient.toJson(klineDto));
                }
            }
        }
	}

	@Override
	public void pushKlineNode(String timeType) {
		//时间节点
        String[] timeNode = KlineTimeNodeConstant.convertToArray();
		Map<String, String> coinpair = jedisClient.hgetall(JedisDataSourceSignleton.DB1, RedisKeyConstant.TRA_COINPAIR);
        if (coinpair != null && !coinpair.isEmpty()) {
            Set<String> set = coinpair.keySet();
            Iterator<String> coinPairIterator = set.iterator();
            while (coinPairIterator.hasNext()) {
            	String coinPair = coinPairIterator.next();
            	for(String time : timeNode){
            		String ws_to = WebSocketSubscibeConfig.klineWs(coinPair, time);
            		
                	String klineKey = String.format(RedisKeyConstant.EXCHANGE_KLINE_DATA, coinPair, timeType);
                    String lastValJson = jedisClient.lindex(JedisDataSourceSignleton.DB0, klineKey, 0);
                    
                    KlineDTO dto = gsonClient.fromJson(lastValJson, KlineDTO.class);
                    simpMessagingTemplate.convertAndSend(ws_to, new Response(JSON.toJSONString(dto)));
                }
            }
        }
	}
	

}
