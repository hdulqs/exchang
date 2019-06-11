/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-19 12:42:46 
*/

package com.batsoft.service.module.otc.service;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.otc.Releaseproject;
import com.batsoft.model.module.otc.vo.ReleaseprojectVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* <p>ReleaseprojectService</p>
* @author: Bat Admin
* @Date :  2017-11-19 12:42:46 
*/
public interface ReleaseprojectService  extends BaseService<Releaseproject, String>{

    /**
     * 通过id 获取数据获取状态为可用 且未删除
     * @param id
     * @return
     */
    Releaseproject find(String id);

    /**
     * 查询求购/出售信息列表
     * @param request 请求数据
     * @return
     */
    PageResult findList(HttpServletRequest request);

    /**
     * 查询首页展示求购/出售信息
     * @param coin 币种类型
     * @param pageSize 数量
     * @param otcType 求购类型
     * @return
     */
  List<ReleaseprojectVo> findProjectIndex( String coin,Integer pageSize, Integer otcType);
}
