/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:21
 */

package com.batsoft.model.module.exchange;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;

import lombok.Data;
import lombok.ToString;

/**
 * 委托交易历史
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_entrust_history")
public class EntrustHistory extends BaseModel {

	private static final long serialVersionUID = -1448129070941444063L;
	/**
     * 委托状态
     *0:未成交
     */
    public static final Integer ENTRUSTSTATE0 = 0;
    /**
     * 委托状态
     *1:部分成交
     */
    public static final Integer ENTRUSTSTATE1 = 1;
    /**
     * 委托状态
     *2:全部成交
     */
    public static final Integer ENTRUSTSTATE2 = 2;
    /**
     * 委托状态
     *3:撤销
     */
    public static final Integer ENTRUSTSTATE3 = 3;
    /**
     * 委托类别
     *0:限价委托
     */
    public static final Integer CATEGORY0 = 0;
    /**
     * 委托类别
     *1:计划委托
     */
    public static final Integer CATEGORY1 = 1;
    /**
     * 委托类别
     *2:止损委托
     */
    public static final Integer CATEGORY2 = 2;

    /**
     * 委托类别
     *3:止损限价委托
     */
    public static final Integer CATEGORY3 = 3;
    /**
     * 来源
     *0:网页
     */
    public static final Integer ORDERFROM0 = 0;
    /**
     * 来源
     *1:移动端
     */
    public static final Integer ORDERFROM1 = 1;


    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 账户id
     */

    @Length(max = 50, message = "账户id长度必须介于1和50之间")
    @Words(field = "账户id", message = "账户id包含敏感词")
    @Column(name = "account_id")
    private String accountId;
    /**
     * 客户id
     */

    @Length(max = 50, message = "客户id长度必须介于1和50之间")
    @Words(field = "客户id", message = "客户id包含敏感词")
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 币代码
     */
    @Length(max = 50, message = "币代码长度必须介于1和50之间")
    @Words(field = "币代码", message = "币代码包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;


    @NotNull(message = "交易币代码不能为空")
    @Length(max = 50, message = "交易币代码长度必须介于1和50之间")
    @Words(field = "交易币代码", message = "交易币代码包含敏感词")
    @Column(name = "trade_coin_code")
    private String tradeCoinCode;

    /**
     * 定价币代码
     */
    @NotNull(message = "定价币代码不能为空")
    @Length(max = 50, message = "定价币代码长度必须介于1和50之间")
    @Words(field = "定价币代码", message = "定价币代码包含敏感词")
    @Column(name = "pricing_coin_code")
    private String pricingCoinCode;


    @NotNull(message = "交易类型")
    @Column(name = "entrust_type")
    private String entrustType;

    /**
     * 订单号
     */
    @NotNull(message = "订单号")
    @Column(name = "order_id")
    private String orderId;

    /**
     * 委托价格
     */
    @Money(point = 15, message = "委托价格金额格式错误")
    @Column(name = "entrust_price")
    private BigDecimal entrustPrice;

    /**
     * 原始委托数量
     */
    @Money(point = 15, message = "委托数量金额格式错误")
    @Column(name = "entrust_amout_sql")
    private BigDecimal entrustAmoutSql;

    /**
     * 未成交委托数量
     */
    @Money(point = 15, message = "委托数量金额格式错误")
    @Column(name = "entrust_amout")
    private BigDecimal entrustAmout;
    /**
     * 委托时间
     */

    @Column(name = "entrust_time")
    private Date entrustTime;

    /**
     * 委托状态
     */
    @Column(name = "entrust_state")
    private Integer entrustState;

    /**
     * 已成交
     */
    @Money(point = 15, message = "已成交金额格式错误")
    @Column(name = "traded_coins")
    private BigDecimal tradedCoins;
    /**
     * 成交总额
     */

    @Money(point = 15, message = "成交总额金额格式错误")
    @Column(name = "total_money")
    private BigDecimal totalMoney;
    /**
     * 成交均价
     */

    @Money(point = 15, message = "成交均价金额格式错误")
    @Column(name = "traded_avg")
    private BigDecimal tradedAvg;
    /**
     * 委托类别
     */

    @NotNull(message = "委托类别不能为空")
    @Column(name = "category")
    private Integer category;
    /**
     * 来源
     */

    @Column(name = "order_from")
    private Integer orderFrom;
    /**
     * 订单说明
     */

    @Length(max = 50, message = "订单说明长度必须介于1和50之间")
    @Words(field = "订单说明", message = "订单说明包含敏感词")
    @Column(name = "remark")
    private String remark;

    /**
     * 用户名
     */
    @Transient
    private String userName;

}
