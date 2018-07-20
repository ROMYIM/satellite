package com.iptv.satellite.dao;

import java.util.List;

import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.ScheduleBean;

/**
 * ScheduleMapper
 */
public interface ScheduleMapper {

    int insertIntoSchedule(List<ScheduleBean> scheduleBeans);

    int deleteFromEpg(List<EpgBean> epgBeans);

    int selectFirstFromSchedule();
}