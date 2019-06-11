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
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * <p>CustomerAccountRecordController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:48
 */
@RestController("customerAccountRecordRemoteController")
@RequestMapping("/remote")

@Slf4j
public class CustomerAccountRecordRemoteController extends GenericController{

    @Autowired
    private CustomerAccountRecordService customerAccountRecordService;

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * 远程充值接口
     * @param data
     */
    @PostMapping("/depositAccountRecord")
    public void depositAccountRecord(@RequestBody JSONObject data) {
        if(data != null){
            CustomerAccount account=new CustomerAccount();
            String coinCode=data.get("coinCode").toString();
            String address=data.get("address").toString();
            String amount=data.get("money").toString();
            String userId = "";
            String accountId = "";

            QueryFilter filter = new QueryFilter(CustomerAccount.class);

            filter.addFilter("coinCode_EQ",coinCode);
            filter.addFilter("coinAddress_EQ",address);
            account = customerAccountService.get(filter);

            if(account != null){
                userId  = account.getUserId();
                accountId = account.getId();
                customerAccountRecordService.saveAccountRecord(CustomerAccountRecord.RECHARGE,accountId,userId ,coinCode,amount);
            }

        }

    }
}
