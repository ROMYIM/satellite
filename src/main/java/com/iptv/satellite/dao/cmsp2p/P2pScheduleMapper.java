package com.iptv.satellite.dao.cmsp2p;

import java.util.List;
import java.util.Map;

import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.ScheduleBean;
import com.iptv.satellite.domain.model.EpgModel;

/** 	 	
*@author:   yim
*@date:  2018年3月14日下午12:10:50
*@description:   p2p数据库中对schedule的持久层接口
*/
public interface P2pScheduleMapper {
	
	/**
	 * 对schedule表的插入操作
	 * @param epgs  需要插入的数据
	 * @return  数据库中的操作行数
	 */
	int insertIntoSchedule(List<EpgBean> epgs);

	/**
	 * 对schedule表的插入操作，插入的数据是已经转换好的scheduleBean
	 * @param scheduleBeans 需要插入的数据
	 * @return  数据库中的操作数
	 */
	int insertIntoP2pFromSchedule(List<ScheduleBean> scheduleBeans);
	
	/**
	 * 删除schedule表中时间重复的数据
	 * @param epgModels  与判定时间重复的条件数据
	 * @return  数据中的操作行数
	 */
	int deleteFromEpg(List<EpgModel> epgModels);
	
	/**
	 * 查询schedule表中时间重复数据主键值，时间太慢，已放弃
	 * @param paramsMap
	 * @return  返回时间重复数的主键值
	 */
	List<Integer> selectOldIdFromSchedule(Map<String, Object> paramsMap);
	
	/**
	 * 查询当前schedule表中记录数，已放弃
	 * @return 返回记录数
	 */
	int selectCountFromSchedule();
	
	/**
	 * 根据之前查询出来的主键值进行删除操作，已放弃
	 * @param idList 时间重复的数据主键集合
	 * @return 返回数据库操作的记录数
	 */
	int deleteByScheduleId(List<Integer> idList);
	
	int selectFirstFromSchedule();
}
