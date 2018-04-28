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
*@author:   yim
*@date:  2018年4月10日上午10:29:59
*@description:   
*/
@Configuration
@MapperScan(basePackages = "com.iptv.satellite.dao.log", sqlSessionFactoryRef = "logSqlSessionFactory")
public class MyBatisLogConfig {
	
	@Resource(name = "logDS")
	private DataSource logSource;
	
	@Bean(name = "logSqlSessionFactory")
	public SqlSessionFactory logSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(logSource);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/log/*.xml"));
		return factoryBean.getObject();
	}
	
	@Bean(name = "logTransaction")
	public DataSourceTransactionManager cmsTransactionManager() throws Exception{
		return new DataSourceTransactionManager(logSource);
	}
	
	@Bean(name = "logSessionTemplate")
	public SqlSessionTemplate cmsSqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(logSqlSessionFactory());
	}
}
