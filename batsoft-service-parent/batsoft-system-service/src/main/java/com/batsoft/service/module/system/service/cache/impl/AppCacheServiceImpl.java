/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-29 19:32:39 
*/
package com.batsoft.service.module.system.service.cache.impl;

import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.system.cache.AppCache;
import com.batsoft.service.module.system.dao.cache.AppCacheDao;
import com.batsoft.service.module.system.service.cache.AppCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* <p> AppCacheServiceImpl </p>
* @author: 
* @Date :  2017-06-29 19:32:39 
*/
@Service("appCacheService")
public class AppCacheServiceImpl extends BaseServiceImpl<AppCache, String> implements AppCacheService{

@Autowired
private AppCacheDao appCacheDao;


    @Override
    public void saveOrUpdateCache(AppCache appCache) {
        QueryFilter filter=new QueryFilter(AppCache.class);
        filter.addFilter("cacheKey_EQ",appCache.getCacheKey());
        AppCache cache=this.get(filter);
        if(null!=cache){
            appCache.setId(cache.getId());
            appCache.setCreateTime(cache.getCreateTime());
            appCache.setDel(cache.getDel());
            this.update(appCache);
        }else {
            this.save(appCache);
        }
    }
}
