/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:22:21 
*/

package com.batsoft.service.module.exchange.service;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.EntrustHistory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* <p>EntrustHistoryService</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:22:21 
*/
public interface EntrustHistoryService  extends BaseService<EntrustHistory, String>{

    int  deleteById( String[] id,Date date);
    EntrustHistory findById(String id,Date date);

    /**
     * sql分页
     * @param
     * @return
     */
    PageResult findPageBySql(String account_id, String customer_id,String entrustPrice,String entrustAmout,String entrustState,String category, int page, int pageSize, Date date);

    /**
     * 根据USERid分页查询
     * @return
     */
    PageResult findPageByUserId(HttpServletRequest request,String userId, int page, int pageSize, Date date);

    BigDecimal sumOfBuyAmoutByStartTimeAndId(String id, Date date, Date create_time,String coinCode,String priceCode);

    BigDecimal sumOfSellAmoutByStartTimeAndId(String id, Date date, Date create_time,String coinCode,String priceCode);

}
