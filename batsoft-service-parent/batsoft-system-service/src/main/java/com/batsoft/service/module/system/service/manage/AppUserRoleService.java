/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-13 16:56:34 
*/
package com.batsoft.service.module.system.service.manage;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.system.manage.AppUserRole;

import java.util.List;


/**
* <p>AppUserRoleService</p>
* @author: Bat Admin
* @Date :  2017-05-13 16:56:34 
*/
public interface AppUserRoleService  extends BaseService<AppUserRole, String>{


    /**
     * 查询用户角色
     * @param userId
     * @return
     */
    List<AppUserRole> findRoleByUserId(String userId);
}
