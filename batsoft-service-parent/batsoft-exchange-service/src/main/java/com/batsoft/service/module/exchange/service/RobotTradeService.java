/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-29 23:10:22 
*/

package com.batsoft.service.module.exchange.service;
import java.util.List;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.RobotTrade;

/**
* <p>RobotTradeService</p>
* @author: Bat Admin
* @Date :  2018-05-29 23:10:22 
*/
public interface RobotTradeService extends BaseService<RobotTrade, String>{
	
	/**
	 * 使用状态查询机器人配置记录
	 * 
	 * @param status
	 * 			状态代码
	 * @return
	 */
	List<RobotTrade> listRecordByStatus(Integer status);

}
