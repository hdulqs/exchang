/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:21:48 
*/

package com.batsoft.service.module.exchange.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.po.FindCustomerAccountRecordExistPO;
import com.batsoft.model.module.exchange.po.FindCustomerEverydayRecordPO;
import com.batsoft.model.module.exchange.vo.FindCustomerEverydayRecordVO;
import org.apache.ibatis.annotations.Param;

/**
* 
* <p>CustomerAccountRecordDao</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:21:48 
*/
public interface CustomerAccountRecordDao extends  BaseDao<CustomerAccountRecord, String> {
	
	/**
	 * 简单的分页查询
	 * 
	 * @param
	 * @return
	 */
    List<CustomerAccountRecord> findPageBySql( Map<String, Object> map);

    CustomerAccountRecord findById(Map<String,Object> map);
    /**
     * 简单的分页查询
     *
     * @param
     * @return
     */
    Long  findPageBySqlTotal( Map<String, Object> map);


    /**
     * 使用交易类型和货币代码统计交易金额
     *
     * @param map
     * @return
     */
    BigDecimal findMoneyByTypeCoinCode(Map<String, Object> map);
    
    /**
     * 流水综合查询
     * 
     * @param param
     * 			综合参数
     * @return
     */
    List<CustomerAccountRecord> findPage(HashMap<String, Object> param);
    /**
     * 流水综合查询 total
     *
     * @param param
     * @return
     */
    Long findPageToalRows(HashMap<String, Object> param);
    
    /**
     * 查询用户每天交易流水金额
     * 
     * @param param
     * 			综合参数
     * @return
     */
    List<FindCustomerEverydayRecordVO> findCustomerEverydayRecord(FindCustomerEverydayRecordPO param);
    
    /**
     * 根据条件查询记录数
     * 
     * @param param
     * 			综合参数
     * @return
     */
    BigInteger findCustomerAccountRecordExist(FindCustomerAccountRecordExistPO param);

    int  deleteById(Map<String, Object> map);

    BigDecimal getRecordSum(Map<String,Object> map);

  }
