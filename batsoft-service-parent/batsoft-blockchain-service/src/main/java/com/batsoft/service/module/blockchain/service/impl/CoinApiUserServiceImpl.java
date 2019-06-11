/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:59 
*/
package com.batsoft.service.module.blockchain.service.impl;

import com.batsoft.model.module.blockchain.CoinApiUser;
import com.batsoft.service.module.blockchain.dao.CoinApiUserDao;
import com.batsoft.service.module.blockchain.service.CoinApiUserService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> CoinApiUserServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-22 14:20:59 
*/
@Service("coinApiUserService")
public class CoinApiUserServiceImpl extends BaseServiceImpl<CoinApiUser, String> implements CoinApiUserService{

@Autowired
private CoinApiUserDao coinApiUserDao;


}
