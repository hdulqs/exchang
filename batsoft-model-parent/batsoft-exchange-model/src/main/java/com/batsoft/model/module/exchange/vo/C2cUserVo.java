package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.C2cUser;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @aouthor LouSir
 * @date 2018/5/14 1:06
 */
@ToString
@Data
public class C2cUserVo extends C2cUser {

    /**
     * 临时生成的验证码
     */
    private String remark;
    /**
     * 转账金额
     */
    private BigDecimal opeationMoney;
}
