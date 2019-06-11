/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-12 20:18:59
*/
package com.batsoft.service.module.system.service.manage;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.system.manage.AppUser;

import java.util.Map;
import java.util.Set;


/**
* <p>AppUserService</p>
* @author: Bat Admin
* @Date :  2017-05-12 20:18:59 
*/
public interface AppUserService extends BaseService<AppUser, String>{
    /**
     * 查詢用户通过 userName
     * @param userName
     * @return
     */
    AppUser findAppUser(String userName);
    /**
     * 查詢用户通过 id
     * @param id
     * @return
     */
    AppUser findAppUserById(String id);

    /**
     * 查询用户角色
     * <p> TODO</p>
     * @author:         Bat Admin
     * @param:    @return
     * @return: String
     * @Date :          2016年6月17日 下午3:25:58
     * @throws:
     */
    Set<Map<String, String>> findUserRoles(String userId);
}
