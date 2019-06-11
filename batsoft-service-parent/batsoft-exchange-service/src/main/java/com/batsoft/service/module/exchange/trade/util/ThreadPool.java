package com.batsoft.service.module.exchange.trade.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPool {
	
	 private static ExecutorService executors = Executors.newFixedThreadPool(128);
	 
	 public static ExecutorService getExecutor(){
		 return executors;
	 }
	 
	 public static void exe(Runnable run){
		 executors.execute(run);
	 };

}
