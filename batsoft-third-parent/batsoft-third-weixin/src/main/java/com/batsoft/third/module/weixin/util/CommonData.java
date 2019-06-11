package com.batsoft.third.module.weixin.util;

import org.nutz.dao.entity.annotation.Comment;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

/**
 * Created by lucl on 2017/9/14.
 */
public class CommonData {
    @Value("${pay.weixin.appid}")
    public String appid;
    @Value("${pay.weixin.mch_id}")
    public String mch_id;
    @Value("${pay.weixin.url}")
    public String url;
    @Value("${pay.weixin.device}")
    public String device;
    @Value("${pay.weixin.spbill_ip}")
    public String spbill_ip;
    @Value("${pay.weixin.notify_url}")
    public String notify_url;


    public final static String SUCCESS = "8888";
    public final static String FAILED = "0000";

    public final static String MD5 = "MD5";
    public static final String HMACSHA256 = "HMAC-SHA256";

    public static final String DOMAIN_API = "www.baidu.com";
    public static final String DOMAIN_API2 = "www.baidu.com";

    public static  String static_appid="";
    public static  String static_mch_id="";
    public static  String static_url="";
    public static  String static_device="";
    public static  String static_spbill_ip="";
    public static  String static_notify_url="";

    /**
     * 取值：三位随机数+通过System.nanoTime()返回最准确的可用系统计时器的当前值，以毫微秒为单位（15位）。
     * 第三方及系统交易流水号共18位
     * @return 第三方交易需要的流水号（系统账户需要的流水号）
     * add by lucl
     */
    public static String createRuestNumber(){
        Random r = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 3; i++)
        {
            result.append(Integer.toString(r.nextInt(10)));
        }
        return result.toString()+System.nanoTime();
    }
}
