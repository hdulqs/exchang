package com.batsoft.client.system.model;

import lombok.Data;

import java.util.Date;

@Data
public class MiningJobResult {
    private String phone;
    private double miningBit;
    private double miningAmount;
    private Date  sendDate;
    private String symbol;
}
