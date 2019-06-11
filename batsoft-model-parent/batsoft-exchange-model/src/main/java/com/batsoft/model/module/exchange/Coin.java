/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:19:04
 */

package com.batsoft.model.module.exchange;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 交易所币种
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_coin")
public class Coin extends BaseModel {

	private static final long serialVersionUID = 1606192079579356737L;
	
	/**
     * 手续费类型
     *0:固定手续费
     */
    public static final Integer FEETYPE0= 0;
    /**
     * 手续费类型
     *1:百分比
     */
    public static final Integer FEETYPE1 = 1;
    /**
     * 是否定价币
     *0:否
     */
    public static final Integer PRICECOIN0 = 0;
    /**
     * 是否定价币
     *1:是
     */
    public static final Integer PRICECOIN1 = 1;
    /**
     * 允许充值
     *0:否
     */
    public static final Integer ALLOWRECHARGE0 = 0;
    /**
     * 允许充值
     *1:是
     */
    public static final Integer ALLOWRECHARGE1 = 1;
    /**
     * 允许提现
     *0:否
     */
    public static final Integer ALLOWDEPOSIT0 = 0;
    /**
     * 允许提现
     *1:是
     */
    public static final Integer ALLOWDEPOSIT1 = 1;
    /**
     * 状态
     *0:关闭
     */
    public static final Integer STATUS0 = 0;
    /**
     * 状态
     *1:开启
     */
    public static final Integer STATUS1 = 1;
    /**
     * 状态
     *2:下架
     */
    public static final Integer STATUS2 = 2;

    /**
     * 是否erc20代币
     *0:否
     */
    public static final Integer ERC20_0 = 0;
    /**
     * 是否erc20代币
     *1:是
     */
    public static final Integer ERC20_1 = 1;


    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 币种名称
     */
    @NotNull(message = "币种名称不能为空")
    @Length(max = 50, message = "币种名称长度必须介于1和50之间")
    @Words(field = "币种名称", message = "币种名称包含敏感词")
    @Column(name = "coin_name")
    private String coinName;
    
