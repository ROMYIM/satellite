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
 * MyBatisInformationSchemaConfig
 */
@Configuration
@MapperScan(basePackages = "com.iptv.satellite.dao.informationSchema", sqlSessionFactoryRef = "informationSchemaSqlSessionFactory")
public class MyBatisInformationSchemaConfig {

    @Resource(name = "informationSchemaDs")
    private DataSource dataSource;

    @Bean(name = "informationSchemaSqlSessionFactory")
    public SqlSessionFactory informationSchemaSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/informationSchema/*.xml"));
        return factoryBean.getObject();
    }
    
    @Bean(name = "informationTransactionManager")
    public DataSourceTransactionManager informationSchemaTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean(name = "informationSessionTemplate")
    public SqlSessionTemplate informationSchemaSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(informationSchemaSqlSessionFactory()); 
    }
}