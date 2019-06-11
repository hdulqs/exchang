/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-07 09:21:46
 */
package com.batsoft.app.exchange;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.exchange.service.C2cUserService;
import com.batsoft.model.module.exchange.C2cUser;
import com.batsoft.service.module.exchange.service.C2cUserService;


import com.batsoft.service.module.system.service.type.AppDictionaryService;
import com.batsoft.utils.StringUtils;
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
 * <p>C2cUserController</p>
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-07 09:21:46
 */
@RestController("c2cUserController")
@RequestMapping("/exchange/c2cUser")

@Slf4j
public class C2cUserController extends BaseController<C2cUser, String> {


    @Resource(name = "c2cUserService")
    @Override
    public void setService(BaseService<C2cUser, String> service) {
        super.service = service;
    }

    @Resource
    private AppDictionaryService appDictionaryService;

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:c2cUser:see", "exchange:c2cUser:edit"}, logical = Logical.OR)
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
    @RequestMapping(value = "/list")
    @RequiresPermissions("exchange:c2cUser:list")

    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }

    /**
     * 权限树
     * @return
     */
    @RequestMapping(value = "/treeList")
    @RequiresPermissions(value = {"exchange:c2cUser:add", "exchange:c2cUser:edit"}, logical = Logical.OR)
    @ResponseBody
    public String treeList() {
        return appDictionaryService.findTreeJson();
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"exchange:c2cUser:add", "exchange:c2cUser:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody C2cUser c2cUser) {
        JsonResult jsonResult = super.save(c2cUser);
        //更新redis缓存
        ((C2cUserService) this.service).updateC2cJson();
        return jsonResult;
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:c2cUser:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        JsonResult jsonResult = super.remove(pksData.getIdsArr());
        ((C2cUserService) this.service).updateC2cJson();
        return jsonResult;
    }

}
