package com.iptv.satellite.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.iptv.satellite.domain.model.DataSourceModel;
import com.iptv.satellite.util.BeanUtil;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

/**
 * DataSourceService
 */
@Service
public class DataSourceService {

    public void addDataSource(DataSourceModel model) {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("driverClassName", model.getDriverClassName());
        propertyMap.put("url", model.getUrl());
        propertyMap.put("username", model.getUserName());
        propertyMap.put("password", model.getPassword());
        String beanName = "Ds";
        if (BeanUtil.createBean("org.apache.tomcat.jdbc.pool.DataSource", beanName, propertyMap, null, null, null, "close")) {
            
        }
    }

    public SqlSessionFactory addSqlSessionFactory() throws Exception {
        DataSource dataSource = BeanUtil.getBean("Ds",DataSource.class);
        if (dataSource != null) {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("mapper/cms/*.xml"));
            return factoryBean.getObject();
        }
        return null;
    }

    public void addJdbcTemplate() {
        
    }
}