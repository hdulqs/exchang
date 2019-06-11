/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-25 16:15:19
 */
package com.batsoft.app.exchange;


import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.CoinWithdraw;
import com.batsoft.service.module.exchange.service.CoinWithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>CoinWithdrawController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-25 16:15:19
 */
@RestController("coinWithdrawController")
@RequestMapping("/exchange/coinWithdraw")

@Slf4j
public class CoinWithdrawController extends BaseController<CoinWithdraw, String> {


    @Resource(name = "coinWithdrawService")
    @Override
    public void setService(BaseService<CoinWithdraw, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:coinWithdraw:see", "exchange:coinWithdraw:edit"}, logical = Logical.OR)
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
    @RequiresPermissions("exchange:coinWithdraw:list")

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
    @RequiresPermissions(value = {"exchange:coinWithdraw:add", "exchange:coinWithdraw:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody CoinWithdraw coinWithdraw) {
        return super.save(coinWithdraw);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coinWithdraw:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

    /**
     * 提币审核通过
     * @param
     * @return
     */
    @RequestMapping(value = "/passStatus", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coinWithdraw:passStatus")
    @ResponseBody
    public JsonResult passStatus(@RequestBody PksData pksData) {
        return ((CoinWithdrawService) this.service).withdrawReview(pksData.getIdsArr(), CoinWithdraw.STATUS1);
    }


    /**
     * 提币拒绝
     * @param
     * @return
     */
    @RequestMapping(value = "/refuseStatus", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coinWithdraw:refuseStatus")
    @ResponseBody
    public JsonResult refuseStatus(@RequestBody PksData pksData) {
        return ((CoinWithdrawService) this.service).withdrawReview(pksData.getIdsArr(), CoinWithdraw.STATUS2);
    }

    /**
     * 查询未审核提币总量
     * @param
     * @return
     */
    @RequestMapping(value = "/getTotleMoneyByUnaudited")
//    @RequiresPermissions("exchange:coinWithdraw:getTotleMoneyByUnaudited")
    @ResponseBody
    public JsonResult getTotleMoneyByUnaudited() {
        return ((CoinWithdrawService) this.service).getTotleMoneyByUnaudited();
    }

    /**
     * 查询各币种提现总量
     * @param
     * @return
     */
    @RequestMapping(value = "/getTotleWithdrawsByCode")
//    @RequiresPermissions("exchange:coinWithdraw:getTotleWithdrawsByCode")
    @ResponseBody
    public JsonResult getTotleWithdrawsByCode() {
        return ((CoinWithdrawService) this.service).getTotleWithdrawsByCode(null);
    }

    /**
     * 查询当日各币种提现总额
     * @param
     * @return
     */
    @RequestMapping(value = "/getTotleWithdrawsByDay")
//    @RequiresPermissions("exchange:coinWithdraw:getTotleWithdrawsByDay")
    @ResponseBody
    public JsonResult getTotleWithdrawsByDay() {
        return ((CoinWithdrawService) this.service).getTotleWithdrawsByCode("day");
    }

    /**
     * 查询提币排行榜
     */
    @RequestMapping(value = "/getWithdrawsBySort")
    @ResponseBody
    public JsonResult getWithdrawsBySort(@RequestParam(value = "limit", required = false) Integer limit) {
        limit = limit != null && limit > 0 ? limit : 5;
        return ((CoinWithdrawService) this.service).getWithdrawsBySort(limit);
    }
}
