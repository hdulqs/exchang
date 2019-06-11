/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:19:04 
*/

package com.batsoft.service.module.exchange.service;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.Coin;

import java.util.List;

/**
* <p>CoinService</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:19:04 
*/
public interface CoinService  extends BaseService<Coin, String>{

    /**
     * 查询可用coins ；status ==1 and del==0
     * 数据库中查询
     * @return
     */
    List<Coin> findCoins();
    /**
     * 查询可用coins ；status ==1 and del==0
     * 优先缓存中查询 缓存中查询不到再从数据库中查询
     * @return
     */
    String findJsonCoins();

    PageResult selectCoin(String coinCode, int page, int pageSize);
    /**
     * 查找可以体现的coins
     * @return
     */
    String findJsonCoinsAllowWithDraw();

    /**
     * 查找可以充值的coins
     * @return
     */
    String findJsonCoinsAllowRecharge();

    /**
     * 更新币种信息
     * @return
     */
    String updateJsonCoins();

    /**
     * 查询可用的定价币  priceCoin==1
     * @return
     */
    List<Coin> findPriceCoins();

    /**
     * 缓存中查询定价币
     * @return
     */
    String findJsonPriceCoins();

    /**
     * 更新定价币
     * @return
     */
    void updateJsonPriceCoins();



    /**
     * 获取提币数据信息/通过coinCode 获取coin
     * @param coinCode
     * @return
     */
    Coin findWithdrawData(String coinCode);

    /**
     * 初始化交易时所用到币种的参数
     */
    void saveRedisCache();
}
