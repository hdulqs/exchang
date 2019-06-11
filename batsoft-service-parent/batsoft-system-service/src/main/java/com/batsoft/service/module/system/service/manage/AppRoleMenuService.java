/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-13 16:56:07 
*/
package com.batsoft.service.module.system.service.manage;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.system.manage.AppRoleMenu;

import java.util.List;


/**
* <p>AppRoleMenuService</p>
* @author: Bat Admin
* @Date :  2017-05-13 16:56:07 
*/
public interface AppRoleMenuService  extends BaseService<AppRoleMenu, String>{

    /**
     * 是否存在该权限
     * @param appRoleMenu
     * @return
     */
    boolean hasRoleMenu(AppRoleMenu appRoleMenu);

    /**删除角色权限
     * @param roleId
     * @return
     */
    boolean removeRoleMenu(String roleId);

    /**
     * 查询角色权限
     * @param id
     * @return
     */
    List<AppRoleMenu> findRoleMenus(String roleId);
}
