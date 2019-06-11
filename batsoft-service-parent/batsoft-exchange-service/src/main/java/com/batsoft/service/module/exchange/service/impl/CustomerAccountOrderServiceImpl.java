/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:21:24 
*/
package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.model.module.exchange.CustomerAccountOrder;
import com.batsoft.service.module.exchange.dao.CustomerAccountOrderDao;
import com.batsoft.service.module.exchange.service.CustomerAccountOrderService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> CustomerAccountOrderServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-14 10:21:24 
*/
@Service("customerAccountOrderService")
public class CustomerAccountOrderServiceImpl extends BaseServiceImpl<CustomerAccountOrder, String> implements CustomerAccountOrderService{

@Autowired
private CustomerAccountOrderDao customerAccountOrderDao;


}
