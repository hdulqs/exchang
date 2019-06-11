package com.batsoft.model.module.exchange.vo;

import com.batsoft.model.module.exchange.CoinPair;
import lombok.Data;
import lombok.ToString;

/**
 * @aouthor LouSir
 * @description  交易对表位数
 * @date 2018/5/4 21:05
 */
@ToString
@Data
public class CoinPairDigits {

    /**
     * 挂单数量最小位数
     */
    private Integer amt_decimal=2;
    /**
     * 交易币单价最小位数
     */
    private Integer price_decimal=2;
    /**
     * 交易总额最小位数
     */
    private Integer amount_decimal=2;

    public CoinPairDigits(CoinPair coinPair){
        if(coinPair.getAmountDecimal()!=null){
            this.amount_decimal = coinPair.getAmountDecimal();
        }
        if(coinPair.getAmountDecimal()!=null){
            this.amt_decimal = coinPair.getAmtDecimal();
        }
        if(coinPair.getPriceDecimal()!=null){
            this.price_decimal = coinPair.getPriceDecimal();
        }
    }

}
