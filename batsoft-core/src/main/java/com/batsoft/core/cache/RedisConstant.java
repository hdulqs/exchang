/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月4日 下午6:01:40
 */
package com.batsoft.core.cache;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年1月4日 下午6:01:40 
 */
public class RedisConstant {
	//服务开启状态
	public static boolean JEDIS_STATUS;
	
	/**
	 * 所有缓存统一默认10分钟 
	 * 需要设置其它时间请自行修改
	 */
	public static int JEDIS_EXPIRE = 600;
	
	public static String FRONT_OAUTH="FRONT-OAUTH";
	
	public static String MANAGE_OAUTH="MANAGE-OAUTH";
	
	/**
	 * 字典前缀
	 */
	public static String DIC_PREFIX = "dic:";
	
	/**
	 * 广告前缀
	 */
	public static String ADV_PREFIX = "adv:";
	
	/**
	 * 分类前缀
	 */
	public static String GCLZ_PREFIX = "gclz:";
	
	/**
	 * 分类前缀
	 */
	public static String AREAS_PREFIX = "areas:";
	
	/**
	 * 设置前缀
	 */
	public static String SETTING_PREFIX = "setting:";
	
	/**
	 * 支付方式前缀
	 */
	public static String PAYMENT_PREFIX = "payment:";
	
	/**
	 * 其它前缀
	 */
	public static String OTHER_PREFIX = "other:";
}
