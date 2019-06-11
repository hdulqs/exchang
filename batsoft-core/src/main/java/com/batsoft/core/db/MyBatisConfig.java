/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.batsoft.core.db;

import com.github.pagehelper.PageHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * MyBatis基础配置
 *
 * @author Bat Admin
 * @since 2016-12-19 10:11
 */

@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
//@ConditionalOnBean(DataSource.class)
@Import({DataBaseConfiguration.class})
@EnableConfigurationProperties(MybatisProperties.class)

public class MyBatisConfig implements TransactionManagementConfigurer {
    private static Log log = LogFactory.getLog(MyBatisConfig.class);

    @Resource(name = "writeDataSource")
    private DataSource dataSource;


    @Resource(name = "roundRobinDataSouceProxy")
    private AbstractRoutingDataSource roundRobinDataSouceProxy;

    @Autowired
    private MybatisProperties properties;

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @PostConstruct
    public void checkConfigFileExists() {
        if (this.properties.isCheckConfigLocation()) {
            org.springframework.core.io.Resource resource = this.resourceLoader.getResource(this.properties.getConfig());
            Assert.state(resource.exists(),
                    "Cannot find config location: " + resource
                            + " (please add config MyFile or check your Mybatis "
                            + "configuration)");
        }
    }

    /**
     * 分页插件
     *
     * @param
     * @return
     * @author Bat Admin
     * @create 2016年2月18日
     */
    @Bean
    public PageHelper pageHelper() {
        log.info("注册MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Bean(name = "sqlSessionFactory")
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(roundRobinDataSouceProxy);
        factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        factory.setMapperLocations(this.properties.getMapperLocations());

        if (this.interceptors != null && this.interceptors.length > 0) {
            factory.setPlugins(this.interceptors);
        }
        return factory.getObject();
    }


    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "txManager")
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
