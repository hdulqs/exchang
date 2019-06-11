/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-18 13:20:52 
*/

package com.batsoft.service.module.otc.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.otc.ExCoin;

import java.util.List;

/**
* 
* <p>ExCoinDao</p>
* @author: Bat Admin
* @Date :  2017-11-18 13:20:52 
*/
public interface ExCoinDao extends  BaseDao<ExCoin, String> {

    List<ExCoin> findListCoins();
}
