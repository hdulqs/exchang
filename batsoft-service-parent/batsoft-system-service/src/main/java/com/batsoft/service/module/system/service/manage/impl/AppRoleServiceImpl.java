/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-05-12 20:19:47 
*/
package com.batsoft.service.module.system.service.manage.impl;

import com.batsoft.model.module.system.manage.AppRole;
import com.batsoft.service.module.system.dao.manage.AppRoleDao;
import com.batsoft.service.module.system.service.manage.AppRoleService;
import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> AppRoleServiceImpl </p>
* @author: 
* @Date :  2017-05-12 20:19:47 
*/
@Service("appRoleService")
public class AppRoleServiceImpl extends BaseServiceImpl<AppRole, String> implements AppRoleService{

@Autowired
private AppRoleDao appRoleDao;


}
