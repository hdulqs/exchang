package com.batsoft.service.module.exchange.trade.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.service.module.exchange.trade.model.TradeEntrust;
import com.batsoft.service.module.exchange.trade.model.TradeEntrustInfo;

import java.util.List;

public class MarketJsonUtil {

    /**
     * 生成交易大厅的JSON串
     *
     * @param buyList  买单
     * @param sellList 卖单
     * @param infoList 成交订单
     */
    public static JSONObject tradeJson(List<TradeEntrust> buyList, List<TradeEntrust> sellList, List<TradeEntrustInfo> infoList) {
        JSONObject rootObject = new JSONObject();
        JSONObject pushEntrusMarket = new JSONObject();

        JSONObject depths = new JSONObject();
        JSONArray asks = new JSONArray();
        if (sellList != null && !sellList.isEmpty()) {
            for (int i = sellList.size() - 1; i >= 0; i--) {
                TradeEntrust traEntrustTO = sellList.get(i);
                asks.add(new double[]{traEntrustTO.getEntrustPrice().doubleValue(), traEntrustTO.getEntrustAmout().doubleValue()});
            }
        }
        depths.put("asks", asks);

        JSONArray bids = new JSONArray();
        if (buyList != null && !buyList.isEmpty()) {
            for (TradeEntrust traEntrustTO : buyList) {
                bids.add(new double[]{traEntrustTO.getEntrustPrice().doubleValue(), traEntrustTO.getEntrustAmout().doubleValue()});
            }
        }
        depths.put("bids", bids);
        pushEntrusMarket.put("depths", depths);
        rootObject.put("pushEntrusMarket", pushEntrusMarket);


        JSONObject pushNewListRecordMarket = new JSONObject();
        JSONArray trades = new JSONArray();
        if (infoList != null && !infoList.isEmpty()) {
            for (int i = infoList.size() - 1; i >= 0; i--) {
                TradeEntrustInfo info = infoList.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("amount", info.getEntrustAmout());
                jsonObject.put("price", info.getEntrustPrice());
                jsonObject.put("time", info.getEntrustTime().getTime() / 1000);
                jsonObject.put("type", "sell");
                trades.add(jsonObject);
            }
        }

        pushNewListRecordMarket.put("trades", trades);
        rootObject.put("pushNewListRecordMarket", pushNewListRecordMarket);

        return rootObject;

    }

//    {
//        "pushEntrusMarket":{
//        "depths":{
//            "asks":[
//                [
//            3010.00,
//                    100
//                ],
//                [
//            3008.00,
//                    0.0480
//                ]
//            ],
//            "bids":[
//                [
//            2999.00,
//                    0.0130
//                ],
//                [
//            2998.00,
//                    0.0370
//                ]
//            ]
//        }
//        },
//        "pushNewListRecordMarket":{
//        "trades":[
//        {
//                "amount":0.0360,
//                "price":5635.00,
//                "tid":"T161221090413615883",
//                "time":1482282254,
//                "type":"sell"
//        },
//        {
//                "amount":0.0350,
//                "price":5627.00,
//                "tid":"T161221090406194001",
//                "time":1482282246,
//                "type":"sell"
//        },
//        {
//                "amount":0.0340,
//                "price":5620.00,
//                "tid":"T161221090359038149",
//                "time":1482282239,
//                "type":"sell"
//        }
//        ]
//        }
//    }



}
