package com.batsoft.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/9/27.
 */
@Slf4j
@Data
@Component("aliOSSConfig")
@ConfigurationProperties(prefix = "oss")
public class AliOSSConfig {
    /**
     * 配置文件中进行配置
     */
    private  String       END_POINT         = "oss-cn-shanghai.aliyuncs.com";
    private  String       ACCESS_KEY_ID     = "xxx";
    private  String       ACCESS_KEY_SECRET = "xxx";
    private  String       BUCKET_NAME       = "xxx-image";
}
