/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.exchange;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.blockchain.common.Coin;
import com.batsoft.client.exchange.common.beans.vo.IndexPageTradeDataVO;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.exchange.ExchangeCoinHistory;
import com.batsoft.service.module.exchange.service.ExchangeCoinHistoryService;
import com.batsoft.utils.date.compute.behavior.DateMathBehavior;
import com.batsoft.utils.date.compute.motion.DateMathMotion;
import com.batsoft.utils.date.convert.behavior.DateConvertBehavior;
import com.batsoft.utils.date.convert.config.DateFormatConfig;
import com.batsoft.utils.date.convert.motion.DateConvertDateMotion;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>OTC项目管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(value = "exchange",description = "交易相关的controller")
@Controller("exchangeMainController")
@RequestMapping("/exchange")
public class MainController extends GenericController {

    @Autowired
    private ExchangeCoinHistoryService exchangeCoinHistoryService;
    
    // 日期计算类
    private static DateMathBehavior motion = new DateMathMotion();
    
    // 日期转换类
    private static DateConvertBehavior<Date, Date> convert = new DateConvertDateMotion();

    private  final String  EXCHANGE = "EXCHANGE";
    /**
     * exchange 首頁
     *
     * @return
     */
    @RequestMapping(value = {""}, method = { RequestMethod.GET, RequestMethod.HEAD })
    public String index (Model model) {
        return "exchange/index";
    }

    /**
     * 交易量数据
     *
     * @return
     */
    @ResponseBody
    @ApiOperation(value = "交易量数据")
    @RequestMapping(value =  { "/tradeData" }, method =  { RequestMethod.GET })
    public JsonResult tradeData() {
        JsonResult result = new JsonResult(Boolean.TRUE);
        IndexPageTradeDataVO data = new IndexPageTradeDataVO();
        
        // 统一当前时间
        Date currentDate = new Date();

        // 统计BT
       this.statisticsBt(data, currentDate);
       
        // 统计USDT
        this.statisticsUsdt(data, currentDate);

        // 响应结果
        result.setData(data);
        return result;
    }

    /**
     * 统计BT
     *
     * @param currentDate
     *          当前系统时间
     * @param data
     *          日期计算类
     */
    private void statisticsBt(IndexPageTradeDataVO data, Date currentDate){
        // 流通中的BT
        String coinCode = Coin.BT.getType();
        BigDecimal borrowedBt = exchangeCoinHistoryService.sumOfTotalByCoinCode(coinCode);
        if(borrowedBt != null ) {
            data.setBorrowedBt(String.valueOf(borrowedBt.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
        }else {
            data.setBorrowedBt("0");
        }
        //  昨天的产出BT
        Date beginTime = convert.convert(DateFormatConfig.YYYYMMDD(), motion.dateAddDay(currentDate, -1));
        ExchangeCoinHistory yesterdayOutputBt = exchangeCoinHistoryService.findByCoinCodeAndDate(coinCode, beginTime);
        if(yesterdayOutputBt != null) {
            data.setYesterdayOutputBt(String.valueOf(yesterdayOutputBt.getTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
        } else {
            data.setYesterdayOutputBt("0");
        }
    }


    /**
     * 统计交易量
     *
     * @param data
     *          引用数据对象
     * @param currentDate
     *          当前系统时间
     */
    private void statisticsUsdt(IndexPageTradeDataVO data, Date currentDate){
        // 今天的交易量
        Date today = new Date();
        ExchangeCoinHistory exchangeHistoryTodayGlobalDate = exchangeCoinHistoryService.findByCoinCodeAndDate(EXCHANGE,today);
        if(exchangeHistoryTodayGlobalDate != null){
            data.setTodayTotalTradeAmount(exchangeHistoryTodayGlobalDate.getTotal().setScale(2,BigDecimal.ROUND_HALF_EVEN).toString());
            data.setTodayTotalTradeFee(String.valueOf(exchangeHistoryTodayGlobalDate.getFee().setScale(2,BigDecimal.ROUND_HALF_EVEN)));
        }else{
            data.setTodayTotalTradeAmount("0");
            data.setTodayTotalTradeFee("0");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        ExchangeCoinHistory ex = exchangeCoinHistoryService.findByCoinCodeAndDate(EXCHANGE,calendar.getTime());
        if(ex!=null){
            data.setYesterdayTotalTradeAmount(String.valueOf(ex.getTotal().setScale(2,BigDecimal.ROUND_HALF_EVEN)));
            data.setYesterdayTotalTradeFee(String.valueOf(ex.getFee().setScale(2,BigDecimal.ROUND_HALF_EVEN)));
        }else{
            data.setYesterdayTotalTradeAmount("0");
            data.setYesterdayTotalTradeFee("0");
        }


    }
    
}
