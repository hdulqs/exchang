package com.batsoft.model.module.exchange;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;

/**
 * 数字货币提现记录
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_coin_withdraw")
public class CoinWithdraw extends BaseModel {

	private static final long serialVersionUID = 3000694756873116171L;
	
	/**
     * 状态
     *0:等待中
     */
    public static final Integer STATUS0 = 0;
    /**
     * 状态
     *1:成功
     */
    public static final Integer STATUS1 = 1;
    /**
     *拒绝充值
     */
    public static final Integer STATUS2 = 2;

    /**
     * 充值失败
     *
     */
    public static final Integer STATUS3 = 3;

    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 提币地址
     */

    @NotNull(message = "提币地址不能为空")
    @Length(max = 255, message = "提币地址长度必须介于1和255之间")
    @Words(field = "提币地址", message = "提币地址包含敏感词")
    @Column(name = "to_address")
    private String toAddress;
    /**
     * 币种代码
     */

    @NotNull(message = "币种代码不能为空")
    @Length(max = 50, message = "币种代码长度必须介于1和50之间")
    @Words(field = "币种代码", message = "币种代码包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;
    /**
     * memo币地址
     */

    @Length(max = 255, message = "memo币地址长度必须介于1和255之间")
    @Words(field = "memo币地址", message = "memo币地址包含敏感词")
    @Column(name = "memo")
    private String memo;
    /**
     * 提币数量
     */

    @Money(point = 15, message = "提币数量金额格式错误")
    @Column(name = "coin_count")
    private BigDecimal coinCount;
    /**
     * 实际到账
     */

    @Money(point = 15, message = "实际到账金额格式错误")
    @Column(name = "actual_count")
    private BigDecimal actualCount;
    /**
     * 手续费
     */

    @Money(point = 15, message = "手续费金额格式错误")
    @Column(name = "fee")
    private BigDecimal fee;
    /**
     * 状态
     */

    @Column(name = "status")
    private Integer status;
    /**
     * 交易单号
     */

    @NotNull(message = "交易单号不能为空")
    @Length(max = 150, message = "交易单号长度必须介于1和150之间")
    @Words(field = "交易单号", message = "交易单号包含敏感词")
    @Column(name = "tx_order")
    private String txOrder;
    /**
     * 用户id
     */

    @NotNull(message = "用户id不能为空")
    @Length(max = 64, message = "用户id长度必须介于1和64之间")
    @Words(field = "用户id", message = "用户id包含敏感词")
    @Column(name = "user_id")
    private String userId;

    /**
     * 币账户对应id
     */
    @Length(max = 64, message = "币账户对应id长度必须介于1和64之间")
    @Words(field = "币账户对应id", message = "币账户对应id包含敏感词")
    @Column(name = "account_id")
    private String accountId;
    /**
     * 用户名
     */

    @Length(max = 50, message = "用户名长度必须介于1和50之间")
    @Words(field = "用户名", message = "用户名包含敏感词")
    @Column(name = "user_name")
    private String userName;
    /**
     * 用户手机号
     */

    @Length(max = 20, message = "用户手机号长度必须介于1和20之间")
    @Words(field = "用户手机号", message = "用户手机号包含敏感词")
    @Column(name = "user_mobile")
    private String userMobile;
    /**
     * 用户邮箱
     */

    @Length(max = 255, message = "用户邮箱长度必须介于1和255之间")
    @Words(field = "用户邮箱", message = "用户邮箱包含敏感词")
    @Column(name = "user_email")
    private String userEmail;
    /**
     * 备注
     */

    @Length(max = 255, message = "备注长度必须介于1和255之间")
    @Words(field = "备注", message = "备注包含敏感词")
    @Column(name = "remark")
    private String remark;
    /**
     * hash转账
     */

    @Length(max = 255, message = "转账哈希")
    @Words(field = "转账哈希", message = "转账哈希包含敏感词")
    @Column(name = "hash_tx")
    private String hashTx;

}
