/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-04-24 14:27:23 
*/

package com.batsoft.service.module.log.service;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.log.MemberLogin;

import java.util.List;

/**
* <p>MemberLoginService</p>
* @author: LouSir
* @Date :  2018-04-24 14:27:23 
*/
public interface MemberLoginService  extends BaseService<MemberLogin, String>{
    List<MemberLogin> find(String userId);

}
