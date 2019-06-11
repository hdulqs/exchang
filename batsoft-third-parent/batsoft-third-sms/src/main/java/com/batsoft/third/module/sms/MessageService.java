package com.batsoft.third.module.sms;

import com.batsoft.core.common.JsonResult;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/8.
 */
public interface MessageService {
    /**
     * 发送短信验证码
     * @param  areaCode 地区号 86
     * @param mobile 电话
     * @param host IP
     * @param data 数据
     * @return
     */
    JsonResult sendCode(String areaCode,String mobile,String host, Map<String ,Object> data) ;
}
