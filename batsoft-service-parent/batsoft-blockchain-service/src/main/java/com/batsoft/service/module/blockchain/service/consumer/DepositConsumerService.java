package com.batsoft.service.module.blockchain.service.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("batsoft-admin")
public interface DepositConsumerService {

    /**
     * 记录流水服务
     * @param data
     */
    @PostMapping("/remote/depositAccountRecord")
    void depositAccountRecord(@RequestBody JSONObject data);

    /**
     * 记录订单服务
     * @param data
     */
    @PostMapping("/remote/depositOrder")
    void depositOrder(@RequestBody JSONObject data);
}
