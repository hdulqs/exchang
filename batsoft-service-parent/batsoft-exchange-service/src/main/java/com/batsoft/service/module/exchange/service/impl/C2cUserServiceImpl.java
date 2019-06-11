/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-07 09:21:46
 */
package com.batsoft.service.module.exchange.service.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.model.module.exchange.C2cOrder;
import com.batsoft.model.module.exchange.C2cUser;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.exchange.dao.C2cUserDao;
import com.batsoft.service.module.exchange.service.C2cUserService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p> C2cUserServiceImpl </p>
 * @author: LouSir
 * @Date :  2018-05-07 09:21:46
 */
@Service("c2cUserService")
public class C2cUserServiceImpl extends BaseServiceImpl<C2cUser, String> implements C2cUserService {

    public static final String CACHE_C2C = Constants.CACHE_EX_PREFIX + "c2c";
    public static final String CACHE_C2C_ACTIVEUSERS = CACHE_C2C + "c2c_users";  // C2C交易有效商户列表

    @Autowired
    private C2cUserDao c2cUserDao;
    @Autowired
    private RedisService redisService;

    public List<C2cUser> findActiveUsers(){
        QueryFilter queryFilter = new QueryFilter(C2cUser.class);
        queryFilter.addFilter("status=", 1);
        queryFilter.orderBy("sort asc");
        List<C2cUser> list = this.find(queryFilter);
        return list;
    }

    /**
     * 在redis中添加C2C有效商户
     */
    @Override
    public void updateC2cJson() {
        String users = JSON.toJSONString(findActiveUsers());
        redisService.set(CACHE_C2C_ACTIVEUSERS, users, RedisService.CACHE_TIME);
    }

    /**
     * 查询有效的商户列表
     * @return
     */
    @Override
    public List<C2cUser> findActiveUsersByRedis(){
        List<C2cUser> list = new ArrayList<C2cUser>();
        String users = redisService.get(CACHE_C2C_ACTIVEUSERS);
        if(StringUtils.isEmpty(users)){
            list = findActiveUsers();
            updateC2cJson();
        }else{
            list = JSON.parseArray(users,C2cUser.class);
        }
        return list;
    }


}
