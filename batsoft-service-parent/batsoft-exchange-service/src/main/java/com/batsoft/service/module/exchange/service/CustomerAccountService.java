/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:20:38 
*/

package com.batsoft.service.module.exchange.service;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.exchange.vo.DepostPkData;

import java.util.List;

/**
* <p>CustomerAccountService</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:20:38 
*/
public interface CustomerAccountService  extends BaseService<CustomerAccount, String>{

    /**
     * 查询我的帐号数据
     * @param  status 0 关闭 1 正常  2 下架 coin 状态
     * @return
     */
    List<CustomerAccountVo> findList(Integer status);

    /**
     *具体币种数据
     * @return
     */
    CustomerAccountVo findCoinAccount(String userId,String coinCode);

    /**
     * 查询数据的币种账户
     * @param userId
     * @return
     */
    List<CustomerAccountVo> findCoinAccounts(String userId);
    /**
     * 登录初始化用户账户到redis账户中
     * 规则1：如果当前redis账户不存在则初始化
     * 规则2：如果redis账户金额为0则初始化金额到redis中
     * @param userId
     */
    void flushRedisAccountByCustomerId(String userId);


    /**
     * mq初始化账户
     * @param message
     * @return
     */
    JsonResult saveMultipleFinanceByMQ(String message);

    /**
     * 手动充币
     * @param data
     * @return
     */
    JsonResult saveDepostData(DepostPkData data);


    /**
     * 检查账户
     * @param userId
     * @return
     */
    JsonResult findCheckAccout(String userId);

    /**
     * 查找是否ERC20 代币
     * @param id
     * @param coinCode
     * @return 如果是返回ETH 地址 否则返回 null
     */
    JSONObject findCoinAddress(String id, String coinCode);

    /**
     * 更新用户地址
     * @param userId
     * @param coinCode
     * @param address
     */
    void updateAccountAddress(String userId,String coinCode,String address);

    /**
     * 新加币种初始化用户信息
     * @param message
     * @return
     */
    JsonResult saveAccountByNewCoinOnMq(String message);

    /**
     * 初始化用户地址到缓存 主要为读区块地址 如ETH 等
     * @param coinCode
     */
    String addAdressToRedis(String coinCode);

    /**
     * 保存用户一个地址到缓存 主要为读区块地址 如ETH 等
     * @param coinCode
     */
    String addOneAdressToRedis(String coinCode,String address);

    /**
     * 获取用户地址
     * @param coinCode
     * @return
     */
    String findAddressFromRedis(String coinCode);
}
