/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-23 23:07:19 
*/

package com.batsoft.service.module.blockchain.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.blockchain.CoinOrder;

/**
* 
* <p>CoinOrderDao</p>
* @author: Bat Admin
* @Date :  2018-05-23 23:07:19 
*/
public interface CoinOrderDao extends  BaseDao<CoinOrder, String> {

    int hashHash(String hash);
}
