/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:20:19
 */
package com.batsoft.app.exchange;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.CoinPair;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CoinService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>CoinPairController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:20:19
 */
@RestController("coinPairController")
@RequestMapping("/exchange/coinPair")

@Slf4j
public class CoinPairController extends BaseController<CoinPair, String> {


    @Resource(name = "coinPairService")
    @Override
    public void setService(BaseService<CoinPair, String> service) {
        super.service = service;
    }

    @Autowired
    private CoinService coinService;



    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:coinPair:see", "exchange:coinPair:edit"}, logical = Logical.OR)
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
    @RequiresPermissions("exchange:coinPair:list")

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
    @RequiresPermissions(value = {"exchange:coinPair:add", "exchange:coinPair:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody CoinPair coinPair) {

        JsonResult jsonResult = super.save(coinPair);
        ((CoinPairService) service).findCoinPairDigitInit();
        coinService.saveRedisCache();
        return jsonResult;

    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coinPair:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();
        List<String> data = new ArrayList<String>();
        try {
            for (String PK : pksData.getIdsArr()) {
                CoinPair coinPair=super.service.get(PK);
                service.delete(PK);
                data.add(PK);


                //更新缓存
                ((CoinPairService)super.service).updateJsonTradeCoins(coinPair.getPricingCoinCode());
                ((CoinPairService)super.service).updateCoinPairStr();
                ((CoinPairService)super.service).updateTradeAreaCoins();
                ((CoinPairService)super.service).findCoinPairDigitInit();
                ((CoinPairService)super.service).findRecomment();
            }
            result.setSuccess(true);
            result.setMsg("删除成功");
            result.setCode(Constants.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            log.info("删除失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("删除失败");
            result.setCode(Constants.FAILED);
        }
        return result;

    }

    /**
     * 设置币种推荐
     * @param
     * @return
     */
    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coinPair:recommend")
    @ResponseBody
    public JsonResult recommend(@RequestBody PksData pksData) {
        return ((CoinPairService) super.service).recommendCoins(pksData.getIdsArr(), CoinPair.RECOMENT1);
    }

    /**
     * 取消币种推荐
     * @param
     * @return
     */
    @RequestMapping(value = "/unRecommend", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coinPair:unRecommend")
    @ResponseBody
    public JsonResult unRecommend(@RequestBody PksData pksData) {
        return ((CoinPairService) super.service).recommendCoins(pksData.getIdsArr(), CoinPair.RECOMENT0);
    }

}
