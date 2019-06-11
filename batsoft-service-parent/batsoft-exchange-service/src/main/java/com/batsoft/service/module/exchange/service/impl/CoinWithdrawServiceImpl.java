/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 17:51:09
 */
package com.batsoft.service.module.exchange.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.model.module.exchange.vo.CoinRechargeVo;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.exception.BusinessRuntimeException;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.core.transaction.RedisDistributedLock;
import com.batsoft.core.transaction.RedisDistributedLock.RedisDistributedKeyEnum;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.CoinWithdraw;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.CustomerAccountFreeze;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.vo.CoinWithdrawVo;
import com.batsoft.model.module.member.User;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.blockchain.service.DepositWalletService;
import com.batsoft.service.module.exchange.dao.CoinWithdrawDao;
import com.batsoft.service.module.exchange.service.CoinService;
import com.batsoft.service.module.exchange.service.CoinWithdrawService;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.util.MessageUtil;
import com.batsoft.service.module.exchange.trade.util.SqlUtil;
import com.batsoft.service.module.exchange.trade.util.UUIDUtil;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;

/**
 * <p> CoinWithdrawServiceImpl </p>
 *
 * @author: Bat Admin
 * @Date :  2018-04-19 17:51:09
 */
@Service("coinWithdrawService")
public class CoinWithdrawServiceImpl extends BaseServiceImpl<CoinWithdraw, String> implements CoinWithdrawService {

	@Autowired
	private CoinWithdrawDao coinWithdrawDao;
	
	@Autowired
	private CoinService coinService;
	
	@Autowired
	private CustomerAccountService customerAccountService;
	
	@Autowired
	private RabbitMqSender rabbitMqSender;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DepositWalletService depositWalletService;
	


