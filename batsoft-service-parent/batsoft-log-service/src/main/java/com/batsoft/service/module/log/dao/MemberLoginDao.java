/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-04-24 14:27:23 
*/

package com.batsoft.service.module.log.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.log.MemberLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 
* <p>MemberLoginDao</p>
* @author: LouSir
* @Date :  2018-04-24 14:27:23 
*/
public interface MemberLoginDao extends  BaseDao<MemberLogin, String> {

    public List<MemberLogin> find(@Param("userId")String userId);
}
