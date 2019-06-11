/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:29 
*/
package com.batsoft.service.module.blockchain.service.impl;

import com.batsoft.model.module.blockchain.CoinNotify;
import com.batsoft.service.module.blockchain.dao.CoinNotifyDao;
import com.batsoft.service.module.blockchain.service.CoinNotifyService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> CoinNotifyServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-22 14:20:29 
*/
@Service("coinNotifyService")
public class CoinNotifyServiceImpl extends BaseServiceImpl<CoinNotify, String> implements CoinNotifyService{

@Autowired
private CoinNotifyDao coinNotifyDao;


}
