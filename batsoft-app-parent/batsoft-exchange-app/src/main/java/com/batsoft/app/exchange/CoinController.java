/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:19:04
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.CoinPair;
import com.batsoft.model.module.exchange.vo.TradePkData;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * <p>CoinController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:19:04
 */
@Slf4j
@RestController("coinController")
@RequestMapping("/exchange/coin")
public class CoinController extends BaseController<Coin, String> {

    @Autowired
    private CoinPairService coinPairService;
    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Resource(name = "coinService")
    @Override
    public void setService(BaseService<Coin, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:coin:see", "exchange:coin:edit"}, logical = Logical.OR)
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
    @RequiresPermissions("exchange:coin:list")

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
    @RequiresPermissions(value = {"exchange:coin:add", "exchange:coin:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody Coin coin) {
        JsonResult save = super.save(coin);
        //新增币账户
        JSONObject object = (JSONObject)save.getData();
        if(save.getSuccess()&&object!=null&&object.containsKey("type") && Constants.SAVE.equals(object.get("type"))) {
            rabbitMqSender.toAddCoinInit(JSON.toJSONString(coin));
        }
        ((CoinService)service).saveRedisCache();

        return save;
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coin:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        JsonResult jsonResult=new JsonResult();
        jsonResult=super.remove(pksData.getIdsArr());
        ((CoinService)service).updateJsonCoins();
        return jsonResult;
    }


    /**
     * 修改币种开启状态
     *
     * @param pksData
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coin:updateStatus")
    @ResponseBody
    public JsonResult updateStatus(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();
        try {
            if (!StringUtils.isNull(pksData.getIdsArr())) {
                for (String id : pksData.getIdsArr()) {
                    Coin coin = findById(id);
                    if (!StringUtils.isNull(coin)) {
                        /*//修改coin的相反状态
                        if (Coin.STATUS1.equals(coin.getStatus())) {
                            coin.setStatus(Coin.STATUS0);
                        } else {
                            coin.setStatus(Coin.STATUS1);
                        }*/
                        coin.setStatus(Coin.STATUS1);
                        this.service.update(coin);
                        ((CoinService)service).updateJsonCoins();
                    }
                }
            }
            result.setSuccess(true);
            result.setMsg("修改成功");
            result.setCode(Constants.SUCCESS);
            result.setData("");
            ((CoinService)service).saveRedisCache();
        } catch (Exception e) {
            log.info("修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("修改失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }
    /**
     * 修改币种关闭状态
     *
     * @param pksData
     * @return
     */
    @RequestMapping(value = "/closeStatus", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coin:closeStatus")
    @ResponseBody
    public JsonResult closeStatus(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();
        try {
            if (!StringUtils.isNull(pksData.getIdsArr())) {
                for (String id : pksData.getIdsArr()) {
                    Coin coin = findById(id);
                    if (!StringUtils.isNull(coin)) {
                        coin.setStatus(Coin.STATUS0);
                        this.service.update(coin);
                        ((CoinService)service).updateJsonCoins();
                    }
                }
            }
            result.setSuccess(true);
            result.setMsg("修改成功");
            result.setCode(Constants.SUCCESS);
            result.setData("");
            ((CoinService)service).saveRedisCache();
        } catch (Exception e) {
            log.info("修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("修改失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }




    /**
     * 设置为定价币/取消设置
     *
     * @param pksData
     * @return
     */
    @RequestMapping(value = "/priceCoin", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coin:priceCoin")
    @ResponseBody
    public JsonResult priceCoin(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();
        try {
            if (!StringUtils.isNull(pksData.getIdsArr())) {
                for (String id : pksData.getIdsArr()) {
                    Coin coin = findById(id);
                    //修改coin的相反状态
                    if (!StringUtils.isNull(coin)) {
                        if (Coin.PRICECOIN1.equals(coin.getPriceCoin())) {
                            coin.setPriceCoin(Coin.PRICECOIN0);
                        } else {
                            coin.setPriceCoin(Coin.PRICECOIN1);
                        }
                        this.service.update(coin);
                        //更新缓存
                        ((CoinService)this.service).updateJsonPriceCoins();
                    }
                }
            }
            result.setSuccess(true);
            result.setMsg("修改成功");
            result.setCode(Constants.SUCCESS);
            result.setData("");
        } catch (Exception e) {
            log.info("修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("修改失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 允许充币/禁止充币
     *
     * @param pksData
     * @return
     */
    @RequestMapping(value = "/allowRecharge", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coin:allowRecharge")
    @ResponseBody
    public JsonResult allowRecharge(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();
        try {
            if (!StringUtils.isNull(pksData.getIdsArr())) {
                for (String id : pksData.getIdsArr()) {
                    Coin coin = findById(id);
                    //修改coin的相反状态
                    if (!StringUtils.isNull(coin)) {
                        if (Coin.ALLOWRECHARGE1.equals(coin.getAllowRecharge())) {
                            coin.setAllowRecharge(Coin.ALLOWRECHARGE0);
                        } else {
                            coin.setAllowRecharge(Coin.ALLOWRECHARGE1);
                        }
                        this.service.update(coin);
                    }
                }
            }
            result.setSuccess(true);
            result.setMsg("修改成功");
            result.setCode(Constants.SUCCESS);
            result.setData("");
        } catch (Exception e) {
            log.info("修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("修改失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 允许提币/禁止提币
     *
     * @param pksData
     * @return
     */
    @RequestMapping(value = "/allowDeposit", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coin:allowDeposit")
    @ResponseBody
    public JsonResult allowDeposit(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();
        try {
            if (!StringUtils.isNull(pksData.getIdsArr())) {
                for (String id : pksData.getIdsArr()) {
                    Coin coin = findById(id);
                    //修改coin的相反状态
                    if (!StringUtils.isNull(coin)) {
                        if (Coin.ALLOWDEPOSIT1.equals(coin.getAllowWithdraw())) {
                            coin.setAllowWithdraw(Coin.ALLOWDEPOSIT0);
                        } else {
                            coin.setAllowWithdraw(Coin.ALLOWDEPOSIT1);
                        }
                        this.service.update(coin);
                    }
                }
            }
            result.setSuccess(true);
            result.setMsg("修改成功");
            result.setCode(Constants.SUCCESS);
            result.setData("");
        } catch (Exception e) {
            log.info("修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("修改失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 设置交易对
     *
     * @param tradePkData
     * @return
     */
    @RequestMapping(value = "/setTrade", method = RequestMethod.POST)
    @RequiresPermissions("exchange:coin:setTrade")
    @ResponseBody
    public JsonResult setTrade(@RequestBody TradePkData tradePkData) {
        JsonResult result = new JsonResult();
        try {
            for (String priceCoin : tradePkData.getPricesArr()) {
                for (String id : tradePkData.getIdsArr()) {
                    Coin coin = findById(id);
                    CoinPair coinPair = new CoinPair();
                    if (!coinPairService.hasCoinTrade(priceCoin, coin.getCoinCode())) {
                        coinPair.setPricingCoinCode(priceCoin);
                        coinPair.setTradeCoinCode(coin.getCoinCode());
                        coinPair.setTradeCoinLogo(coin.getCoinLogo());
                        coinPairService.save(coinPair);
                    }
                   // ((CoinService) this.service).klineCache(coin.getSymbol(), priceCoin);
                }
                //更新缓存
                coinPairService.updateJsonTradeCoins(priceCoin);
                coinPairService.updateCoinPairStr();
                coinPairService.updateTradeAreaCoins();
                coinPairService.findCoinPairDigitInit();
            }
            result.setSuccess(true);
            result.setMsg("设置成功");
            result.setCode(Constants.SUCCESS);
            result.setData("");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("设置失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("设置失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 获取定价币coins
     * @param
     * @return
     */
    @RequestMapping(value = "/priceCoins", method = RequestMethod.GET)
    @RequiresPermissions("exchange:coin:setTrade")
    @ResponseBody
    public String priceCoins() {
        return ((CoinService) this.service).findJsonPriceCoins();
    }

}
