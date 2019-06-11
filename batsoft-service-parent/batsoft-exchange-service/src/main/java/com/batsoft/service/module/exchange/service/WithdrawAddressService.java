/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/

package com.batsoft.service.module.exchange.service;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.WithdrawAddress;

import java.util.List;

/**
* <p>WithdrawAddressService</p>
* @author: Bat Admin
* @Date :  2018-04-25 15:16:42 
*/
public interface WithdrawAddressService  extends BaseService<WithdrawAddress, String>{

    /**
     * 查询用户提币地址
     * @param coinCode
     * @return
     */
    List<WithdrawAddress> findAddresList(String coinCode,String userId);

    /**
     * 修改默认提现地址
     * @param userId 用户id
     * @param addressId 提币地址id
     * @return
     */
//    Integer   modifyWithdrawAddress(String withdrawAddressId);

    /**
     * 删除用户的地址
     * @param userId
     * @param addressId
     * @return
     */
    int deleteUserCoinAddress(String userId,String addressId);


}
