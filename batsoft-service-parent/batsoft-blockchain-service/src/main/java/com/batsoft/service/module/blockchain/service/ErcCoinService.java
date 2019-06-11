/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-24 00:43:24 
*/

package com.batsoft.service.module.blockchain.service;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.blockchain.ErcCoin;

/**
* <p>ErcCoinService</p>
* @author: Bat Admin
* @Date :  2018-05-24 00:43:24 
*/
public interface ErcCoinService  extends BaseService<ErcCoin, String>{


    /**
     * 通过合约地址获取代币信息
     * @param address
     * @return
     */
    ErcCoin findCoin(String address);
}
