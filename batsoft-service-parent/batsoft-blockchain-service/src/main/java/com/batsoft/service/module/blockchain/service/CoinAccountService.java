/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:22:17 
*/

package com.batsoft.service.module.blockchain.service;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.blockchain.CoinAccount;

/**
* <p>CoinAccountService</p>
* @author: Bat Admin
* @Date :  2018-05-22 14:22:17 
*/
public interface CoinAccountService extends BaseService<CoinAccount, String>{

    /**
     * 创建钱包地址
     * @param coinCode 钱包code
     * @param count 数量
     * @return
     */
    JsonResult saveCoinAddress(String coinCode, Integer count);


    /**
     * 获取一条可用钱包地址
     * @param walletCode 钱包代码
     * @return
     */
    String findAddress(String walletCode);

    /**
     * 修改地址状态
     * @param address
     */
    void updateAddressStatus(String address);

    /**
     * 获取有效币地址数量
     * @return
     */
    JsonResult findAddressByEffective();



}
