/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月13日 下午4:14:01
 */
package com.batsoft.core.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 根据方法名称切换数据源
 * 
 * @author simon
 */
@Aspect
@Order(3)
@Component
public class DataSourceAop {

	// 只读切点
	@Pointcut("execution(* com.batsoft..service..*.find*(..)) || execution(* com.batsoft..service..*.get*(..))|| execution(* com.batsoft..service..*.has*(..))")
	public void read() {}
	
	// 只读切面
    @Before(value = "read()")
    public void setReadDataSourceType(JoinPoint point) {
        DataSourceContextHolder.read();
    }
    
    // 清空当前线程只读设置
    @After(value = "read()")
    public void readAfter(JoinPoint point) {
    	DataSourceContextHolder.clear();
    }
    
    // -------------------------------------------------------------------------------------------------------------------------
    
    // 只写切点
 	@Pointcut("execution(* com.batsoft..service..*.save*(..)) || execution(* com.batsoft..service..*.update*(..))|| execution(* com.batsoft..service..*.remove*(..))|| execution(* com.batsoft.core.service..*.delete*(..))|| execution(* com.batsoft.core.service..*.add*(..))|| execution(* com.batsoft.core.service..*.edit*(..))|| execution(* com.batsoft.core.service..*.bind*(..))|| execution(* com.batsoft.core.service..*.send*(..))")
 	public void write() {}
  
    @Before(value = "write()")
    public void setWriteDataSourceType(JoinPoint point) {
        DataSourceContextHolder.write();
    }
    
}  
