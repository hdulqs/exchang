package com.batsoft.nettysocket.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class RequestData {
    private String event;
    private String channel;
    private JSONObject data;

}
