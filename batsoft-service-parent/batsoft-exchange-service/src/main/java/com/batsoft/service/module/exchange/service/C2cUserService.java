/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:21:46 
*/

package com.batsoft.service.module.exchange.service;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.exchange.C2cUser;

import java.util.List;

/**
* <p>C2cUserService</p>
* @author: LouSir
* @Date :  2018-05-07 09:21:46 
*/
public interface C2cUserService  extends BaseService<C2cUser, String>{

    void updateC2cJson();
    List<C2cUser> findActiveUsersByRedis();
}
