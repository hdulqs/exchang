/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-12 20:18:59 
*/
package com.batsoft.service.module.system.service.manage.impl;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.system.manage.AppUser;
import com.batsoft.service.module.system.dao.manage.AppUserDao;
import com.batsoft.service.module.system.service.manage.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
* <p> AppUserServiceImpl </p>
* @author: 
* @Date :  2017-05-12 20:18:59 
*/
@Service("appUserService")
@Order(9)
public class AppUserServiceImpl extends BaseServiceImpl<AppUser, String> implements AppUserService {

@Autowired
private AppUserDao appUserDao;


    @Override
    public AppUser findAppUser(String userName) {
        return appUserDao.findAppUser(userName);
    }

    @Override
    public AppUser findAppUserById(String id) {
        return appUserDao.findAppUserById(id);
    }

    @Override
    public Set<Map<String, String>> findUserRoles(String userId) {
        return appUserDao.findUserRoles(userId);
    }
}
