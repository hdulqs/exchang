/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:20:38
 */
package com.batsoft.app.exchange;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.vo.DepostPkData;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.member.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>CustomerAccountController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:20:38
 */
@RestController("customerAccountController")
@RequestMapping("/exchange/customerAccount")
@Slf4j
public class CustomerAccountController extends BaseController<CustomerAccount, String> {
	
	private JedisDataSourceSignleton jedisClent = JedisDataSourceSignleton.getInstance();
	
    @Autowired
    private UserService userService;
    @Resource(name = "customerAccountService")
    @Override
    public void setService(BaseService<CustomerAccount, String> service) {
        super.service = service;
    }

    /**
     * 查找
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:customerAccount:see", "exchange:customerAccount:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
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
    @RequiresPermissions("exchange:customerAccount:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }




    /**
     * 保存或修改
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"exchange:customerAccount:add", "exchange:customerAccount:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody CustomerAccount customerAccount) {

        return super.save(customerAccount);
    }

    /**
     * 删除操作
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:customerAccount:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

    /**
     * 充值方法
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/depost", method = RequestMethod.POST)
    @RequiresPermissions("exchange:customerAccount:depost")
    @ResponseBody
    public JsonResult depost(@RequestBody DepostPkData data) {
        //TODO
        return ((CustomerAccountService) super.service).saveDepostData(data);
    }


    /**
     * 检查用户账户
     *
     * @return
     */
    @RequestMapping(value = "/checkAccount/{id}", method = RequestMethod.GET)
    @RequiresPermissions("exchange:customerAccount:checkAccout")
    @ResponseBody
    public JsonResult checkAccout(@PathVariable String id) {
        JsonResult result = new JsonResult();

        result = ((CustomerAccountService) service).findCheckAccout(id);
        return result;

    }

    /**
     * 用户list
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/userList")
    @RequiresPermissions("exchange:customerAccount:userList")
    @ResponseBody
    public PageResult userList(HttpServletRequest request) {

        try {
            QueryFilter filter = new QueryFilter(User.class, request);
            return userService.findPage(filter);
        } catch (Exception e) {
            log.info(this.getClass().getName() + " pageList error：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
}
