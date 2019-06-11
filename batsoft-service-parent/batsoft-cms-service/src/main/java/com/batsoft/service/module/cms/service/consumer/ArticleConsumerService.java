package com.batsoft.service.module.cms.service.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("batsoft-front")
public interface ArticleConsumerService {
    @GetMapping("/dc")
    String consumer();

    @PostMapping("/post")
    void post(@RequestBody JSONObject user);
}
