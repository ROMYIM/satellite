package com.iptv.satellite.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
*@author:   yim
*@date:  2018年3月14日上午9:55:04
*@description:   多数据源配置  生成三个数据源并交给spring控制
*/
@Configuration
public class DataSourceConfig {
	
	/**
	 * 生成epg数据源的bean
	 * @return 
	 */
	@Bean(name = "epgDS")
	@ConfigurationProperties(prefix = "spring.datasource.epg")
	public DataSource createEpgDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	/**
	 * 生成log数据源的bean
	 * @return
	 */
	@Bean(name = "logDS")
	@ConfigurationProperties(prefix = "spring.datasource.log")
	public DataSource createLogDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 生成ds数据源的bean
	 * @return
	 */
	@Bean(name = "dsDS")
	@ConfigurationProperties(prefix = "spring.datasource.ds")
	public DataSource createDsDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 生成informationSchema数据源的bean
	 * @return
	 */
	@Bean(name = "informationSchemaDs")
	@ConfigurationProperties(prefix = "spring.datasource.informationSchema")
	public DataSource createInformationSchemaDataSource() {
		return DataSourceBuilder.create().build();
	}
}
