package com.iptv.satellite.service;

import java.util.List;

import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.LogBean;
import com.iptv.satellite.domain.model.EpgModel;

/**
*@author:   yim
*@date:  2018年3月18日下午4:27:33
*@description:   Schedule表的service接口。
*/
public interface ICmsService {
	
	/**
	 * 向schedule表epg的新数据
	 * 如果epg新数据的数量 > eachInserCount，则分开多次插入，每次插入eachInsertCount
	 * 如果epg的新数据 < eachInsertCount，则一次性插入epg所有的新数据
	 * @param epgs                                 需要插入的epg新数据 
	 * @param log                                    用于记录操作的日志
	 * @param eachInsertCount         每一次插入的操作数
	 */
	void addNewIntoSchedule(List<EpgBean> epgs, LogBean log, int eachInsertCount);
	
	/**
	 * 删除schedule表上时间重复的数据
	 * 如果匹配删除条件的的数据 > eachDeleteCount， 则分开多次删除，每次删除eachDeleteCount
	 * 如果epg的新数据 < eachDeleteCount，则一次性删除epg所有的新数据
	 * @param epgModels                      用于匹配需要删除的条件数据
	 * @param log                                      用于记录操作的日志
	 * @param eachDeleteCount          每一次删除的操作数
	 */
	void deleteOldFromEpg(List<EpgModel> epgModels, LogBean log, int eachDeleteCount);
	
	/**
	 * 查询schedule表的第一条记录的id
	 * @return
	 */
	int findFirstFromSchedule();

	String getDataSourceName();
}
