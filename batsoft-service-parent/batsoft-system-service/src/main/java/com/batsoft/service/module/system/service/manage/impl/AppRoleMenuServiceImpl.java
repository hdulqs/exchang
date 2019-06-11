/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-13 16:56:07 
*/
package com.batsoft.service.module.system.service.manage.impl;

import com.batsoft.model.module.system.manage.AppRoleMenu;
import com.batsoft.service.module.system.dao.manage.AppRoleMenuDao;
import com.batsoft.service.module.system.service.manage.AppRoleMenuService;
import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* <p> AppRoleMenuServiceImpl </p>
* @author: 
* @Date :  2017-05-13 16:56:07 
*/
@Service("appRoleMenuService")
public class AppRoleMenuServiceImpl extends BaseServiceImpl<AppRoleMenu, String> implements AppRoleMenuService{

@Autowired
private AppRoleMenuDao appRoleMenuDao;


    @Override
    public boolean hasRoleMenu(AppRoleMenu appRoleMenu) {
        Integer ret=appRoleMenuDao.hasRoleMenu(appRoleMenu);
        if(ret>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeRoleMenu(String roleId) {
        Integer ret=appRoleMenuDao.removeRoleMenu(roleId);
        if(ret>0){
            return true;
        }
        return false;
    }

    @Override
    public List<AppRoleMenu> findRoleMenus(String roleId) {
        return appRoleMenuDao.findRoleMenus(roleId);
    }
}
