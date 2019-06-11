package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.EntrustIng;
import lombok.ToString;

/**
 * @aouthor LouSir
 * @date 2018/5/24 17:22
 */
@ToString
public class EntrustIngVo extends EntrustIng {
    /**
     * 交易类型
     */
    private String typeStr;

    public String getTypeStr() {
        String typeValue = "";
        switch (super.getEntrustType()){
            case "buy": typeValue = "买入";break;
            case "sell": typeValue = "卖出";break;
            default : typeValue = "买入";break;
        }
        return typeValue;
    }
}
