/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:24:59 
*/
package com.batsoft.service.module.cms.service.impl;

import com.batsoft.model.module.cms.Friend;
import com.batsoft.service.module.cms.dao.FriendDao;
import com.batsoft.service.module.cms.service.FriendService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> FriendServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-14 10:24:59 
*/
@Service("friendService")
public class FriendServiceImpl extends BaseServiceImpl<Friend, String> implements FriendService{

@Autowired
private FriendDao friendDao;


}
