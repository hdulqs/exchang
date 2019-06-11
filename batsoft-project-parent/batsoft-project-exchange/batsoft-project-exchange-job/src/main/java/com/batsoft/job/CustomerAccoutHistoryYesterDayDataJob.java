package com.batsoft.job;


import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.CustomerAccountHistory;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.service.module.exchange.dao.CustomerAccountDao;
import com.batsoft.service.module.exchange.dao.CustomerAccountFreezeDao;
import com.batsoft.service.module.exchange.dao.CustomerAccountHistoryDao;
import com.batsoft.service.module.exchange.dao.CustomerAccountRecordDao;
import com.batsoft.service.module.exchange.service.CustomerAccountHistoryService;
import com.batsoft.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * exchange_customer_account_history表的昨天的数据统计的定时任务
 */
@Component
public class CustomerAccoutHistoryYesterDayDataJob {

    @Autowired
    CustomerAccountFreezeDao customerAccountFreezeDao;

    @Autowired
    CustomerAccountRecordDao customerAccountRecordDao;

    @Autowired
    CustomerAccountHistoryDao customerAccountHistoryDao;

    @Autowired
    CustomerAccountDao customerAccountDao;

    @Autowired
    CustomerAccountHistoryService customerAccountHistoryService;
    /**
     * 从昨日的exchange_customer_account_record表里把昨天用户的记录数据统计和
     * 从昨日的exchange_customer_account_freeze表里把昨日用户的记录数据统计
     * 他们2个数据的和再加上前台历史数据
     * 每天的 00:00:10 执行
     */
    @Scheduled(cron = "10 0 0 * * ?")
    public void doJob(){
        List<CustomerAccountVo> customerAccounts = customerAccountDao.findListAll();
        for (CustomerAccount c:customerAccounts
             ) {
            Date date = new Date();
            HashMap<String,Object> param = new HashMap<>();
            param.put("coinCode",c.getCoinCode());
            param.put("userId",c.getUserId());
            param.put("time",DateUtils.dateFormatToString(date,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            param.put("tables","exchange_customer_account_record"+ DateUtils.dateFormatToString(calendar.getTime(),DateUtils.TABLES_DAY_FIX));
            BigDecimal yearterDaySum = customerAccountRecordDao.getRecordSum(param);
            if( yearterDaySum.floatValue() != 0 ){
                CustomerAccountHistory recordSum = customerAccountHistoryDao.getRecordSum(param);
                if(recordSum != null){
                    recordSum.setAvailable(recordSum.getAvailable().add(yearterDaySum));
                    recordSum.setUpdateTime(date);
                    if(c.getAvailable().compareTo(recordSum.getAvailable()) != 0){
                        recordSum.setWarning(1);
                        recordSum.setWarningRemark("在"+date+"账户余额数据和历史表统计不正确");
                    }
                    customerAccountHistoryDao.updateDateById(recordSum);
                }
            }
            HashMap<String,Object> freeze_param = new HashMap<>();
            freeze_param.put("coinCode",c.getCoinCode());
            freeze_param.put("userId",c.getUserId());
            freeze_param.put("time",DateUtils.dateFormatToString(date,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD));
            freeze_param.put("tables","exchange_customer_account_freeze"+DateUtils.dateFormatToString(calendar.getTime(),DateUtils.TABLES_DAY_FIX));
            BigDecimal freezeSum = customerAccountFreezeDao.getFreezeSum(freeze_param);
            if( freezeSum.floatValue() != 0 ){
                CustomerAccountHistory YesterDayfreezeSum = customerAccountHistoryDao.getFreezeSum(freeze_param);
                if( YesterDayfreezeSum != null){
                    YesterDayfreezeSum.setFreeze(YesterDayfreezeSum.getFreeze().add(freezeSum));
                    YesterDayfreezeSum.setUpdateTime(date);
                    if(c.getFreeze().compareTo(YesterDayfreezeSum.getFreeze()) != 0){
                        YesterDayfreezeSum.setWarning(1);
                        YesterDayfreezeSum.setWarningRemark("在"+date+"账户余额数据和历史表统计不正确");
                    }
                    customerAccountHistoryDao.updateDateById(YesterDayfreezeSum);

                }
            }
        }

    }
}
