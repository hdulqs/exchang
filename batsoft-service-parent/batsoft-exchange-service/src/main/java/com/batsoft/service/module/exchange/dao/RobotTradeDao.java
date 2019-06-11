/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-29 23:10:22 
*/

package com.batsoft.service.module.exchange.dao;
import java.util.List;
import java.util.Map;

import com.batsoft.core.dao.BaseDao;

import com.batsoft.model.module.exchange.RobotTrade;

/**
* 
* <p>RobotTradeDao</p>
* @author: Bat Admin
* @Date :  2018-05-29 23:10:22 
*/
public interface RobotTradeDao extends BaseDao<RobotTrade, String> {
	
	/**
	 * 使用状态查询机器人配置记录
	 * 
	 * @param values
	 * @return
	 */
	List<RobotTrade> findRecordByStatus(Map<String, Object> values);
}
