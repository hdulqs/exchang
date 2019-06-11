/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-05 23:23:40 
*/

package com.batsoft.service.module.system.service.type;

import com.batsoft.core.common.JsTree;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.service.BaseTreeService;
import com.batsoft.model.module.system.type.AppDictionary;
import com.batsoft.model.module.system.type.vo.AppDictionaryVo;

import java.util.List;

/**
* <p>AppDictionaryService</p>
* @author: Bat Admin
* @Date :  2018-05-05 23:23:40 
*/
public interface AppDictionaryService extends BaseService<AppDictionary, String>,BaseTreeService<AppDictionaryVo,String> {


    /**
     * 通过key 查找
     * @param dicKey
     * @return
     */
    Object findByKey(String dicKey);

    void updateCache();

    /**
     * 查找数据字典通过key
     * @param key
     * @return
     */
    List<JsTree> findAppDictionByKey(String key);


}
