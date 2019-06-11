/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-08-07 23:16:08 
*/
package com.batsoft.service.module.system.service.navigation;

import com.batsoft.core.service.BaseService;
import com.batsoft.core.service.BaseTreeService;
import com.batsoft.model.module.system.navigation.AppNavigation;
import com.batsoft.model.module.system.navigation.vo.AppNavigationVo;


/**
* <p>AppNavigationService</p>
* @author: Bat Admin
* @Date :  2017-08-07 23:16:08 
*/
public interface AppNavigationService  extends BaseService<AppNavigation, String>,BaseTreeService<AppNavigationVo,String> {

     void updateCache();

     /**
      * 查询全部可显示导航
      * @return
      */
     String  findNav();

}
