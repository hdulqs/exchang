/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-26 17:29:20
 */
package com.batsoft.model.module.system.config;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>AppConfig</p>
 * @author: Bat Admin
 * @Date :  2017-06-26 17:29:20
 */
@Table(name = "system_app_config")
@Entity
@Data
@ToString
public class AppConfig extends BaseModel {

    /**
     * 对应数据库 key字符串
     */
    public static final String FILEHOST="fileHost";	//文件域名
    public static final String ICONPATH="iconPath";//icon 路径
    public static final String WEBSITEHOST="webSiteHost";	//站点域名
    public static final String WEBSITTITLE="webSiteTitle";	//站点名称
    public static final String WEB_SHOP_ADDRESS="shopAddress";	//商城地址
    public static final String WEBSITKEYWORDS="webSiteKeyWords";	//站点名称
    public static final String WEBSITEREMARK="webSiteRemark";	//站点备注
    public static final String ALLOWFILE="allowFile";	//允许上传文件
    public static final String ALLOWMAXSIZE="allowMaxSize";	//最大允许上传(KB)
    public static final String OPENVERIFICATIONCODE="openVerificationCode";	//开启验证码 0 否 1是
    public static final String USEDEFAULTMOBILECODE="useDefaultMobileCode";// 使用默认短信验证码 0 否 1是
    public static final String DEFAULTMOBILECODE="defaultMobileCode";// 默认短信验证码
    public static final String MOBILECODETIMEOUT="mobileCodeTimeOut";// 默认短信验证码 超时时间
    public static final String USECODENUMBER="useCodeNumber";//短信验证码是否是纯数字 0 否 1是
    public static final String MOBILECODELENGTH="mobileCodeLength";// 短信验证码长度
    public static final String FTPHOST="ftpHost";	//FTP地址
    public static final String FTPACCOUNT="ftpAccount";	//FTP帐号
    public static final String FTPPORT="ftpPort";	//FTP端口
    public static final String FTPPASSWORD="ftpPassword";	//FTP密码
    public static final String SMSHOST="smsHost";	//短信通道地址
    public static final String SMSAPPKEY="smsAppKey";	//帐号
    public static final String SMSPASSWORD="smsPassword";	//密码
    public static final String COMPANY="company";	//公司名称
    public static final String MANAGELOGO="manage_logo";	//后台logo
    public static final String FRONTLOGOLIGHT="front_logo_light";	//前台logo light
    public static final String FRONTLOGODARK="front_logo_dark";	//前台logo dark
    public static final String FRONTLOGINBANNER="front_login_banner";	//前台登录页banner
    public static final String FRONTUSERBANNER="front_user_banner";	//前台用户中心banner
    public static final String MOBILEAREA="mobileArea";	//是否支持国际短信注册
    public static final String MALLPORT="mallPort";	//商城后端Port
    public static final String MALLURL="mallURL";	//商城后端URL
    public static final String USDT_PRICE="usdtPrice";	//usdt 价格
    public static final String DIG_REWARD_SCALE = "DIG_REWARD_SCALE"; // 交易挖矿奖励比例

    //是否开启c2c
    public static final String C2COPEN="c2c_open";
    
    //是否开启绑卡
    public static final String BANKOPEN="bank_open";

    // 后台copyright
    public static final String MANAGECOPYRIGHT="manageCopyright";
    //日手机号发送短信最大数
    public static final String DAYSENDMOBILECODEMAXNUM ="daySendMobileCodeMaxNum";

    //日IP发送短信最大数
    public static final String DAYHOSTSENDMOBILECODEMAXNUM = "dayHostSendMobileCodeMaxNum";

    //ETH WALLET Adress
    public static final String ETH_WALLET_WHITE_ADDRESS = "ETH_wallet_white_address";

    /**
     * 第三方短信模版代码
     */
    public static final String SMSTPLCODE="smsTplCode";

    //金融参数
    public static final String WITHDRAWRATE="withdrawRate";	//提现费率

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 配置key
     */
    @Length(max = 255, message = "配置key长度必须介于1和255之间")
    @Words(field = "配置key", message = "配置key包含敏感词")
    @Column(name = "conf_key")
    private String confKey;
    
    /**
     * 配置名称
     */
    @Length(max = 100, message = "配置名称长度必须介于1和100之间")
    @Words(field = "配置名称", message = "配置名称包含敏感词")
    @Column(name = "conf_name")
    private String confName;
    
    /**
     * 备注
     */
    @Length(max = 255, message = "备注长度必须介于1和255之间")
    @Words(field = "备注", message = "备注包含敏感词")
    @Column(name = "remark")
    private String remark;
    
    /**
     * 数据类型
     */
    @Column(name = "data_type")
    private Integer dataType;
    
    /**
     * 配置值
     */
    @Length(max = 255, message = "配置值长度必须介于1和255之间")
    @Words(field = "配置值", message = "配置值包含敏感词")
    @Column(name = "conf_value")
    private String confValue;
    
    /**
     * 选项值
     */
    @Length(max = 255, message = "选项值长度必须介于1和255之间")
    @Words(field = "选项值", message = "选项值包含敏感词")
    @Column(name = "select_value")
    private String selectValue;
    
    /**
     * 类型key
     */
    @Length(max = 100, message = "类型key长度必须介于1和64之间")
    @Words(field = "类型key", message = "类型key包含敏感词")
    @Column(name = "type_key")
    private String typeKey;
    
    /**
     * 类型名称
     */
    @Length(max = 255, message = "类型名称长度必须介于1和255之间")
    @Words(field = "类型名称", message = "类型名称包含敏感词")
    @Column(name = "type_name")
    private String typeName;
    
    /**
     * 描述
     */
    @Length(max = 255, message = "描述长度必须介于1和255之间")
    @Words(field = "描述", message = "描述包含敏感词")
    @Column(name = "description")
    private String description;
    
    /**
     * 排序
     */
    private Integer sort;
}
