/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:20:38
 */
package com.batsoft.service.module.exchange.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.CustomerAccountHistory;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.exchange.vo.DepostPkData;
import com.batsoft.model.module.member.User;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.dao.CustomerAccountDao;
import com.batsoft.service.module.exchange.dao.CustomerAccountFreezeDao;
import com.batsoft.service.module.exchange.dao.CustomerAccountHistoryDao;
import com.batsoft.service.module.exchange.dao.CustomerAccountRecordDao;
import com.batsoft.service.module.exchange.service.CoinRechargeService;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.service.module.exchange.service.CustomerAccountHistoryService;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.util.MessageUtil;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.exchange.trade.util.UUIDUtil;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;

import redis.clients.jedis.Jedis;

/**
 * <p> CustomerAccountServiceImpl </p>
 * @author: Bat Admin
 * @Date :  2018-04-14 10:20:38
 */
@Service("customerAccountService")
public class CustomerAccountServiceImpl extends BaseServiceImpl<CustomerAccount, String> implements CustomerAccountService {

    private  static final String USERADDRESS=Constants.CACHE_EX_PREFIX+"user_address:";
    private static final Logger logger = LoggerFactory.getLogger(CustomerAccountServiceImpl.class);
    @Autowired
    private CustomerAccountDao customerAccountDao;

    @Autowired
    private CustomerAccountRecordDao customerAccountRecordDao;

    @Autowired
    private CustomerAccountFreezeDao customerAccountFreezeDao;

    @Autowired
    private CustomerAccountHistoryDao customerAccountHistoryDao;

    @Autowired
    private CoinService coinService;
    @Autowired
    private RabbitMqSender rabbitMqSender;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerAccountRecordService customerAccountRecordService;

    @Autowired
    private CoinRechargeService coinRechargeService;

    @Autowired
    private CustomerAccountHistoryService customerAccountHistoryService;

    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    @Override
    public List<CustomerAccountVo> findList(Integer status) {
        User user = UserUtils.getUser();
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",user.getId());
        map.put("status",status);
        return customerAccountDao.findListByIdAndStatue(map);
    }

    /**
     * 具体币种数据
     *
     * @return
     */
    @Override
    public CustomerAccountVo findCoinAccount(String userId,String coinCode) {
        CustomerAccount customerAccount = RedisUserUtil.getByCoinCode(userId,coinCode);
        CustomerAccountVo customerAccountVo = customerAccountDao.findCoinAccount(userId,coinCode);
        if(customerAccount != null) {
            customerAccountVo.setFreeze(customerAccount.getFreeze());
            customerAccountVo.setAvailable(customerAccount.getAvailable());
        }
        return customerAccountVo;
    }


    @Override
    public List<CustomerAccountVo> findCoinAccounts(String userId) {
      return  customerAccountDao.findList(userId);
    }

    @Override
    public void flushRedisAccountByCustomerId(String userId) {
        Jedis jedis = jedisClient.getJedis(JedisDataSourceSignleton.DB1);
        try {
        	String key = "user:dcAccount:" + userId;
            //查询redis中账户是否存在
            String str = jedis.get(key);

            //查询个人所有的币账户信息
            List<CustomerAccountVo> accounts = customerAccountDao.findList(userId);
            if(StringUtils.isEmpty(str)){//为空直接初始化
                jedis.set(key, JSON.toJSONString(accounts));
            }else{
                List<CustomerAccountVo> list = JSON.parseArray(str, CustomerAccountVo.class);
                for(CustomerAccountVo redisAccount : list){
                    //可用为0，重新初始化
                    if(redisAccount.getAvailable().compareTo(BigDecimal.ZERO)==0){
                        for(CustomerAccountVo sqlAccount : accounts){
                            if(redisAccount.getCoinCode().equals(sqlAccount.getCoinCode())){
                                redisAccount.setAvailable(sqlAccount.getAvailable());
                            }
                        }
                    }

                    //冻结为0,重新初始化
                    if(redisAccount.getFreeze().compareTo(BigDecimal.ZERO)==0){
                        for(CustomerAccountVo sqlAccount : accounts){
                            if(redisAccount.getCoinCode().equals(sqlAccount.getCoinCode())){
                                redisAccount.setFreeze(sqlAccount.getFreeze());
                            }
                        }
                    }
                }
                jedis.set(key,JSON.toJSONString(accounts));
            }
		} finally {
			jedisClient.close(jedis);
		}
    }

