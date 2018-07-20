package com.iptv.satellite.dao.datasource;

import java.util.List;

import com.iptv.satellite.domain.db.DataSourceBean;

import org.apache.ibatis.annotations.Param;

/**
 * DataSourceMapper
 */
public interface DataSourceMapper {

    int inserIntoDataSource(DataSourceBean dataSourceBean);

    List<DataSourceBean> selectAllFromDataSource();

    int deleteFromDataSource(@Param("beanName")String beanName);
}