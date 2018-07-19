package com.iptv.satellite.dao;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * ScheduleDAO
 */
public class ScheduleDAO {

    private SqlSessionTemplate sqlSession;

    /**
     * @param sqlSession the sqlSession to set
     */
    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }
}