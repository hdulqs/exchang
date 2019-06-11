/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-26 15:11:40 
*/
package com.batsoft.service.module.system.dao.config;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.system.config.AppConfigType;
import com.batsoft.model.module.system.config.vo.AppConfigTypeVo;

import java.util.List;

/**
* <p>AppConfigTypeDao</p>
* @author: Bat Admin
* @Date :  2017-06-26 15:11:40 
*/
public interface AppConfigTypeDao extends  BaseDao<AppConfigType, String> {

    List<AppConfigTypeVo> findTypes();
}
