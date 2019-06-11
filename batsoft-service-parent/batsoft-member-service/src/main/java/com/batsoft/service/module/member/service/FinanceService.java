/**
* Copyright:    http://www.batsoft.cn
* @author:      Lucl
* @version:     V1.0
* @Date:        2017-11-17 17:28:47 
*/

package com.batsoft.service.module.member.service;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.member.Finance;

import java.util.List;

/**
* <p>FinanceService</p>
* @author: Lucl
* @Date :  2017-11-17 17:28:47 
*/
public interface FinanceService  extends BaseService<Finance, String>{

    /**
     * 保存币种账户信息
     * @param list
     * @return
     */
   boolean saveCurrency(List<Finance> list);

    /**
     * 生成人民币账户
     * @return
     */
   boolean saveCNY();
}