    /**
     * 币种代码
     */
    @NotNull(message = "币种代码不能为空")
    @Length(max = 50, message = "币种代码长度必须介于1和50之间")
    @Words(field = "币种代码", message = "币种代码包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;

    /**
     * 发行方名称
     */
    @Length(max = 100, message = "发行方名称长度必须介于1和100之间")
    @Words(field = "发行方名称", message = "发行方名称包含敏感词")
    @Column(name = "issuerName")
    private String issuerName;
    
    /**
     * 发行总额
     */
    @NotNull(message = "发行总额不能为空")
    @Money(point = 15, message = "发行总额金额格式错误")
    @Column(name = "totleMoney")
    private BigDecimal totleMoney;
    
    /**
     * 发行总量
     */
    @NotNull(message = "发行总量不能为空")
    @Column(name = "totleNumber")
    private Integer totleNumber;
    
    /**
     * 发行单价
     */
    @NotNull(message = "发行单价不能为空")
    @Money(point = 15, message = "发行单价金额格式错误")
    @Column(name = "price")
    private BigDecimal price;
    
    /**
     * logo
     */
    @Length(max = 255, message = "logo长度必须介于1和255之间")
    @Words(field = "logo", message = "logo包含敏感词")
    @Column(name = "coinLogo")
    private String coinLogo;
    
    /**
     * 是否定价币
     */
    @Column(name = "price_coin")
    private Integer priceCoin;

    /**
     * 是否为erc20代币
     */
    @Column(name = "erc20_status")
    private Integer erc20Status;
    /**
     * 结算位数
     */

    @Column(name = "calculation_len")
    private Integer calculationLen;
    /**
     * 允许充值
     */

    @Column(name = "allow_recharge")
    private Integer allowRecharge;
    /**
     * 允许提现
     */

    @Column(name = "allow_withdraw")
    private Integer allowWithdraw;

    /**
     * 手续费类型
     */

    @Column(name = "withdraw_fee_type")
    private Integer withdrawFeeType;

    /**
     * 基础手续费
     */

    @Money(point = 15, message = "基础手续费金额格式错误")
    @Column(name = "withdraw_base_fee")
    private BigDecimal withdrawBaseFee;

    /**
     * 最小提币数量
     */

    @Money(point = 15, message = "最小提币数量金额格式错误")
    @Column(name = "withdraw_min")
    private BigDecimal withdrawMin;
    /**
     * 单日最大提币数量
     */

    @Money(point = 15, message = "单日最大提币数量金额格式错误")
    @Column(name = "withdraw_max")
    private BigDecimal withdrawMax;

    /**
     * 未实名单日最大提币数量
     */

    @Money(point = 15, message = "未实名单日最大提币数量金额格式错误")
    @Column(name = "no_auth_withdraw_max")
    private BigDecimal noAuthWithdrawMax;
    /**
     * 单笔小于多少无需审核
     */
    @Money(point = 15, message = "单笔小于多少无需审核金额格式错误")
    @Column(name = "withdraw_ac_max_amt")
    private BigDecimal withdrawAcMaxAmt;
    /**
     * 充手续费%
     */

    @Money(point = 15, message = "充手续费%金额格式错误")
    @Column(name = "chargeRate")
    private BigDecimal chargeRate;
    /**
     * 提手续费%
     */

    @Money(point = 15, message = "提手续费%金额格式错误")
    @Column(name = "withdrawRate")
    private BigDecimal withdrawRate;
    /**
     * 卖手续费%
     */

    @Money(point = 15, message = "卖手续费%金额格式错误")
    @Column(name = "sellRate")
    private BigDecimal sellRate;
    /**
     * 买手续费%
     */

    @Money(point = 15, message = "买手续费%金额格式错误")
    @Column(name = "buyRate")
    private BigDecimal buyRate;
    /**
     * 排序
     */

    @Column(name = "sort")
    private Integer sort;
    /**
     * 充值节点确认个数
     */

    @NotNull(message = "充值节点确认个数不能为空")
    @Column(name = "coin_confirm")
    private Integer coinConfirm;
    /**
     * 最大成交量
     */

    @Money(point = 15, message = "最大成交量金额格式错误")
    @Column(name = "maxNum")
    private BigDecimal maxNum;
    /**
     * 最小成交量
     */

    @Money(point = 15, message = "最小成交量金额格式错误")
    @Column(name = "minNum")
    private BigDecimal minNum;
    /**
     * 证明方式
     */

    @Length(max = 255, message = "证明方式长度必须介于1和255之间")
    @Words(field = "证明方式", message = "证明方式包含敏感词")
    @Column(name = "prove")
    private String prove;
    /**
     * 区块最小值
     */

    @Money(point = 15, message = "区块最小值金额格式错误")
    @Column(name = "minBlock")
    private BigDecimal minBlock;
    /**
     * 区块最大值
     */

    @Money(point = 15, message = "区块最大值金额格式错误")
    @Column(name = "maxBlock")
    private BigDecimal maxBlock;
    /**
     * 区块多少秒产生一个
     */

    @Length(max = 10, message = "区块多少秒产生一个长度必须介于1和10之间")
    @Words(field = "区块多少秒产生一个", message = "区块多少秒产生一个包含敏感词")
    @Column(name = "prodSpeed")
    private String prodSpeed;
    /**
     * 钱包地址
     */

    @Length(max = 255, message = "钱包地址长度必须介于1和255之间")
    @Words(field = "钱包地址", message = "钱包地址包含敏感词")
    @Column(name = "tokenUrl")
    private String tokenUrl;
    /**
     * 源码地址
     */

    @Length(max = 255, message = "源码地址长度必须介于1和255之间")
    @Words(field = "源码地址", message = "源码地址包含敏感词")
    @Column(name = "sourceUrl")
    private String sourceUrl;
    /**
     * 区块浏览器
     */

    @Length(max = 255, message = "区块浏览器长度必须介于1和255之间")
    @Words(field = "区块浏览器", message = "区块浏览器包含敏感词")
    @Column(name = "blockBrow")
    private String blockBrow;
    /**
     * 官网地址
     */

    @Length(max = 255, message = "官网地址长度必须介于1和255之间")
    @Words(field = "官网地址", message = "官网地址包含敏感词")
    @Column(name = "officialWeb")
    private String officialWeb;
    /**
     * 官网论坛
     */

    @Length(max = 255, message = "官网论坛长度必须介于1和255之间")
    @Words(field = "官网论坛", message = "官网论坛包含敏感词")
    @Column(name = "officialForum")
    private String officialForum;
    /**
     * 挖矿地址
     */

    @Length(max = 255, message = "挖矿地址长度必须介于1和255之间")
    @Words(field = "挖矿地址", message = "挖矿地址包含敏感词")
    @Column(name = "miningUrl")
    private String miningUrl;
    /**
     * 状态
     */

    @NotNull(message = "状态不能为空")
    @Column(name = "status")
    private Integer status;
    /**
     * 算法说明
     */

    @Length(max = 255, message = "算法说明长度必须介于1和255之间")
    @Words(field = "算法说明", message = "算法说明包含敏感词")
    @Column(name = "algorithm")
    private String algorithm;
    /**
     * 产品介绍
     */

    @Length(max = 255, message = "产品介绍长度必须介于1和255之间")
    @Words(field = "产品介绍", message = "产品介绍包含敏感词")
    @Column(name = "productIntroduction")
    private String productIntroduction;

}
