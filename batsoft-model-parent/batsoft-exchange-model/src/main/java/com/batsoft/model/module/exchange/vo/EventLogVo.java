package com.batsoft.model.module.exchange.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventLogVo implements Serializable {
    int platform;
    String eventId;
    String cookie;
}
