/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月11日 下午4:12:10
 */
package com.batsoft.service.module.system.dao.menu;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.core.dao.BaseTreeDao;
import com.batsoft.model.module.system.menu.AppMenu;
import com.batsoft.model.module.system.menu.vo.AppMenuTree;
import com.batsoft.model.module.system.menu.vo.AppMenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年1月11日 下午4:12:10 
 */
public interface AppMenuDao extends BaseDao<AppMenu,String>,BaseTreeDao<AppMenuVo,String>{


    AppMenu findByKey(String menuKey);

    Integer findMaxSort(String pid);

    List<AppMenuTree> findChildMenus(@Param("pid") String pid, @Param("display") Integer display, @Param("menu") Integer menu);
}
