package com.iptv.satellite.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iptv.satellite.dao.ScheduleDAO;
import com.iptv.satellite.dao.datasource.DataSourceMapper;
import com.iptv.satellite.domain.db.DataSourceBean;
import com.iptv.satellite.util.BeanUtil;

import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

/**
 * DataSourceService
 */
@Service
public class DataSourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceService.class);

    private final DataSourceMapper dataSourceMapper;

    private final ApplicationContext applicationContext;

    private volatile List<String> runtimeServiceList;

    /**
     * @return the runtimeServiceList
     */
    public List<String> getRuntimeServiceList() {
        return runtimeServiceList;
    }

    @Autowired
    public DataSourceService(DataSourceMapper dataSourceMapper, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.dataSourceMapper = dataSourceMapper;
        try {
            BeanUtil.initBeanFactory(this.applicationContext);
			startUpService();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public String addDataSource(DataSourceBean model) {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("driverClassName", model.getDriverClassName());
        propertyMap.put("url", model.getUrl());
        propertyMap.put("username", model.getUserName());
        propertyMap.put("password", model.getPassword());
        propertyMap.put("maxActive", 50);
        propertyMap.put("maxIdle", 50);
        propertyMap.put("initialSize", 1);
        propertyMap.put("maxWait", 60000);
        propertyMap.put("minIdle", 1);
        propertyMap.put("timeBetweenEvictionRunsMillis", 60000);
        propertyMap.put("minEvictableIdleTimeMillis", 300000);
        propertyMap.put("validationQuery", "select 'x'");
        propertyMap.put("testWhileIdle", true);
        propertyMap.put("testOnBorrow", false);
        propertyMap.put("testOnReturn", false);
        String beanName = model.getBeanName() + "Ds";
        if (BeanUtil.createBean("org.apache.tomcat.jdbc.pool.DataSource", beanName, propertyMap, null, null, null, "close")) {
           return beanName;
        }
        return null;
    }

    public String addSqlSessionFactory(String dataSourceBeanName, String beanName) throws Exception {
        DataSource dataSource = BeanUtil.getBean(dataSourceBeanName,DataSource.class);
        if (dataSource != null) {
            String sessionFactoryBeanName = beanName + "SqlSessionFactory";
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("mapperLocations", new PathMatchingResourcePatternResolver().getResource("classpath:mapper/schedule/ScheduleMapper.xml"));
            Map<String, String> referenceMap = new HashMap<>();
            referenceMap.put("dataSource", dataSourceBeanName);
            if (BeanUtil.createBean("org.mybatis.spring.SqlSessionFactoryBean", sessionFactoryBeanName, propertyMap, referenceMap, null, null, null)) {
                addTransaction(dataSourceBeanName, beanName);
                return sessionFactoryBeanName;
            }
        }
        return null;
    }

    public void addTransaction(String dataSourceBeanName, String beanName) {
        DataSource dataSource = BeanUtil.getBean(dataSourceBeanName, DataSource.class);
        if (dataSource != null) {
            List<String> referenceConstructList = new ArrayList<>();
            referenceConstructList.add(dataSourceBeanName);
            String transactionBeanName = beanName + "transaction";
            BeanUtil.createBean("org.springframework.jdbc.datasource.DataSourceTransactionManager", transactionBeanName, null, null, null, referenceConstructList, null);
        }
    }

    public String addSqlSessionTemplate(String sqlSessionFactoryBeanName, String beanName) throws Exception {
        DefaultSqlSessionFactory sqlSessionFactory = BeanUtil.getBean(sqlSessionFactoryBeanName, DefaultSqlSessionFactory.class);
        if (sqlSessionFactory != null) {
            List<String> referenceConstructList = new ArrayList<>();
            referenceConstructList.add(sqlSessionFactoryBeanName);
            String sessionTempalteBeanName = beanName + "SessionTemplate";
            if (BeanUtil.createBean("org.mybatis.spring.SqlSessionTemplate", sessionTempalteBeanName, null, null, null, referenceConstructList, null)) {
                return sessionTempalteBeanName;
            }
        }
        return null;
    }

    public String addScheduleDAO(String sessionTemplateBeanName, String beanName) {
        SqlSessionTemplate sqlSessionTemplate = BeanUtil.getBean(sessionTemplateBeanName, SqlSessionTemplate.class);
        if (sqlSessionTemplate != null) {
            String daoBeanName = beanName + "ScheduleDAO";
            Map<String, String> referenceMap = new HashMap<>();
            referenceMap.put("sqlSession", sessionTemplateBeanName);
            if (BeanUtil.createBean("com.iptv.satellite.dao.ScheduleDAO", daoBeanName, null, referenceMap, null, null, null)) {
                return daoBeanName;
            }
        }
        return null;
    }

    public String addScheduleService(String scheduleDAOBeanName, String beanName) {
        ScheduleDAO scheduleDAO = BeanUtil.getBean(scheduleDAOBeanName, ScheduleDAO.class);
        if (scheduleDAO != null) {
            String serviceBeanName = beanName + "ScheduleService";
            Map<String, String> referenceMap = new HashMap<>();
            Map<String, Object> propertyMap = new HashMap<>();
            referenceMap.put("scheduleDAO", scheduleDAOBeanName);
            propertyMap.put("dataSourceName", beanName);
            if (BeanUtil.createBean("com.iptv.satellite.service.ScheduleService", serviceBeanName, propertyMap, referenceMap, null, null, null)) {
                return serviceBeanName;
            }
        }
        return null;
    }

    public boolean addService(DataSourceBean model) {
        String serviceBeanName = null;
        if (!runtimeServiceList.contains(model.getBeanName())) {
            try {
                serviceBeanName = addScheduleService(
                    addScheduleDAO(
                        addSqlSessionTemplate(
                            addSqlSessionFactory(
                                addDataSource(model), model.getBeanName()
                            ), model.getBeanName()
                        ), model.getBeanName()
                    ), model.getBeanName()
                );
                if (serviceBeanName != null) {
                    dataSourceMapper.inserIntoDataSource(model);
                    runtimeServiceList.add(serviceBeanName);
                    return true;
                }
            } catch (Exception e) {	
                LOGGER.info(e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean startUpService() throws Exception {
        this.runtimeServiceList = new ArrayList<>();
        List<DataSourceBean> runtimeServiceList = dataSourceMapper.selectAllFromDataSource();
        for (DataSourceBean dataSourceBean : runtimeServiceList) {
            String beanName = dataSourceBean.getBeanName();
            String serviceBeanName = addScheduleService(
                addScheduleDAO(
                    addSqlSessionTemplate(
                        addSqlSessionFactory(
                            addDataSource(dataSourceBean), beanName
                        ), beanName
                    ), beanName
                ), beanName
            );
            if (serviceBeanName != null) {
                LOGGER.info("启动服务：" + beanName);
               this.runtimeServiceList.add(serviceBeanName);
            } else {
               return false;
            }
        }
        return true;
    }

}