/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年2月8日 上午11:48:42
 */
package com.batsoft.app.system.appmenu;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseTreeController;
import com.batsoft.model.module.system.menu.AppMenu;
import com.batsoft.model.module.system.menu.vo.AppMenuTree;
import com.batsoft.model.module.system.menu.vo.AppMenuVo;
import com.batsoft.service.module.system.auth.UserUtils;
import com.batsoft.service.module.system.service.menu.AppMenuService;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * <p> TODO</p>
 *
 * @author: Bat Admin
 * @Date :          2017年2月8日 上午11:48:42
 */
@RestController("appMenuController")
@RequestMapping("/system/appmenu/appMenu")
@Slf4j
public class AppMenuController extends BaseTreeController<AppMenu, String> {

    @Resource(name="appMenuService")
    @Override
    public void setService(BaseService<AppMenu, String> service) {
     super.service=service;
    }


    /**
     * 查找
     *
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:manage:appMenu:see", "system:manage:appMenu:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }


    /**
     * 编辑或修改
     *
     * @param
     * @return
     */
    @Override
    @RequiresPermissions(value = {"system:manage:appMenu:add", "system:manage:appMenu:edit"}, logical = Logical.OR)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult save(@RequestBody AppMenu appMenu) {
        JsonResult result = new JsonResult();
        String mpath = "";
        String[] str;
        try {
            if (!"0".equals(appMenu.getParentId()) && !"".equals(appMenu.getParentId() + "")) {
                String mpaths = "";
                mpath = getparents(appMenu.getParentId(), mpaths);
                appMenu.setLevelPath(mpath);
                str = mpath.split(",");
                log.debug("str长度:" + str.length);
                appMenu.setLevel(str.length);
            }
            if ("0".equals(appMenu.getParentId())) {
                appMenu.setLevelPath("0,");
                appMenu.setLevel(1);
            }
            //敏感词过滤
            if (!beanValidatorForJson(appMenu)) {
                this.baseResult.setMsg("参数校验错误！");
                return this.baseResult;
            }
            if (StringUtils.isEmpty(appMenu.getId())) {
                if(null==((AppMenuService)super.service).findByKey(appMenu.getMenuKey())) {
                    //新增
                    super.service.save(appMenu);
                    result.setSuccess(true);
                    result.setMsg("保存成功");
                    result.setCode(Constants.SUCCESS);
                    result.setData("");
                }else{
                    result.setSuccess(false);
                    result.setMsg("菜单KEY已存在");
                    result.setCode(Constants.FAILED);
                }
            } else {
                //appMenu.setIsShow("1");
                //appMenu.setLevel(1);
                //	appMenu.setLevelPath("2");
                //修改
                int ret = super.service.update(appMenu);
                result.setSuccess(true);
                result.setMsg("修改成功");
                result.setCode(Constants.SUCCESS);
                result.setData("");
            }

            // 更新缓存
            ((AppMenuService)super.service).updateCache();
        } catch (Exception e) {
            log.info("导航添加/修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("操作失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }


    /**
     * 列表树
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:manage:appMenu:list")
    @ResponseBody
    public JsonResult list() {
        return super.listTree();
    }

    /**
     * 权限树
     * @return
     */
    @RequestMapping(value = "/treeList")
    @RequiresPermissions(value = {"system:manage:appRole:perm"}, logical = Logical.OR)
    @ResponseBody
    public String treeList() {
        return super.tree();
    }

    /**
     * 查询子列表
     *
     * @param id 父id
     * @return json
     */
    @RequestMapping("child")
    public
    @ResponseBody
    List<AppMenuVo> child(@RequestParam String id, @RequestParam int level) {
        //存入deep，配合ajax
        List<AppMenuVo> classList = ((AppMenuService)super.service).findChildList(id,AppMenu.DISPLAY1);
        List<AppMenu> roleMenuList= UserUtils.getMenuList();
        List<AppMenuVo> myMenuList=new ArrayList<>();
        if (!StringUtils.isNull(roleMenuList)) {
	            for(AppMenuVo vo : classList){
	                vo.setDeep(level);
	                for(AppMenu rolemenu:roleMenuList){
	                	if(rolemenu!=null){
		                	if(vo.getId().equals(rolemenu.getId())){
                                            myMenuList.add(vo);
		                	}
	                  }
	                }
	            }
        }
        return myMenuList;
    }

    /**
     * 查询权限
     * @return json
     */
    @RequestMapping("menu")
    public
    @ResponseBody
    List<AppMenuTree> menu() {
        return  ((AppMenuService)super.service).findUserMenus();
    }



    /**
     * 删除
     * @param
     * @return
     */
    @RequiresPermissions("system:manage:appMenu:remove")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
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
            ((AppMenuService)super.service).updateCache();
        } catch (Exception e) {
            log.info("删除失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("删除失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }



}
