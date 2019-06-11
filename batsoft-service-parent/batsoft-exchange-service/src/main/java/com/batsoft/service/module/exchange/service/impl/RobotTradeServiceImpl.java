/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-29 23:10:22 
*/
package com.batsoft.service.module.exchange.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.RobotTrade;
import com.batsoft.service.module.exchange.dao.RobotTradeDao;
import com.batsoft.service.module.exchange.service.RobotTradeService;

/**
* <p> RobotTradeServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-29 23:10:22 
*/
@Service("robotTradeService")
public class RobotTradeServiceImpl extends BaseServiceImpl<RobotTrade, String> implements RobotTradeService{

	@Autowired
	private RobotTradeDao dao;

	@Override
	public List<RobotTrade> listRecordByStatus(Integer status) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("status", status);
		return dao.findRecordByStatus(values);
	}


}
