package com.batsoft.service.module.exchange.trade.util;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

@Deprecated
public class MessageUtil {

    /**
     * 生成可用账户操作json信息
     * @param userId
     * @param coinCode
     * @param money
     * @return
     */
    public static String addCoinAvailable(String userId,String coinCode,BigDecimal money){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("coinCode", coinCode);
        jsonObject.put("type", "available");
        jsonObject.put("money", money);
        return jsonObject.toJSONString();
    }

    /**
     * 生成冻结账户操作json信息
     * @param userId
     * @param coinCode
     * @param money
     * @return
     */
    public static String addCoinFreeze(String userId, String coinCode, BigDecimal money){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("coinCode", coinCode);
        jsonObject.put("type", "freeze");
        jsonObject.put("money", money);
        return jsonObject.toJSONString();
    }

}
