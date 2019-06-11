/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:21:02 
*/

package com.batsoft.service.module.exchange.service;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.CustomerAccountFreeze;

import java.util.Date;

/**
* <p>CustomerAccountFreezeService</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:21:02 
*/
public interface CustomerAccountFreezeService  extends BaseService<CustomerAccountFreeze, String>{

    /**
     * 分页查询
     * @param account_id
     * @param customer_id
     * @param page
     * @param pageSize
     * @param date
     * @return
     */
    PageResult findPageBySql(String account_id,String customer_id,int page, int pageSize, Date date);

    CustomerAccountFreeze findById(String id,Date date);
    /**
     * 保持用户
     * @param freeze
     * @param date
     * @return
     */
    int saveObject(CustomerAccountFreeze freeze,Date date);

    int deleteById(String[] id,Date date);
}
