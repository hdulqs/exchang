package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.CoinRecharge;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class CoinRechargeVo extends CoinRecharge {

    /**
     * 状态
     *0:等待中
     */
    public static final String STATUS_VALUE0 = "审核中";
    /**
     * 状态
     *1:成功
     */
    public static final String STATUS_VALUE1 = "充币成功";
    /**
     * 状态
     *2:失败
     */
    public static final String STATUS_VALUE2 = "充币失败";

    /**
     * 充币状态
     */
    private String statusValue;

    /**
     * 币种总量
     */
    private BigDecimal totleMoney;

    public String getStatusValue() {
        if(getStatus() == 0){
            return STATUS_VALUE0;
        }else if(getStatus() == 1){
            return STATUS_VALUE1;
        }else{
            return STATUS_VALUE2;
        }
    }
}
