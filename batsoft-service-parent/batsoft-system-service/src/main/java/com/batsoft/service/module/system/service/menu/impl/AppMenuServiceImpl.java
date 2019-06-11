/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2016年12月11日 下午3:33:51
 */
package com.batsoft.service.module.system.service.menu.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsTree;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.system.menu.AppMenu;
import com.batsoft.model.module.system.menu.vo.AppMenuTree;
import com.batsoft.model.module.system.menu.vo.AppMenuVo;
import com.batsoft.service.module.system.auth.UserUtils;
import com.batsoft.service.module.system.dao.menu.AppMenuDao;
import com.batsoft.service.module.system.service.menu.AppMenuService;
import com.batsoft.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> TODO</p>
 * @author: Bat Admin
 * @Date :          2016年12月11日 下午3:33:51 
 */
@Service("appMenuService")

public class AppMenuServiceImpl extends BaseServiceImpl<AppMenu, String> implements AppMenuService {


    @Autowired
    private AppMenuDao appMenuDao;
    @Autowired
    private RedisService redisService;

    public static final String APPMENU_TREE = "appmenu_tree";

    public  static final String PARENT_ID="0";


    /* (non-Javadoc)
     * @see com.batsoft.service.module.system.service.menu.AppMenuService#findChildList(java.lang.String)
     */
    @Override
    public List<AppMenuVo> findChildList(String id,Integer display) {
        // TODO Auto-generated method stub
        return appMenuDao.findChildList(id,display);
    }

    /* (non-Javadoc)
     * @see com.batsoft.service.module.system.service.menu.AppMenuService#findListTree(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public List<AppMenuVo> findListTree() {
        List<AppMenuVo> find = findChildList("0",null);
        for (AppMenuVo appMenu : find) {
            recursivefind(appMenu);
        }
        return find;
    }

    @Override
    public List<JsTree> findJsTree(String pid) {
        JsTree root = new JsTree();
        root.setId("0");
        root.setText("Root");
        root.setValue("0");
        root.setKey("0");
        root.setLabel("Root");

        List<JsTree> tree = new ArrayList<>();

        List<JsTree> find = findJsTreeChildList(pid,null);
        for (JsTree appMenu : find) {
            recursiveFindJsTree(appMenu);
        }
        root.setChildren(find);

        tree.add(root);

        return tree;
    }

    @Override
    public String findTreeJson() {
        String ret = redisService.get(APPMENU_TREE);
        if (StringUtils.isEmpty(ret)) {
            ret = JSON.toJSONString(findJsTree("0"));
            redisService.set(APPMENU_TREE, ret, 0);
        }
        return ret;
    }



    @Override
    public List<JsTree> findJsTreeChildList(String id,Integer display) {
        return appMenuDao.findJsTreeChildList(id,display);
    }

    @Override
    public String findParentId(String pid) {
        return appMenuDao.findParentId(pid);
    }

    @Override
    public String findParentName(String id) {
        return appMenuDao.findParentName(id);
    }

    /**
     * <p> 查询menu</p>
     *
     * @param pid
     * @param display
     * @param menu
     * @author: Bat Admin
     * @param: @param pid
     * @param: @param display 是否显示
     * @param: @param menu 是否menu
     * @param: @return
     * @return: List<AppMenuVo>
     * @Date :          2017年2月23日 下午1:53:06
     * @throws:
     */
    @Override
    public List<AppMenuTree> findChildMenus(String pid, Integer display, Integer menu) {
        return appMenuDao.findChildMenus( pid,  display,  menu) ;
    }

    @Override
    public void updateCache() {
        String ret = JSON.toJSONString(findJsTree("0"));
        redisService.set(APPMENU_TREE, ret, 0);
    }

    @Override
    public AppMenu findByKey(String menuKey) {
        return appMenuDao.findByKey(menuKey);
    }

    @Override
    public Integer findMaxSort(String pid) {
        return appMenuDao.findMaxSort(pid);
    }

