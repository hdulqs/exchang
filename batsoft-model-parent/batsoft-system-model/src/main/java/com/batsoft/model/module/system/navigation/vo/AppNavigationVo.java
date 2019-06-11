/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年2月21日 下午9:01:34
 */
package com.batsoft.model.module.system.navigation.vo;

import com.batsoft.model.module.system.navigation.AppNavigation;
import lombok.Data;

import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年2月21日 下午9:01:34 
 */
@Data
public class AppNavigationVo extends AppNavigation {


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


    
    private List<AppNavigationVo> children;

}
