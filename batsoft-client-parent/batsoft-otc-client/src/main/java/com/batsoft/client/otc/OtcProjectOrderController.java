/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-11-28 16:42:32
 */
package com.batsoft.client.otc;


import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.otc.ProjectOrder;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.otc.service.ProjectOrderService;
import com.batsoft.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 *
 * <p>ProjectOrderController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-11-28 16:42:32
 */
@Api(description = "订单Controller")
@Controller("projectOrderController")
@RequestMapping("/otc/order")

@Slf4j
public class OtcProjectOrderController extends BaseController<ProjectOrder, String> {


    @Resource(name = "projectOrderService")
    @Override
    public void setService(BaseService<ProjectOrder, String> service) {
        super.service = service;
    }



    /**
     * 查找
     * @param
     * @return
     */
    @ApiOperation(value = "查找订单",notes = "根据id查找")
    @ApiImplicitParam(value = "订单id",name = "id",paramType = "query",dataType = "String")
    @Override
    @RequestMapping(value = "/user/find",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult find(@RequestParam String id) {
        return super.find(id);
    }

    /**
     * 分页list
     * @return
     */
    @ApiOperation(value = "用户订单列表")
    @RequestMapping(value = "/user/listData",method = RequestMethod.GET)
    @ResponseBody
    public PageResult listData(HttpServletRequest request) {
        return ((ProjectOrderService) super.service).findList(request);
    }

    /**
     * 用户订单list
     *
     * @return
     */
    @RequestMapping(value = "/user/list",method = RequestMethod.GET)
    public String list () {
        return "otc/user_order_list";
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @ApiOperation(value ="保存订单" )
    @ApiImplicitParam(value = "工程对象",name = "projectOrder",paramType = "body",dataType = "ProjectOrder")
    @Override
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult save(@ModelAttribute ProjectOrder projectOrder) {
        projectOrder.setUserId(UserUtils.getUser().getId());
        projectOrder.setUserName(UserUtils.getUser().getUserName());
        projectOrder.setOrderNum(StringUtils.createOrderNum());
        projectOrder.setOrderFee(new BigDecimal("0.00"));
        projectOrder.setOrderRemarkNum(StringUtils.createRandom(true, 6));
        JsonResult jsonResult=super.save(projectOrder);
        return jsonResult;
    }

}
