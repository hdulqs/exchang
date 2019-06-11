/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月13日 下午3:43:55
 */
package com.batsoft.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * <p> TODO</p>
 *
 * @author: Bat Admin
 * @Date :          2017年1月13日 下午3:43:55
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataBaseConfiguration {
	
	@Getter
	@Setter
	private Class<? extends DataSource> type;

	@Getter
	@Setter
	private String readSize;
	
	/**
	 * 写库定义
	 * 
	 * @return
	 */
	@Bean(name = "writeDataSource", destroyMethod = "close", initMethod = "init")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource writeDataSource() {
		return DataSourceBuilder.create().type(type).build();
	}

	/**
	 * 读库定义
	 *
	 * @return
	 */
	@Bean(name = "readDataSource1")
	@ConfigurationProperties(prefix = "spring.slave1")
	public DataSource readDataSourceOne() {
		return DataSourceBuilder.create().type(type).build();
	}
	
	/**
	 * 读库定义
	 *
	 * @return
	 */
	@Bean(name = "readDataSource2")
	@ConfigurationProperties(prefix = "spring.slave2")
	public DataSource readDataSourceTwo() {
		return DataSourceBuilder.create().type(type).build();
	}
	
	@Bean(name = "readDataSources")
	public List<DataSource> readDataSources() {
		List<DataSource> dataSources = new ArrayList<>();
		dataSources.add(readDataSourceOne());
		dataSources.add(readDataSourceTwo());
		return dataSources;
	}

	@Bean(name = "roundRobinDataSouceProxy")
	public AbstractRoutingDataSource roundRobinDataSouceProxy() {
		int size = Integer.parseInt(readSize);
		MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource(size);
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		
		// 写
		targetDataSources.put(DataSourceType.write.getType(), writeDataSource());
		
		// 读数据库
		for (int i = 0; i < size; i++) {
			targetDataSources.put(i, readDataSources().get(i));
		}
		
		proxy.setDefaultTargetDataSource(writeDataSource());
		proxy.setTargetDataSources(targetDataSources);
		return proxy;
	}
}  
