/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-19 12:42:46 
*/

package com.batsoft.service.module.otc.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.otc.Releaseproject;
import com.batsoft.model.module.otc.vo.ReleaseprojectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 
* <p>ReleaseprojectDao</p>
* @author: Bat Admin
* @Date :  2017-11-19 12:42:46 
*/
public interface ReleaseprojectDao extends  BaseDao<Releaseproject, String> {

    List<ReleaseprojectVo> findProjectIndex(@Param("coin") String coin,@Param("pageSize") Integer pageSize, @Param("otcType")Integer otcType);

    List<ReleaseprojectVo>  findPageBySql(Map<String, Object> map);

    Releaseproject find(String id);
}
