package com.batsoft.client.exchange.common.beans.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 交易数据
 *
 */
@Data
public class IndexPageTradeDataVO implements Serializable {

	private static final long serialVersionUID = -3621557752174252675L;

	/**
     * 流通中的总BT
     *
     */
    private String borrowedBt;

    /**
     * 昨日产出BT
     *
     */
    private String yesterdayOutputBt;

    /**
     * 昨日全站总交易量 USDT
     *
     */
    private String yesterdayTotalTradeAmount;

    /**
     * 昨日交易手续费折合 USDT：
     *
     */
    private String yesterdayTotalTradeFee;

    /**
     * 今日全站总交易量 USDT：
     *
     */
    private String todayTotalTradeAmount;

    /**
     * 今日交易手续费折合：USDT
     *
     */
    private String todayTotalTradeFee;


}
