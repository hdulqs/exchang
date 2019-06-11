package com.batsoft.third.module.weixin.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by lucl on 2017/9/15.
 */
@Component
@ConfigurationProperties(prefix = "pay.weixin")
@Data
public class WeixinConfig {
    private String appid;
    private String mch_id;
    private String url;
    private String spbill_ip;
    private String notify_url;
}
