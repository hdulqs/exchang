/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-05 23:23:40
 */
package com.batsoft.app.system.type;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsTree;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseTreeController;
import com.batsoft.model.module.system.type.AppDictionary;
import com.batsoft.service.module.system.service.type.AppDictionaryService;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * <p>AppDictionaryController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-05 23:23:40
 */
@RestController("appDictionaryController")
@RequestMapping("/system/type/appDictionary")

@Slf4j
public class AppDictionaryController extends BaseTreeController<AppDictionary, String> {


    @Resource(name = "appDictionaryService")
    @Override
    public void setService(BaseService<AppDictionary, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:type:appDictionary:see", "system:type:appDictionary:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }

    /**
     * 权限树
     * @return
     */
    @RequestMapping(value = "/treeList")
    @RequiresPermissions(value = {"system:type:appDictionary:add", "system:type:appDictionary:edit"}, logical = Logical.OR)
    @ResponseBody
    public String treeList() {
        return super.tree();
    }

    /**
     * 通过Key 获取字典
     * @return
     */
    @RequestMapping(value = "/findByKey/{key}")
   // @RequiresPermissions(value = {"system:type:appDictionary:add", "system:type:appDictionary:edit"}, logical = Logical.OR)
    @ResponseBody
    public List<JsTree> findByKey(@PathVariable String key) {
        List<JsTree> list=((AppDictionaryService)super.service).findAppDictionByKey(key);
        return list;
    }

    /**
     * 列表树
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:type:appDictionary:list")
    @ResponseBody
    public JsonResult list() {
        return super.listTree();
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"system:type:appDictionary:add", "system:type:appDictionary:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody AppDictionary appDictionary) {
        JsonResult result = new JsonResult();
        String mpath = "";
        String[] str;
        try {
            if (!"0".equals(appDictionary.getParentId()) && !"".equals(appDictionary.getParentId() + "")) {
                String mpaths = "";
                mpath = getparents(appDictionary.getParentId(), mpaths);
                appDictionary.setLevelPath(mpath);
                str = mpath.split(",");
                log.debug("str长度:" + str.length);
                appDictionary.setLevel(str.length);
            }
            if ("0".equals(appDictionary.getParentId())) {
                appDictionary.setLevelPath("0,");
                appDictionary.setLevel(1);
            }
            //敏感词过滤
            if (!beanValidatorForJson(appDictionary)) {
                this.baseResult.setMsg("参数校验错误！");
                return this.baseResult;
            }
            if (StringUtils.isEmpty(appDictionary.getId())) {
                if (null == ((AppDictionaryService) super.service).findByKey(appDictionary.getDicKey())) {
                    //新增
                    super.service.save(appDictionary);
                    result.setSuccess(true);
                    result.setMsg("保存成功");
                    result.setCode(Constants.SUCCESS);
                    result.setData("");
                } else {
                    result.setSuccess(false);
                    result.setMsg("菜单KEY已存在");
                    result.setCode(Constants.FAILED);
                }
            } else {
                //appDictionary.setIsShow("1");
                //appDictionary.setLevel(1);
                //	appDictionary.setLevelPath("2");
                //修改
                int ret = super.service.update(appDictionary);
                result.setSuccess(true);
                result.setMsg("修改成功");
                result.setCode(Constants.SUCCESS);
                result.setData("");
            }
// 更新缓存
            ((AppDictionaryService) super.service).updateCache();
        } catch (Exception e) {
            log.info("导航添加/修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("操作失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("system:type:appDictionary:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

}
