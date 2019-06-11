package com.batsoft.admin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="batsoftCms-service", fallback = ComputeClientImpl.class )
public interface ComputeClient {

	
    @RequestMapping(method = RequestMethod.GET, value = "/article/add")
    Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);

}