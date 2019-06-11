package com.batsoft.model.module.exchange.vo;

/**
 * 设置交易对coin ids 和 定价币 codes
 * Created by Administrator on 2017/6/14.
 */
public class TradePkData {
    String ids;
    String[] idsArr;
    String prices;
    String[] pricesArr;
    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
        setPricesArr(prices);
    }

    public String[] getPricesArr() {
        return pricesArr;
    }

    public void setPricesArr(String prices) {
        this.pricesArr = prices.split(",");
    }


    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
        setIdsArr(ids);
    }
    public String[] getIdsArr() {
        return idsArr;
    }

    public void setIdsArr(String ids) {
        this.idsArr = ids.split(",");
    }
}
