package com.iptv.satellite.dao;

import java.util.List;

import com.iptv.satellite.domain.db.ScheduleBean;
import com.iptv.satellite.domain.model.EpgModel;

/**
 * ScheduleMapper
 */
public interface ScheduleMapper {

    int insertIntoSchedule(List<ScheduleBean> scheduleBeans);

    int deleteFromEpg(List<EpgModel> epgBeans);

    int selectFirstFromSchedule();
}