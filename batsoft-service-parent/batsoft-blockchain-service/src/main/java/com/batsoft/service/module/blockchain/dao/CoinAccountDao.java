/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:22:17 
*/

package com.batsoft.service.module.blockchain.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.blockchain.CoinAccount;

/**
* 
* <p>CoinAccountDao</p>
* @author: Bat Admin
* @Date :  2018-05-22 14:22:17 
*/
public interface CoinAccountDao extends BaseDao<CoinAccount, String> {

    /**
     * 获取币地址
     * @param walletCode 钱包代码
     * @return
     */
   String findAddress(String walletCode);

    /**
     * 修改地址状态为已用
     * @param address
     */
   void updateAddressStatus(String address);

    /**
     * 获取有效币地址数量
     * @return
     */
    Integer findAddressNum();
}
