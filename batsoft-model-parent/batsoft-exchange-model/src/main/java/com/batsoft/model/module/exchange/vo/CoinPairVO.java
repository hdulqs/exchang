package com.batsoft.model.module.exchange.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CoinPairVO {



    private String id;

    /**
     * 交易币代码
     */
    private String tradeCoinCode;

    /**
     * 定价币代码
     */
    private String pricingCoinCode;

    /**
     * 交易币logo
     */
    private String tradeCoinLogo;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否推荐
     */
    private Integer recommend;

    /**
     * 挂单数量最小位数
     */
    private Integer amtDecimal;

    /**
     * 定价币单价最小位数
     */
    private Integer priceDecimal;

    /**
     * 交易总额最小位数
     */
    private Integer amountDecimal;

    /**
     * 发行价
     */
    private BigDecimal openPrice;

    /**
     * 24小时涨跌幅
     */
    private String rate;

    /**
     * 交易量
     */
    private String tradingVolume;


}
