package com.batsoft.core.log;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.batsoft.core.common.enums.CHS;
import com.batsoft.utils.date.BaseDate;
import com.batsoft.utils.date.convert.config.DateFormatConfig;
import com.batsoft.utils.date.convert.motion.DateConvertStringMotion;

/**
 * 日志工具类
 * 
 * @author simon
 */
public class LogUtils {
	
	public static Logger logger = LoggerFactory.getLogger(LogUtils.class);
	
	public static DateConvertStringMotion motion = new DateConvertStringMotion();

	/**
	 * 打印 info 日志信息
	 * 
	 * @param message
	 *            内容
	 */
	public static void info(Object message) {
		
		logger.info(motion.convert(DateFormatConfig.YYYYMMDDMMHHSS(), BaseDate.getNowTime()) + CHS.equal.getValue() + message);
	}

	/**
	 * 打印错误 ERROR 日志信息
	 * 
	 * @param message
	 *            内容
	 */
	public static void error(Object message) {

		logger.error(motion.convert(DateFormatConfig.YYYYMMDDMMHHSS(), BaseDate.getNowTime()) + CHS.equal.getValue() + message);
	}

	/**
	 * 打印 警告 （warn） 日志
	 * 
	 * @param message
	 *            内容
	 */
	public static void warn(Object message) {

		logger.warn(motion.convert(DateFormatConfig.YYYYMMDDMMHHSS(), BaseDate.getNowTime()) + CHS.equal.getValue() + message);
	}

	/**
	 * 打印 debug 日志信息
	 * 
	 * @param message
	 *            内容
	 */
	public static void debug(Object message) {

		logger.debug(motion.convert(DateFormatConfig.YYYYMMDDMMHHSS(), BaseDate.getNowTime()) + CHS.equal.getValue() + message);
	}

	/**
	 * 打印 调试 日志信息
	 * 
	 * @param message
	 *            内容
	 */
	public static void trace(Object message) {

		logger.trace(motion.convert(DateFormatConfig.YYYYMMDDMMHHSS(), BaseDate.getNowTime()) + CHS.equal.getValue() + message);
	}

	/**
	 * 使用info 打印MAP参数集合
	 * 
	 * @param map
	 *            参数集合
	 */
	public static void infoPrintMap(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return;
		}
		LogUtils.info("--------------------------- 开始打印参数 ---------------------------");
		for (Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				LogUtils.info(motion.convert(DateFormatConfig.YYYYMMDDMMHHSS(), BaseDate.getNowTime()) + CHS.equal.getValue() + "***********" + entry.getKey() + "--" + entry.getValue());
			}
		}
		LogUtils.info("--------------------------- 结束打印参数 ----------------------------\n");
	}

	/**
	 * 使用info 打印MAP参数集合
	 * 
	 * @param map
	 *            参数集合
	 */
	public static void infoPrintObjMap(Map<String, Object> map) {
		if (map == null || map.size() == 0) {
			return;
		}
		LogUtils.info("--------------------------- 开始打印参数 ---------------------------");
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				LogUtils.info(motion.convert(DateFormatConfig.YYYYMMDDMMHHSS(), BaseDate.getNowTime()) + CHS.equal.getValue() + "***********" + entry.getKey() + "--" + entry.getValue());
			}
		}
		LogUtils.info("--------------------------- 结束打印参数 ---------------------------\n");
	}

}
