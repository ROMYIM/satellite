package com.iptv.satellite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iptv.satellite.dao.cms.ScheduleMapper;
import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.LogBean;
import com.iptv.satellite.domain.model.EpgModelBean;
import com.iptv.satellite.service.ICmsService;

/**
*@author:   yim
*@date:  2018年3月18日下午4:42:16
*@description:   
*/
@Service("cmsService")
public class CmsService implements ICmsService {
	
	@Autowired
	private ScheduleMapper scheduleDAO;

	@Override
	public void addNewIntoSchedule(List<EpgBean> epgs, LogBean log, int eachInsertCount) {
		// TODO Auto-generated method stub
		if (epgs != null && epgs.size() > 0) {                                            //判断要插入数据的数量
			if (epgs.size() > eachInsertCount) {                              //插入数目大于每次的插入数目就间隔插入
				int insertCount = log.getInsertCount();
				for (int j = 0; j < epgs.size() / eachInsertCount + 1; j++) {          //防止不能整除，所以总循环加一
					int count = insertCount + eachInsertCount > epgs.size() ? epgs.size() : 
						insertCount + eachInsertCount;                      //防止最后一次循环超过数据数组的界限
					insertCount += scheduleDAO.insertIntoSchedule(epgs.subList(insertCount, count));     //插入新数据并累加数目
				}
				log.setInsertCount(insertCount);                                        //把插入数目写入日志
			} else {
				log.setInsertCount(scheduleDAO.insertIntoSchedule(epgs));   //直接插入并添加到日志
			}                                         
		}
	}

	@Override
	public void deleteOldFromEpg(List<EpgModelBean> epgModels, LogBean log, int eachDeletCount) {
		// TODO Auto-generated method stub
		if (epgModels != null && epgModels.size() > 0) {
			if (epgModels.size() > eachDeletCount) {
				int epgModelsIndex = 0;
				int deleteCount = log.getDeleteCount();
				for (int j = 0; j < epgModels.size() / eachDeletCount + 1; j++) {   //防止不能整除，所以总循环加一
					int count = epgModelsIndex + eachDeletCount > epgModels.size() ? epgModels.size() : 
						epgModelsIndex + eachDeletCount;                //防止最后一次循环超过条件数组的界限
					deleteCount += scheduleDAO.deleteFromEpg(epgModels.subList(epgModelsIndex, count));   //删除旧数据并累加删除数目
					epgModelsIndex = count;
				}
				log.setDeleteCount(deleteCount);
			} else {
				log.setDeleteCount(scheduleDAO.deleteFromEpg(epgModels));
			}
		}
	}

	@Override
	public int findFirstFromSchedule() {
		// TODO Auto-generated method stub
		return scheduleDAO.selectFirstFromSchedule();
	}

}
