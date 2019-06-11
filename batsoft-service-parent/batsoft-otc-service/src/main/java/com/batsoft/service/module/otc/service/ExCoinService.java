/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-18 13:20:52 
*/

package com.batsoft.service.module.otc.service;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.otc.ExCoin;

import java.util.List;

/**
* <p>ExCoinService</p>
* @author: Bat Admin
* @Date :  2017-11-18 13:20:52 
*/
public interface ExCoinService  extends BaseService<ExCoin, String>{

    /**
     * 查询可用coins ；status ==1 and del==0
     * 数据库中查询
     * @return
     */
    List<ExCoin> findCoins();
    /**
     * 查询可用coins ；status ==1 and del==0
     * 优先缓存中查询 缓存中查询不到再从数据库中查询
     * @return
     */
    String findJsonCoins();

}
