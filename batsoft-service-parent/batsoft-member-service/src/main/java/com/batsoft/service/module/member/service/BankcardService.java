/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:22:51 
*/

package com.batsoft.service.module.member.service;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.member.Bankcard;

/**
* <p>BankcardService</p>
* @author: LouSir
* @Date :  2018-05-07 09:22:51 
*/
public interface BankcardService  extends BaseService<Bankcard, String>{

    JsonResult addBankCard(Bankcard bankcard);

    /**
     * 通过用户ID查询对应的银行卡信息
     * @param
     * @return
     */
    JsonResult findByUserId();
}
