package com.iptv.satellite.dao.cms;

import java.util.List;

import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.ScheduleBean;
import com.iptv.satellite.domain.model.EpgModelBean;

/** 	 	
*@author:   yim
*@date:  2018年3月14日下午12:10:50
*@description:   cms数据库中对schedule持久层接口
*/
public interface ScheduleMapper {
	
	/**
	 * 对schedule的插入操作
	 * @param epgs    要插入的数据
	 * @return              对数据库操作的行数
	 */
	int insertIntoSchedule(List<EpgBean> epgs);
	
	
	int insertIntoCmsFromSchedule(List<ScheduleBean> scheduleBeans);
	
	/**
	 * 对schedule的删除操作
	 * @param epgModels     用于匹配要删除的条件数据
	 * @return                            对数据操作的行数
	 */
	int deleteFromEpg(List<EpgModelBean> epgModels);
	
	int selectFirstFromSchedule();
}
