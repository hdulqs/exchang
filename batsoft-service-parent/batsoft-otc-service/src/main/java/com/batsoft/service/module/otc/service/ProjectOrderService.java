/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-28 17:39:53 
*/

package com.batsoft.service.module.otc.service;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.otc.ProjectOrder;

import javax.servlet.http.HttpServletRequest;

/**
* <p>ProjectOrderService</p>
* @author: Bat Admin
* @Date :  2017-11-28 17:39:53 
*/
public interface ProjectOrderService  extends BaseService<ProjectOrder, String>{
    /**
     * 订单列表
     * @param request 请求数据
     * @return
     */
    PageResult findList(HttpServletRequest request);

}
