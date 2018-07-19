package com.iptv.satellite.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.satellite.domain.model.DataSourceModel;
import com.iptv.satellite.util.BeanUtil;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

/**
 * DataSourceService
 */
@Service
public class DataSourceService {

    public boolean addDataSource(DataSourceModel model) {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("driverClassName", model.getDriverClassName());
        propertyMap.put("url", model.getUrl());
        propertyMap.put("username", model.getUserName());
        propertyMap.put("password", model.getPassword());
        String beanName = "Ds";
        if (BeanUtil.createBean("org.apache.tomcat.jdbc.pool.DataSource", beanName, propertyMap, null, null, null, "close")) {
           return true;
        }
        return false;
    }

    public boolean addSqlSessionFactory(String dataSourceBeanName) throws Exception {
        DataSource dataSource = BeanUtil.getBean(dataSourceBeanName,DataSource.class);
        if (dataSource != null) {
            String beanName = "sqlSessionFactory";
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("mapperLocations", new PathMatchingResourcePatternResolver().getResource("/mapper/schedule/*.xml"));
            Map<String, String> referenceMap = new HashMap<>();
            referenceMap.put("dataSource", dataSourceBeanName);
            if (BeanUtil.createBean("org.mybatis.spring.SqlSessionFactoryBean", beanName, propertyMap, referenceMap, null, null, null)) {
                return true;
            }
        }
        return false;
    }

    public void addTransaction(String dataSourceBeanName) {
        DataSource dataSource = BeanUtil.getBean(dataSourceBeanName, DataSource.class);
        if (dataSource != null) {
            List<String> referenceConstructList = new ArrayList<>();
            referenceConstructList.add(dataSourceBeanName);
            String beanName = "transaction";
            BeanUtil.createBean("org.springframework.jdbc.datasource.DataSourceTransactionManager", beanName, null, null, null, referenceConstructList, null);
        }
    }

    public boolean addSqlSessionTemplate(String sqlSessionFactoryBeanName) throws Exception {
        DefaultSqlSessionFactory sqlSessionFactory = BeanUtil.getBean(sqlSessionFactoryBeanName, DefaultSqlSessionFactory.class);
        if (sqlSessionFactory != null) {
            List<String> referenceConstructList = new ArrayList<>();
            referenceConstructList.add(sqlSessionFactoryBeanName);
            String beanName = "sqlSessionTemplate";
            if (BeanUtil.createBean("org.mybatis.spring.SqlSessionTemplate", beanName, null, null, null, referenceConstructList, null)) {
                return true;
            }
        }
        return false;
    }

    public boolean addScheduleDAO(String sessionTemplateBeanName) {
        SqlSessionTemplate sqlSessionTemplate = BeanUtil.getBean(sessionTemplateBeanName, SqlSessionTemplate.class);
        if (sqlSessionTemplate != null) {
            String beanName = "scheduleDAO";
            Map<String, String> referenceMap = new HashMap<>();
            referenceMap.put("sqlSession", sessionTemplateBeanName);
            if (BeanUtil.createBean("com.iptv.satellite.domain.ScheduleDAO", beanName, null, referenceMap, null, null, null)) {
                return true;
            }
        }
        return false;
    }
}