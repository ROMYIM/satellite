package com.yim.satellite.config.mybatis;

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
*@date:  2018年3月14日上午10:36:16
*@description:   
*/
@Configuration
@MapperScan(basePackages = "com.yim.satellite.dao.cms", sqlSessionFactoryRef = "cmsSqlSessionFactory")
public class MyBatisCmsConfig {
	
	@Resource(name = "cmsDS")
	private DataSource cmsSource;
	
	@Bean(name = "cmsSqlSessionFactory")
	public SqlSessionFactory cmsSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(cmsSource);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/cms/*.xml"));
		return factoryBean.getObject();
	}
	
	@Bean(name = "cmsTransaction")
	public DataSourceTransactionManager cmsTransactionManager() throws Exception{
		return new DataSourceTransactionManager(cmsSource);
	}
	
	@Bean(name = "cmsSessionTemplate")
	public SqlSessionTemplate cmsSqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(cmsSqlSessionFactory());
	}
}
