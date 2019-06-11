/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-17 18:53:03
 */
package com.batsoft.app.member.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.utils.StringUtils;
import com.google.gson.JsonObject;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.batsoft.app.member.service.UserBusService;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.member.service.UserService;

import java.util.HashMap;

/**
 *
 * <p>UserController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-17 18:53:03
 */
@RestController("userController")
@RequestMapping("/member/user")
public class UserController extends BaseController<User, String> {

	@Resource
	private UserBusService userBusService;


    @Resource(name = "userService")
    @Override
    public void setService(BaseService<User, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"member:user:see", "member:user:edit"}, logical = Logical.OR)
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
    @RequiresPermissions("member:user:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        int page = 1;
        int pageSize = 10;
        String pageStr = request.getParameter("page");
        if(pageStr != null){
            page = Integer.valueOf(pageStr);
        }
        String pageSizeStr = request.getParameter("pageSize");
        if(pageSizeStr != null){
            pageSize = Integer.valueOf(pageSize);
        }

        String usernName = request.getParameter("userName_LK");
        String realName = request.getParameter("realName_LK");
        String mobile = request.getParameter("userMobile_LK");
        String userCardType = request.getParameter("userCardType_EQ");
        String userCardNumber = request.getParameter("userCardNumber_LK");

        HashMap<String,Object> map = new HashMap();
        if(!StringUtils.isNull(usernName)) {
            map.put("userName", usernName);
        }
        if(!StringUtils.isNull(realName)) {
            map.put("realName", realName);
        }
        if(!StringUtils.isNull(mobile)) {
            map.put("mobile", mobile);
        }
        if(!StringUtils.isNull(userCardType)) {
            map.put("userCardType", Integer.valueOf(userCardType));
        }
        if(!StringUtils.isNull(userCardNumber)) {
            map.put("userCardNumber", userCardNumber);
        }

        return  userBusService.findPageBySql(page,pageSize,map);
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"member:user:add", "member:user:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody User user) {
        return super.save(user);
    }

    /**
     * 删除操作
     * @param
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("member:user:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) throws InterruptedException {
    	String[] userIds = pksData.getIdsArr();
    	for(String userId : userIds) {
    		// 移除委托中的订单
    		userBusService.removeEntrustIng(userId);

    		// 移除用户账户
    		userBusService.removeCustomerAccount(userId);

    		// 移除银行卡
    		userBusService.removeUserBankcard(userId);
    	}
        return super.remove(userIds);
    }

    /**
     * 实名审核
     * 注册人数
     * 审核注册用户
     * @param
     * @return
     */
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @RequiresPermissions("member:user:review")
    @ResponseBody
    public JsonResult review(@RequestBody PksData pksData) {
        if(pksData.getIdsArr().length == 1){
            JSONObject jsonObject = JSONObject.parseObject(pksData.getData());
            if(jsonObject != null && jsonObject.get("agree") != null ){
                return ((UserService)super.service).updateRealState(pksData.getIdsArr(),jsonObject.getString("agree"),jsonObject.getString("msg"));
            }
            return ((UserService)super.service).updateRealState(pksData.getIdsArr(),"true","");
        }else{
            if(pksData.getIdsArr().length>0) {
                return ((UserService) super.service).updateRealState(pksData.getIdsArr(), "true", "");
            }else {
                return new JsonResult(false,Language.L("msg_system_params_error"));
            }
        }
    }

    /**
     * 注册人数
     * @param
     * @return
     */
    @RequestMapping(value = "/registerNum", method = RequestMethod.POST)
    @RequiresPermissions("member:user:registerNum")
    @ResponseBody
    public JsonResult registerNum() {
        return ((UserService)super.service).findUserNum();
    }
    /**
     * 未审核用户数量
     * @param
     * @return
     */
    @RequestMapping(value = "/unauditedUserNum", method = RequestMethod.POST)
    @RequiresPermissions("member:user:unauditedUserNum")
    @ResponseBody
    public JsonResult unauditedUserNum() {
        return ((UserService)super.service).findUnauditedUserNum();
    }
    
}





