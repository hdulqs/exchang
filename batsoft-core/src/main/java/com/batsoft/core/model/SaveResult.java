package com.batsoft.core.model;

import lombok.Data;

/**
 * 用于返回save 方法后的结果
 * Created by Administrator on 2017/6/14.
 */
@Data
public class SaveResult {
    //执行结果返回sql执行影响行数
    Integer excuse;
    //返回保存后的id值
    String id;
}
