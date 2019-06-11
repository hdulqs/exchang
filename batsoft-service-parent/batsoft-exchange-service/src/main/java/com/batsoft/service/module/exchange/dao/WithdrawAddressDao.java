/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/

package com.batsoft.service.module.exchange.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.exchange.WithdrawAddress;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
* 
* <p>WithdrawAddressDao</p>
* @author: Bat Admin
* @Date :  2018-04-25 15:16:42 
*/
public interface WithdrawAddressDao extends  BaseDao<WithdrawAddress, String> {

    List<WithdrawAddress> findAddresList(@Param("userId")String userId,@Param("coinCode") String coinCode);

    int deleteUserCoinAddress(HashMap<String,Object> map);
}
