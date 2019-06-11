/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:19:47
 */
package com.batsoft.app.system.manage;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.system.manage.AppRole;
import com.batsoft.model.module.system.manage.AppRoleMenu;
import com.batsoft.model.module.system.manage.vo.RolePremData;
import com.batsoft.service.module.system.auth.UserUtils;
import com.batsoft.service.module.system.service.manage.AppRoleMenuService;
import com.batsoft.service.module.system.service.menu.AppMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>AppRoleController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:19:47
 */
@RestController("appRoleController")
@RequestMapping("/system/manage/appRole")
@Slf4j
public class AppRoleController extends BaseController<AppRole, String> {


    @Autowired
    private AppRoleMenuService appRoleMenuService;
    @Autowired
    private AppMenuService appMenuService;

    @Resource(name="appRoleService")
    @Override
    public void setService(BaseService<AppRole, String> service) {
        super.service = service;
    }


    /**
     * 查找
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:manage:appRole:see", "system:manage:appRole:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find( @PathVariable String id) {
        return super.find(id);
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:manage:appRole:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {

        return pageList(request);
    }


    /**
     * list
     * @param
     * @return
     */
    @RequestMapping(value = "/findRoles")
    @RequiresPermissions(value = {"system:manage:appRole:add", "system:manage:appRole:edit","system:manage:appUser:see", "system:manage:appUser:edit"}, logical = Logical.OR)
    @ResponseBody
    public List<AppRole> findRoles(HttpServletRequest request) {
        try {
            QueryFilter filter = new QueryFilter(AppRole.class, request);
            filter.addFilter("del=",0);
            filter.addFilter("available=",0);
            return super.service.find(filter);
        } catch (Exception e) {
            log.info("AppRoleController findRoles error：" + e.getMessage());
        }
        return null;
    }


    /**
     * 通过角色id 查menuid
     * @param
     * @return
     */
    @RequestMapping(value = "/findMenu/{roleId}",method = RequestMethod.GET)
    @RequiresPermissions("system:manage:appRole:perm")
    @ResponseBody
    public List<AppRoleMenu> findMenu(@PathVariable String roleId) {
        try {
            QueryFilter filter = new QueryFilter(AppRoleMenu.class);
            filter.addFilter("role_id=",roleId);
            return appRoleMenuService.find(filter);
        } catch (Exception e) {
            log.info("AppRoleController listPage error：" + e.getMessage());
            return null;
        }
    }


    /**
     * 编辑或修改
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"system:manage:appRole:add", "system:manage:appRole:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult save(@RequestBody AppRole appRole) {

        if(appRole.getId()!=null){
            appRole.setUpdateUserId(UserUtils.getUser().getId());
            appRole.setUpdateUser(UserUtils.getUser().getUserName());

            appRole.setCreateUserId(super.findById(appRole.getId()).getCreateUserId());
            appRole.setCreateUser(super.findById(appRole.getId()).getCreateUser());


        }else{
            appRole.setCreateUserId(UserUtils.getUser().getId());
            appRole.setCreateUser(UserUtils.getUser().getUserName());
            appRole.setUpdateUserId(UserUtils.getUser().getId());
            appRole.setUpdateUser(UserUtils.getUser().getUserName());
        }
        return super.save(appRole);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("system:manage:appRole:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData removeData) {
        return super.remove(removeData.getIdsArr());
    }
    /**
     * 权限设置
     * @param
     * @return
     */
    @RequestMapping(value = "/savePerm", method = RequestMethod.POST)
    @RequiresPermissions("system:manage:appRole:perm")
    @ResponseBody
    public JsonResult savePerm(@RequestBody RolePremData rolePremData) {
        JsonResult result = new JsonResult();
        List
                <String> data = new ArrayList
                <String>();

        try {
            appRoleMenuService.removeRoleMenu(rolePremData.getRoleId());
            for (String id : rolePremData.getPermsArr()) {
                AppRoleMenu appRoleMenu=new AppRoleMenu();
                appRoleMenu.setRoleId(rolePremData.getRoleId());
                appRoleMenu.setMenuId(id);
                appRoleMenuService.save(appRoleMenu);
                data.add(id);
            }

            result.setSuccess(true);
            result.setMsg("权限设置成功");
            result.setCode(Constants.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            log.info("权限设置失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("权限设置失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }




}
