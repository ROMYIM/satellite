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
*@date:  2018年3月14日上午10:45:24
*@description:   
*/
@Configuration
@MapperScan(basePackages = "com.iptv.satellite.dao.cmsp2p", sqlSessionFactoryRef = "cmsp2pSqlSessionFactory")
public class MyBatisCmsp2pConfig {

	@Resource(name = "cmsp2pDS")
	private DataSource cmsp2pSource;
	
	@Bean(name = "cmsp2pSqlSessionFactory")
	public SqlSessionFactory cmsp2pSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(cmsp2pSource);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/cmsp2p/*.xml"));
		return factoryBean.getObject();
	}
	
	@Bean(name = "cmsp2pTransaction")
	public DataSourceTransactionManager cmsp2pTransactionManager() {
		return new DataSourceTransactionManager(cmsp2pSource);
	}
	
	@Bean(name = "cmsp2p2SessionTemplate")
	public SqlSessionTemplate cmsp2pSqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(cmsp2pSqlSessionFactory());
	}
}
