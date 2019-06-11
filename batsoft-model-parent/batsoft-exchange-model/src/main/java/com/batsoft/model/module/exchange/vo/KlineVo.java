package com.batsoft.model.module.exchange.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class KlineVo implements Serializable {

	private static final long serialVersionUID = 1371762272321460629L;

    /**
     * 收盘价格
     */
    private BigDecimal close;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 开盘价
     */
    private BigDecimal open;
    /**
     * 交易量
     */
    private BigDecimal volume;

    /**
     * 时间戳
     */
    private Long createdDate;

    private String dateToString;

    private int type;

    private int marketFrom;
}
