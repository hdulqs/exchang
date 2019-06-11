/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-24 00:43:24 
*/
package com.batsoft.service.module.blockchain.service.impl;

import com.batsoft.core.common.QueryFilter;
import com.batsoft.model.module.blockchain.ErcCoin;
import com.batsoft.service.module.blockchain.dao.ErcCoinDao;
import com.batsoft.service.module.blockchain.service.ErcCoinService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> ErcCoinServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-24 00:43:24 
*/
@Service("ercCoinService")
public class ErcCoinServiceImpl extends BaseServiceImpl<ErcCoin, String> implements ErcCoinService{

@Autowired
private ErcCoinDao ercCoinDao;


    @Override
    public ErcCoin findCoin(String address) {
        QueryFilter filter=new QueryFilter(ErcCoin.class);
        filter.addFilter("coinAddress_EQ",address);
        return this.get(filter);
    }
}
