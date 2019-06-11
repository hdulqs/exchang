/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月11日 下午3:33:51
 */
package com.batsoft.service.module.system.service.menu;


import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.service.BaseTreeService;
import com.batsoft.model.module.system.menu.AppMenu;
import com.batsoft.model.module.system.menu.vo.AppMenuTree;
import com.batsoft.model.module.system.menu.vo.AppMenuVo;

import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2016年12月11日 下午3:33:51 
 */

public interface AppMenuService extends BaseService<AppMenu, String>,BaseTreeService<AppMenuVo,String>{
    /**
     * <p> 查询menu</p>
     * @author:         Bat Admin
     * @param:    @param pid
     * @param:    @param display 是否显示
     * @param:    @param menu 是否menu
     * @param:    @return
     * @return: List<AppMenuTree>
     * @Date :          2017年2月23日 下午1:53:06
     * @throws:
     */
    List<AppMenuTree> findChildMenus(String pid, Integer display, Integer menu);
    /**
     * 更新缓存
     */
    void updateCache();

    /**
     * 通过Key 查询
     * @param menuKey
     * @return
     */
    AppMenu findByKey(String menuKey);

    /**
     * 通过pid 查询最大的sort值
     * @param pid
     * @return
     */
    Integer findMaxSort(String pid);

    /**
     * 保存menu
     * @param appMenu
     * @return
     */
    JsonResult saveAppMenu(AppMenu appMenu);

    /**
     * 查询用户menus
     * @return List<AppMenuTree>
     */
    List<AppMenuTree> findUserMenus();
}
