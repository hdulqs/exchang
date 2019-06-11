/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-13 16:56:34 
*/
package com.batsoft.service.module.system.dao.manage;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.system.manage.AppUserRole;

import java.util.List;

/**
* <p>AppUserRoleDao</p>
* @author: Bat Admin
* @Date :  2017-05-13 16:56:34 
*/
public interface AppUserRoleDao extends  BaseDao<AppUserRole, String> {

    List<AppUserRole> findRoleByUserId(String userId);
}
