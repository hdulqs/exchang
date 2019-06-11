package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.EntrustHistory;

/**
 * @aouthor LouSir
 * @date 2018/5/24 17:27
 */
public class EntrustHistoryVo extends EntrustHistory {

    /**
     * 交易类型
     */
    private String typeStr;

    /**
     * 委托状态显示
     */
    private String entrustStateStr;


    public String getTypeStr() {
        String typeValue = "";
        switch (super.getEntrustType()){
            case "buy": typeValue = "买入";break;
            case "sell": typeValue = "卖出";break;
            default : typeValue = "买入";break;
        }
        return typeValue;
    }
    public String getEntrustStateStr() {
        String stateValue = "";
        switch (super.getEntrustState()){
            case 0: stateValue = "未成交";break;
            case 1: stateValue = "部分成交";break;
            case 2: stateValue = "全部成交";break;
            case 3: stateValue = "撤销";break;
            default : stateValue = "";break;
        }
        return stateValue;
    }
}
