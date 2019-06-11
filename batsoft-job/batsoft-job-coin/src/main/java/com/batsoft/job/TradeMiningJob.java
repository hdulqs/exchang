package com.batsoft.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batsoft.service.TradingDigService;

/**
 * 交易挖矿
 * 
 */
@Component
public class TradeMiningJob {
	
	 @Autowired
 	 private TradingDigService tradingDigService;
	
 	 //@Scheduled(cron="15 0 0 * * *")
     public void run() {
 		System.out.println("----------------开启执行交易挖矿----------------");
 		tradingDigService.excute();
 		System.out.println("----------------交易挖矿执行完毕----------------");
     }
 	 
 	 
}
