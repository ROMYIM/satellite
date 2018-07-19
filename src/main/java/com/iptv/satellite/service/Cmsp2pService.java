package com.iptv.satellite.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iptv.satellite.dao.cmsp2p.P2pScheduleMapper;
import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.LogBean;
import com.iptv.satellite.domain.db.ScheduleBean;
import com.iptv.satellite.domain.model.EpgModelBean;
import com.iptv.satellite.service.ICmsService;
import com.iptv.satellite.util.FormatUtil;

/**
*@author:   yim
*@date:  2018年3月23日下午12:16:06
*@description:   p2p schedule表的service实现类。
*                                  重写的两个方法才是真正的调用，其余的方法仅做测试。
*/
@Service("cmsp2pService")
public class Cmsp2pService implements ICmsService {
	
	/**
	 * p2p schedule表的持久层接口依赖注入
	 */
	@Autowired
	private P2pScheduleMapper scheduleDAO;
	
	@Override
	public void addNewIntoSchedule(List<EpgBean> epgs, LogBean log, int eachInsertCount) {
		try {
			if (epgs != null && epgs.size() > 0) {                                            //判断要插入数据的数量
				List<ScheduleBean> scheduleBeans = new ArrayList<>();
				for (EpgBean epgBean : epgs) {
					scheduleBeans.add(FormatUtil.epgToCms(epgBean));
				}
				if (scheduleBeans.size() > eachInsertCount) {                                          //插入数目大于每次的插入数目就间隔插入
					int insertCount = log.getInsertCount();
					for (int j = 0; j < scheduleBeans.size() / eachInsertCount + 1; j++) {             //防止不能整除，所以总循环加一
						int count = insertCount + eachInsertCount > scheduleBeans.size() ? scheduleBeans.size() : 
							insertCount + eachInsertCount;                                   //防止最后一次循环超过数据数组的界限
						insertCount += scheduleDAO.insertIntoP2pFromSchedule(scheduleBeans.subList(insertCount, count));     //插入新数据并累加数目
					}
					log.setInsertCount(insertCount);                                         //把插入数目写入日志
				} else {
					log.setInsertCount(scheduleDAO.insertIntoP2pFromSchedule(scheduleBeans));               //直接插入并添加到日志
				}                                         
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteOldFromEpg(List<EpgModelBean> epgModels, LogBean log, int eachDeletCount) {
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
	
	public List<Integer> findOldIdFromSchedule(List<EpgModelBean> epgModels, Integer pageNum, Integer pageSize) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("pageNum", pageNum);
		paramsMap.put("pageSize", pageSize);
		paramsMap.put("list", epgModels);
		return scheduleDAO.selectOldIdFromSchedule(paramsMap);
	}
	
	public int findCountFromSchedule() {
		return scheduleDAO.selectCountFromSchedule();
	}
	
	public int deleteById(List<Integer> idList) {
		return scheduleDAO.deleteByScheduleId(idList);
	}

	@Override
	public int findFirstFromSchedule() {
		return scheduleDAO.selectFirstFromSchedule();
	}

}
