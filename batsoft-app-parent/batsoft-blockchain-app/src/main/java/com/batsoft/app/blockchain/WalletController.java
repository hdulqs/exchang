/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-25 10:54:59
 */
package com.batsoft.app.blockchain;


import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.blockchain.Wallet;
import com.batsoft.service.module.blockchain.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>WalletController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-25 10:54:59
 */
@RestController("walletController")
@RequestMapping("/blockchain/wallet")

@Slf4j
public class WalletController extends BaseController<Wallet, String> {


    @Resource(name = "walletService")
    @Override
    public void setService(BaseService<Wallet, String> service) {
        super.service = service;
    }

    /**
     * 查找
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"blockchain:wallet:see", "blockchain:wallet:edit"}, logical = Logical.OR)
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
    @RequiresPermissions("blockchain:wallet:list")

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
    @RequiresPermissions(value = {"blockchain:wallet:add", "blockchain:wallet:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody Wallet wallet) {
        JsonResult jsonResult=new JsonResult();
        Long blockHigh = 0L;
        try {
            blockHigh=((WalletService)super.service).findBlockHigh(wallet);
            wallet.setBlockHigh(blockHigh);

        } catch (Exception e) {

        }finally {
            jsonResult=super.save(wallet);
        }
        return jsonResult;
    }

    /**
     * 删除操作
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("blockchain:wallet:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

}
