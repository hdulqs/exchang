package com.batsoft.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */
@Data
public class KeyValue implements Serializable {
   public String key="";
    public String value="";
}
