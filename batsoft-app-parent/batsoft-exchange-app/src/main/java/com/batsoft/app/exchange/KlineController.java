/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-09 11:11:13
 */
package com.batsoft.app.exchange;


import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.Kline;
import com.batsoft.service.module.exchange.service.impl.KlineServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>KlineController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-09 11:11:13
 */
@RestController("klineController")
@RequestMapping("/exchange/kline")

@Slf4j
public class KlineController extends BaseController<Kline, String> {

    @Resource(name = "klineService")
    @Override
    public void setService(BaseService<Kline, String> service) {
        super.service = service;
    }

    @Autowired
    private RedisService redisService;

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:kline:see", "exchange:kline:edit"}, logical = Logical.OR)
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
    @RequiresPermissions("exchange:kline:list")

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
    @RequiresPermissions(value = {"exchange:kline:add", "exchange:kline:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody Kline kline) {
        redisService.set(KlineServiceImpl.CACHE_KLINE_COINS + kline.getCoinPair() +":"+ kline.getKlineTime(), kline.getKlineData(), RedisService.CACHE_TIME);
        return super.save(kline);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:kline:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

}
