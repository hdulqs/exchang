package com.batsoft.core.common.enums;

/**
 * 系统配置键
 * 
 * @author simon
 */
public enum AppConfigKeyEnum {
	
	/**
	 * 交易控制校验密码
	 * 
	 */
	HANDLE_TRADE_TOKEN("交易控制校验密码"),
	
	/**
	 * 日ip发送短信的最大条数
	 * 
	 */
	dayHostSendMobileCodeMaxNum("日ip发送短信的最大条数"),
	
	/**
	 * 日发送验证码最大条数
	 * 
	 */
	daySendMobileCodeMaxNum("日发送验证码最大条数"),
	
	/**
	 * 交易挖矿奖励比例
	 * 
	 */
	DIG_REWARD_SCALE("交易挖矿奖励比例"),
	
	/**
	 * USDT定价
	 * 
	 */
	usdtPrice("USDT定价"),
	
	/**
	 * 商城后端Port
	 * 
	 */
	mallPort("商城后端Port"),
	
	/**
	 * 商城后端URL
	 * 
	 */
	mallURL("商城后端URL"),
	
	/**
	 * 公司邮箱
	 * 
	 */
	business_email("公司邮箱"),

	/**
	 * 开启极验
	 * 
	 */
	open_geetest("开启极验"),
	
	/**
	 * 极验key
	 * 
	 */
	geetest_key("极验key"),
	
	/**
	 * 极验apiUrl
	 * 
	 */
	geetest_api("极验apiUrl"),
	
	/**
	 * 极验ID
	 * 
	 */
	geetest_id("极验ID"),
	
	/**
	 * twitter
	 * 
	 */
	twitter("twitter"),
	
	/**
	 * 电报群
	 * 
	 */
	telegram("电报群"),
	
	
	/**
	 * 平台币CODE
	 * 
	 */
	plat_coin("平台币CODE"),
	
	
	/**
	 * 是否开启绑卡
	 * 
	 */
	bank_open("是否开启绑卡"),
	
	
	/**
	 * 是否开启C2C
	 * 
	 */
	c2c_open("是否开启C2C"),
	
	
	/**
	 * 后台copyright
	 * 
	 */
	manageCopyright("后台copyright"),
	
	
	/**
	 * 站点icon
	 * 
	 */
	iconPath("站点icon"),
	
	
	/**
	 * test0012
	 * 
	 */
	test0012("test0012"),
	
	
	/**
	 * 国际短信注册
	 * 
	 */
	mobileArea("国际短信注册"),
	
	
	/**
	 * 用户中心Banner
	 * 
	 */
	front_user_banner("用户中心Banner"),
	
	
	/**
	 * 前台LOGO_dark
	 * 
	 */
	front_logo_dark("前台LOGO_dark"),
	
	
	/**
	 * 公司名称
	 * 
	 */
	company("公司名称"),
	
	
	/**
	 * 前台登录bannber
	 * 
	 */
	front_login_banner("前台登录bannber"),
	
	
	/**
	 * 前台LOGO
	 * 
	 */
	front_logo_light("前台LOGO"),
	
	
	/**
	 * 后台LOGO
	 * 
	 */
	manage_logo("后台LOGO"),
	
	/**
	 * 文件域名
	 * 
	 */
	fileHost("文件域名"),
	
	/**
	 * 短信模版
	 * 
	 */
	smsTplCode("短信模版"),
	
	
	/**
	 * 验证码长度
	 * 
	 */
	mobileCodeLength("验证码长度"),
	
	/**
	 * 验证码纯数字
	 * 
	 */
	useCodeNumber("验证码纯数字"),
	
	/**
	 * 验证码超时(秒)
	 * 
	 */
	mobileCodeTimeOut("验证码超时(秒)"),
	
	/**
	 * 使用默认短信验证码
	 * 
	 */
	useDefaultMobileCode("使用默认短信验证码"),
	
	/**
	 * 允许上传文件
	 * 
	 */
	allowFile("允许上传文件"),
	
	/**
	 * 默认短信验证码
	 * 
	 */
	defaultMobileCode("默认短信验证码"),
	
	/**
	 * 站点关键词
	 * 
	 */
	webSiteKeyWords("站点关键词"),
	
	/**
	 * 开启验证码
	 * 
	 */
	openVerificationCode("开启验证码"),
	
	/**
	 * ftp密码
	 * 
	 */
	ftpPassword("ftp密码"),
	
	/**
	 * ftp帐号
	 * 
	 */
	ftpAccount("ftp帐号"),
	
	/**
	 * ftp端口
	 * 
	 */
	ftpPort("ftp端口"),
	
	/**
	 * ftp地址
	 * 
	 */
	ftpHost("ftp地址"),
	
	/**
	 * 站点备注
	 * 
	 */
	webSiteRemark("站点备注"),
	
	/**
	 * 最大允许上传(KB)
	 * 
	 */
	allowMaxSize("最大允许上传(KB)"),
	
	/**
	 * 密码
	 * 
	 */
	smsPassword("密码"),
	
	/**
	 * 短信appkey
	 * 
	 */
	smsAppKey("短信appkey"),
	
	/**
	 * 短信通道地址
	 * 
	 */
	smsHost("短信通道地址"),
	
	/**
	 * 站点名称
	 * 
	 */
	webSiteTitle("站点名称"),
	
	
	/**
	 * 站点域名
	 * 
	 */
	webSiteHost("站点域名"),

	promotionAward("推广人奖励币"),

	promotionParentAward("父推广人奖励币"),
	/**
	 * 提现费率
	 * 
	 */
	withdrawRate("提现费率"),
	
	;



	
	private AppConfigKeyEnum(String message) {
		this.code = this.name();
		this.message = message;
	}
	
	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
