/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Lucl
 * @version: V1.0
 * @Date: 2017-08-22 14:13:21
 */
package com.batsoft.app.ico;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.ico.Project;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>ProjectController</p>
 * @author: Lucl
 * @version: V1.0
 * @Date: 2017-08-22 14:13:21
 */
@Controller("projectController")
@RequestMapping("/ico/project")

@Slf4j
public class ProjectController extends BaseController<Project, String> {


    @Resource(name = "projectService")
    @Override
    public void setService(BaseService<Project, String> service) {
        super.service = service;
    }

    @RequestMapping("/view/add")
    @RequiresPermissions("otc:project:add")
    public String viewAdd(Model model) {
        return "otc/project/add";

    }

    @RequestMapping("/view/edit")
    @RequiresPermissions("otc:project:edit")

    public String viewEdit(Model model) {
        return "otc/project/edit";

    }

    @RequestMapping("/view/see")
    @RequiresPermissions("otc:project:see")

    public String viewSee(Model model) {
        return "otc/project/see";

    }


    @RequestMapping("/view/list")
    @RequiresPermissions("otc:project:list")

    public String viewList(Model model) {
        return "otc/project/list";

    }

    /**
     * 查找
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find")
    @RequiresPermissions(value = {"otc:project:see", "otc:project:edit"}, logical = Logical.OR)

    @ResponseBody
    public JsonResult find(@RequestParam String id) {
        return super.find(id);
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("otc:project:list")

    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }


    /**
     * 保存或修改33
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"otc:project:add", "otc:project:edit"}, logical = Logical.OR)

    @ResponseBody
    public JsonResult save(@ModelAttribute Project project) {
        return super.save(project);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("otc:project:remove")

    @ResponseBody
    public JsonResult remove(@RequestParam(value = "ids[]") String[] ids) {
        return super.remove(ids);
    }

    /**
     * 审核
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping(value = "/updateStatus")
    @RequiresPermissions("otc:project:updateStatus")
    @ResponseBody
    public JsonResult updateStatus(@RequestParam(value = "ids[]") String[] ids ,@RequestParam String status){
        JsonResult result = new JsonResult();
        try {
            if(!StringUtils.isNull(ids)){
                for(String id:ids){
                    Project project = findById(id);
                    if(project!=null){
                        project.setStatus(Integer.valueOf(status));
                        this.service.update(project);
                    }
                }
            }
            result.setSuccess(true);
            result.setMsg("审核完成");
            result.setCode(Constants.SUCCESS);
            result.setData("");
        } catch (Exception e) {
            log.info("审核失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("审核失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }


}
