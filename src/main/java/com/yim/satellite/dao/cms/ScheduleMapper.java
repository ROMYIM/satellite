package com.yim.satellite.dao.cms;

import java.util.List;

import com.yim.satellite.domain.epg.Epg;

/**
*@author:   yim
*@date:  2018年3月14日下午12:10:50
*@description:   
*/
public interface ScheduleMapper {
	int insertFromEpg_1055e(List<Epg> epgs);
	
	int insertFromEpg_50w(List<Epg> epgs);
	
	int deleteFrom_1055eStartTime(List<String> startDates);
	
	int deleteFrom_50wStartTime(List<String> startDates);
}
