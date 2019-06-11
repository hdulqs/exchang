/**
 * Copyright:   领航者
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2015年12月7日 下午3:26:31
 */
package com.batsoft.service.module.exchange.trade.util;

import java.util.UUID;

/**
 * UUID 生成器
 * <p>
 * TODO
 * </p>
 * 
 * @author: Bat Admin
 * @Date : 2015年12月7日 下午3:26:31
 */
public class UUIDUtil {

	/**
	 * 生成UUID
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * @author: Bat Admin
	 * @param: @return
	 * @return: String
	 * @Date : 2015年12月7日 下午3:27:56
	 * @throws:
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}
	


}
