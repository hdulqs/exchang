package com.batsoft.model.module.system.config.vo;

import com.batsoft.model.KeyValue;
import lombok.Data;

import java.util.List;

/**
 * 用于批量操作对象传递 主键数组
 * Created by Administrator on 2017/6/14.
 */
@Data
public class UpdateConfigData {
    List<KeyValue> data;

}
