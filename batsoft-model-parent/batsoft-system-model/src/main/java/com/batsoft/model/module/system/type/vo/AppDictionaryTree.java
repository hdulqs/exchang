/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年2月21日 下午9:01:34
 */
package com.batsoft.model.module.system.type.vo;

import lombok.Data;

import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年2月21日 下午9:01:34 
 */
@Data
public class AppDictionaryTree {
    private String id;
    private String name;
    private String dicKey;
    private String path;
    private String icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     *是否有子类
     */
    private Integer hasChild;

    /**
     * 是否选中
     */
    private Integer change;

    private List<AppDictionaryTree> children;

}
