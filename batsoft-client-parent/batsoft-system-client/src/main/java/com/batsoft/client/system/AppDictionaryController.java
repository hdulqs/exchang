/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-05 23:23:40
 */
package com.batsoft.client.system;


import com.batsoft.core.common.JsTree;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseTreeController;
import com.batsoft.model.module.system.type.AppDictionary;
import com.batsoft.service.module.system.service.type.AppDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * <p>AppDictionaryController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-05 23:23:40
 */
@RestController("appDictionaryController")
@RequestMapping("/system/type/appDictionary")

@Slf4j
public class AppDictionaryController extends BaseTreeController<AppDictionary, String> {


    @Resource(name = "appDictionaryService")
    @Override
    public void setService(BaseService<AppDictionary, String> service) {
        super.service = service;
    }


    /**
     * 通过Key 获取字典
     * @return
     */
    @RequestMapping(value = "/findByKey/{key}")
    @ResponseBody
    public List<JsTree> findByKey(@PathVariable String key) {
        List<JsTree> list=((AppDictionaryService)super.service).findAppDictionByKey(key);
        return list;
    }


}
