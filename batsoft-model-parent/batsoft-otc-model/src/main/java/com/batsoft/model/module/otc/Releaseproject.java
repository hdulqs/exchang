package com.batsoft.model.module.otc;

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
 *
 * <p>Releaseproject</p>
 * @author: Yuan Zhicheng
 * @Date :  2017-11-19 12:42:46
 */
@Entity
@Data
@ToString
@Table(name = "otc_releaseproject")
public class Releaseproject extends BaseModel {

	private static final long serialVersionUID = -563074560967818482L;
	
	/**
     * 发布类型
     *0:在线出售
     */
    public static final Integer OTCTYPE0 = 0;
    /**
     * 发布类型
     *1:在线购买
     */
    public static final Integer OTCTYPE1 = 1;

    /**
     * 开启固定价格
     *0:关闭
     */
    public static final Integer FIXED0 = 0;
    /**
     * 开启固定价格
     *1:开启
     */
    public static final Integer FIXED1 = 1;
    /**
     * 开启自动回复
     *0:否
     */
    public static final Integer AUTOMATIC0 = 0;
    /**
     * 开启自动回复
     *1:是
     */
    public static final Integer AUTOMATIC1 = 1;
    /**
     * 是否启用安全选项
     *0:否
     */
    public static final Integer SECURITY0 = 0;
    /**
     * 是否启用安全选项
     *1:是
     */
    public static final Integer SECURITY1 = 1;
    /**
     * 授信交易者
     *0:否
     */
    public static final Integer BELIVED0 = 0;
    /**
     * 授信交易者
     *1:是
     */
    public static final Integer BELIVED1 = 1;
    /**
     * 状态
     *0:发布
     */
    public static final Integer STATUS0 = 0;
    /**
     * 状态
     *1:已审核
     */
    public static final Integer STATUS1 = 1;
    /**
     * 状态
     *2:交易完成
     */
    public static final Integer STATUS2 = 2;
    /**
     * 状态
     *3:交易关闭
     */
    public static final Integer STATUS3 = 3;


    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 发布类型
     */

    @NotNull(message = "发布类型不能为空")
    @Column(name = "otcType")
    private Integer otcType;

    /**
     * 项目编号
     */
    @Length(max = 30, message = "项目编号长度必须介于1和100之间")
    @Words(field = "项目编号", message = "项目编号包含敏感词")
    @Column(name = "projectNum")
    private String projectNum;
    /**
     * 交易币种
     */

    @Length(max = 30, message = "交易币种长度必须介于1和30之间")
    @Words(field = "交易币种", message = "交易币种包含敏感词")
    @Column(name = "coin")
    private String coin;
    /**
     * 国家
     */

    @NotNull(message = "国家不能为空")
    @Length(max = 50, message = "国家长度必须介于1和50之间")
    @Words(field = "国家", message = "国家包含敏感词")
    @Column(name = "country")
    private String country;
    /**
     * 货币
     */

    @NotNull(message = "货币不能为空")
    @Length(max = 30, message = "货币长度必须介于1和30之间")
    @Words(field = "货币", message = "货币包含敏感词")
    @Column(name = "currency")
    private String currency;
    /**
     * 开启固定价格
     */

    @Column(name = "fixed")
    private Integer fixed;
    /**
     * 购买价格
     */

    @Money(point = 15, message = "购买价格金额格式错误")
    @Column(name = "price")
    private BigDecimal price;
    /**
     * 交易所名称
     */

    @NotNull(message = "交易所名称不能为空")
    @Length(max = 50, message = "交易所名称长度必须介于1和50之间")
    @Words(field = "交易所名称", message = "交易所名称包含敏感词")
    @Column(name = "exchangeName")
    private String exchangeName;
    /**
     * 溢价
     */

    @Money(point = 15, message = "溢价金额格式错误")
    @Column(name = "overflowMoney")
    private BigDecimal overflowMoney;
    /**
     * 付款方式
     */

    @Length(max = 30, message = "付款方式长度必须介于1和30之间")
    @Words(field = "付款方式", message = "付款方式包含敏感词")
    @Column(name = "payType")
    private String payType;
    /**
     * 付款方式备注
     */

    @Length(max = 255, message = "付款方式备注长度必须介于1和255之间")
    @Words(field = "付款方式备注", message = "付款方式备注包含敏感词")
    @Column(name = "payRemarks")
    private String payRemarks;
    /**
     * 交易条款
     */

    @Length(max = 255, message = "交易条款长度必须介于1和255之间")
    @Words(field = "交易条款", message = "交易条款包含敏感词")
    @Column(name = "transaction")
    private String transaction;
    /**
     * 开启自动回复
     */

    @Column(name = "automatic")
    private Integer automatic;
    /**
     * 是否启用安全选项
     */

    @Column(name = "security")
    private Integer security;
    /**
     * 授信交易者
     */

    @Column(name = "belived")
    private Integer belived;
    /**
     * 最大交易额
     */

    @Money(point = 15, message = "最大交易额金额格式错误")
    @Column(name = "maxMoney")
    private BigDecimal maxMoney;
    /**
     * 最小交易额
     */

    @Money(point = 15, message = "最小交易额金额格式错误")
    @Column(name = "minMoney")
    private BigDecimal minMoney;
    /**
     * 用户帐号
     */

    @NotNull(message = "用户帐号不能为空")
    @Length(max = 100, message = "用户帐号长度必须介于1和100之间")
    @Words(field = "用户帐号", message = "用户帐号包含敏感词")
    @Column(name = "userName")
    private String userName;
    /**
     * 用户昵称
     */

    @NotNull(message = "用户昵称不能为空")
    @Length(max = 30, message = "用户帐号长度必须介于1和30之间")
    @Words(field = "用户昵称", message = "用户昵称包含敏感词")
    @Column(name = "userNick")
    private String userNick;
    /**
     * 发布人ID
     */

    @NotNull(message = "发布人ID不能为空")
    @Length(max = 64, message = "发布人ID长度必须介于1和64之间")
    @Words(field = "发布人ID", message = "发布人ID包含敏感词")
    @Column(name = "userId")
    private String userId;
    /**
     * 状态
     */

    @Column(name = "status")
    private Integer status;
    /**
     * 交易地点
     */

    @Length(max = 255, message = "交易地点长度必须介于1和255之间")
    @Words(field = "交易地点", message = "交易地点包含敏感词")
    @Column(name = "adress")
    private String adress;

    /**
     * 付款期限
     */
    @Column(name = "payTime")
    private Integer payTime;

}
