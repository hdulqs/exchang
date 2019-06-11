/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-05 23:23:40 
*/

package com.batsoft.service.module.system.dao.type;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.core.dao.BaseTreeDao;
import com.batsoft.model.module.system.type.AppDictionary;
import com.batsoft.model.module.system.type.vo.AppDictionaryVo;

/**
* 
* <p>AppDictionaryDao</p>
* @author: Bat Admin
* @Date :  2018-05-05 23:23:40 
*/
public interface AppDictionaryDao extends BaseDao<AppDictionary,String>,BaseTreeDao<AppDictionaryVo,String> {

    Object findByKey(String dicKey);
}
