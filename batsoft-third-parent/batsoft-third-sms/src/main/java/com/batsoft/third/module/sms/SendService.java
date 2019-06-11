package com.batsoft.third.module.sms;

import com.batsoft.core.common.JsonResult;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/12.
 */
public interface SendService {
    /**
     * 发送短信
     * @param  areaCode 地区号
     * @param mobile
     * @param data
     * @param tplCode
     * @return
     */
    JsonResult send(String areaCode,String mobile, Map<String, Object> data, String tplCode);
}