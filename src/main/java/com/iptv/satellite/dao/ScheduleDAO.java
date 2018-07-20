package com.iptv.satellite.dao;

import java.util.List;

import com.iptv.satellite.domain.db.ScheduleBean;
import com.iptv.satellite.domain.model.EpgModelBean;

import org.apache.ibatis.session.SqlSession;

/**
 * ScheduleDAO
 */
public class ScheduleDAO {

    private SqlSession sqlSession;

    /**
     * @param sqlSession the sqlSession to set
     */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int addSchedules(List<ScheduleBean> scheduleBeans) {
        return sqlSession.insert("com.iptv.satellite.dao.ScheduleMapper.insertIntoSchedule", scheduleBeans);
    }

    public int deleteOldEpg(List<EpgModelBean> epgModels) {
        return sqlSession.delete("com.iptv.satellite.dao.ScheduleMapper.deleteFromEpg", epgModels);
    }

    public int getFirstSchedule() {
        return sqlSession.selectOne("com.iptv.satellite.dao.ScheduleMapper.selectFirstFromSchedule");
    }
}