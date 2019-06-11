/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月13日 下午3:59:56
 */
package com.batsoft.core.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;  
  
/** 
 * Created by Bat Admin
 * 多数据源切换 
 */  
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource { 
	
    private final int dataSourceNumber;  
    
    private AtomicInteger count = new AtomicInteger(0);  
  
    public MyAbstractRoutingDataSource(int dataSourceNumber) {  
        this.dataSourceNumber = dataSourceNumber;  
    } 
  
    @Override  
    protected Object determineCurrentLookupKey() {  
        String typeKey = DataSourceContextHolder.getJdbcType();  
        if(Objects.equals(typeKey, DataSourceType.read.getType())){
            int number = count.getAndAdd(1);  
            int lookupKey = number % dataSourceNumber;  
            return new Integer(lookupKey);  
        }else {
        	return DataSourceType.write.getType();
        }
    }
} 
