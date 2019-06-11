package com.batsoft.nettysocket.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
@FeignClient("batsoft")
//@Service("socketDataService")
public interface SocketDataService {
    @GetMapping("/init_book")
    String initBook(String coinPair);
    @GetMapping("/test_book")
    String testBook();
}


