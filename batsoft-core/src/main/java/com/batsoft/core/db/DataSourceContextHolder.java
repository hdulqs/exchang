/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月13日 下午3:58:26
 */
package com.batsoft.core.db;

/** 
 * Created by Bat Admin on 2016/12/29.
 * 本地线程全局变量 
 */  
public class DataSourceContextHolder  {
	
    private static final ThreadLocal<String> local = new ThreadLocal<String>();  
  
    public static ThreadLocal<String> getLocal() {  
        return local;  
    }  
  
    /** 
     * 读可能是多个库 
     */  
    public static String read() {
        local.set(DataSourceType.read.getType());
        return DataSourceType.read.getType();
    }
  
    /** 
     * 写只有一个库 
     */  
    public static String write() {
        local.set(DataSourceType.write.getType());
        return DataSourceType.write.getType();
    }
    
    /**
     * 通过Key选择数据源
     * 
     * @return
     */
    public static String getJdbcType() {
        return local.get();  
    }
    
    /**
     * 切换回默认的数据源
     * 
     */
    public static void clear() {
    	local.remove();
    }
} 
