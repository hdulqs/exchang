/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:16:15 
*/
package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.*;
import com.batsoft.model.module.exchange.vo.C2cOrderVo;
import com.batsoft.model.module.exchange.vo.C2cUserVo;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.member.Bankcard;
import com.batsoft.model.module.member.User;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.exchange.dao.C2cOrderDao;
import com.batsoft.service.module.exchange.service.C2cOrderService;
import com.batsoft.service.module.exchange.service.C2cProductService;
import com.batsoft.service.module.exchange.service.C2cUserService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.util.MessageUtil;
import com.batsoft.service.module.exchange.trade.util.SqlUtil;
import com.batsoft.service.module.member.service.BankcardService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
* <p> C2cOrderServiceImpl </p>
* @author: LouSir
* @Date :  2018-05-07 09:16:15 
*/
@Service("c2cOrderService")
public class C2cOrderServiceImpl extends BaseServiceImpl<C2cOrder, String> implements C2cOrderService{

	@Autowired
	private C2cOrderDao c2cOrderDao;
	
	@Autowired
	private C2cUserService c2cUserService;
	
	@Autowired
	private BankcardService bankcardService;
	
	@Autowired
	private CustomerAccountService customerAccountService;
	
	@Autowired
	private C2cProductService c2cProductService;
	
	@Autowired
	private RabbitMqSender rabbitMqSender;
	
