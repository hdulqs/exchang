/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-06-03 10:51:01 
*/
package com.batsoft.app.blockchain;


import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.blockchain.DepositWallet;
import com.batsoft.service.module.blockchain.service.DepositWalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
/**
* 
* <p>DepositWalletController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-06-03 10:51:01 
*/
@RestController("depositWalletController")
@RequestMapping("/blockchain/depositWallet")

@Slf4j
public class DepositWalletController extends BaseController<DepositWallet,String> {


@Resource(name="depositWalletService")
@Override
public void setService(BaseService<DepositWallet, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"blockchain:depositWallet:see","blockchain:depositWallet:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult find(@PathVariable  String id) {
return super.find(id);
}

/**
* 分页list
* @param
* @return
*/
@RequestMapping(value = "/list")
@RequiresPermissions("blockchain:depositWallet:list")

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
@RequiresPermissions(value={"blockchain:depositWallet:add","blockchain:depositWallet:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody DepositWallet depositWallet) {
return super.save(depositWallet);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("blockchain:depositWallet:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

    /**
     * 生成提币地址
     * @param
     * @return
     */
    @RequestMapping(value = "/createAddress", method = RequestMethod.POST)
    @RequiresPermissions(value = {"blockchain:depositWallet:createAddress"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult createAddress(@RequestBody JSONObject object) {

        String coinCode=object.get("coinCode").toString();
        return ((DepositWalletService)super.service).saveCoinAddress(coinCode);
    }
}
