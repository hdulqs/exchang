/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-26 17:29:20 
*/
package com.batsoft.service.module.system.service.config;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.system.config.AppConfig;

import java.util.List;


/**
* <p>AppConfigService</p>
* @author: Bat Admin
* @Date :  2017-06-26 17:29:20 
*/
public interface AppConfigService extends BaseService<AppConfig, String>{

	/**
	 * 根据配置键查询
	 * 
	 * @param typeKey
	 * 			配置键
	 * @return
	 */
    List<AppConfig> findByTypeKey(String typeKey);

    /**
     * 更新配置
     * 
     * @param key
     * 			键
     * @param value
     * 			值
     */
    void updateConfig(String key, String value);

    /**
     * 将config 放入缓存
     */
    void findConfigToCache();

    /**
     * 通过key获取配置值 ，先从缓存中获取，再从数据库中获取
     * @param key
     * @return
     */
    String findValueByKey(String key);

    /**
     * 是否缓存
     * @param key
     * @param cache
     * @return
     */
    String findValueByKey(String key,boolean cache);
}
