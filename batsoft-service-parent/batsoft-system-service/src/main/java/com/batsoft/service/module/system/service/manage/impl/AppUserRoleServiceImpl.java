/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-13 16:56:34 
*/
package com.batsoft.service.module.system.service.manage.impl;

import com.batsoft.model.module.system.manage.AppUserRole;
import com.batsoft.service.module.system.dao.manage.AppUserRoleDao;
import com.batsoft.service.module.system.service.manage.AppUserRoleService;
import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* <p> AppUserRoleServiceImpl </p>
* @author: 
* @Date :  2017-05-13 16:56:34 
*/
@Service("appUserRoleService")
public class AppUserRoleServiceImpl extends BaseServiceImpl<AppUserRole, String> implements AppUserRoleService{

@Autowired
private AppUserRoleDao appUserRoleDao;


    @Override
    public List<AppUserRole> findRoleByUserId(String userId) {
        return appUserRoleDao.findRoleByUserId(userId);
    }
}
