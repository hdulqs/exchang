/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-28 10:34:53 
*/

package com.batsoft.service.module.cms.service;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.cms.Single;

/**
* <p>SingleService</p>
* @author: LouSir
* @Date :  2018-05-28 10:34:53 
*/
public interface SingleService  extends BaseService<Single, String>{

    /**
     * 查询有效单页列表
     * @return
     */
    JsonResult singleAll();
}