    /**
     * 注册用户初始化币种账户
     * @param message
     * @return
     */
    @Override
    public JsonResult saveMultipleFinanceByMQ(String message) {
        try {
            User user = JSONObject.parseObject(message, User.class);
            List<Coin> list = coinService.findCoins();
            //if exist users and delete it
            QueryFilter filter = new QueryFilter(CustomerAccount.class);
            filter.addFilter("user_name=",user.getUserName());
            List<CustomerAccount> existUser = find(filter);
            if(!existUser.isEmpty()){
                delete(filter);
            }
            List<CustomerAccountHistory> existAccountHistoryUsers = customerAccountHistoryService.find(filter);
            if(!existAccountHistoryUsers.isEmpty()){
                customerAccountHistoryService.delete(filter);
            }
            List<CustomerAccountHistory> accountHistoryList = new ArrayList<>();
            List<CustomerAccount> accountList = new ArrayList<>();
            for (Coin coin : list) {
                CustomerAccount finance = new CustomerAccount();
                CustomerAccountHistory customerAccountHistory = new CustomerAccountHistory();
                finance.setCoinCode(coin.getCoinCode());
                customerAccountHistory.setCoinCode(coin.getCoinCode());
                finance.setUserId(user.getId());
                customerAccountHistory.setUserId(user.getId());
                finance.setUserName(user.getUserName());
                customerAccountHistory.setUserName(user.getUserName());
                customerAccountHistory.setUpdateTime(new Date());
                finance.setId(UUIDUtil.getUUID());
                customerAccountHistory.setId(UUIDUtil.getUUID());
                accountList.add(finance);
                accountHistoryList.add(customerAccountHistory);
            }
            //批量插入数据库
            if(!accountHistoryList.isEmpty()&& !accountList.isEmpty()){
               int accountInsertRows =  customerAccountDao.insertBatch(accountList);
               int accountHistoryInsertRows = customerAccountHistoryDao.insertBatch(accountHistoryList);
               if(accountHistoryInsertRows!= accountInsertRows){
                   throw new IllegalStateException("插入数据不一致");
               }
            }
            logger.info("exchangefinance消息处理成功！" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新加币种初始化用户信息
     * @param message
     * @return
     */
    @Override
    public JsonResult saveAccountByNewCoinOnMq(String message) {

            Coin coin = JSONObject.parseObject(message, Coin.class);
            List<User> list = userService.findAll();

            for (User user : list) {
                try {
                        CustomerAccount finance = new CustomerAccount();
                        finance.setCoinCode(coin.getCoinCode());
                        finance.setUserId(user.getId());
                        finance.setUserName(user.getUserName());
                        save(finance);
                    }catch (Exception e){
                    logger.info("新加币初始化报错---"+coin.getCoinCode()+"   ||" + user.getId());
                 }
                }
            logger.info("exchangefinance消息处理成功！" + message);
        return null;
    }

    @Override
    public JsonResult saveDepostData(DepostPkData data) {
        JsonResult jsonResult=new JsonResult();
        try {
            for (String id : data.getIdsArr()) {
                CustomerAccount customerAccount = this.get(id);
                rabbitMqSender.toRedisAccount(MessageUtil.addCoinAvailable(customerAccount.getUserId(), customerAccount.getCoinCode(), new BigDecimal(data.getAmount())));

                //充币流水
                customerAccountRecordService.saveAccountRecord(CustomerAccountRecord.RECHARGE,customerAccount.getId(),customerAccount.getUserId(),customerAccount.getCoinCode(),data.getAmount());
                coinRechargeService.saveOrder(customerAccount.getId(),customerAccount.getUserId(),customerAccount.getCoinCode(),data.getAmount(),"admin",customerAccount.getCoinAddress()==null?"000":customerAccount.getCoinAddress(),StringUtils.createOrderNum());

            }
            jsonResult.setMsg("充币订单提交成功");
            jsonResult.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setMsg("充币订单提交失败");
            jsonResult.setSuccess(false);
        }finally {

        }
        return jsonResult;
    }

    @Override
    public JsonResult findCheckAccout(String userId) {
        JsonResult jsonResult = new JsonResult();
        String redisStr = jedisClient.get(JedisDataSourceSignleton.DB1, "user:dcAccount:" + userId);
        JSONArray redisArray = JSON.parseArray(redisStr);

        //查询用户所有的币
        QueryFilter filter = new QueryFilter(CustomerAccount.class);
        filter.addFilter("user_id=",userId);
        List<CustomerAccount> list = find(filter);

        JSONArray jsonArray = new JSONArray();
        if(list!=null&&!list.isEmpty()){
            for(CustomerAccount account : list){
                JSONObject obj = new JSONObject();
                obj.put("coinCode",account.getCoinCode());
                //查数据库
                obj.put("mysql_available",account.getAvailable());
                obj.put("mysql_freeze",account.getFreeze());

                //查redisf
                JSONObject redisObj = null;
                if(redisArray!=null) {
                    a:
                    for (int i = 0; i < redisArray.size(); i++) {
                        if (account.getCoinCode().equals(redisArray.getJSONObject(i).getString("coinCode"))) {
                            redisObj = redisArray.getJSONObject(i);
                            break a;
                        }
                    }
                }
                if(redisObj!=null){
                    obj.put("redis_available",redisObj.getBigDecimal("available"));
                    obj.put("redis_freeze",redisObj.getBigDecimal("freeze"));
                }else{
                    obj.put("redis_available",0);
                    obj.put("redis_freeze",0);
                }
                HashMap<String,Object> param = new HashMap<>();
                param.put("coinCode",account.getCoinCode());
                param.put("userId",userId);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                param.put("tables","exchange_customer_account_record"+DateUtils.dateFormatToString(calendar.getTime(),DateUtils.TABLES_DAY_FIX));
                //查流水
               CustomerAccountHistory YearterDaySum = customerAccountHistoryDao.getRecordSum(param);
               if(YearterDaySum != null) {
                   BigDecimal recordSum = customerAccountRecordDao.getRecordSum(param);
                   if (recordSum != null) {
                       obj.put("record_available", recordSum.add(YearterDaySum.getAvailable()));
                   } else {
                       obj.put("record_available", 0);
                   }
               }else{
                   obj.put("record_available", 0);
               }

                HashMap<String,Object> freeze_param = new HashMap<>();
                freeze_param.put("coinCode",account.getCoinCode());
                freeze_param.put("userId",userId);
                freeze_param.put("tables","exchange_customer_account_freeze"+DateUtils.dateFormatToString(calendar.getTime(),DateUtils.TABLES_DAY_FIX));
                CustomerAccountHistory YesterDayfreezeSum = customerAccountHistoryDao.getFreezeSum(freeze_param);
                if(YesterDayfreezeSum != null){
                    BigDecimal freezeSum = customerAccountFreezeDao.getFreezeSum(freeze_param);
                    if(freezeSum != null){
                        obj.put("record_freeze",freezeSum.add(YesterDayfreezeSum.getAvailable()));
                    } else {
                        obj.put("record_freeze",0);
                    }
                } else {
                    obj.put("record_freeze",0);
                }
                jsonArray.add(obj);
            }
        }
        jsonResult.setData(jsonArray);
        jsonResult.setSuccess(true);

        return jsonResult;
    }

    @Override
    public JSONObject findCoinAddress(String id, String coinCode) {
        JSONObject data=new JSONObject();
        Coin  coin=coinService.findWithdrawData(coinCode);
        if(Coin.ERC20_1.equals(coin.getErc20Status())){
            //本地用户是否有address
            String  address = findCoinAccount(id,"ETH").getCoinAddress();
            data.put("ERC",true);
            data.put("address",address);
            if(address!=null && !"".equals(address)){
                updateAccountAddress(id,coinCode,address);
            }
        } else {
            data.put("ERC",false);
        }
        return data;
    }

    @Override
    public void updateAccountAddress(String userId, String coinCode, String address) {
        QueryFilter filter=new QueryFilter(CustomerAccount.class);
        filter.addFilter("userId_EQ",userId);
        filter.addFilter("coinCode_EQ",coinCode);
        CustomerAccount account=this.get(filter);
        if(account!=null){
            if(account.getCoinAddress() == null|| "".equals(account.getCoinAddress())) {
                account.setCoinAddress(address);
                update(account);
            }
        }

        // 更新ETH 用户地址到缓存
        if(coinCode.equals("ETH")){
            addOneAdressToRedis("ETH",address);
        }
    }

    private String findAddress(String coinCode){
        StringBuffer address_str=new StringBuffer();
        QueryFilter filter = new QueryFilter(CustomerAccount.class);
        filter.addFilter("coinCode_EQ",coinCode);
        List<CustomerAccount> list = find(filter);
        for(CustomerAccount account : list){
            if(!StringUtils.isEmpty(account.getCoinAddress())){
                address_str.append(account.getCoinAddress());
                address_str.append("|");
            }
        }
        return address_str.toString();
    }
    
    @Override
    public String addAdressToRedis(String coinCode) {
        String address_str=null;
        if(StringUtils.isEmpty(findAddressFromRedis(coinCode))) {
            address_str=findAddress(coinCode);
            redisService.set(USERADDRESS+coinCode,address_str,RedisService.CACHE_TIME);
        }
        return address_str;
    }

    @Override
    public String addOneAdressToRedis(String coinCode, String address) {
        String address_str=redisService.get(USERADDRESS + coinCode);
        String address_str_ret = "";
        if(!StringUtils.isEmpty(address_str)){
            address_str_ret=address_str+"|"+address;
        }else{
            address_str=addAdressToRedis(coinCode);
            address_str_ret=address_str+"|"+address;
        }
        redisService.set(USERADDRESS+coinCode,address_str_ret,RedisService.CACHE_TIME);
        return address_str_ret;
    }

    @Override
    public String findAddressFromRedis(String coinCode) {
        String address_str=redisService.get(USERADDRESS+coinCode);
        if(StringUtils.isEmpty(address_str) && address_str.length() > 0){
            address_str=findAddress(coinCode);
            redisService.set(USERADDRESS+coinCode,address_str,RedisService.CACHE_TIME);
        }
        return address_str;
    }
    
}
