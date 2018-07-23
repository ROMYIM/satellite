package com.iptv.satellite.service;

import java.util.ArrayList;
import java.util.List;

import com.iptv.satellite.dao.ScheduleDAO;
import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.LogBean;
import com.iptv.satellite.domain.db.ScheduleBean;
import com.iptv.satellite.domain.model.EpgModel;
import com.iptv.satellite.util.FormatUtil;

/**
 * ScheduleService
 */
public class ScheduleService implements ICmsService {

	private ScheduleDAO scheduleDAO;
	
	private String dataSourceName;

    /**
     * @param scheduleDAO the scheduleDAO to set
     */
    public void setScheduleDAO(ScheduleDAO scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
	}
	
	/**
	 * @param dataSourceName the dataSourceName to set
	 */
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	/**
	 * @return the dataSourceName
	 */
	@Override
	public String getDataSourceName() {
		return dataSourceName;
	}

	@Override
	public void addNewIntoSchedule(List<EpgBean> epgs, LogBean log, int eachInsertCount) {
		try {
			if (epgs != null && epgs.size() > 0) {                                            //判断要插入数据的数量
				List<ScheduleBean> scheduleBeans = new ArrayList<>();
				for (EpgBean epgBean : epgs) {
					scheduleBeans.add(FormatUtil.epgToCms(epgBean));
				}
				if (scheduleBeans.size() > eachInsertCount) {                              //插入数目大于每次的插入数目就间隔插入
					int insertCount = log.getInsertCount();
					for (int j = 0; j < scheduleBeans.size() / eachInsertCount + 1; j++) {          //防止不能整除，所以总循环加一
						int count = insertCount + eachInsertCount > scheduleBeans.size() ? scheduleBeans.size() : 
							insertCount + eachInsertCount;                      //防止最后一次循环超过数据数组的界限
						insertCount += scheduleDAO.addSchedules(scheduleBeans.subList(insertCount, count));     //插入新数据并累加数目
					}
					log.setInsertCount(insertCount);                                        //把插入数目写入日志
				} else {
					log.setInsertCount(scheduleDAO.addSchedules(scheduleBeans));   //直接插入并添加到日志
				}                                         
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteOldFromEpg(List<EpgModel> epgModels, LogBean log, int eachDeleteCount) {
		try {
			if (epgModels != null && epgModels.size() > 0) {
				if (epgModels.size() > eachDeleteCount) {
					int epgModelsIndex = 0;
					int deleteCount = log.getDeleteCount();
					for (int j = 0; j < epgModels.size() / eachDeleteCount + 1; j++) {   //防止不能整除，所以总循环加一
						int count = epgModelsIndex + eachDeleteCount > epgModels.size() ? epgModels.size() : 
							epgModelsIndex + eachDeleteCount;                //防止最后一次循环超过条件数组的界限
						deleteCount += scheduleDAO.deleteOldEpg(epgModels.subList(epgModelsIndex, count));   //删除旧数据并累加删除数目
						epgModelsIndex = count;
					}
					log.setDeleteCount(deleteCount);
				} else {
					log.setDeleteCount(scheduleDAO.deleteOldEpg(epgModels));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int findFirstFromSchedule() {
		return scheduleDAO.getFirstSchedule();
	}

    
}