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
*@date:  2018年3月14日上午10:36:16
*@description:   Cms数据源与持久层的映射和配置
*/
@Configuration
@MapperScan(basePackages = "com.iptv.satellite.dao.cms", sqlSessionFactoryRef = "cmsSqlSessionFactory")
public class MyBatisCmsConfig {
	
	/**
	 * cms数据源依赖注入
	 */
	@Resource(name = "cmsDS")
	private DataSource cmsSource;
	
	/**
	 * 创建cms的回话工厂，与相应的mapper文件映射，用于之后的回话模板创建
	 * @return  回话工厂实例，并声明为Bean交由spring容器管理
	 * @throws Exception
	 */
	@Bean(name = "cmsSqlSessionFactory")
	public SqlSessionFactory cmsSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(cmsSource);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/cms/*.xml"));
		return factoryBean.getObject();
	}
	
	/**
	 * 创建cms的事务管理器，用于开启cms数据源事务，该操作交由Spring自动完成
	 * @return  事务管理器实例，并声明为Bean交由spring容器管理
	 * @throws Exception
	 */
	@Bean(name = "cmsTransaction")
	public DataSourceTransactionManager cmsTransactionManager() throws Exception{
		return new DataSourceTransactionManager(cmsSource);
	}
	
	/**
	 * 创建cms数据源的回话模板，用于事务的提交，该操作有Spring容器完成
	 * @return  回话模板实例，并声明为Bean交由spring容器管理
	 * @throws Exception
	 */
	@Bean(name = "cmsSessionTemplate")
	public SqlSessionTemplate cmsSqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(cmsSqlSessionFactory());
	}
}
