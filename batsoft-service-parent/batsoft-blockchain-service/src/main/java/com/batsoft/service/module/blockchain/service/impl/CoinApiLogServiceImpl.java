/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:21:20 
*/
package com.batsoft.service.module.blockchain.service.impl;

import com.batsoft.model.module.blockchain.CoinApiLog;
import com.batsoft.service.module.blockchain.dao.CoinApiLogDao;
import com.batsoft.service.module.blockchain.service.CoinApiLogService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> CoinApiLogServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-22 14:21:20 
*/
@Service("coinApiLogService")
public class CoinApiLogServiceImpl extends BaseServiceImpl<CoinApiLog, String> implements CoinApiLogService{

@Autowired
private CoinApiLogDao coinApiLogDao;


}
