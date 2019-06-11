/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-12 20:18:59 
*/
package com.batsoft.service.module.system.dao.manage;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.system.manage.AppUser;

import java.util.Map;
import java.util.Set;

/**
* <p>AppUserDao</p>
* @author: Bat Admin
* @Date :  2017-05-12 20:18:59 
*/
public interface AppUserDao extends  BaseDao<AppUser, String> {
    /**
     * 查詢用户通过 userName
     * @param userName
     * @return
     */
    AppUser findAppUser(String userName);

    AppUser findAppUserById(String id);

    Set<Map<String, String>> findUserRoles(String userId);
}
