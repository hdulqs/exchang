package com.batsoft.service.module.exchange.trade.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.batsoft.model.module.exchange.CustomerAccountFreeze;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.model.module.exchange.EntrustHistory;
import com.batsoft.model.module.exchange.EntrustInfo;
import com.batsoft.model.module.exchange.EntrustIng;

/**
 * Created by Administrator on 2018/4/13.
 */
public class SqlUtil {

    public static void main(String[] args){

    }

    /**
     * 字符串为空返回""
     * @param str
     * @return
     */
    public static String StringIsNull(String str){
        if(str==null){
            return "";
        }
        return str;
    }

    /**
     * BigDecimal为空返回0
     * @param big
     * @return
     */
    public static BigDecimal BigDecimalIsNull(BigDecimal big){
        if(big==null){
            return BigDecimal.ZERO;
        }
        return big;
    }

    /**
     * Integer为空返回0
     * @param i
     * @return
     */
    public static Integer IntegerIsNull(Integer i){
        if(i==null){
            return 0;
        }
        return i;
    }

    /**
     * Date为空返回当前日期字符串
     * @param date
     * @return
     */
    public static String DateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(date==null){
            date= new Date();
        }
        return sdf.format(date);
    }

    /**
     * 成交信息转sql
     * @param info
     * @return
     */
    public static String createSql(EntrustInfo info, String tableDate){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO exchange_entrust_info" + tableDate);
        sb.append("(");
        sb.append("id").append(",");
        sb.append("sell_account_id").append(",");
        sb.append("buy_account_id").append(",");
        sb.append("sell_customer_id").append(",");
        sb.append("buy_customer_id").append(",");
        sb.append("trade_coin_code").append(",");
        sb.append("pricing_coin_code").append(",");
        sb.append("type").append(",");
        sb.append("buy_rate").append(",");
        sb.append("sell_rate").append(",");
        sb.append("buy_fee").append(",");
        sb.append("sell_fee").append(",");
        sb.append("entrust_price").append(",");
        sb.append("entrust_amout").append(",");
        sb.append("real_time_usdt_rate");
        sb.append(")");
        sb.append("VALUES");
        sb.append("(");
        sb.append("'").append(UUIDUtil.getUUID()).append("'").append(",");
        sb.append("'").append(StringIsNull(info.getSellAccountId())).append("'").append(",");
        sb.append("'").append(StringIsNull(info.getBuyAccountId())).append("'").append(",");
        sb.append("'").append(StringIsNull(info.getSellCustomerId())).append("'").append(",");
        sb.append("'").append(StringIsNull(info.getBuyCustomerId())).append("'").append(",");
        sb.append("'").append(StringIsNull(info.getTradeCoinCode())).append("'").append(",");
        sb.append("'").append(StringIsNull(info.getPricingCoinCode())).append("'").append(",");
        sb.append("'").append(StringIsNull(info.getType())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(info.getBuyRate())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(info.getSellRate())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(info.getBuyFee())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(info.getSellFee())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(info.getEntrustPrice())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(info.getEntrustAmout())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(info.getRealTimeUsdtRate())).append("'");
        sb.append(")");
        sb.append(";");
        return sb.toString();
    }


    /**
     * 当前委托转sql
     * @param obj
     * @return
     */
	public static String createSql(EntrustIng obj){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO exchange_entrust_ing");
        sb.append("(");
        sb.append("id").append(",");
        sb.append("account_id").append(",");
        sb.append("customer_id").append(",");
        sb.append("trade_coin_code").append(",");
        sb.append("pricing_coin_code").append(",");
        sb.append("entrust_price").append(",");
        sb.append("entrust_amout").append(",");
        sb.append("entrust_amout_sql").append(",");
        sb.append("entrust_time").append(",");
        sb.append("entrust_state").append(",");
        sb.append("entrust_type").append(",");
        sb.append("order_id").append(",");
        sb.append("remark");
        sb.append(")");
        sb.append("VALUES");
        sb.append("(");
        sb.append("'").append(UUIDUtil.getUUID()).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getAccountId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getCustomerId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getTradeCoinCode())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getPricingCoinCode())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getEntrustPrice())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getEntrustAmout())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getEntrustAmoutSql())).append("'").append(",");
        sb.append("'").append(DateToString(obj.getEntrustTime())).append("'").append(",");
        sb.append("'").append(IntegerIsNull(obj.getEntrustState())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getEntrustType())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getOrderId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getRemark())).append("'");
        sb.append(")");
        sb.append(";");
        return sb.toString();
    }

    /**
     * 历史委托转sql
     * @param obj
     * @return
     */
    public static String createSql(EntrustHistory obj, String tableDate){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO exchange_entrust_history" + tableDate);
        sb.append("(");
        sb.append("id").append(",");
        sb.append("account_id").append(",");
        sb.append("customer_id").append(",");
        sb.append("trade_coin_code").append(",");
        sb.append("pricing_coin_code").append(",");
        sb.append("entrust_price").append(",");
        sb.append("entrust_amout").append(",");
        sb.append("entrust_amout_sql").append(",");
        sb.append("entrust_time").append(",");
        sb.append("entrust_state").append(",");
        sb.append("entrust_type").append(",");
        sb.append("order_id").append(",");
        sb.append("remark");
        sb.append(")");
        sb.append("VALUES");
        sb.append("(");
        sb.append("'").append(UUIDUtil.getUUID()).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getAccountId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getCustomerId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getTradeCoinCode())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getPricingCoinCode())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getEntrustPrice())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getEntrustAmout())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getEntrustAmoutSql())).append("'").append(",");
        sb.append("'").append(DateToString(obj.getEntrustTime())).append("'").append(",");
        sb.append("'").append(IntegerIsNull(obj.getEntrustState())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getEntrustType())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getOrderId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getRemark())).append("'");
        sb.append(")");
        sb.append(";");
        return sb.toString();
    }


    /**
     * 生成实际操作的流水
     * @param obj
     * @return
     */
    public static String createSql(CustomerAccountRecord obj, String tableDate){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO exchange_customer_account_record" + tableDate);
        sb.append("(");
        sb.append("id").append(",");
        sb.append("account_id").append(",");
        sb.append("customer_id").append(",");
        sb.append("account_number").append(",");
        sb.append("coin_code").append(",");
        sb.append("type").append(",");
        sb.append("order_id").append(",");
        sb.append("money").append(",");
        sb.append("usdt_amount").append(",");
        sb.append("trade_coin_code").append(",");
        sb.append("pricing_coin_code").append(",");
        sb.append("trade_coin_real_time_price").append(",");
        sb.append("remark");
        sb.append(")");
        sb.append("VALUES");
        sb.append("(");
        sb.append("'").append(UUIDUtil.getUUID()).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getAccountId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getCustomerId())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getAccountNumber())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getCoinCode())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getType())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getOrderId())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getMoney())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getUsdtAmount())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getTradeCoinCode())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getPricingCoinCode())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(obj.getTradeCoinRealTimePrice())).append("'").append(",");
        sb.append("'").append(StringIsNull(obj.getRemark())).append("'");
        sb.append(")");
        sb.append(";");
        return sb.toString();
    }

    /**
     * 生成实际操作的流水
     * @param record
     * @return
     */
    public static String createSql(CustomerAccountFreeze record, String tableDate){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO exchange_customer_account_freeze"+tableDate);
        sb.append("(");
        sb.append("id").append(",");
        sb.append("account_id").append(",");
        sb.append("customer_id").append(",");
        sb.append("account_number").append(",");
        sb.append("coin_code").append(",");
        sb.append("freeze_type").append(",");
        sb.append("order_id").append(",");
        sb.append("freeze_money").append(",");
        sb.append("remark");
        sb.append(")");
        sb.append("VALUES");
        sb.append("(");
        sb.append("'").append(UUIDUtil.getUUID()).append("'").append(",");
        sb.append("'").append(StringIsNull(record.getAccountId())).append("'").append(",");
        sb.append("'").append(StringIsNull(record.getCustomerId())).append("'").append(",");
        sb.append("'").append(StringIsNull(record.getAccountNumber())).append("'").append(",");
        sb.append("'").append(StringIsNull(record.getCoinCode())).append("'").append(",");
        sb.append("'").append(StringIsNull(record.getFreezeType())).append("'").append(",");
        sb.append("'").append(StringIsNull(record.getOrderId())).append("'").append(",");
        sb.append("'").append(BigDecimalIsNull(record.getFreezeMoney())).append("'").append(",");
        sb.append("'").append(StringIsNull(record.getRemark())).append("'");
        sb.append(")");
        sb.append(";");
        return sb.toString();
    }

    /**
     * 生成修改账户的sql
     * @return
     */
    @Deprecated
    public static String updateAccountSql(String customerId,String coinCode,BigDecimal useableMoney,BigDecimal disableMoney){
        StringBuffer sb = new StringBuffer();
        sb.append(" update exchange_customer_account");
        sb.append(" set ");
        sb.append(" available= ").append(useableMoney.doubleValue());
        sb.append(",");
        sb.append(" freeze= ").append(disableMoney.doubleValue());
        sb.append(" where ");
        sb.append(" user_id= ").append("'").append(customerId).append("'");
        sb.append(" and ");
        sb.append(" coin_code=").append("'").append(coinCode).append("'");
        sb.append(";");
        return sb.toString();
    }
    
    /**
     * 生成修改账户可用余额SQL
     * 
     * @param customerId
     * 				客户ID
     * @param coinCode
     * 				货币代码
     * @param money
     * 				金额
     * @return
     */
    public static String updateAccountAvailableSql(String customerId, String coinCode, BigDecimal money){
        StringBuffer statement = new StringBuffer();
        statement.append(" UPDATE exchange_customer_account");
        statement.append(" SET ");
        statement.append(" available = available + (").append(money.doubleValue()).append(")");
        statement.append(" WHERE ");
        statement.append(" user_id = ").append("'").append(customerId).append("'");
        statement.append(" AND ");
        statement.append(" coin_code = ").append("'").append(coinCode).append("'");
        statement.append(";");
        return statement.toString();
    }
    
    /**
     * 生成修改账户冻结余额SQL
     * 
     * @param customerId
     * 				客户ID
     * @param coinCode
     * 				货币代码
     * @param money
     * 				金额
     * @return
     */
    public static String updateAccountFreezeSql(String customerId, String coinCode, BigDecimal money){
        StringBuffer statement = new StringBuffer();
        statement.append(" UPDATE exchange_customer_account");
        statement.append(" SET ");
        statement.append(" freeze = freeze + (").append(money.doubleValue()).append(")");
        statement.append(" WHERE ");
        statement.append(" user_id = ").append("'").append(customerId).append("'");
        statement.append(" AND ");
        statement.append(" coin_code = ").append("'").append(coinCode).append("'");
        statement.append(";");
        return statement.toString();
    }
    

    /**
     * 生成修改当前委托单的sql
     * @return
     */
    public static String updateEntrustIngSql(String orderId,BigDecimal entrustAmout){
        StringBuffer sb = new StringBuffer();
        sb.append(" update exchange_entrust_ing ");
        sb.append(" set ");
        sb.append(" entrust_amout = ").append(entrustAmout);
        sb.append(" where ");
        sb.append(" order_id= ").append("'").append(orderId).append("'");
        sb.append(";");
        return sb.toString();
    }

    /**
     * 删除当前委托单
     * @return
     */
    public static String deleteEntrustIngSql(String orderId){
        StringBuffer sb = new StringBuffer();
        sb.append(" delete from  exchange_entrust_ing ");
        sb.append(" where ");
        sb.append(" order_id= ").append("'").append(orderId).append("'");
        sb.append(";");
        return sb.toString();
    }


}