    /**
     * 查询对应币种的买卖记录
     * @param coinCode
     * @param type
     * @param start
     * @param limit
     * @return
     */
    @Override
    public JsonResult findUserOrders(String coinCode,Integer type,int start,int limit){
        JsonResult jsonResult = new JsonResult();
        try {
            List<C2cOrder> orders = c2cOrderDao.findUserOrders(coinCode, type, start, limit);
            List<C2cOrderVo> vos = new ArrayList<C2cOrderVo>();
            if(orders!=null&&orders.size()>0){
                for(C2cOrder order:orders){
                    C2cOrderVo vo = new C2cOrderVo();
                    //名称掩码修改
                    if(order.getTransactionName()!=null&&order.getTransactionName().length()>3){
                        String nameStr = order.getTransactionName().substring(0,3)+"*****"+
                               order.getTransactionName().substring(order.getTransactionName().length()-3,order.getTransactionName().length()-1);
                        vo.setUserNameStr(nameStr);
                    }else{
                        vo.setUserNameStr(order.getTransactionName());
                    }
                    // 转入转出
                    if(order.getOperationType()!=null&&order.getOperationType()==1){
                        vo.setOperationTypeStr("卖出");
                    }else{
                        vo.setOperationTypeStr("买入");
                    }
                    // 状态
                    if(order.getOpeationState()!=null){
                        switch (order.getOpeationState()){
                            case 0: vo.setOpeationStateStr("处理中");break;
                            case 1:vo.setOpeationStateStr("成功");break;
                            case 2:vo.setOpeationStateStr("失败");break;
                            default:vo.setOpeationStateStr("处理中");break;
                        }
                    }
                    vo.setNumber(order.getNumber());
                    vo.setCoinCode(order.getCoinCode());
                    vos.add(vo);
                }
            }
            jsonResult.setData(vos);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);

        }catch (Exception e){
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);
            jsonResult.setMsg("查询失败-"+e.getMessage());
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * 查询用户c2c交易记录
     * @param coinCode  币种
     * @param opeationState 状态
     * @param type  业务类型
     * @param start
     * @param limit
     * @return
     */
    @Override
    public JsonResult findOrderByUserId(String coinCode,Integer opeationState,Integer type,Integer start,Integer limit){
        JsonResult jsonResult = new JsonResult();
        try {
            User user = UserUtils.getUser();
            List<C2cOrderVo> vos = new ArrayList<C2cOrderVo>();
            List<C2cOrder> userOrders = c2cOrderDao.findOrderByUserId(user.getId(), coinCode,
                    opeationState, type, start, limit);
            if (userOrders != null && userOrders.size() > 0) {
                for(C2cOrder order:userOrders){
                    C2cOrderVo vo = new C2cOrderVo();
//                    BeanUtils.copyProperties(vo,order);
                    vo.setId(order.getId());
                    vo.setTradeTime(order.getTradeTime());
                    vo.setOperationTypeStr(order.getOperationType()==2?"卖出":"买入");
                    vo.setNumber(order.getNumber());
                    vo.setPrice(order.getPrice());
                    vo.setOpeationMoney(order.getOpeationMoney());
                    String stateStr = "";
                    switch (order.getOpeationState()){
                        case 0:stateStr="处理中";break;
                        case 1:stateStr="成功";break;
                        case 2:stateStr="失败";break;
                        default:stateStr="处理中";break;
                    }
                    vo.setOpeationStateStr(stateStr);
                    vos.add(vo);
                }
            }
            jsonResult.setSuccess(true);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setData(vos);
            jsonResult.setMsg("查询成功");
        }catch (Exception e ){
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg("查询失败-"+e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 查询代币买卖自定义价格
     * @param coinCode 币种
     * @param type  买卖类型
     * @return
     */
    @Override
    public BigDecimal findPriceByCoinCode(String coinCode,String type){
        BigDecimal price = BigDecimal.ZERO;
        if("buy".equals(type)){
            if("QQ".equals(coinCode)){
                price = new BigDecimal("1");
            }else if("USDT".equals(coinCode)){
                price = new BigDecimal("6.7");
            }
        }else{
            if("QQ".equals(coinCode)){
                price = new BigDecimal("0.9");
            }else if("USDT".equals(coinCode)){
                price = new BigDecimal("6.6");
            }
        }
        return price;
    }

    /**
     * 根据订单号获得商户信息
     * @param id
     * @return
     */
    @Override
    public C2cUserVo findC2cUserVoById(String id){
        C2cOrder c2cOrder = this.get(id);
        C2cUserVo c2cUserVo = new C2cUserVo();
        if(c2cOrder!=null&&StringUtils.isNotEmpty(c2cOrder.getTransactionName())){
            C2cUser c2cUser = c2cUserService.get(c2cOrder.getTransactionId());

            if(c2cUser!=null){
                //吧银行信息返回给前端
                c2cUserVo.setUserName(c2cUser.getUserName());
                c2cUserVo.setBankName(c2cUser.getBankName());
                c2cUserVo.setBranchName(c2cUser.getBranchName());
                c2cUserVo.setTrueName(c2cUser.getTrueName());
                c2cUserVo.setCardNumber(c2cUser.getCardNumber());
                c2cUserVo.setRemark(c2cOrder.getRemark());
                c2cUserVo.setOpeationMoney(c2cOrder.getOpeationMoney());
                c2cUserVo.setCoinCode(c2cOrder.getCoinCode());
            }
        }
        return c2cUserVo;
    }

    /**
     * 插入c2c交易订单记录
     * @param c2cOrder
     * @return buytype 购买类型
     */
    @Override
    public JsonResult addTransactionOrder(C2cOrder c2cOrder,int buytype){
        JsonResult jsonResult = new JsonResult();
        String tableDate = DateUtils.dateFormatToString(new Date(),DateUtils.TABLES_DAY_FIX);
        try{
            User user = UserUtils.getUser();
            if(user!=null) {
                //判断订单是否满足上下限
                C2cProduct product = c2cProductService.getProductByCode(c2cOrder.getCoinCode());
                if(product!=null){
                    if(product.getMinNumber()!=null&&product.getMinNumber().compareTo(c2cOrder.getNumber())>0){
                        jsonResult.setSuccess(false);
                        jsonResult.setCode(Constants.FAILED);
                        jsonResult.setMsg("最小交易量为："+product.getMinNumber());
                        return jsonResult;
                    }
                    if(product.getMaxNumber()!=null&&product.getMaxNumber().compareTo(c2cOrder.getNumber())<0){
                        jsonResult.setSuccess(false);
                        jsonResult.setCode(Constants.FAILED);
                        jsonResult.setMsg("最大交易量为："+product.getMaxNumber());
                        return jsonResult;
                    }
                }else{
                    jsonResult.setSuccess(false);
                    jsonResult.setCode(Constants.FAILED);
                    jsonResult.setMsg("该币种还未上架");
                    return jsonResult;
                }
                //查询用户银行卡信息
                QueryFilter queryFilter = new QueryFilter(Bankcard.class);
                queryFilter.addFilter("user_id=", user.getId());
                queryFilter.addFilter("status=", 1);
                List<Bankcard> cards = bankcardService.find(queryFilter);
                if (cards != null && cards.size() > 0) {
                    c2cOrder.setUserId(user.getId());
                    c2cOrder.setUserName(user.getUserName());
                    //查询客户币账户信息
                    CustomerAccountVo accountVo = customerAccountService.findCoinAccount(UserUtils.getUser().getId(),c2cOrder.getCoinCode());
                    if(accountVo!=null){
                        c2cOrder.setAccountId(accountVo.getId());
                    }else{
//                        c2cOrder.setAccountId("0001");
                        jsonResult.setSuccess(false);
                        jsonResult.setCode(Constants.FAILED);
                        jsonResult.setMsg("您的账户还没有交易该币的权限,请联系客户");
                        return jsonResult;
                    }
                    //查询商户号
                    List<C2cUser> c2cUsers = c2cUserService.findActiveUsersByRedis();
                    if (c2cUsers != null && c2cUsers.size() > 0) {
                        if (c2cUsers.size() > 0) {
                            Random rand = new Random();  //在合适商户用随机选取
                            int size = c2cUsers.size();
                            int n = rand.nextInt(size);
                            C2cUser c2cUser = c2cUsers.get(n);
                            if (c2cUser != null) {
                                c2cOrder.setTransactionId(c2cUser.getId());
                                c2cOrder.setTransactionName(c2cUser.getUserName());
                                c2cOrder.setTraBankcard(c2cUser.getCardNumber());
                                c2cOrder.setTraBankname(c2cUser.getBankName());
                                c2cOrder.setOpeationState(C2cOrder.OPEATIONSTATE0);
                                c2cOrder.setRemark((int)((Math.random() * 9 + 1) * 100000) + "");  //设置银行转账备注
                                if(c2cOrder.getOperationType()==1){
                                    //买入不需查询账户相关币种可用金额
                                    //查询客户是否有未交易的充值订单
                                    List<C2cOrder> userOrders = c2cOrderDao.findOrderByUserId(user.getId(),c2cOrder.getCoinCode(),
                                            0,1,0,2);
                                    if(userOrders!=null&&userOrders.size()>0){
                                        jsonResult.setSuccess(false);
                                        jsonResult.setCode(Constants.FAILED);
                                        jsonResult.setMsg("您还有未处理的充值订单");
                                        return jsonResult;
                                    }
                                    //重新按照产品中的定价计算（买入）
                                    c2cOrder.setPrice(product.getBuyPrice());
                                    if(C2cOrder.OPERATIONTYPEBUYTYPE_NUM.equals(buytype)){
                                        c2cOrder.setOpeationMoney(product.getBuyPrice().multiply(c2cOrder.getNumber()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    }else  {
                                        //如果是按购买金额计算
                                        c2cOrder.setOpeationMoney(c2cOrder.getNumber());
                                        BigDecimal num = c2cOrder.getNumber().divide(product.getBuyPrice(),2,BigDecimal.ROUND_HALF_UP);
                                        c2cOrder.setNumber(num);
                                    }
                                }else if(c2cOrder.getOperationType()==2){
                                    //查询用户对应币种账户可用余额
                                    BigDecimal money = accountVo==null?BigDecimal.ZERO:accountVo.getAvailable();
                                    if(money!=null&&money.compareTo(c2cOrder.getNumber())>=0){
                                        //重新按照产品中的定价计算（卖出）
                                        c2cOrder.setPrice(product.getSellPrice());
                                        c2cOrder.setOpeationMoney(product.getSellPrice().multiply(c2cOrder.getNumber()).setScale(2,BigDecimal.ROUND_HALF_UP));
                                    }else{
                                        jsonResult.setSuccess(false);
                                        jsonResult.setCode(Constants.FAILED);
                                        jsonResult.setMsg("交易金额不足");
                                        return jsonResult;
                                    }
                                }else{
                                    jsonResult.setSuccess(false);
                                    jsonResult.setCode(Constants.FAILED);
                                    jsonResult.setMsg("未找到该交易类型");
                                    return jsonResult;
                                }
                                //必填信息校验
                                if(beanValidatorForJson(c2cOrder)){
                                    if(c2cOrder.getOperationType()==2) {
                                        //调用账户冻结方法
                                        rabbitMqSender.toRedisAccount(MessageUtil.addCoinAvailable(c2cOrder.getUserId(), c2cOrder.getCoinCode(), c2cOrder.getNumber().multiply(new BigDecimal("-1"))));
                                        rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(c2cOrder.getUserId(), c2cOrder.getCoinCode(), c2cOrder.getNumber()));

                                        StringBuffer sql = new StringBuffer("");
                                        //增加冻结流水
                                        CustomerAccountFreeze record = new CustomerAccountFreeze();
                                        record.setFreezeType(CustomerAccountFreeze.C2C);
                                        record.setAccountId(c2cOrder.getAccountId());
                                        record.setCustomerId(c2cOrder.getUserId());
                                        record.setCoinCode(c2cOrder.getCoinCode());
                                        record.setOrderId(c2cOrder.getTraNumber());
                                        record.setFreezeMoney(c2cOrder.getNumber());
                                        sql.append(SqlUtil.createSql(record,tableDate));
                                        rabbitMqSender.toSql(sql.toString());

                                    }
                                    this.save(c2cOrder);
                                    //吧银行信息返回给前端
//                                    C2cUserVo c2cUserVo = findC2cUserVoById(c2cOrder.getId());
                                    /*C2cUserVo c2cUserVo = new C2cUserVo();
                                    c2cUserVo.setUserName(c2cUser.getUserName());
                                    c2cUserVo.setBankName(c2cUser.getBankName());
                                    c2cUserVo.setBranchName(c2cUser.getBranchName());
                                    c2cUserVo.setTrueName(c2cUser.getTrueName());
                                    c2cUserVo.setCardNumber(c2cUser.getCardNumber());
                                    c2cUserVo.setRemark(c2cOrder.getRemark());
                                    c2cUserVo.setOpeationMoney(c2cOrder.getOpeationMoney());
                                    c2cUserVo.setCoinCode(c2cOrder.getCoinCode());*/

                                    jsonResult.setData(c2cOrder.getId());
                                    jsonResult.setSuccess(true);
                                    jsonResult.setMsg("交易成功");
                                    jsonResult.setCode(Constants.SUCCESS);
                                }else{
                                    jsonResult.setSuccess(false);
                                    jsonResult.setCode(Constants.FAILED);
                                    jsonResult.setMsg("必填信息缺失");
                                }
                            }
                        } else {
                            jsonResult.setSuccess(false);
                            jsonResult.setCode(Constants.FAILED);
                            jsonResult.setMsg("没有出售" + c2cOrder.getCoinCode() + "的用户");
                        }
                    } else {
                        jsonResult.setSuccess(false);
                        jsonResult.setCode(Constants.FAILED);
                        jsonResult.setMsg("没有出售" + c2cOrder.getCoinCode() + "的用户");
                    }
                } else {
                    jsonResult.setSuccess(false);
                    jsonResult.setCode(Constants.FAILED);
                    jsonResult.setMsg("请绑定银行卡后再进行交易");
                }
            }else{
                jsonResult.setSuccess(false);
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setMsg("您还未登录");
                jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
                return jsonResult;
            }
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg("购买失败-"+e.getMessage());
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * c2c订单审核通过方法
     * @param id
     * @return
     */
    @Override
    public JsonResult c2cPass(String id){
        JsonResult jsonResult = new JsonResult();
        C2cOrder order = this.get(id);
        String tableDate = DateUtils.dateFormatToString(new Date(),DateUtils.TABLES_DAY_FIX);
        try {
            if (order != null&&C2cOrder.OPEATIONSTATE0.equals(order.getOpeationState())) {
                //买入订单  直接给对应的账户转币即可
                if(C2cOrder.OPERATIONTYPE1.equals(order.getOperationType())){
                    rabbitMqSender.toRedisAccount(MessageUtil.addCoinAvailable(order.getUserId(),order.getCoinCode(),order.getNumber()));
                    order.setOpeationState(C2cOrder.OPEATIONSTATE1);
                    this.update(order);
                    //实际流水增加
                    CustomerAccountRecord record = new CustomerAccountRecord();
                    record.setType(CustomerAccountRecord.C2C);
                    record.setAccountId(order.getAccountId());
                    record.setCustomerId(order.getUserId());
                    record.setCoinCode(order.getCoinCode());
                    record.setOrderId(order.getTraNumber());
                    record.setMoney(order.getNumber());
                    //同步sql消息
                    rabbitMqSender.toSql(SqlUtil.createSql(record,tableDate));
                }
                if(C2cOrder.OPERATIONTYPE2.equals(order.getOperationType())){
                    rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(order.getUserId(),order.getCoinCode(),order.getNumber().multiply(new BigDecimal("-1"))));
                    order.setOpeationState(C2cOrder.OPEATIONSTATE1);
                    this.update(order);
                    StringBuffer sql = new StringBuffer("");
                    //实际流水减少
                    CustomerAccountRecord record = new CustomerAccountRecord();
                    record.setType(CustomerAccountRecord.C2C);
                    record.setAccountId(order.getAccountId());
                    record.setCustomerId(order.getUserId());
                    record.setCoinCode(order.getCoinCode());
                    record.setOrderId(order.getTraNumber());
                    record.setMoney(order.getNumber().multiply(new BigDecimal(-1)));
                    sql.append(SqlUtil.createSql(record,tableDate));
                    //减少冻结流水
                    CustomerAccountFreeze record1 = new CustomerAccountFreeze();
                    record1.setFreezeType(CustomerAccountFreeze.C2C);
                    record1.setAccountId(order.getAccountId());
                    record1.setCustomerId(order.getUserId());
                    record1.setCoinCode(order.getCoinCode());
                    record1.setOrderId(order.getTraNumber());
                    record1.setFreezeMoney(order.getNumber().multiply(new BigDecimal(-1)));
                    sql.append(SqlUtil.createSql(record1,tableDate));
                    rabbitMqSender.toSql(sql.toString());
                }
                jsonResult.setCode(Constants.SUCCESS);
                jsonResult.setSuccess(true);
                jsonResult.setMsg("操作成功");
            }else{
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setSuccess(false);
                jsonResult.setMsg("该笔订单有误");
            }
        }catch (Exception e){
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
            jsonResult.setMsg("订单有误-"+e.getMessage());
        }
        return jsonResult;
    }

    /**
     * c2c订单审核拒绝方法
     * @param id
     * @return
     */
    @Override
    public JsonResult c2cRefuse(String id){
        String tableDate = DateUtils.dateFormatToString(new Date(),DateUtils.TABLES_DAY_FIX);
        JsonResult jsonResult = new JsonResult();
        C2cOrder order = this.get(id);
        try {
            if (order != null&&C2cOrder.OPEATIONSTATE0.equals(order.getOpeationState())) {
                //买入订单  不用操作数据
                if(C2cOrder.OPERATIONTYPE1.equals(order.getOperationType())){
                    order.setOpeationState(C2cOrder.OPEATIONSTATE2);
                    this.update(order);
                }
                //卖出操作 需要把冻结金额减去  可用金额加上
                if(C2cOrder.OPERATIONTYPE2.equals(order.getOperationType())){
                    rabbitMqSender.toRedisAccount(MessageUtil.addCoinAvailable(order.getUserId(),order.getCoinCode(),order.getNumber()));
                    rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(order.getUserId(),order.getCoinCode(),order.getNumber().multiply(new BigDecimal("-1"))));
                    order.setOpeationState(C2cOrder.OPEATIONSTATE2);
                    this.update(order);
                    //减少冻结流水
                    CustomerAccountFreeze record = new CustomerAccountFreeze();
                    record.setFreezeType(CustomerAccountFreeze.C2C);
                    record.setAccountId(order.getAccountId());
                    record.setCustomerId(order.getUserId());
                    record.setCoinCode(order.getCoinCode());
                    record.setOrderId(order.getTraNumber());
                    record.setFreezeMoney(order.getNumber().multiply(new BigDecimal(-1)));
                    rabbitMqSender.toSql(SqlUtil.createSql(record,tableDate));
                }
                jsonResult.setCode(Constants.SUCCESS);
                jsonResult.setSuccess(true);
                jsonResult.setMsg("操作成功");
            }else{
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setSuccess(false);
                jsonResult.setMsg("该笔订单有误");
            }
        }catch (Exception e){
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
            jsonResult.setMsg("订单有误-"+e.getMessage());
        }
        return jsonResult;
    }


}
