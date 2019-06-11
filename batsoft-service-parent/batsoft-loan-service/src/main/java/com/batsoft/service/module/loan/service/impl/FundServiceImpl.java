/**
* Copyright:    http://www.batsoft.cn
* @author:      Lucl
* @version:     V1.0
* @Date:        2017-10-28 18:03:41 
*/
package com.batsoft.service.module.loan.service.impl;

import com.batsoft.model.module.loan.Fund;
import com.batsoft.service.module.loan.dao.FundDao;
import com.batsoft.service.module.loan.service.FundService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> FundServiceImpl </p>
* @author: 
* @Date :  2017-10-28 18:03:41 
*/
@Service("fundService")
public class FundServiceImpl extends BaseServiceImpl<Fund, String> implements FundService{

@Autowired
private FundDao fundDao;


}
