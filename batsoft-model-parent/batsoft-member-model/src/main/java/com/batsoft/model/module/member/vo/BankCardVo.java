package com.batsoft.model.module.member.vo;

import com.batsoft.model.module.member.Bankcard;
import lombok.Data;
import lombok.ToString;

/**
 * @aouthor LouSir
 * @date 2018/5/21 16:29
 */
@ToString
@Data
public class BankCardVo extends Bankcard {

    /**
     * 银行卡脱敏
     */
    private String bankCardStr;
}
