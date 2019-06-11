package com.batsoft.app.system.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2017/5/10.
 */
@Configuration
public class GlobalInfo {
    /**
     * 返回用户全局数据到页面
     * @return
     */
    @Bean(name="globalService")
    public  GlobalService globalService(){
        return  new GlobalServiceImpl();
    }
}
