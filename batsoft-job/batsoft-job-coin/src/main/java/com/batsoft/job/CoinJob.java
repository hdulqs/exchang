package com.batsoft.job;

import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.system.service.config.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.batsoft.service.module.blockchain.service.CoinOrderService;

@Component
public class CoinJob {

    @Autowired
    private CoinOrderService coinOrderService;

    @Autowired
    private AppConfigService configService;
    /**
     * 查询区块链充值交易 1分钟执行
     */
    @Scheduled(cron="*/60 * * * * *")
    public void findTraction() {
        String ethAddress = configService.findValueByKey(AppConfig.ETH_WALLET_WHITE_ADDRESS);
        coinOrderService.findBlockTraction(ethAddress);
    }


    /**
     * 查询区块链充值交易 3分钟执行
     */
     @Scheduled(cron="*/180 * * * * *")
    public void updataTraction() {
        coinOrderService.findConfirm();
    }
     
}
