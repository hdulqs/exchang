/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-25 10:54:59 
*/

package com.batsoft.service.module.blockchain.service;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.blockchain.Wallet;

/**
* <p>WalletService</p>
* @author: Bat Admin
* @Date :  2018-05-25 10:54:59 
*/
public interface WalletService  extends BaseService<Wallet, String>{

    /**
     * 查询区块高度
     * @param wallet
     * @return
     */
    Long findBlockHigh(Wallet wallet);

}
