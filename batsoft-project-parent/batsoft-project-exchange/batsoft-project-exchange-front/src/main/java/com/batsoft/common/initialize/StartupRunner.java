package com.batsoft.common.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统启动初始话数据
 * 
 */
@Slf4j
@Component
@Order(1)
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private EntrustIngService entrustIngService;
    
    @Autowired
    private CoinService coinService;
    
    @Autowired
    private CoinPairService coinPairService;
    
    @Override
    public void run(String... args) {
        log.info("front初始化coin交易参数rediskey=tra:coin");
        coinService.saveRedisCache();
        
        log.info("front初始化mysql中的交易对的交易位数");
        coinPairService.findCoinPairDigitInit();
        
        log.info("front初始化mysql中的委托单到数据库中");
        entrustIngService.ininRedis();
    }
    
}
