/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-08-07 23:16:08 
*/
package com.batsoft.service.module.system.dao.navigation;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.core.dao.BaseTreeDao;
import com.batsoft.model.module.system.navigation.AppNavigation;
import com.batsoft.model.module.system.navigation.vo.AppNavigationVo;

/**
* 前端导航管理
* <p>AppNavigationDao</p>
* @author: Bat Admin
* @Date :  2017-08-07 23:16:08 
*/
public interface AppNavigationDao extends  BaseDao<AppNavigation, String>,BaseTreeDao<AppNavigationVo,String> {

}
