package com.iptv.satellite.config.mybatis;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * MyBatisDataSourceConfig
 */
@Configuration
@MapperScan(basePackages = "com.iptv.satellite.dao.datasource", sqlSessionFactoryRef = "dataSourceSqlSessionFactory")
public class MyBatisDataSourceConfig {

    @Resource(name = "dsDS")
    private DataSource dataSource;

    @Bean(name = "dataSourceSqlSessionFactory")
    public SqlSessionFactory dataSourceSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/ds/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "dataSourceTransaction")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "dataSourceSessionTemplate")
    public SqlSessionTemplate dataSourceSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(dataSourceSqlSessionFactory());
    }
}