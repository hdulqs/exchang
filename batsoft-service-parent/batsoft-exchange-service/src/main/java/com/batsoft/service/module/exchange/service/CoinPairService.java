/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:20:19 
*/

package com.batsoft.service.module.exchange.service;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.CoinPair;

import java.util.List;

/**
* <p>CoinPairService</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:20:19 
*/
public interface CoinPairService  extends BaseService<CoinPair, String>{

    /**
     * 是否已经存在交易对
     * @param priceSymbol
     * @param tradeSymobl
     * @return
     */
    boolean hasCoinTrade(String priceSymbol,String tradeSymobl);

    /**
     * 获取DB中定价币下的交易币种
     * @param priceCoin
     * @return
     */
    List<CoinPair> findTradeCoins(String priceCoin);

    /**
     * 获取缓存中的定价币
     * @param priceCoin
     * @return
     */
    String findJsonTradeCoins(String priceCoin);
    /**
     * 更新该定价币下的交易币信息
     * @param priceCoin
     * @return
     */
    void updateJsonTradeCoins(String priceCoin);

    /**
     * 更新交易区数据 --保存全部数据[{"ETH:"[{},{}]},{"USDT":[{},{}]}]
     * @return
     */
    String updateTradeAreaCoins();

    /**
     * 查询交易区所有数据
     * @return
     */
    String findTradeAreaCoins();

    /**
     * 查询币种推荐列表
     * @return
     */
    String findRecommentSession();

    /**
     * 查询定价币和交易币的比例
     * @return
     */
    String findMapTradeCoins();

    /**
     * 查询交易币小数定价规则
     */
    String findCoinPairDigit();

    void findCoinPairDigitInit();

    /**
     * 从缓冲中查询交易对字符串 如：usdt_eos usdt_ltc
     * @return
     */
    List<String> findCoinPairStr();

    /**
     * 更新交易对字符串到缓冲
     */
    List<String> updateCoinPairStr();

    /**
     * 币推荐管理
     * @param PKS
     * @return
     */
    JsonResult recommendCoins(String[] PKS,Integer type);

    /**
     * 查询推荐币种列表
     * @return
     */
    String findRecomment();

}
