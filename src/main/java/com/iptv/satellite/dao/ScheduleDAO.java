package com.iptv.satellite.dao;

import java.util.List;

import com.iptv.satellite.domain.db.ScheduleBean;
import com.iptv.satellite.domain.model.EpgModel;

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
        ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
        return mapper.insertIntoSchedule(scheduleBeans);
    }

    public int deleteOldEpg(List<EpgModel> epgModels) {
        ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
        return mapper.deleteFromEpg(epgModels);
    }

    public int getFirstSchedule() {
        ScheduleMapper mapper = sqlSession.getMapper(ScheduleMapper.class);
        return mapper.selectFirstFromSchedule();
    }
}