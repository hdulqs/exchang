/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/
package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.model.module.exchange.WithdrawAddress;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.exchange.dao.WithdrawAddressDao;
import com.batsoft.service.module.exchange.service.WithdrawAddressService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.service.module.member.service.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
* <p> WithdrawAddressServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-25 15:16:42 
*/
@Service("withdrawAddressService")
public class WithdrawAddressServiceImpl extends BaseServiceImpl<WithdrawAddress, String> implements WithdrawAddressService{

@Autowired
private WithdrawAddressDao withdrawAddressDao;


    /**
     * 查询用户提币地址
     *
     * @return
     */
    @Override
    public List<WithdrawAddress> findAddresList(String coinCode,String userId) {
        return withdrawAddressDao.findAddresList(userId,coinCode);
    }

    @Override
    public int deleteUserCoinAddress(String userId, String addressId) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("address_id",addressId);
       return withdrawAddressDao.deleteUserCoinAddress(map);
    }

   /* @Override
    public Integer modifyWithdrawAddress(String withdrawAddressId) {
        User user = UserUtils.getUser();



        Integer count=  withdrawAddressDao.updateWithdrawAddress(Boolean.FALSE,null,user.getId());
        if(count>0){
          return   withdrawAddressDao.updateWithdrawAddress(Boolean.TRUE,withdrawAddressId,user.getId());
        }
        return 0;
    }*/
}
