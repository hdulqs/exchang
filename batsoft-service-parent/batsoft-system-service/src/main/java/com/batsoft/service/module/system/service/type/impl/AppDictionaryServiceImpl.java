/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-05 23:23:40 
*/
package com.batsoft.service.module.system.service.type.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.JsTree;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.system.type.AppDictionary;
import com.batsoft.model.module.system.type.vo.AppDictionaryVo;
import com.batsoft.service.module.system.dao.type.AppDictionaryDao;
import com.batsoft.service.module.system.service.type.AppDictionaryService;
import com.batsoft.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* <p> AppDictionaryServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-05 23:23:40 
*/
@Service("appDictionaryService")
public class AppDictionaryServiceImpl extends BaseServiceImpl<AppDictionary, String> implements AppDictionaryService{

@Autowired
private AppDictionaryDao appDictionaryDao;


    @Autowired
    private RedisService redisService;

    public static final String APPDICTIONARY_TREE = "appdic_tree";

    public  static final String PARENT_ID="0";


    /* (non-Javadoc)
     * @see com.batsoft.service.module.system.service.menu.appDictionaryService#findChildList(java.lang.String)
     */
    @Override
    public List<AppDictionaryVo> findChildList(String id, Integer display) {
        // TODO Auto-generated method stub
        return appDictionaryDao.findChildList(id,display);
    }

    /* (non-Javadoc)
     * @see com.batsoft.service.module.system.service.menu.appDictionaryService#findListTree(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public List<AppDictionaryVo> findListTree() {
        List<AppDictionaryVo> find = findChildList("0",null);
        for (AppDictionaryVo appDictionary : find) {
            recursivefind(appDictionary);
        }
        return find;
    }

    @Override
    public List<JsTree> findJsTree(String pid) {
        JsTree root = new JsTree();
        root.setId(pid);
        root.setText(this.get(pid).getName());
        root.setValue(pid);
        root.setKey(pid);
        root.setLabel(this.get(pid).getName());

        List<JsTree> tree = new ArrayList<>();

        List<JsTree> find = findJsTreeChildList(pid,null);
        for (JsTree appDictionary : find) {
            recursiveFindJsTree(appDictionary);
        }
        root.setChildren(find);

        tree.add(root);

        return tree;
    }


    @Override
    public String findTreeJson() {
        String ret = redisService.get(APPDICTIONARY_TREE);
        if (StringUtils.isEmpty(ret)) {
            ret = JSON.toJSONString(findJsTree("0"));
            redisService.set(APPDICTIONARY_TREE, ret, 0);
        }
        return ret;
    }



    @Override
    public List<JsTree> findJsTreeChildList(String id,Integer display) {
        return appDictionaryDao.findJsTreeChildList(id,display);
    }



    @Override
    public String findParentId(String pid) {
        return appDictionaryDao.findParentId(pid);
    }

    @Override
    public String findParentName(String id) {
        return appDictionaryDao.findParentName(id);
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
     * @param:    @param appDictionary
     * @return: void
     * @Date :          2016年5月26日 下午2:51:55
     * @throws:
     */
    public void recursiveFindJsTree(JsTree appDictionary) {

        List<JsTree> list = this.findJsTreeChildList(appDictionary.getId(),null);
        if (list != null && list.size() > 0) {
            appDictionary.setChildren(list);
            for (JsTree item : list) {
                recursiveFindJsTree(item);
            }
        }
    }

    /**
     * 递归查询
     * <p> TODO</p>
     * @author: Bat Admin
     * @param:    @param appDictionary
     * @return: void
     * @Date :          2016年5月26日 下午2:51:55
     * @throws:
     */
    private void recursivefind(AppDictionaryVo appDictionary) {

        List<AppDictionaryVo> list = this.findChildList(appDictionary.getId(),null);
        if (list != null && list.size() > 0) {
            appDictionary.setChildren(list);
            for (AppDictionaryVo item : list) {
                recursivefind(item);
            }
        }
    }


    @Override
    public Object findByKey(String dicKey) {
        return appDictionaryDao.findByKey(dicKey);
    }

    @Override
    public void updateCache() {
        String ret = JSON.toJSONString(findJsTree("0"));
        redisService.set(APPDICTIONARY_TREE, ret, 0);
    }

    @Override
    public List<JsTree> findAppDictionByKey(String key) {
        AppDictionary appDictionary=(AppDictionary)findByKey(key);

        return  findJsTree(appDictionary.getId());
    }
}
