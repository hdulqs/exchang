/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-08-07 23:16:08 
*/
package com.batsoft.service.module.system.service.navigation.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsTree;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.system.navigation.AppNavigation;
import com.batsoft.model.module.system.navigation.vo.AppNavigationVo;
import com.batsoft.service.module.system.dao.navigation.AppNavigationDao;
import com.batsoft.service.module.system.service.navigation.AppNavigationService;
import com.batsoft.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* <p> AppNavigationServiceImpl </p>
* @author: 
* @Date :  2017-08-07 23:16:08 
*/
@Service("appNavigationService")
public class AppNavigationServiceImpl extends BaseServiceImpl<AppNavigation, String> implements AppNavigationService{

@Autowired
private AppNavigationDao appNavigationDao;

    @Autowired
    private RedisService redisService;



    public static final String CACHE_TREE = Constants.CACHE_CONFIG_KEY+"nav_tree";

    public static final String CACHE_WEB_NAV= Constants.CACHE_WEB_KEY+"nav";


    /* (non-Javadoc)
     * @see com.batsoft.service.module.system.service.menu.appNavigationService#findChildList(java.lang.String)
     */
    @Override
    public List<AppNavigationVo> findChildList(String id,Integer display) {
        // TODO Auto-generated method stub
        return appNavigationDao.findChildList(id,display);
    }

    /* (non-Javadoc)
     * @see com.batsoft.service.module.system.service.menu.appNavigationService#findListTree(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public List<AppNavigationVo> findListTree() {
        List<AppNavigationVo> find = findChildList("0",null);
        for (AppNavigationVo appNavigation : find) {
            recursivefind(appNavigation);
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
        for (JsTree appNavigation : find) {
            recursiveFindJsTree(appNavigation);
        }
        root.setChildren(find);

        tree.add(root);

        return tree;
    }

    @Override
    public String findTreeJson() {
        String ret = "";
        ret = redisService.get(CACHE_TREE);
        if (StringUtils.isEmpty(ret)) {
            ret = JSON.toJSONString(findJsTree("0"));
            redisService.set(CACHE_TREE, ret, 0);
        }
        return ret;
    }

    @Override
    public String findNav() {
        String ret = "";
        ret = redisService.get(CACHE_WEB_NAV);
        if (StringUtils.isEmpty(ret)) {
            List<JsTree> list = findJsTreeChildList("0",AppNavigation.DISPLAY1);
            for (JsTree appNavigation : list) {
                recursiveFindJsTree(appNavigation);
            }
            ret = JSON.toJSONString(list);
            redisService.set(CACHE_WEB_NAV, ret, 0);
        }
        return ret;
    }



    @Override
    public List<JsTree> findJsTreeChildList(String id,Integer display) {
        return appNavigationDao.findJsTreeChildList(id,display);
    }

    @Override
    public String findParentId(String pid) {
        return appNavigationDao.findParentId(pid);
    }

    @Override
    public String findParentName(String id) {
        return appNavigationDao.findParentName(id);
    }

    @Override
    public void updateCache() {
        String ret = JSON.toJSONString(findJsTree("0"));
        redisService.set(CACHE_TREE, ret, 0);
    }

    /**
     * 递归查询
     * <p> TODO</p>
     * @author: Bat Admin
     * @param:    @param appNavigation
     * @return: void
     * @Date :          2016年5月26日 下午2:51:55
     * @throws:
     */
    public void recursiveFindJsTree(JsTree appNavigation) {

        List<JsTree> list = this.findJsTreeChildList(appNavigation.getId(),null);
        if (list != null && list.size() > 0) {
            appNavigation.setChildren(list);
            for (JsTree item : list) {
                recursiveFindJsTree(item);
            }
        }
    }

    /**
     * 递归查询
     * <p> TODO</p>
     * @author: Bat Admin
     * @param:    @param appNavigation
     * @return: void
     * @Date :          2016年5月26日 下午2:51:55
     * @throws:
     */
    public void recursivefind(AppNavigationVo appNavigation) {

        List<AppNavigationVo> list = this.findChildList(appNavigation.getId(),null);
        if (list != null && list.size() > 0) {
            appNavigation.setChildren(list);
            for (AppNavigationVo item : list) {
                recursivefind(item);
            }
        }
    }

}
