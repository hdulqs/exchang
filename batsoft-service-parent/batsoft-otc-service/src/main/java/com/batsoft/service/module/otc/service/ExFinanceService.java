/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-12-08 09:56:42 
*/

package com.batsoft.service.module.otc.service;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.otc.ExFinance;
import com.batsoft.model.module.otc.vo.ExFinanceVo;

import java.util.List;

/**
* <p>ExFinanceService</p>
* @author: Bat Admin
* @Date :  2017-12-08 09:56:42 
*/
public interface ExFinanceService  extends BaseService<ExFinance, String>{

    /**
     * 生成单一币种账户服务
     * @param userName
     * @param userId
     * @param coin
     * @return
     */
    JsonResult saveOneFinance(String userName,String userId,String coin);

    /**
     * 查询我的帐号数据
     * @return
     */
    List<ExFinanceVo> findList();

}
