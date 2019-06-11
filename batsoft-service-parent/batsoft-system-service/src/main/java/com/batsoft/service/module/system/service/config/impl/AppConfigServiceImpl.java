/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-26 17:29:20
 */
package com.batsoft.service.module.system.service.config.impl;

import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.system.dao.config.AppConfigDao;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> AppConfigServiceImpl </p>
 * @author:
 * @Date :  2017-06-26 17:29:20
 */
@Service("appConfigService")
public class AppConfigServiceImpl extends BaseServiceImpl<AppConfig, String> implements AppConfigService {

    @Autowired
    private AppConfigDao appConfigDao;

    @Autowired
    private RedisService redisService;


    @Override
    public List<AppConfig> findByTypeKey(String typeKey) {
        return appConfigDao.findByTypeKey(typeKey);
    }

    @Override
    public void updateConfig(String key, String value) {
        appConfigDao.updateConfig(key, value);
        redisService.set(Constants.CACHE_CONFIG_KEY+key,value,0);

    }

    @Override
    public void findConfigToCache() {
        QueryFilter filter =new QueryFilter(AppConfig.class);
        filter.addFilter("del_EQ",0);
        List<AppConfig> appConfigList = find(filter);
        if (null != appConfigList) {
            for (AppConfig config : appConfigList) {
                redisService.set(Constants.CACHE_CONFIG_KEY+config.getConfKey(), config.getConfValue(), 0);
            }
        }

    }

    @Override
    public String findValueByKey(String key) {
        String value = redisService.get(Constants.CACHE_CONFIG_KEY + key);
        if(StringUtils.isEmpty(value)){
            QueryFilter filter = new QueryFilter(AppConfig.class);
            filter.addFilter("del_EQ", 0);
            filter.addFilter("confKey_EQ",key);
            AppConfig appConfig=this.get(filter);
            if(!StringUtils.isNull(appConfig)){
                value=appConfig.getConfValue();
                redisService.set(Constants.CACHE_CONFIG_KEY + key,value,0);
            }
        }
        return value;
    }
    @Override
    public String findValueByKey(String key,boolean cache) {
        String value = "";
        if(cache){
           return findValueByKey(key);
        } else {
                QueryFilter filter = new QueryFilter(AppConfig.class);
                filter.addFilter("del_EQ", 0);
                filter.addFilter("confKey_EQ", key);
                AppConfig appConfig = this.get(filter);
                if (!StringUtils.isNull(appConfig)) {
                    value = appConfig.getConfValue();
                    redisService.set(Constants.CACHE_CONFIG_KEY + key, value, 0);
                }
        }
        return value;
    }
}
