/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-26 15:11:40 
*/
package com.batsoft.service.module.system.service.config.impl;

import com.batsoft.model.module.system.config.AppConfigType;
import com.batsoft.model.module.system.config.vo.AppConfigTypeVo;
import com.batsoft.service.module.system.dao.config.AppConfigTypeDao;
import com.batsoft.service.module.system.service.config.AppConfigTypeService;
import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* <p> AppConfigTypeServiceImpl </p>
* @author: 
* @Date :  2017-06-26 15:11:40 
*/
@Service("appConfigTypeService")
public class AppConfigTypeServiceImpl extends BaseServiceImpl<AppConfigType, String> implements AppConfigTypeService{

@Autowired
private AppConfigTypeDao appConfigTypeDao;


    @Override
    public List<AppConfigTypeVo> findTypes() {
        return appConfigTypeDao.findTypes();
    }
}
