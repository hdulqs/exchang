/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-28 17:39:53 
*/

package com.batsoft.service.module.otc.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.otc.ProjectOrder;
import com.batsoft.model.module.otc.vo.ProjectOrderVo;

import java.util.List;
import java.util.Map;

/**
* 
* <p>ProjectOrderDao</p>
* @author: Bat Admin
* @Date :  2017-11-28 17:39:53 
*/
public interface ProjectOrderDao extends  BaseDao<ProjectOrder, String> {

    List<ProjectOrderVo> findPageBySql(Map<String, Object> map);
}
