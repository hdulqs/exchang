package com.batsoft.job;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.batsoft.enums.CoinHistoryCoinCodeEnum;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.ExchangeCoinHistory;
import com.batsoft.model.module.exchange.vo.IndexUsdtTradeAmount;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.service.EntrustInfoService;
import com.batsoft.service.module.exchange.service.ExchangeCoinHistoryService;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.date.BaseDate;
import com.batsoft.utils.date.compute.behavior.DateMathBehavior;
import com.batsoft.utils.date.compute.motion.DateMathMotion;

@Component
public class SumOfYearterDayJob {

    @Autowired
    private ExchangeCoinHistoryService exchangeCoinHistoryService;

    @Autowired
    private CustomerAccountRecordService customerAccountRecordService;

    @Autowired
    private EntrustInfoService entrustInfoService;
    
    // 日期计算类
    private DateMathBehavior mathMotion = new DateMathMotion();
    
    @Scheduled(cron = "0 */5 * * * ?")
    public void sumBtSumOfYearDay() throws SQLException, ClassNotFoundException{
        Date today = mathMotion.dateAddMinute(BaseDate.getNowTime(), -5);
        
        // 今天的数据
        List<ExchangeCoinHistory> todayCoinHistoryList = exchangeCoinHistoryService.getListByDate(today);
        if(todayCoinHistoryList.isEmpty()){
        	// 统计今日流通中的BT
        	BigDecimal borrowedBt = customerAccountRecordService.sumMoneyByTypeCoinCode(CustomerAccountRecord.TRADE_MINING, CoinHistoryCoinCodeEnum.BT.name(), today);
            ExchangeCoinHistory coinHistory = new ExchangeCoinHistory();
            coinHistory.setCoinCode(CoinHistoryCoinCodeEnum.BT.name());
            coinHistory.setTime(DateUtils.dateFormat(today, DateUtils.DEFAULT_FORMAT_YYYY_MM_DD));
            coinHistory.setTotal(borrowedBt);
            exchangeCoinHistoryService.save(coinHistory);
            
            // 统计今日交易USDT交易额
            IndexUsdtTradeAmount source = entrustInfoService.findTradeAmountFormUsdt(today);
            coinHistory = new ExchangeCoinHistory();
            coinHistory.setCoinCode(CoinHistoryCoinCodeEnum.EXCHANGE.name());
            coinHistory.setTime(DateUtils.dateFormat(today, DateUtils.DEFAULT_FORMAT_YYYY_MM_DD));
            coinHistory.setTotal(source.getTradeCoinAmount());
            coinHistory.setFee(source.getFeeCoinAmount());
            exchangeCoinHistoryService.save(coinHistory);
        }else{//如果有数据，那么更新今天的数据
        	todayCoinHistoryList.forEach((ExchangeCoinHistory record) -> {
        		if(Objects.equals(record.getCoinCode(), CoinHistoryCoinCodeEnum.BT.name())) {
        			// 更新的是流通中的数据
        			BigDecimal borrowedBt = customerAccountRecordService.sumMoneyByTypeCoinCode(CustomerAccountRecord.TRADE_MINING, CoinHistoryCoinCodeEnum.BT.name(), today);
        			record.setTotal(borrowedBt);
                    exchangeCoinHistoryService.update(record);
        		}else {
        			// 更新的是交易总量数据
        			IndexUsdtTradeAmount source = entrustInfoService.findTradeAmountFormUsdt(today);
        			record.setTotal(source.getTradeCoinAmount());
        			record.setFee(source.getFeeCoinAmount());
                    exchangeCoinHistoryService.update(record);
        		}
        	});
        }
    }
    
}
