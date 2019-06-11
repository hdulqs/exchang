package com.batsoft.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.batsoft.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batsoft.core.common.enums.CoinEnum;
import com.batsoft.core.transaction.RedisDistributedLock;
import com.batsoft.core.transaction.RedisDistributedLock.RedisDistributedKeyEnum;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.po.FindCustomerAccountRecordExistPO;
import com.batsoft.model.module.exchange.po.FindCustomerEverydayRecordPO;
import com.batsoft.model.module.exchange.vo.FindCustomerEverydayRecordVO;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.TradingDigService;
import com.batsoft.service.module.exchange.dao.CustomerAccountRecordDao;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.trade.util.MessageUtil;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.utils.BeansUtil;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.date.BaseDate;
import com.batsoft.utils.date.compute.behavior.DateMathBehavior;
import com.batsoft.utils.date.compute.motion.DateMathMotion;
import com.batsoft.utils.date.convert.config.DateFormatConfig;
import com.batsoft.utils.date.convert.motion.DateConvertDateMotion;
import com.batsoft.utils.date.convert.motion.DateConvertStringMotion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "tradingDigService")
public class TradingDigServiceImpl implements TradingDigService {
	
	// 时间处理对象
	private DateConvertDateMotion convert = new DateConvertDateMotion();
	
	// 时间计算工具类
	private DateMathBehavior dateMathUtil = new DateMathMotion();
	
	// 用户账户流水
	@Autowired
	private CustomerAccountRecordDao customerAccountRecordDao;
	
	@Autowired
	private RabbitMqSender rabbitMqSender;
	
	@Autowired
	private CustomerAccountRecordService customerAccountRecordService;
	
	@Autowired
	private AppConfigService appConfigService;
	
	@Override
	public void excute() {
		Date currentDate = new Date();
		String tableDate = DateUtils.dateFormatToString(currentDate,DateUtils.TABLES_DAY_FIX);
		// 获取配置奖励比例
		String digRewardScale = appConfigService.findValueByKey(AppConfig.DIG_REWARD_SCALE);
		if(StringUtils.isNull(digRewardScale)) {
			log.error("ERROR:挖矿奖励比例未配置");
			return;
		}
		BigDecimal awardRate = new BigDecimal(digRewardScale);
		
		// 昨天时间
        Date beginTime = convert.convert(DateFormatConfig.YYYYMMDD(), dateMathUtil.dateAddDay(currentDate, -1));
        Date endTime = convert.convert(DateFormatConfig.YYYYMMDD(), currentDate);
		
		// 准备查询条件
		FindCustomerEverydayRecordPO param = new FindCustomerEverydayRecordPO();
		param.setType(CustomerAccountRecord.HANDFEE);
		param.setBeginTime(beginTime);
		param.setEndTime(endTime);
		param.setTradeCoinCode(CoinEnum.BT.getCode());
		
		// 精准提取昨天的表名
		String tableName = BeansUtil.findBeanTableValue(CustomerAccountRecord.class);
		DateMathMotion dateMathMotion = new DateMathMotion();
		DateConvertStringMotion dataConvert = new DateConvertStringMotion();
		Date yesterDate = dateMathMotion.dateAddDay(BaseDate.getNowTime(), -1);
		String tableSuffix = dataConvert.convert(DateFormatConfig._yyyy_MM_dd(), yesterDate);
		param.setTableName(tableName + tableSuffix);
		
		// 查询用户昨日交易流水
		List<FindCustomerEverydayRecordVO> listRecord = customerAccountRecordDao.findCustomerEverydayRecord(param);
		if(listRecord != null && listRecord.size() > 0) {
			// 分布式锁
			RedisDistributedLock lock = new RedisDistributedLock();
			for(FindCustomerEverydayRecordVO record : listRecord) {
				if(record.getAwardAmount() != null) {
					String customerId = record.getCustomerId();
					String personLockKey = String.format(RedisDistributedKeyEnum.ENTRUST_TRADE_PERSION_KEY.getValue(), customerId);
					try {
						// BEGIN:给用户添加分布式锁
						lock.lock(personLockKey, customerId);
						
						// 奖励金额
						BigDecimal awardAmount = record.getAwardAmount().multiply(awardRate).setScale(15, BigDecimal.ROUND_HALF_DOWN);
						if(checkToDayRecordExist(customerId , awardAmount, currentDate)) {
							// 奖励的货币类型
							String coinCode = CoinEnum.BT.getCode();
							
							// 奖励用户可用余额
							rabbitMqSender.toRedisAccount(MessageUtil.addCoinAvailable(customerId, coinCode, awardAmount));
							
							// 记录类型
							String type = CustomerAccountRecord.TRADE_MINING;
							
							// 交易币
							String tradeCoinCode = record.getTradeCoinCode();
							
							// 定价币
							String pricingCoinCode = record.getPricingCoinCode();
							
							// 描述
							String remark = "交易挖矿";
							
							// 增加交易记录
							customerAccountRecordService.saveAccountRecord(type, customerId, coinCode, awardAmount, tradeCoinCode, pricingCoinCode, remark,tableDate);
							log.info("成功增加用户挖矿奖励 【" + awardAmount + "】BT"); 
						}
					} finally {
						// END:释放分布式锁
						lock.lock(personLockKey, customerId);
					}
			    }
			}
		}
	}
	
	/**
	 * 校验该用户今日挖矿奖励是否产生
	 * 
	 * @param customerId
	 * 				客户ID
	 * @return
	 */
	private boolean checkToDayRecordExist(String customerId, BigDecimal usdtAmount, Date currentDate) {
		Date beginTime = convert.convert(DateFormatConfig.YYYYMMDD(), currentDate);
        Date endTime =  convert.convert(DateFormatConfig.YYYYMMDD(), dateMathUtil.dateAddDay(currentDate, 1));
        // 查询客户ID是否存在今天的交易挖矿数据
        FindCustomerAccountRecordExistPO param = new FindCustomerAccountRecordExistPO();
        param.setType(CustomerAccountRecord.TRADE_MINING);
        param.setCustomerId(customerId);
        param.setMoney(usdtAmount);
        param.setBeginTime(beginTime);
        param.setEndTime(endTime);
        param.setCoinCode(CoinEnum.BT.getCode());
        
        // 精准提取今天的表名
 		String tableName = BeansUtil.findBeanTableValue(CustomerAccountRecord.class);
 		DateConvertStringMotion dataConvert = new DateConvertStringMotion();
 		String tableSuffix = dataConvert.convert(DateFormatConfig._yyyy_MM_dd(), BaseDate.getNowTime());
 		param.setTableName(tableName + tableSuffix);
        
        // 查询
        BigInteger number = customerAccountRecordDao.findCustomerAccountRecordExist(param);
        if(number != null && BigInteger.ZERO.compareTo(number) < 0) {
        	return Boolean.FALSE;
        }
		return Boolean.TRUE;
	}
	
}
