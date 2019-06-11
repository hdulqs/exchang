/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-04-24 14:27:23 
*/
package com.batsoft.service.module.log.service.impl;

import com.batsoft.model.module.log.MemberLogin;
import com.batsoft.service.module.log.dao.MemberLoginDao;
import com.batsoft.service.module.log.service.MemberLoginService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* <p> MemberLoginServiceImpl </p>
* @author: LouSir
* @Date :  2018-04-24 14:27:23 
*/
@Service("memberLoginService")
public class MemberLoginServiceImpl extends BaseServiceImpl<MemberLogin, String> implements MemberLoginService{

@Autowired
private MemberLoginDao memberLoginDao;

@Override
public List<MemberLogin> find(String userId){
    return memberLoginDao.find(userId);
}


}
