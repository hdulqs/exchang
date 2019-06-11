/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:19:04 
*/

package com.batsoft.service.module.exchange.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.Coin;
import org.web3j.crypto.Hash;

import java.util.HashMap;
import java.util.List;

/**
* 
* <p>CoinDao</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:19:04 
*/
public interface CoinDao extends  BaseDao<Coin, String> {
    /**
     * 查询可用币
     * @return
     */
    List<Coin> findListCoins();

    /**
     * 查询可体现的币
     * @return
     */
    List<Coin> findListCoinsAllowWithDraw();

    /**
     * 查询可充值的币
     * @return
     */
    List<Coin> findListCoinsAllowRecharge();
    /**
     * 查询定价币
     * @return
     */
    List<Coin> findListPriceCoins();

    /**
     * 获取提币数据信息/通过coinCode 获取coin
     * @param coinCode
     * @return
     */
    Coin findWithdrawData(String coinCode);

    /**
     * 查找币
     * @param coinCode
     * @return
     */
    List<Coin> selectCoins(HashMap coinCode);

    Long selectCoinsTotal(HashMap coinCode);
}
