/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-12-08 09:56:42 
*/

package com.batsoft.service.module.otc.dao;
import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.otc.ExFinance;
import com.batsoft.model.module.otc.vo.ExFinanceVo;

import java.util.List;

/**
* 
* <p>ExFinanceDao</p>
* @author: Bat Admin
* @Date :  2017-12-08 09:56:42 
*/
public interface ExFinanceDao extends  BaseDao<ExFinance, String> {

    List<ExFinanceVo> findList(String userId);
}
