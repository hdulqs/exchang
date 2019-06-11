/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:21:48 
*/

package com.batsoft.service.module.exchange.service;

import java.math.BigDecimal;
import java.util.Date;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.CustomerAccountRecord;

/**
* <p>CustomerAccountRecordService</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:21:48 
*/
public interface CustomerAccountRecordService extends BaseService<CustomerAccountRecord, String>{

    int  deleteById( String[] id,Date date);
    /**
     * sql分页
     * @param account_id 用户id
     * @param customer_id 客服id
     * @param page
     * @param pageSize
     * @param date 请求的日期
     * @return
     */
    PageResult findPageBySql(String account_id,String customer_id,int page,int pageSize,Date date);

    /**
     * 根据id和时间查询账号记录详情
     * @param id
     * @param date
     * @return
     */
    CustomerAccountRecord findById( String id,Date date);

    /**
     * 保存实际流水
     * @param type
     * @param accountId
     * @param userId
     * @param coinCode
     * @param amount
     */
    void saveAccountRecord(String type, String accountId, String userId,String coinCode, String amount);
    
    /**
     * 保存实际流水
     * 
     * @param type
     * 			交易类型
     * @param userId
     * 			用户ID
     * @param coinCode
     * 			交易货币代码
     * @param money
     * 			交易货币数量
     * @param tradeCoinCode
     * 			交易对=》交易币
     * @param pricingCoinCode
     * 			交易对=》定价币
     * @param remark
     */
    void saveAccountRecord(String type, String userId, String coinCode, BigDecimal money, String tradeCoinCode, String pricingCoinCode, String remark,String tableDate);


    /**
     * 统计用户交易金额
     *
     * @param type
     *          交易类型
     * @param coinCode
     *          货币代码
     * @param beginTime
     *          开始时间
     * @return
     */
    BigDecimal sumMoneyByTypeCoinCode(String type, String coinCode, Date beginTime);
    
    /**
     * 流水综合查询
     * 
     * @param param
     * 			综合参数
     * @return
     */
    PageResult findPage(CustomerAccountRecord param,int page,int pageSize,Date date);
}
