/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 17:46:20 
*/
package com.batsoft.app.exchange;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.exchange.service.CoinRechargeService;
import com.batsoft.model.module.exchange.CoinRecharge;
import com.batsoft.service.module.exchange.service.CoinRechargeService;


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
* <p>CoinRechargeController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 17:46:20 
*/
@RestController("coinRechargeController")
@RequestMapping("/exchange/coinRecharge")

@Slf4j
public class CoinRechargeController extends BaseController<CoinRecharge,String> {


@Resource(name="coinRechargeService")
@Override
public void setService(BaseService<CoinRecharge, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"exchange:coinRecharge:see","exchange:coinRecharge:edit"},logical=Logical.OR)
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
@RequiresPermissions("exchange:coinRecharge:list")

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
@RequiresPermissions(value={"exchange:coinRecharge:add","exchange:coinRecharge:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody CoinRecharge coinRecharge) {
return super.save(coinRecharge);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("exchange:coinRecharge:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

    /**
     * 查询当日各币种充值总额
     * @param
     * @return
     */
    @RequestMapping(value = "/getTotleRechargesByDay", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coinWithdraw:getTotleRechargesByDay")
    @ResponseBody
    public JsonResult getTotleRechargesByDay() {
        return ((CoinRechargeService)this.service).getTotleRechargesByCode("day");
    }

    /**
     * 查询充值排行榜
     */
    @RequestMapping(value = "/getRechargesBySort")
    @ResponseBody
    public JsonResult getRechargesBySort(@RequestParam(value = "limit",required = false) Integer limit) {
        limit = limit!=null&&limit>0?limit:5;
        return ((CoinRechargeService)this.service).getRechargesBySort(limit);
    }
}
