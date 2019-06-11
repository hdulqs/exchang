/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-06-03 10:51:01 
*/

package com.batsoft.service.module.blockchain.service;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.blockchain.DepositWallet;

/**
* <p>DepositWalletService</p>
* @author: Bat Admin
* @Date :  2018-06-03 10:51:01 
*/
public interface DepositWalletService  extends BaseService<DepositWallet, String>{

    /**
     * 创建钱包地址
     * @param coinCode 钱包code
     * @return
     */
    JsonResult saveCoinAddress(String coinCode);

    /**
     * 转币--基于btc接口的转币
     * @param coinCode 提币代码
     * @param toAddress 提币地址
     * @param amount 转币数量
     * @return
     */
    JsonResult sendBtcChainCoin( String coinCode,String toAddress,String amount);

    /**
     * 转币--基于ETH接口的转币
     * @param coinCode 提币代码
     * @param toAddress 提币地址
     * @param amount 转币数量
     * @return
     */
    JsonResult sendEthChainCoin( String coinCode,String toAddress,String amount);


    JsonResult sendCoin(JSONObject data);

}
