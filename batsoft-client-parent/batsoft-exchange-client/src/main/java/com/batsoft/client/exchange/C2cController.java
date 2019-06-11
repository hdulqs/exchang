/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.exchange;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.exchange.C2cOrder;
import com.batsoft.model.module.exchange.vo.C2cUserVo;
import com.batsoft.service.module.exchange.service.C2cOrderService;
import com.batsoft.service.module.exchange.service.C2cProductService;
import com.batsoft.utils.StringUtils;

/**
 * <p>C2C管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(value = "c2cController",description = "一对一交易")
@Controller("c2cController")
@RequestMapping("/c2c")
public class C2cController extends GenericController {

    @Autowired
    private C2cOrderService c2cOrderService;
    
    @Autowired
    private C2cProductService c2cProductService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String market(HttpServletRequest request, Model model) {
        return "exchange/c2c";
    }

    @RequestMapping(value = "/operation-menu",method = RequestMethod.GET)
    public String operation_mu(HttpServletRequest request, Model model) {
        return "operation-menu";
    }

    /**
     * 根据类型查询商户订单
     *
     * @param coinCode
     * @param type
     * @return
     */
    @ApiOperation(value = "根据类型查询商户订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinCode",value = "币的类型",required = true,dataType = "String" ,example = "BT"),
            @ApiImplicitParam(name = "type",value = "交易类型",required = true,dataType = "String" ,example = "sell,buy")
    })
    @RequestMapping(value = "/findC2cOrders",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult findC2cOrders(@RequestParam(value = "coinCode") String coinCode,
                                    @RequestParam(value = "type") Integer type) {
        int start = 0;
        int limit = 2;
        return c2cOrderService.findUserOrders(coinCode, type, start, limit);
    }

    /**
     * 根据类型查询商户订单
     *
     * @param coinCode
     * @return
     */
    @RequestMapping(value = "/findUserOrders",method = RequestMethod.GET)
    @ApiOperation(value = "根据币类型查询商户订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinCode",value = "币的类型",paramType = "query",required = true,dataType = "String" ,example = "BT")
    })
    @ResponseBody
    public JsonResult findUserOrders(@RequestParam(value = "coinCode") String coinCode) {
        int start = 0;
        int limit = 10;
        return c2cOrderService.findOrderByUserId(coinCode, null, null, start, limit);
    }



    /**
     * c2c购买/卖出
     *
     * @param coinCode
     * @param type sell buy
     * @param number
     * @param buy_type 购买的类型 0是购买的数量，1是购买金额，只有单type是buy类型的是否才有用
     * @return
     */
    @ApiOperation(value = "c2c购买/卖出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinCode",value = "币的类型",paramType = "query",required = true,dataType = "String" ,example = "BT"),
            @ApiImplicitParam(name = "type",value = "买卖类型",paramType = "query",required = true,dataType = "String" ,example = "sell,buy"),
            @ApiImplicitParam(name = "buy_type",value = "购买的类型 0是购买的数量，1是购买金额，只有单type是buy类型的是否才有用",paramType = "query",required = true,dataType = "String" ),
            @ApiImplicitParam(name = "number",value = "买卖数量",paramType = "query",required = true,dataType = "String" )
    })
    @RequestMapping(value = "/transactionC2c",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult transactionC2c(@RequestParam(value = "coinCode") String coinCode,
                                     @RequestParam(value = "type") String type,
                                     @RequestParam(value = "buy_type", required = false,defaultValue = "0") int buy_type,
                                     @RequestParam(value = "number") BigDecimal number) {
        C2cOrder order = new C2cOrder();
        order.setCoinCode(coinCode);
//        BigDecimal price = c2cOrderService.findPriceByCoinCode(coinCode, type);
        if ("sell".equals(type)) {
            order.setDirection(C2cOrder.DIRECTION0);
            order.setOperationType(C2cOrder.OPERATIONTYPE2);
        }  else {
            order.setDirection(C2cOrder.DIRECTION1);
            order.setOperationType(C2cOrder.OPERATIONTYPE1);
        }
//        order.setPrice(price);
        order.setOpeationState(C2cOrder.OPEATIONSTATE0);
        order.setNumber(number);
//        order.setOpeationMoney(order.getNumber().multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setTradeTime(new Date());
        order.setTraNumber(StringUtils.createOrderNum());
        return c2cOrderService.addTransactionOrder(order,buy_type);
    }


    @ApiOperation(value = "查询有效的代币")
    @RequestMapping(value = "/findProducts",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult findProducts() {
        return c2cProductService.findProducts();
    }


    @ApiOperation(value = "跳转有效的代币" ,notes = "根据币种名称和数量跳转")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinCode" ,value = "币种",required = true,paramType = "query" ,dataType = "String"),
            @ApiImplicitParam(name = "mount" ,value = "输入数量",required = true,paramType = "query" ,dataType = "String")
    })
    @RequestMapping(value = "/purchase",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult purchase(@RequestParam(name = "coinCode") String coinCode,@RequestParam(name = "mount") String mount,Model model) {
        model.addAttribute("coinCode",coinCode);
        model.addAttribute("mount",mount);
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.setMsg("?coinCode="+coinCode+"&mount="+mount);
        jsonResult.setData( "/c2c/clickpurchase");
        return jsonResult;
    }

    @RequestMapping(value = "/clickpurchase",method = RequestMethod.GET)
    public String clickpurchase(@RequestParam(name = "coinCode") String coinCode,@RequestParam(name = "mount") String mount,Model model) {
        model.addAttribute("mount",mount);
        model.addAttribute("coinCode",coinCode);
        return "exchange/clickpurchase";
    }

    @RequestMapping(value = "/clickmoneylist",method = RequestMethod.GET)
    public String clickmoneylist(@RequestParam(name = "id") String id,@RequestParam(name = "moner_nost") String moner_nost, @RequestParam(name = "money_price") String money_price,Model model) {
        model.addAttribute("id",id);
        model.addAttribute("moner_nost",moner_nost);
        model.addAttribute("money_price",money_price);
        return "exchange/clickmoneylist";
    }

    @ApiOperation(value = "查询的币信息" ,notes = "根据币种名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinCode" ,value = "币种",required = true,paramType = "query" ,dataType = "String")
    })
    @RequestMapping(value = "/findProduct",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult findProduct(@RequestParam(value = "coinCode") String coinCode) {
        return c2cProductService.findProduct(coinCode);
    }

    /**
     * 查询用户账户
     *
     * @return
     */
    @ApiOperation(value = "查询用户账户" ,notes = "根据用户id查询用户账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "用户id",required = true,paramType = "query" ,dataType = "String")
    })
    @RequestMapping(value = "/findC2cUserVoById",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult findC2cUserVoById(@RequestParam(value = "id") String id) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        jsonResult.setCode(Constants.SUCCESS);
        C2cUserVo c2cUserVo = c2cOrderService.findC2cUserVoById(id);
        jsonResult.setData(c2cUserVo);
        jsonResult.setMsg("成功");
        return jsonResult;
    }


}
