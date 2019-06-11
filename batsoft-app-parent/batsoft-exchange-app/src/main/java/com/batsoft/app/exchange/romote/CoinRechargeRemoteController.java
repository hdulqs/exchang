/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:48
 */
package com.batsoft.app.exchange.romote;


import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.service.module.exchange.service.CoinRechargeService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * <p>CoinRechargeRemoteController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:48
 */
@RestController("coinRechargeRemoteController")
@RequestMapping("/remote")

@Slf4j
public class CoinRechargeRemoteController extends GenericController{

    @Autowired
    private CoinRechargeService coinRechargeService;

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * 远程充值保存订单接口
     * @param data
     */
    @PostMapping("/depositOrder")
    public void depositOrder(@RequestBody JSONObject data) {

        if(data!=null){

            CustomerAccount account=new CustomerAccount();


            String coinCode=data.get("coinCode").toString();
            String address=data.get("address").toString();
            String amount=data.get("money").toString();
            String fromAddress=data.get("fromAddress").toString();
            String hash=data.get("hash").toString();
            String userId="";
            String accountId="";

            QueryFilter filter=new QueryFilter(CustomerAccount.class);

            filter.addFilter("coinCode_EQ",coinCode);
            filter.addFilter("coinAddress_EQ",address);
            account=customerAccountService.get(filter);

            if(account!=null){
                userId=account.getUserId();
                accountId=account.getId();
                coinRechargeService.saveOrder(accountId,userId,coinCode,amount,fromAddress,address,hash);
            }

        }

    }
}
