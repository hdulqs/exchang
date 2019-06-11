package com.batsoft.utils.date.convert.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateFormatConfig {

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyy-MM-dd HH:mm:ss}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDDMMHHSS() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyy-MM-dd HH:mm}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDDHHMM() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format;
	}
	
	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyy-MM-dd HH}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDDHH() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：HH:mm}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat HHMM() {
		DateFormat format = new SimpleDateFormat("HH:mm");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyy-MM-dd}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDD() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format;
	}
	
	/**
	 * 日期格式化对象 {@inheritDoc 格式为：MM-dd}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat MMDD() {
		DateFormat format = new SimpleDateFormat("MM-dd");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyy/MM/dd/}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDDS() {
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyyMMddHHmmssSSS}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMddHHmmssSSS() {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyyMMddHHmmss}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDDHHMMSST() {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyyMMdd}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDDL() {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyy年MM月dd日}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYYMMDDM() {
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：yyyy}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat YYYY() {
		DateFormat format = new SimpleDateFormat("yyyy");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：MM}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat MM() {
		DateFormat format = new SimpleDateFormat("MM");
		return format;
	}

	/**
	 * 日期格式化对象 {@inheritDoc 格式为：dd}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat DD() {
		DateFormat format = new SimpleDateFormat("dd");
		return format;
	}
	
	/**
	 * 日期格式化对象 {@inheritDoc 格式为：_yyyy_MM_dd}
	 * 
	 * @return DateFormat 对象
	 */
	public final static DateFormat _yyyy_MM_dd() {
		DateFormat format = new SimpleDateFormat("_yyyy_MM_dd");
		return format;
	}
	
}
