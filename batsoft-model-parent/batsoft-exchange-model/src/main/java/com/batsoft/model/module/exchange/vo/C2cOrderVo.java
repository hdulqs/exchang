package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.C2cOrder;
import lombok.Data;
import lombok.ToString;

/**
 * @aouthor LouSir
 * @date 2018/5/14 1:35
 */
@ToString
@Data
public class C2cOrderVo extends C2cOrder {

    private String userNameStr;

    private String opeationStateStr;

    private String operationTypeStr;
}