	/**
	 * 判断提现逻辑  并记录到数据库中
	 *
	 * @param address
	 * @param memo
	 * @param amt
	 * @param symbol
	 * @return
	 */
	@Override
	public JsonResult saveWithdrawOrder(String address, String memo, String amt, String symbol) {
		JsonResult jsonResult = new JsonResult();
		String tableDate = DateUtils.dateFormatToString(new Date(),DateUtils.TABLES_DAY_FIX);
		String userId = UserUtils.getUser().getId();
        String lockVal = UUIDUtil.getUUID();
        String personLockKey = String.format(RedisDistributedKeyEnum.ENTRUST_TRADE_PERSION_KEY.getValue(), userId);
        RedisDistributedLock lock = new RedisDistributedLock();
		CoinWithdraw coinWithdraw = null;
		try {
			Coin coin = coinService.findWithdrawData(symbol);
            if(coin == null){
                throw new BusinessRuntimeException("提现币种不存在");
            }
            // TD.增加分布式锁
            lock.lock(personLockKey, lockVal);
			if (valid_withdraw(coin, new BigDecimal(amt))) {
				//获取客户该币种账户
				coinWithdraw = new CoinWithdraw();
				CustomerAccount account = customerAccountService.findCoinAccount(userId, coin.getCoinCode());
				Map<String, BigDecimal> data = compute(new BigDecimal(amt), coin.getWithdrawFeeType(), coin.getWithdrawRate(), coin.getWithdrawBaseFee());
				coinWithdraw.setActualCount(data.get("actual_count"));
				coinWithdraw.setCoinCode(symbol);
				coinWithdraw.setCoinCount(new BigDecimal(amt));
				coinWithdraw.setFee(data.get("fee_count"));
				coinWithdraw.setToAddress(address);
				coinWithdraw.setMemo(memo);
				coinWithdraw.setTxOrder(StringUtils.createOrderNum());
				coinWithdraw.setUserId(userId);
				coinWithdraw.setAccountId(account.getId());
				coinWithdraw.setUserName(UserUtils.getUser().getUserName());
				coinWithdraw.setUserMobile(UserUtils.getUser().getUserMobile());
				coinWithdraw.setUserEmail(UserUtils.getUser().getUserEmail());
				coinWithdraw.setCreateTime(new Date());

				//  未设置提币状态----
//                coinWithdraw.setStatus(0);
				//设置冻结金额和可用金额
				rabbitMqSender.toRedisAccount(MessageUtil.addCoinAvailable(userId, symbol, new BigDecimal(amt).multiply(new BigDecimal("-1"))));
				rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(userId, symbol, new BigDecimal(amt).multiply(new BigDecimal("1"))));
				StringBuffer sql = new StringBuffer("");
				//增加冻结流水
				CustomerAccountFreeze record = new CustomerAccountFreeze();
				record.setFreezeType(CustomerAccountFreeze.WITHDRAW);
				record.setAccountId(coinWithdraw.getAccountId());
				record.setCustomerId(coinWithdraw.getUserId());
				record.setCoinCode(coinWithdraw.getCoinCode());
				record.setOrderId(coinWithdraw.getTxOrder());
				record.setFreezeMoney(coinWithdraw.getActualCount().add(coinWithdraw.getFee()));
				sql.append(SqlUtil.createSql(record,tableDate));
				rabbitMqSender.toSql(sql.toString());

				//  判断免审金额  如果小于免审金额  直接设置为成功 并向地址转币 且修改币账户金额
//				if (coin.getWithdrawAcMaxAmt() != null && new BigDecimal(amt).compareTo(coin.getWithdrawAcMaxAmt()) <= 0) {
//					//提币数量小于免审金额
//					coinWithdraw.setStatus(CoinWithdraw.STATUS0);  //直接设置为成功？  无需后台审核
//					//对接提币操作
//					//    .....do withdraw    李泉豫修改
//					JSONObject data1 = new JSONObject();
//					data1.put("coinCode", coinWithdraw.getCoinCode());
//					data1.put("amount", coinWithdraw.getActualCount());
//					data1.put("toAddress", coinWithdraw.getToAddress());
//
//					System.out.println("直接通过提币参数" + JSON.toJSONString(data1));
//					JsonResult jsonResult1 = depositWalletService.sendCoin(data1);
//
////					boolean withdrawSucess = true;
//					if (jsonResult1.getSuccess()) {
//						rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(userId, symbol, new BigDecimal(amt).multiply(new BigDecimal("-1"))));
//						//rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(coinWithdraw.getUserId(), coinWithdraw.getCoinCode(), coinWithdraw.getCoinCount().multiply(new BigDecimal("-1"))));
//						StringBuffer sql1 = new StringBuffer("");
//						//实际流水减少
//						CustomerAccountRecord record1 = new CustomerAccountRecord();
//						record1.setType(CustomerAccountRecord.WITHDRAW);
//						record1.setAccountId(coinWithdraw.getAccountId());
//						record1.setCustomerId(coinWithdraw.getUserId());
//						record1.setCoinCode(coinWithdraw.getCoinCode());
//						record1.setOrderId(coinWithdraw.getTxOrder());
//						record1.setMoney((coinWithdraw.getActualCount().add(coinWithdraw.getFee())).multiply(new BigDecimal("-1")));
//						sql1.append(SqlUtil.createSql(record1,tableDate));
//
//						//减少冻结流水
//						CustomerAccountFreeze record2 = new CustomerAccountFreeze();
//						record2.setFreezeType(CustomerAccountFreeze.WITHDRAW);
//						record2.setAccountId(coinWithdraw.getAccountId());
//						record2.setCustomerId(coinWithdraw.getUserId());
//						record2.setCoinCode(coinWithdraw.getCoinCode());
//						record2.setOrderId(coinWithdraw.getTxOrder());
//						record2.setFreezeMoney((coinWithdraw.getActualCount().add(coinWithdraw.getFee())).multiply(new BigDecimal("-1")));
//						sql1.append(SqlUtil.createSql(record2,tableDate));
//						rabbitMqSender.toSql(sql1.toString());
//						coinWithdraw.setStatus(CoinWithdraw.STATUS1);  //直接设置为成功？  无需后台审核
//						coinWithdraw.setHashTx(jsonResult1.getData() + ""); //李泉豫修改 set交易成功hash值
//						coinWithdraw.setTxOrder(jsonResult1.getData() + "");//他的逻辑是orderid  所以就写在这里边成功之后 orderid变成hash
//					}else{
//						coinWithdraw.setStatus(CoinWithdraw.STATUS0);
//					}
//				} else {
				//提币数量大于免审金额需后台审核
				coinWithdraw.setStatus(CoinWithdraw.STATUS0);
				jsonResult.setSuccess(true);
				jsonResult.setMsg(Language.L("apply_withdraw_success"));
			} else {
				jsonResult.setSuccess(true);
				jsonResult.setMsg(Language.L("apply_withdraw_success"));
			}
		} catch (BusinessRuntimeException var){
			jsonResult.setSuccess(false);
			jsonResult.setMsg(var.getMessage());
        } catch (Exception e) {
			jsonResult.setSuccess(true);
			jsonResult.setMsg(Language.L("apply_withdraw_success"));
			e.printStackTrace();
		} finally {
			// TD.释放分布式锁
			if(coinWithdraw!=null){
				save(coinWithdraw);
			}
			lock.unLock(personLockKey, lockVal, 0);
		}
		return jsonResult;
	}

	/**
	 * 计算手续费
	 * (feeType==0: 固定手续费，实际到账=申请金额-基础手续费-fee
	 * (feeType==1: 百分比手续费，实际到账=申请金额-基础手续费-fee*money
	 *
	 * @param money
	 * @param feeType 手续费类型 01 固定手续费 1为按照百分比收取手续费用
	 * @param fee
	 * @param baseFee
	 * @return
	 */
	private Map<String, BigDecimal> compute(BigDecimal money, Integer feeType, BigDecimal fee, BigDecimal baseFee) {
		//实际提币金额
		BigDecimal actual_count = BigDecimal.ZERO;
		//手续费
		BigDecimal fee_count = BigDecimal.ZERO;
		Map<String, BigDecimal> retMoney = new HashMap<>();
		if (Coin.FEETYPE0.equals(feeType)) {
			fee_count = baseFee.add(fee).setScale(5,BigDecimal.ROUND_DOWN);
			actual_count = money.subtract(fee_count).setScale(5,BigDecimal.ROUND_DOWN);
		} else {
			fee_count = baseFee.add(money.multiply(fee).multiply(new BigDecimal(0.01))).setScale(5,BigDecimal.ROUND_DOWN);
			actual_count = money.subtract(fee_count).setScale(5,BigDecimal.ROUND_DOWN);
		}
		retMoney.put("actual_count", actual_count);
		retMoney.put("fee_count", fee_count);
		return retMoney;
	}

	/**
	 * 提币校验
	 * 1.校验金额格式.>0 &&大于基础费用
	 * 2.满足最小提币数量
	 * 3.满足单日最大提币数量
	 * 4.满足未实名最大可提币数量
	 * 5.账户于额是否>=money
	 * 6. 该币是否允许提现
	 * 7.是否少于基础手续费
	 *
	 * @param coin
	 * @param money
	 * @return
	 */
	private boolean valid_withdraw(Coin coin, BigDecimal money) {
        if(money.compareTo(BigDecimal.ZERO)<=0){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_AVALIABLE_MORE_THAN_ZERO"));
        }
        //判读手续费类型，0是固定费用，1是百分比手续费用
        if(coin.getWithdrawFeeType().equals(Coin.FEETYPE0)&&money.compareTo(coin.getWithdrawBaseFee() == null ? BigDecimal.ZERO : coin.getWithdrawBaseFee()) <= 0){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_AVALIABLE_COIN_LESS_FEE"));
        }
        if(coin.getWithdrawFeeType().equals(Coin.FEETYPE1)&&money.compareTo(coin.getWithdrawRate()==null? BigDecimal.ZERO :coin.getWithdrawRate().multiply(money))<=0){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_AVALIABLE_COIN_LESS_FEE"));
        }
        //判断是否允许提现
        if(!coin.getAllowWithdraw().equals(Coin.ALLOWDEPOSIT1) ){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_APPlY_CLOSE"));
        }
        User user = UserUtils.getUser(false);
        //获取客户该币种账户
		CustomerAccount account = RedisUserUtil.getByCoinCode(user.getId(), coin.getCoinCode());
        if(account==null){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_NO_TRADE_COIN"));
        }
        //判断用户余额是否充足
        if(money.compareTo(account.getAvailable() == null ? BigDecimal.ZERO : account.getAvailable()) > 0){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_APPLY_NO_AVALIABLE"));
        }
        //判断币种有没有设置最小提现笔数，如果设置了最小提现币数，判断用户提现数额，如果小于最小提现数量抛出异常
        if(coin.getWithdrawMin()!=null && money.compareTo(coin.getWithdrawMin())<0){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_APPLY_NUM_LESS_THAN_MIN_NUM"));
        }
        //获取用户当日提现数量
        BigDecimal daysMoney = coinWithdrawDao.getTotleMoneyByDays(user.getId(), "day",coin.getCoinCode());
        //判断用户是否有实名认证 1表示已经通过实名认证  //如果设置了最大提现数量，并且当次提现数量和当日已经提现数量累加超过最大提现数量抛出异常
        if(user.getRealState() .equals(User.REALSTATE_SUCCESS) &&
                (coin.getWithdrawMax() != null && (money.add(daysMoney)).compareTo(coin.getWithdrawMax()) > 0)){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_APPLY_NUM_MORE_THAN_MAX_NUM")+coin.getWithdrawMax());
        }else if(coin.getNoAuthWithdrawMax() != null && (money.add(daysMoney)).compareTo(coin.getNoAuthWithdrawMax()) > 0){
            throw new BusinessRuntimeException(Language.L("MSG_COIN_NO_AUTHUSER_APPLY_NUM_MORE_THAN_MAX_NUM")+coin.getNoAuthWithdrawMax());
        }
        return Boolean.TRUE;
	}

	/**
	 * c查询用户提现记录
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public PageResult findList(Integer page,Integer pageSize,String userId) {
		PageResult pageResult = new PageResult();
		int from = pageSize*(page-1);
		Map<String,Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("from", from);
		param.put("pageSize", pageSize);
		List<CoinWithdrawVo>  datalist = coinWithdrawDao.findList(param);
		Long rows = coinWithdrawDao.findListTotal(param);
		pageResult.setRows(datalist);
		pageResult.setPageSize(pageSize);
		pageResult.setPage(page);
		pageResult.setTotal(rows);
		return pageResult;
	}

	/**
	 * 后台提现审核
	 *
	 * @param status
	 * @return
	 */
	@Override
	public JsonResult withdrawReview(String[] pks, Integer status) {
		String tableDate = DateUtils.getDateStr(new Date(),DateUtils.TABLES_DAY_FIX);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(false);
		Object[] ret = new Object[]{false, "处理失败"};
		if (pks != null && pks.length > 0) {
			for (String pk : pks) {
				try {
					CoinWithdraw coinWithdraw = get(pk);
					//进行提现审核验证
					if(coinWithdraw == null){
						jsonResult.setSuccess(false);
						jsonResult.setMsg("不存在这样的记录");
						return jsonResult;
					}
					if (coinWithdraw.getStatus().equals( CoinWithdraw.STATUS0)) {
						//审核通过
						if (status.equals(CoinWithdraw.STATUS1)) {
							JSONObject data = new JSONObject();
							data.put("coinCode", coinWithdraw.getCoinCode());
							data.put("amount", coinWithdraw.getActualCount());
							data.put("toAddress", coinWithdraw.getToAddress());
							System.out.println("审核通过参数" + JSON.toJSONString(data));
							JsonResult jsonResult1 = depositWalletService.sendCoin(data);
							if (jsonResult1.getSuccess()) {
								rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(coinWithdraw.getUserId(), coinWithdraw.getCoinCode(), coinWithdraw.getCoinCount().multiply(new BigDecimal("-1"))));
								StringBuffer sql1 = new StringBuffer("");
								//实际流水减少
								CustomerAccountRecord record1 = new CustomerAccountRecord();
								record1.setType(CustomerAccountRecord.WITHDRAW);
								record1.setAccountId(coinWithdraw.getAccountId());
								record1.setCustomerId(coinWithdraw.getUserId());
								record1.setCoinCode(coinWithdraw.getCoinCode());
								record1.setOrderId(coinWithdraw.getTxOrder());
								record1.setMoney((coinWithdraw.getActualCount().add(coinWithdraw.getFee())).multiply(new BigDecimal("-1")));
								sql1.append(SqlUtil.createSql(record1,tableDate));
								//减少冻结流水
								CustomerAccountFreeze record2 = new CustomerAccountFreeze();
								record2.setFreezeType(CustomerAccountFreeze.WITHDRAW);
								record2.setAccountId(coinWithdraw.getAccountId());
								record2.setCustomerId(coinWithdraw.getUserId());
								record2.setCoinCode(coinWithdraw.getCoinCode());
								record2.setOrderId(coinWithdraw.getTxOrder());
								record2.setFreezeMoney((coinWithdraw.getActualCount().add(coinWithdraw.getFee())).multiply(new BigDecimal("-1")));
								sql1.append(SqlUtil.createSql(record2,tableDate));
								rabbitMqSender.toSql(sql1.toString());
								coinWithdraw.setStatus(CoinWithdraw.STATUS1);
								coinWithdraw.setHashTx(jsonResult1.getData() + "");
								coinWithdraw.setTxOrder(jsonResult1.getData() + "");
								ret[0] = true;
								ret[1] = "提现成功";
							}else{
								ret[0] = false;
								ret[1] = "提现失败";
								coinWithdraw.setStatus(CoinWithdraw.STATUS3);
							}
						} else if (status.intValue() == CoinWithdraw.STATUS2.intValue()) {
							//提币审核拒绝
							rabbitMqSender.toRedisAccount(MessageUtil.addCoinAvailable(coinWithdraw.getUserId(), coinWithdraw.getCoinCode(), coinWithdraw.getCoinCount()));
							rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(coinWithdraw.getUserId(), coinWithdraw.getCoinCode(), coinWithdraw.getCoinCount().multiply(new BigDecimal("-1"))));
							//减少冻结流水
							StringBuffer sql1 = new StringBuffer("");
							CustomerAccountFreeze record2 = new CustomerAccountFreeze();
							record2.setFreezeType(CustomerAccountFreeze.WITHDRAW);
							record2.setAccountId(coinWithdraw.getAccountId());
							record2.setCustomerId(coinWithdraw.getUserId());
							record2.setCoinCode(coinWithdraw.getCoinCode());
							record2.setOrderId(coinWithdraw.getTxOrder());
							record2.setFreezeMoney((coinWithdraw.getActualCount().add(coinWithdraw.getFee())).multiply(new BigDecimal("-1")));
							sql1.append(SqlUtil.createSql(record2,tableDate));
							rabbitMqSender.toSql(sql1.toString());
							coinWithdraw.setStatus(CoinWithdraw.STATUS2);  //设置为失败
							ret[0] = true;
							ret[1] = "已拒绝";
						}
						update(coinWithdraw);
					}else if(coinWithdraw.getStatus().equals( CoinWithdraw.STATUS3) ){
						if (status.equals(CoinWithdraw.STATUS1)) {
							JSONObject data = new JSONObject();
							data.put("coinCode", coinWithdraw.getCoinCode());
							data.put("amount", coinWithdraw.getActualCount());
							data.put("toAddress", coinWithdraw.getToAddress());
							System.out.println("审核通过参数" + JSON.toJSONString(data));
							JsonResult jsonResult1 = depositWalletService.sendCoin(data);
							if (jsonResult1.getSuccess()) {
								rabbitMqSender.toRedisAccount(MessageUtil.addCoinFreeze(coinWithdraw.getUserId(), coinWithdraw.getCoinCode(), coinWithdraw.getCoinCount().multiply(new BigDecimal("-1"))));
								StringBuffer sql1 = new StringBuffer("");
								//实际流水减少
								CustomerAccountRecord record1 = new CustomerAccountRecord();
								record1.setType(CustomerAccountRecord.WITHDRAW);
								record1.setAccountId(coinWithdraw.getAccountId());
								record1.setCustomerId(coinWithdraw.getUserId());
								record1.setCoinCode(coinWithdraw.getCoinCode());
								record1.setOrderId(coinWithdraw.getTxOrder());
								record1.setMoney((coinWithdraw.getActualCount().add(coinWithdraw.getFee())).multiply(new BigDecimal("-1")));
								sql1.append(SqlUtil.createSql(record1,tableDate));
								//减少冻结流水
								CustomerAccountFreeze record2 = new CustomerAccountFreeze();
								record2.setFreezeType(CustomerAccountFreeze.WITHDRAW);
								record2.setAccountId(coinWithdraw.getAccountId());
								record2.setCustomerId(coinWithdraw.getUserId());
								record2.setCoinCode(coinWithdraw.getCoinCode());
								record2.setOrderId(coinWithdraw.getTxOrder());
								record2.setFreezeMoney((coinWithdraw.getActualCount().add(coinWithdraw.getFee())).multiply(new BigDecimal("-1")));
								sql1.append(SqlUtil.createSql(record2,tableDate));
								rabbitMqSender.toSql(sql1.toString());
								coinWithdraw.setStatus(CoinWithdraw.STATUS1);
								coinWithdraw.setHashTx(jsonResult1.getData() + "");
								coinWithdraw.setTxOrder(jsonResult1.getData() + "");
								ret[0] = true;
								ret[1] = "提现成功";
							}else{
								ret[0] = false;
								ret[1] = "充值失败";
								coinWithdraw.setStatus(CoinWithdraw.STATUS3);
							}
						}
						update(coinWithdraw);
					}else{
						ret[0] = false;
						ret[1] = "已经拒绝的，不能操作";
					}
				} catch (Exception e) {
					ret[0] = true;
					ret[1] = "审核失败-" + e.getMessage();
					e.printStackTrace();
				}
			}
		}
		jsonResult.setSuccess((Boolean) ret[0]);
		jsonResult.setCode((Boolean) ret[0] ? Constants.SUCCESS : Constants.FAILED);
		jsonResult.setMsg((String) ret[1]);
		return jsonResult;
	}

	@Override
	public JsonResult getTotleMoneyByUnaudited() {
		JsonResult jsonResult = new JsonResult();
		try {
			jsonResult = super.createJsonResult(true, null);
			jsonResult.setData(coinWithdrawDao.getTotleMoneyByUnaudited());
		} catch (Exception e) {
			jsonResult = super.createJsonResult(false, null);
			jsonResult.setData(BigDecimal.ZERO);
			e.printStackTrace();
		}
		return jsonResult;
	}

	@Override
	public JsonResult getTotleWithdrawsByCode(String type) {
		JsonResult jsonResult = new JsonResult();
		try {
			jsonResult = super.createJsonResult(true, null);
			jsonResult.setData(coinWithdrawDao.getTotleWithdrawsByCode(type));
		} catch (Exception e) {
			jsonResult = super.createJsonResult(false, null);
			e.printStackTrace();
		}
		return jsonResult;
	}

	@Override
	public JsonResult getWithdrawsBySort(Integer limit) {
		JsonResult jsonResult = super.createJsonResult(false, "");
		try {
			//查询对应币种的提现记录
			List<CoinWithdrawVo> coins = coinWithdrawDao.getCoins();
			if (coins != null && coins.size() > 0) {
				int i = 0;
				StringBuffer sb = new StringBuffer();
				for (CoinWithdrawVo c : coins) {
					String sql = "(SELECT\n" +
							"\tcoin_code,\n" +
							"\tSUM(coin_count + fee) AS totleMoney,\n" +
							"\tuser_name\n" +
							"FROM\n" +
							"\texchange_coin_withdraw\n" +
							"where 1=1 and `status`=1 and coin_code='" + c.getCoinCode() + "' GROUP BY user_id ORDER BY totleMoney desc limit " + limit + ")";
					if (i == 0) {
						sb.append(sql);
					} else {
						sb.append("UNION ").append(sql);
					}
					i++;
				}
				//查询对应sql
				if (sb.length() > 0) {
					List<CoinWithdrawVo> list = jdbcTemplate.query(sb.toString(), new CoinWithdrawVoMapper());
					jsonResult = createJsonResult(true, "");
					jsonResult.setData(list);
				}
			}
		} catch (Exception e) {
			jsonResult = createJsonResult(false, "查询失败-" + e.getMessage());
		}
		return jsonResult;
	}

	class CoinWithdrawVoMapper implements RowMapper<CoinWithdrawVo> {
		@Override
		public CoinWithdrawVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CoinWithdrawVo vo = new CoinWithdrawVo();
			vo.setCoinCode(rs.getString("coin_code"));
			vo.setTotleMoney(rs.getBigDecimal("totleMoney"));
			vo.setUserName(rs.getString("user_name"));
			return vo;
		}
	}
}
