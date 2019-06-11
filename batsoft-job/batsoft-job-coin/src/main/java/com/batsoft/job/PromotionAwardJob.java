package com.batsoft.job;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.member.service.UserService;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

@Component
public class PromotionAwardJob {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;
    ExecutorService executorService =  new ThreadPoolExecutor(5,40,3000, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(50));
    ExecutorService listExecutorService =  new ThreadPoolExecutor(10,40,3000, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(150));
    CompletionService<Integer> completionService = new ExecutorCompletionService<>(listExecutorService);
    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

//    @Scheduled(cron = "*/60 * * * * ?")
    public void doJob(){
        //批量方式
        Date now = new Date();
        get(now);
    }

    public  int get(Date time){
            CompletableFuture<Integer> f0 = CompletableFuture.supplyAsync(() -> {
                return userService.findUserHavePromotionAward(time, 1, 3);
            }, executorService).thenApplyAsync((s) -> {
                if (s.size() == 0) {
                    return 0;
                } else {
                    FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return dealCon(s);
                        }
                    });
                  executorService.submit(futureTask);
                    try {
                        int res =  futureTask.get();
                        if(res !=0 && res == s.size() ){//继续递归调用
                          return   get(s.get(s.size()-1).getCreateTime());
                        }else{
                            return 0;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return 0;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            }).exceptionally(new Function<Throwable, Integer>() {
                @Override
                public Integer apply(Throwable throwable) {
                    System.out.println("查询出现问题");
                    return 0;
                }
            });
            return f0.join();
    }



    public int dealCon(List<User> users){
        int rows = 0;
        for (User user:users){
            completionService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    //sql减奖励，加余额
                    String sql = "update member_user set  promotion_award = promotion_award -"+user.getPromotionAward()+" where id = \"" + user.getId() +"\"";
                    int rest = jdbcTemplate.update(sql);
                    if(rest == 1){
                        addAvailable(user.getId(),"BT",user.getPromotionAward());
                    } else {
                        return 0;
                    }
                    return 1;
                }
            });
        }
        for (int i=0;i<users.size();i++){
            try {
                int result = completionService.take().get();
                if(result == 1){
                    rows++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("");
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    private CustomerAccount addAvailable(String userId, String coinCode, BigDecimal money) {
        CustomerAccount result = null;
        String key = String.format(RedisKeyConstant.USER_DCACCOUNT, userId);
        String value = jedisClient.get(JedisDataSourceSignleton.DB1, key);
        if (!StringUtils.isEmpty(value)) {
            List<CustomerAccount> list = JSON.parseArray(value, CustomerAccount.class);
            if (list != null && !list.isEmpty()) {
                for (CustomerAccount account : list) {
                    if (coinCode.equals(account.getCoinCode())) {
                        account.setAvailable(account.getAvailable().add(money));
                        result = account;
                        break;
                    }
                }
                jedisClient.set(JedisDataSourceSignleton.DB1, key, JSON.toJSONString(list));
            }
        }
        return result;
    }




}
