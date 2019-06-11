/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:25:25 
*/
package com.batsoft.service.module.log.service.impl;

import com.batsoft.model.module.log.Api;
import com.batsoft.service.module.log.dao.ApiDao;
import com.batsoft.service.module.log.service.ApiService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> ApiServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-14 10:25:25 
*/
@Service("apiService")
public class ApiServiceImpl extends BaseServiceImpl<Api, String> implements ApiService{

@Autowired
private ApiDao apiDao;


}
