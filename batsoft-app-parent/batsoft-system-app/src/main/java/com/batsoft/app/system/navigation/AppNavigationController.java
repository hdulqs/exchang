/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-08-08 18:29:21
 */
package com.batsoft.app.system.navigation;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseTreeController;
import com.batsoft.model.module.system.navigation.AppNavigation;
import com.batsoft.service.module.system.service.navigation.AppNavigationService;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 前端导航管理
 * <p>AppNavigationController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-08-08 18:29:21
 */
@RestController("appNavigationController")
@RequestMapping("/system/navigation/appNavigation")
@Slf4j
public class AppNavigationController extends BaseTreeController<AppNavigation, String> {


    @Resource(name = "appNavigationService")
    @Override
    public void setService(BaseService<AppNavigation, String> service) {
        super.service = service;
    }

    /**
     * 查找
     *
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:navigation:appNavigation:see", "system:navigation:appNavigation:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }

    /**
     * 分页list
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:navigation:appNavigation:list")
    @ResponseBody
    public JsonResult list() {
        return super.listTree();
    }


    /**
     * 保存或修改
     *
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"system:navigation:appNavigation:add", "system:navigation:appNavigation:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult save(@RequestBody AppNavigation appNavigation) {
        JsonResult result = new JsonResult();
        String mpath = "";
        String[] str;
        try {
            if (!"0".equals(appNavigation.getParentId()) && !"".equals(appNavigation.getParentId() + "")) {
                String mpaths = "";
                mpath = getparents(appNavigation.getParentId(), mpaths);
                appNavigation.setPath(mpath);
                str = mpath.split(",");
                log.debug("str长度:" + str.length);
                appNavigation.setLevel(str.length);
            }
            if ("0".equals(appNavigation.getParentId())) {
                appNavigation.setPath("0,");
                appNavigation.setLevel(1);
            }
            //敏感词过滤
            if (!beanValidatorForJson(appNavigation)) {
                this.baseResult.setMsg("参数校验错误！");
                return this.baseResult;
            }
            if (StringUtils.isEmpty(appNavigation.getId())) {
                //新增
                super.service.save(appNavigation);
                result.setSuccess(true);
                result.setMsg("保存成功");
                result.setCode(Constants.SUCCESS);
                result.setData("");
            } else {
                //appNavigation.setIsShow("1");
                //appNavigation.setLevel(1);
                //	appNavigation.setPath("2");
                //修改
                int ret = super.service.update(appNavigation);
                result.setSuccess(true);
                result.setMsg("修改成功");
                result.setCode(Constants.SUCCESS);
                result.setData("");
            }

            // 更新缓存
           ((AppNavigationService)super.service).updateCache();
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
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("system:navigation:appNavigation:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();

        List<String> data=new ArrayList<String>();
        try {
            for(String id:pksData.getIdsArr()) {
                super.service.delete(id);
                data.add(id);
            }
            result.setSuccess(true);
            result.setMsg("删除成功");
            result.setCode(Constants.SUCCESS);
            result.setData(data);

            // 更新缓存
            ((AppNavigationService)super.service).updateCache();
        } catch (Exception e) {
            log.info("删除失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("删除失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }


    /**
     * 权限树
     *
     * @return
     */
    @RequestMapping(value = "/treeList")
    @RequiresPermissions(value = {"system:manage:appRole:perm"}, logical = Logical.OR)
    @ResponseBody
    public String treeList() {
        return super.tree();
    }



}
