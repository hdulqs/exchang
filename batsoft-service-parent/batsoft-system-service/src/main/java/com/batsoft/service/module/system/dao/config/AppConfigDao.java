/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-26 17:29:20 
*/
package com.batsoft.service.module.system.dao.config;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.system.config.AppConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>AppConfigDao</p>
* @author: Bat Admin
* @Date :  2017-06-26 17:29:20 
*/
public interface AppConfigDao extends  BaseDao<AppConfig, String> {

    List<AppConfig> findByTypeKey(@Param("typeKey") String typeKey);

    void updateConfig(@Param("confKey") String key, @Param("confValue")String value);
}
