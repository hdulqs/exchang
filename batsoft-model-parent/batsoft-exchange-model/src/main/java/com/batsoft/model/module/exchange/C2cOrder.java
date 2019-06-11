/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-07 09:16:15
 */

package com.batsoft.model.module.exchange;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import com.batsoft.utils.annotation.Money;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 法币交易订单
 *
 */
@Entity
@Data
@ToString
@Table(name = "exchange_c2c_order")
public class C2cOrder extends BaseModel {

	private static final long serialVersionUID = 8566671112540934240L;
	
	/**
     * 方向
     *0:支出
     */
    public static final Integer DIRECTION0 = 0;
    /**
     * 方向
     *1:收入
     */
    public static final Integer DIRECTION1 = 1;
    /**
     * 操作类型
     *1:买入
     */
    public static final Integer OPERATIONTYPE1 = 1;
    /**
     * 操作类型
     *2:卖出
     */
    public static final Integer OPERATIONTYPE2 = 2;
    /**
     * 操作类型
     *买入购买类型 0是数据按购买数量 1是按购买价格
     */
    public static final Integer OPERATIONTYPEBUYTYPE_NUM = 0;
    /**
     * 操作类型
     *买入购买类型 0是数据按购买数量 1是按购买价格
     */
    public static final Integer OPERATIONTYPEBUYTYPE_MENOY = 1;
    /**
     * 订单状态
     *0:进行中
     */
    public static final Integer OPEATIONSTATE0 = 0;
    /**
     * 订单状态
     *1:已完成
     */
    public static final Integer OPEATIONSTATE1 = 1;
    /**
     * 订单状态
     *2:失败
     */
    public static final Integer OPEATIONSTATE2 = 2;


    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 账户ID
     */

    @NotNull(message = "账户ID不能为空")
    @Length(max = 64, message = "账户ID长度必须介于1和64之间")
    @Words(field = "账户ID", message = "账户ID包含敏感词")
    @Column(name = "account_id")
    private String accountId;
    /**
     * 用户id
     */

    @NotNull(message = "用户id不能为空")
    @Length(max = 64, message = "用户id长度必须介于1和64之间")
    @Words(field = "用户id", message = "用户id包含敏感词")
    @Column(name = "user_id")
    private String userId;
    /**
     * 用户名
     */

    @NotNull(message = "用户名不能为空")
    @Length(max = 50, message = "用户名长度必须介于1和50之间")
    @Words(field = "用户名", message = "用户名包含敏感词")
    @Column(name = "user_name")
    private String userName;
    /**
     * 币种
     */

    @NotNull(message = "币种不能为空")
    @Length(max = 50, message = "币种长度必须介于1和50之间")
    @Words(field = "币种", message = "币种包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;
    /**
     * 方向
     */

    @Column(name = "direction")
    private Integer direction;
    /**
     * 操作类型
     */

    @Column(name = "operation_type")
    private Integer operationType;

    /**
     * 订单单价
     */

    @Money(point = 15, message = "单价格式错误")
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 订单数量
     */

    @Money(point = 15, message = "订单数量格式错误")
    @Column(name = "number")
    private BigDecimal number;
    /**
     * 订单金额
     */

    @Money(point = 15, message = "订单金额金额格式错误")
    @Column(name = "opeation_money")
    private BigDecimal opeationMoney;
    /**
     * 交易时间
     */

    @Column(name = "trade_time")
    private Date tradeTime;
    /**
     * 订单状态
     */

    @Column(name = "opeation_state")
    private Integer opeationState;
    /**
     * 资金转入方ID
     */

    @Length(max = 64, message = "资金转入方ID长度必须介于1和64之间")
    @Words(field = "资金转入方ID", message = "资金转入方ID包含敏感词")
    @Column(name = "transaction_id")
    private String transactionId;
    /**
     * 资金转入方名称
     */

    @Length(max = 100, message = "资金转入方名称长度必须介于1和100之间")
    @Words(field = "资金转入方名称", message = "资金转入方名称包含敏感词")
    @Column(name = "transaction_name")
    private String transactionName;

    /**
     * 资金转入方银行卡号
     */
    @Length(max = 50, message = "银行卡号长度必须介于1和50之间")
    @Words(field = "银行卡号", message = "银行卡号包含敏感词")
    @Column(name = "tra_bankcard")
    private String traBankcard;

    /**
     * 银行名称
     */

    @Length(max = 50, message = "银行名称长度必须介于1和50之间")
    @Words(field = "银行名称", message = "银行名称包含敏感词")
    @Column(name = "tra_bankname")
    private String traBankname;

    /**
     * 交易流水号
     */

    @Length(max = 50, message = "交易流水号长度必须介于1和50之间")
    @Words(field = "交易流水号", message = "交易流水号包含敏感词")
    @Column(name = "tra_number")
    private String traNumber;
    /**
     * 订单说明
     */

    @Length(max = 50, message = "订单说明长度必须介于1和50之间")
    @Words(field = "订单说明", message = "订单说明包含敏感词")
    @Column(name = "remark")
    private String remark;

}
