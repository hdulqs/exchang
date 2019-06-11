/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-28 10:34:53
 */
package com.batsoft.app.cms;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.cms.service.SingleService;
import com.batsoft.model.module.cms.Single;
import com.batsoft.service.module.cms.service.SingleService;


import com.batsoft.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.batsoft.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
 *
 * <p>SingleController</p>
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-28 10:34:53
 */
@Api(description = "链接管理")
@RestController("singleController")
@RequestMapping("/cms/single")

@Slf4j
public class SingleController extends BaseController<Single, String> {


    @Resource(name = "singleService")
    @Override
    public void setService(BaseService<Single, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @ApiOperation(value = "查找链接")
    @ApiImplicitParam(value = "链接id",name = "id",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/find/{id}",method = RequestMethod.GET)
    @RequiresPermissions(value = {"cms:single:see", "cms:single:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @ApiOperation(value = "查看链接列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("cms:single:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @ApiOperation(value = "保存链接列表")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"cms:single:add", "cms:single:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody Single single) {
        JsonResult jsonResult = super.save(single);
        ((SingleService)super.service).singleAll();
        return jsonResult;
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @ApiOperation(value = "删除链接",notes = "在PksData的对象的idsArr里面存放删除的链接的id")
    @ApiImplicitParam(value = "删除列表",name = "pksData",paramType = "body",dataType = "PksData")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("cms:single:remove")
    @ResponseBody
    public JsonResult remove(@ModelAttribute PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

}
