package com.batsoft.third.module.basepay.service;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by lucl on 2017/9/13.
 */
public interface BasePayService<T, PK extends Serializable> {
    /**
     * 充值接口
     * @param dataMap
     * @return
     */
    public String recharge(Map<String,String> dataMap);

    public void doUnifiedOrder(Map<String,String> dataMap);
}
