/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月11日 上午8:23:34
 */
package com.batsoft.job;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.batsoft.service.KlineJobService;

/**
 * 定时更新交易大厅数据
 * 
 * @author simon
 */
@Component
public class KlineJob {

    @Resource
    private KlineJobService klineJobService;
    
    /**
     *	更新【最新价、24H最高价、24H最低价】
     * 
     */
    @Scheduled(cron="0 0 8 * * ?")
    public void updateOpenAndMaxAndMinPrice() {
    	klineJobService.updateOpenAndMaxAndMinPrice();
    }


}
