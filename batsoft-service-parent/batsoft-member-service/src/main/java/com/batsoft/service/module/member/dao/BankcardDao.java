/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:22:51 
*/

package com.batsoft.service.module.member.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.member.Bankcard;
import org.apache.ibatis.annotations.Param;

/**
* 
* <p>BankcardDao</p>
* @author: LouSir
* @Date :  2018-05-07 09:22:51 
*/
public interface BankcardDao extends  BaseDao<Bankcard, String> {

    /**
     * 根据用户ID查询用户有效银行卡
     * @param userId
     * @return
     */
    public Bankcard findByUserId(String userId);

}
