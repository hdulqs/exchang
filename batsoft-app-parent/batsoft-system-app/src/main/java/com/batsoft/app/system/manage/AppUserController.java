/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:18:59
 */
package com.batsoft.app.system.manage;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.system.manage.AppUser;
import com.batsoft.model.module.system.manage.AppUserRole;
import com.batsoft.model.module.system.manage.vo.AppUserVo;
import com.batsoft.service.module.system.auth.UserUtils;
import com.batsoft.service.module.system.service.manage.AppRoleService;
import com.batsoft.service.module.system.service.manage.AppUserRoleService;
import com.batsoft.service.module.system.service.manage.AppUserService;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * <p>AppUserController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:18:59
 */
@RestController("appUserController")
@RequestMapping("/system/manage/appUser")
@Slf4j
public class AppUserController extends BaseController<AppUser, String> {


    @Resource(name="appUserService")
    @Override
    public void setService(BaseService<AppUser, String> service) {
        super.service = service;
    }
    @Autowired
    private AppRoleService appRoleService;
    @Autowired
    AppUserRoleService appUserRoleService;

    @RequestMapping(value = "/current")
    @ResponseBody
    public JsonResult findCurrent() {
        JsonResult jsonResult=new JsonResult();
        try {
            AppUser appUser = UserUtils.getUser();
            AppUserVo appUserVo = new AppUserVo();
            BeanUtils.copyProperties(appUser, appUserVo);
            jsonResult.setData(appUserVo);
            jsonResult.setSuccess(true);
            jsonResult.setMsg("加载成功");
            jsonResult.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            log.info("AppUserController listPage error：" + e.getMessage());
            jsonResult.setSuccess(false);
            jsonResult.setMsg("加载失败,error:" + e.getMessage());
            jsonResult.setCode(Constants.FAILED);
        }
        return jsonResult;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:manage:appUser:see", "system:manage:appUser:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id) {
        JsonResult jsonResult=new JsonResult();
        try {
            AppUser appUser = super.service.get(id);
            AppUserVo appUserVo = new AppUserVo();
            BeanUtils.copyProperties(appUser, appUserVo);
            ArrayList roleIds=new ArrayList();


            appUserVo.setRoles(((AppUserService)super.service).findUserRoles(id));

            jsonResult.setData(appUserVo);
            jsonResult.setSuccess(true);
            jsonResult.setMsg("加载成功");
            jsonResult.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            log.info("AppUserController listPage error：" + e.getMessage());
            jsonResult.setSuccess(false);
            jsonResult.setMsg("加载失败,error:" + e.getMessage());
            jsonResult.setCode(Constants.FAILED);
        }
        return jsonResult;
    }
    /**
     * 分页list
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:manage:appUser:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return pageList(request);
    }


    /**
     * 编辑或修改
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"system:manage:appUser:add", "system:manage:appUser:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult save(@RequestBody AppUserVo appUserVo) {
        JsonResult result = new JsonResult();
        AppUser appUser = new AppUser();
        //复制对象
        BeanUtils.copyProperties(appUserVo, appUser);
        // 如果是修改操作 必须将数据库中的密码重新赋值 否则下边的校验过不去
        if (!StringUtils.isEmpty(appUser.getId())) {
            AppUser user = super.service.get(appUser.getId());
            appUser.setPassword(user.getPassword());
        }

        try {

            //敏感词过滤 格式校验
            if (!beanValidatorForJson(appUser)) {
                return this.baseResult;
            }

            if (StringUtils.isEmpty(appUser.getId())) {
                //新增
                //处理密码
                PasswordHelper passwordHelper = new PasswordHelper();
                PasswordHelper.PasswordInfo passwordInfo = passwordHelper.encryptPassword(appUser.getPassword());
                appUser.setPassword(passwordInfo.getPassword());
                appUser.setSalt(passwordInfo.getSalt());

                super.service.save(appUser);
                result.setSuccess(true);
                result.setMsg("保存成功！");
                result.setCode(Constants.SUCCESS);
            } else {
                //修改
                int ret = super.service.update(appUser);
                result.setSuccess(true);
                result.setMsg("修改成功！");
                result.setCode(Constants.SUCCESS);
            }


            if (appUserVo.getRolesIds().length > 0) {
                // 删除原有角色
                if (!StringUtils.isEmpty(appUser.getId())) {
                    QueryFilter filter = new QueryFilter(AppUserRole.class);
                    filter.addFilter("user_id=", appUser.getId());
                    appUserRoleService.delete(filter);
                }

                // 保存角色

                for (String id : appUserVo.getRolesIds()) {
                    AppUserRole appUserRole = new AppUserRole();
                    appUserRole.setUserId(appUser.getId());
                    appUserRole.setRoleId(id);
                    appUserRoleService.save(appUserRole);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导航添加/修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("操作失败！");
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
    @RequiresPermissions("system:manage:appUser:remove")
    @ResponseBody
    public JsonResult  remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }


}
