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
*@date:  2018年3月14日上午10:15:11
*@description:   
*/
@Configuration
@MapperScan(basePackages = "com.iptv.satellite.dao.epg", sqlSessionFactoryRef = "epgSqlSessionFactory")
public class MybatisEpgConfig {
	
	@Resource(name = "epgDS")
	private DataSource epgSource;
	
	@Bean(name = "epgSqlSessionFactory")
	public SqlSessionFactory epgSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(epgSource);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/epg/*.xml"));
		return factoryBean.getObject();
	}
	
	@Bean(name = "epgTransactionManager")
	public DataSourceTransactionManager epgTransactionManager() throws Exception{
		return new DataSourceTransactionManager(epgSource);
	}
	
	@Bean(name = "epgSessionTemplate")
	public SqlSessionTemplate epgSqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(epgSqlSessionFactory());
	}
}
