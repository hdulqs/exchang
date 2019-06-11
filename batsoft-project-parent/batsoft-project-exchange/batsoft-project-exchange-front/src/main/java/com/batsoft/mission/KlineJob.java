/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月11日 上午8:23:34
 */
package com.batsoft.mission;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.batsoft.core.common.constant.KlineTimeNodeConstant;
import com.batsoft.service.KlineJobBusService;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2016年12月11日 上午8:23:34 
 */
@Component
public class KlineJob {
	
	@Resource
	private KlineJobBusService klineJobBusService;

    /**
     * 1m
     * 
     */
    @Scheduled(cron="0 0/1 * * * ?")
    public void klineJob1() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.MINUTE_1, date);
    }

    /**
     * 5m
     * 
     */
    @Scheduled(cron="0 0/5 * * * ?")
    public void klineJob2() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.MINUTE_5, date);
    }

    /**
     * 15m
     * 
     */
    @Scheduled(cron="0 0/15 * * * ?")
    public void klineJob3() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.MINUTE_15, date);
    }

    /**
     * 30m
     * 
     */
    @Scheduled(cron="0 0/30 * * * ?")
    public void klineJob4() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.MINUTE_30, date);
    }

    /**
     * 1h
     * 
     */
    @Scheduled(cron="0 0 0/1 * * ?")
    public void klineJob5() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.HOUR_1, date);
    }
    
    /**
     * 2h
     * 
     */
    @Scheduled(cron="0 0 0/2 * * ?")
    public void klineJob6() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.HOUR_2, date);
    }
    
    /**
     * 4h
     * 
     */
    @Scheduled(cron="0 0 0/4 * * ?")
    public void klineJob7() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.HOUR_4, date);
    }
    
    /**
     * 6h
     * 
     */
    @Scheduled(cron="0 0 0/6 * * ?")
    public void klineJob8() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.HOUR_6, date);
    }
    
    /**
     * 12h
     * 
     */
    @Scheduled(cron="0 0 0/12 * * ?")
    public void klineJob9() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.HOUR_12, date);
    }

    /**
     * 1d
     * 
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void klineJob10() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.DAY_1, date);
    }
    
    /**
     * 1week
     * 
     */
    @Scheduled(cron="0 0 0 ? * MON")
    public void klineJob11() {
        Date date = new Date();
        klineJobBusService.updateKlineNode(KlineTimeNodeConstant.WEEK_1, date);
    }
    
}
