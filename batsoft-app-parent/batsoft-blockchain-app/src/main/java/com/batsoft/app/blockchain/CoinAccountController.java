/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-22 14:22:17
 */
package com.batsoft.app.blockchain;


import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.blockchain.CoinAccount;
import com.batsoft.service.module.blockchain.service.CoinAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>CoinAccountController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-22 14:22:17
 */
@RestController("coinAccountController")
@RequestMapping("/blockchain/coinAccount")

@Slf4j
public class CoinAccountController extends BaseController<CoinAccount, String> {


    @Resource(name = "coinAccountService")
    @Override
    public void setService(BaseService<CoinAccount, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"blockchain:coinAccount:see", "blockchain:coinAccount:edit"}, logical = Logical.OR)
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
    @RequiresPermissions("blockchain:coinAccount:list")

    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"blockchain:coinAccount:add", "blockchain:coinAccount:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody CoinAccount coinAccount) {
        return super.save(coinAccount);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("blockchain:coinAccount:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

    /**
     * 批量生成币地址
     * @param
     * @return
     */
    @RequestMapping(value = "/createAddress", method = RequestMethod.POST)
    @RequiresPermissions(value = {"blockchain:coinAccount:createAddress"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult createAddress(@RequestBody JSONObject object) {

        String coinCode=object.get("coinCode").toString();
        int count=Integer.valueOf(object.get("count").toString());
        return ((CoinAccountService)super.service).saveCoinAddress(coinCode,count);
    }

    /**
     * 可用地址数量
     * @param
     * @return
     */
    @RequestMapping(value = "/effectiveAddress", method = RequestMethod.POST)
    @RequiresPermissions(value = "blockchain:coinAccount:effectiveAddress")
    @ResponseBody
    public JsonResult effectiveAddress() {
        return ((CoinAccountService)super.service).findAddressByEffective();
    }

}
