package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.CoinWithdraw;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 *
 * <p>CoinWithdrawVo</p>
 * @author: Yuan LouSir
 * @Date :  2017-12-08 09:56:42
 */
@Data
@ToString
public class CoinWithdrawVo extends CoinWithdraw {

    /**
     * 状态
     *0:等待中
     */
    public static final String STATUS_VALUE0 = "审核中";
    /**
     * 状态
     *1:成功
     */
    public static final String STATUS_VALUE1 = "提现成功";
    /**
     * 状态
     *2:失败
     */
    public static final String STATUS_VALUE2 = "提现失败";

    /**
     * 提币状态
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
