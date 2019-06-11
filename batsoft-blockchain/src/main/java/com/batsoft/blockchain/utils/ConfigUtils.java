/**
 * Copyright:   北京互融时代软件有限公司
 *
 * @author: SHANGXL
 * @version: V1.0
 * @Date: 2018年5月11日 下午5:57:21
 */
package com.batsoft.blockchain.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

import static com.batsoft.blockchain.common.Constant.CONFIGURL;

/**
 * @Desc                                    //TODO  添加描述
 * @author: shangxl
 * @Date :          2018年5月11日 下午5:57:21
 */
public class ConfigUtils {
    /**
     * 配置文件对象
     */
    private static JSONObject config;

    /**
     * @Desc 读取配置文件
     * @author: SHANGXL
     * @param:            @return
     * @return: JSONObject
     * @Date :  	2018年5月14日 上午10:14:28
     */
    public static JSONObject getConfig() {
        if (config == null) {
            InputStream in = ConfigUtils.class.getResourceAsStream(CONFIGURL);
            try {
                String result = IOUtils.toString(in, "UTF-8");
                config = JSON.parseObject(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    /**
     * @Desc 读取钱包节点的配置信息
     * @author: SHANGXL
     * @param:            @return
     * @return: String
     * @Date :  	2018年5月14日 上午10:15:00
     */
    public static String getConfigValueByKey(String coinType, String param) {
        JSONObject coin = getConfig().getJSONObject(coinType);
        if (!coin.isEmpty() && StringUtils.isNotEmpty(coin.getString(param))) {
            return coin.getString(param);
        }
        return null;
    }

    public static void main(String[] args) {
        System.err.println(getConfigValueByKey("BTC", "ip"));
    }
}
