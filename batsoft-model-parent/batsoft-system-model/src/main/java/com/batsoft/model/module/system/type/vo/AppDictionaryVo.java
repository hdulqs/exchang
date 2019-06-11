/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年2月21日 下午9:01:34
 */
package com.batsoft.model.module.system.type.vo;

import com.batsoft.model.module.system.type.AppDictionary;
import lombok.Data;

import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年2月21日 下午9:01:34 
 */
@Data
public class AppDictionaryVo extends AppDictionary {

    private static final long serialVersionUID = 979008948230666647L;
    /**
     * ant design tree use.
     */
    private  String title;
    private  String key;
    /**
     *是否有子类
     */
    private Integer hasChild;

    /**
     * 深度
     */
    private Integer deep;
    /**
     * 是否选中
     */
    private Integer change;

    private List<AppDictionaryVo> children;

}
