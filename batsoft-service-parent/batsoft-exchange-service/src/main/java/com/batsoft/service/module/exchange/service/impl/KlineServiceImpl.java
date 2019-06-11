/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-09 11:11:13 
*/
package com.batsoft.service.module.exchange.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.batsoft.core.common.Constants;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.Kline;
import com.batsoft.service.module.exchange.dao.KlineDao;
import com.batsoft.service.module.exchange.service.KlineService;

/**
* <p> KlineServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-09 11:11:13 
*/
@Service("klineService")
public class KlineServiceImpl extends BaseServiceImpl<Kline, String> implements KlineService{

	@Resource
	private KlineDao dao;
	
	public static final String CACHE_KLINE_COINS= Constants.CACHE_EX_PREFIX+"kline:";

}
