package com.batsoft.model.module.exchange.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页交易量
 *
 */
@Data
@Deprecated
public class IndexTradeAmountVO implements Serializable {

	private static final long serialVersionUID = 2311889252765462878L;

	/**
     * 交易手续费
     *
     */
    private BigDecimal tradeFee;

    /**
     * 交易金额
     *
     */
    private BigDecimal tradeAmount;

    /**
     * 交易数量
     *
     */
    private BigDecimal tradeMun;

    /**
     * 交易币代码
     *
     */
    private String tradeCoinCode;

    /**
     * 定价币代码
     *
     */
    private String pricingCoinCode;



}