    @Override
    public JsonResult saveAppMenu(AppMenu appMenu) {
        JsonResult result = new JsonResult();
        String mpath = "";
        String[] str;
        try {
            if (!"0".equals(appMenu.getParentId()) && !"".equals(appMenu.getParentId() + "")) {
                String mpaths = "";
                mpath = getparents(appMenu.getParentId(), mpaths);
                appMenu.setLevelPath(mpath);
                str = mpath.split(",");
                appMenu.setLevel(str.length);
            }
            if ("0".equals(appMenu.getParentId())) {
                appMenu.setLevelPath("0,");
                appMenu.setLevel(1);
            }

            if (StringUtils.isEmpty(appMenu.getId())) {
                //新增
                this.save(appMenu);
                result.setSuccess(true);
                result.setMsg("保存成功");
                result.setCode(Constants.SUCCESS);
                result.setData("");
            } else {
                int ret = this.update(appMenu);
                result.setSuccess(true);
                result.setMsg("修改成功");
                result.setCode(Constants.SUCCESS);
                result.setData("");
            }
            // 更新缓存
            this.updateCache();
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("操作失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 查询用户menus
     *
     * @return List<AppMenuTree>
     */
    @Override
    public List<AppMenuTree> findUserMenus() {
        List<AppMenuTree> retUserMenus=new ArrayList<>();
        //用户menus
        List<AppMenuTree> userMenus= this.findChildMenus(PARENT_ID,AppMenu.DISPLAY1,AppMenu.MENU1);
        for (AppMenuTree appMenu : userMenus) {
          AppMenuTree appMenuTree=  findOauthMenus(appMenu);
          if(appMenuTree!=null) {
              retUserMenus.add(appMenuTree);
          }
        }
        return retUserMenus;
    }

    /**
     * 递归获取树路径
     * @param mparentid
     * @param parpath
     * @return
     */
    protected String getparents(String mparentid, String parpath) {//递归方法，循环调用
        String parentid =this.findParentId(mparentid);
        if ("0".equals(parentid)) {
            return "0," + mparentid + "," + parpath;
        } else {
            parpath = mparentid + "," + parpath;
            return (getparents(parentid, parpath));
        }
    }

    /**
     * 递归查询
     * <p> TODO</p>
     * @author: Bat Admin
     * @param:    @param appMenu
     * @return: void
     * @Date :          2016年5月26日 下午2:51:55
     * @throws:
     */
    public void recursiveFindJsTree(JsTree appMenu) {

        List<JsTree> list = this.findJsTreeChildList(appMenu.getId(),null);
        if (list != null && list.size() > 0) {
            appMenu.setChildren(list);
            for (JsTree item : list) {
                recursiveFindJsTree(item);
            }
        }
    }

    /**
     * 递归查询
     * <p> TODO</p>
     * @author: Bat Admin
     * @param:    @param appMenu
     * @return: void
     * @Date :          2016年5月26日 下午2:51:55
     * @throws:
     */
    private void recursivefind(AppMenuVo appMenu) {

        List<AppMenuVo> list = this.findChildList(appMenu.getId(),null);
        if (list != null && list.size() > 0) {
            appMenu.setChildren(list);
            for (AppMenuVo item : list) {
                recursivefind(item);
            }
        }
    }

    /**
     * 递归查询并且校验权限
     * <p> TODO</p>
     * @author: Bat Admin
     * @param:    @param appMenu
     * @return: void
     * @Date :          2016年5月26日 下午2:51:55
     * @throws:
     */
    private AppMenuTree findOauthMenus(AppMenuTree appMenu) {
        if(hasOauth(appMenu)) {
            List<AppMenuTree> list = this.findChildMenus(appMenu.getId(), null,AppMenu.MENU1);
            if (list != null && list.size() > 0) {

                // 保存有权限的menu
                List<AppMenuTree> hasOauthList=new ArrayList<>();


                for (AppMenuTree item : list) {

                    AppMenuTree appMenuTree=findOauthMenus(item);

                    if (appMenuTree != null) {

                        hasOauthList.add(appMenuTree);
                    }
                }
                appMenu.setChildren(hasOauthList);
            }
            return appMenu;
        }

        return null;
    }

    /**
     * 是否具有权限
     * @param appMenu
     * @return
     */
    public boolean hasOauth(AppMenuTree appMenu){
        boolean hasOauth=false;
       //该用户对应角色的权限数据
        List<AppMenu> roleMenuList= UserUtils.getMenuList();
        for(AppMenu roleMenu:roleMenuList){
            if(roleMenu!=null){
                if(appMenu.getId().equals(roleMenu.getId())){
                    hasOauth=true;
                    break;
                }
            }
        }
        return hasOauth;
    }
}
