/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-11-19 12:42:46
 */
package com.batsoft.app.otc;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.otc.Releaseproject;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>ReleaseprojectController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-11-19 12:42:46
 */
@Controller("releaseprojectController")
@RequestMapping("/otc/releaseproject")

@Slf4j
public class ReleaseprojectController extends BaseController<Releaseproject, String> {


    @Resource(name = "releaseprojectService")
    @Override
    public void setService(BaseService<Releaseproject, String> service) {
        super.service = service;
    }

    @RequestMapping("/view/add")
    @RequiresPermissions("otc:releaseproject:add")
    public String viewAdd(Model model) {
        return "otc/releaseproject/add";

    }

    @RequestMapping("/view/edit")
    @RequiresPermissions("otc:releaseproject:edit")

    public String viewEdit(Model model) {
        return "otc/releaseproject/edit";

    }

    @RequestMapping("/view/see")
    @RequiresPermissions("otc:releaseproject:see")

    public String viewSee(Model model) {
        return "otc/releaseproject/see";

    }


    @RequestMapping("/view/list")
    @RequiresPermissions("otc:releaseproject:list")

    public String viewList(Model model) {
        return "otc/releaseproject/list";

    }

    /**
     * 查找
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find")
    @RequiresPermissions(value = {"otc:releaseproject:see", "otc:releaseproject:edit"}, logical = Logical.OR)

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
    @RequiresPermissions("otc:releaseproject:list")

    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }


    /**
     * 保存或修改
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"otc:releaseproject:add", "otc:releaseproject:edit"}, logical = Logical.OR)

    @ResponseBody
    public JsonResult save(@ModelAttribute Releaseproject releaseproject) {
        if(StringUtils.isEmpty(releaseproject.getProjectNum())) {
            releaseproject.setProjectNum(StringUtils.createRandom(false, 10));
        }
        return super.save(releaseproject);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("otc:releaseproject:remove")

    @ResponseBody
    public JsonResult remove(@RequestParam(value = "ids[]") String[] ids) {
        return super.remove(ids);
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @RequiresPermissions("otc:releaseproject:check")
    @ResponseBody
    public JsonResult check(@RequestParam(value = "ids[]") String[] ids) {
        JsonResult result = new JsonResult();
        List<String> data = new ArrayList<String>();
        Releaseproject releaseproject =null;
        try {
            for (String PK : ids) {
                releaseproject=service.get(PK);
                releaseproject.setStatus(Releaseproject.SECURITY1);
                service.update(releaseproject);
            }
            result.setSuccess(true);
            result.setMsg("审核成功");
            result.setCode(Constants.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            log.info("审核成功：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("审核成功");
            result.setCode(Constants.FAILED);
        }
        return result;
    }
}
