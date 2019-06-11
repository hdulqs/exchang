/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-13 16:56:07 
*/
package com.batsoft.service.module.system.dao.manage;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.system.manage.AppRoleMenu;

import java.util.List;

/**
* <p>AppRoleMenuDao</p>
* @author: Bat Admin
* @Date :  2017-05-13 16:56:07 
*/
public interface AppRoleMenuDao extends  BaseDao<AppRoleMenu, String> {
    /**
     * 是否存在该权限
     * @param appRoleMenu
     * @return
     */
    Integer hasRoleMenu(AppRoleMenu appRoleMenu);

    /**删除角色权限
     * @param roleId
     * @return
     */
    Integer removeRoleMenu(String roleId);

    List<AppRoleMenu> findRoleMenus(String roleId);
}
