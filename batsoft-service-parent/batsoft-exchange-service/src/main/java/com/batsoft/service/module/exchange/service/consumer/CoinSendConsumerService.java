package com.batsoft.service.module.exchange.service.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("batsoft-admin")
public interface CoinSendConsumerService {

    /**
     * 提币转账服务
     * @param data
     */
    @PostMapping("/remote/coinSend")
    void coinSend(@RequestBody JSONObject data);

    /**
     * 提币转账服务
     */
    @GetMapping("/remote/coinSend1")
    void coinSend1();
}
