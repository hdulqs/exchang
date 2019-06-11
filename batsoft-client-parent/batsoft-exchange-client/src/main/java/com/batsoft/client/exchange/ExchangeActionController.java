package com.batsoft.client.exchange;

import com.batsoft.blockchain.common.ActionStatus;
import com.batsoft.client.exchange.common.ExchangeActionUtil;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.model.module.exchange.ExchangeAction;
import com.batsoft.service.module.exchange.service.ExchangeActionService;
import com.batsoft.service.module.system.service.config.AppConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@Api(value = "ExchangeActionController",description = "活动的Controller")
@RequestMapping(value = "/exchangeAction")
public class ExchangeActionController {

    @Autowired
    private ExchangeActionService exchangeActionService;

    @RequestMapping(value = "/lastAction",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取活动时间",notes = "根据交易对获取活动时间,返回的数据是距离活动开始的秒数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinCode",value = "交易币",dataType = "String",required = true,paramType = "query"),
            @ApiImplicitParam(name = "priceCode",value = "计价币",dataType = "String",required = true,paramType = "query")
    })
    public JsonResult<ExchangeAction> getAction(@RequestParam(value = "coinCode") String coinCode,@RequestParam(value = "priceCode") String priceCode){
        JsonResult<ExchangeAction> jsonResult = new JsonResult(true);
        Date now = new Date();
        List<ExchangeAction> exchangeActionList = exchangeActionService.getAction(coinCode,priceCode,now);
        //读取明天的活动
        if(!exchangeActionList.isEmpty()){
            ExchangeAction exchangeAction = exchangeActionList.get(0);
            if(exchangeAction !=null ) {
                if (exchangeAction.getStartTime().compareTo(now) >= 0) {
                    jsonResult.setMsg(Language.L_Failed("msg_trade_action_no_start"));
                    exchangeAction.setValide(0);
                    if (exchangeAction.getStartTime() != null) {
                        Calendar actionCalendar = Calendar.getInstance();
                        Calendar nowCalendar = Calendar.getInstance();
                        nowCalendar.setTime(now);
                        actionCalendar.setTime(exchangeAction.getStartTime());
                        long lead = actionCalendar.getTimeInMillis() - nowCalendar.getTimeInMillis();
                        exchangeAction.setStartSeconds(lead / 1000);
                        if (exchangeAction.getValide() != ActionStatus.ACTION_NO_START.getStatus()) {
                            exchangeAction.setValide(ActionStatus.ACTION_NO_START.getStatus());
                            exchangeActionService.updateActionStatus(exchangeAction);
                        }
                        jsonResult.setData(exchangeAction);
                    } else {
                        throw new NullPointerException("action starttime is null");
                    }
                } else if (exchangeAction.getEndTime().compareTo(now) <= 0) {
                    jsonResult.setMsg(Language.L_Failed("msg_trade_action_end"));
                    if (exchangeAction.getValide() != ActionStatus.ACTION_END.getStatus()) {
                        exchangeAction.setValide(ActionStatus.ACTION_END.getStatus());
                        exchangeActionService.updateActionStatus(exchangeAction);
                    }
                    exchangeAction.setStartSeconds(0);
                    jsonResult.setData(exchangeAction);
                } else {
                    if (exchangeAction.getValide() != ActionStatus.ACTIONING.getStatus()) {
                        exchangeAction.setValide(ActionStatus.ACTIONING.getStatus());
                        exchangeActionService.updateActionStatus(exchangeAction);
                    }
                    exchangeAction.setStartSeconds(0);
                    jsonResult.setData(exchangeAction);
                }
            }
        }
        return  jsonResult;
    }


    public String getTime(long time){
        String date = "";
        long day = time / 24 / 60 /60 ;
        long hour = time /60/60 % 24;
        long mm = time /60 % 60;
        long ss = time % 60;
        if(day<10){
            date +="0"+day ;
        }else{
            date += day;
        }
        if(hour<10){
            date +=":0"+hour;
        }else{
            date+=":"+hour;
        }
        if(mm<10){
            date +=":0"+mm;
        }else{
            date+=":"+mm;
        }
        if(ss<10){
            date +=":0"+ss;
        }else{
            date+=":"+ss;
        }
        return date;
    }

}

