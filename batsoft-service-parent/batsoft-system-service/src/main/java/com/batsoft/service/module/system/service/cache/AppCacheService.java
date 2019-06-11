/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-29 19:32:39 
*/
package com.batsoft.service.module.system.service.cache;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.system.cache.AppCache;


/**
* <p>AppCacheService</p>
* @author: Bat Admin
* @Date :  2017-06-29 19:32:39 
*/
public interface AppCacheService  extends BaseService<AppCache, String>{

	void saveOrUpdateCache(AppCache appCache);
}
