package com.iptv.satellite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iptv.satellite.dao.log.LogMapper;
import com.iptv.satellite.domain.db.LogBean;
import com.iptv.satellite.service.ILogService;

/**
*@author:   yim
*@date:  2018年3月23日下午5:56:38
*@description:   
*/
@Service
public class LogService implements ILogService {

	private final LogMapper logDAO;

	@Autowired
	public LogService(LogMapper logMapper) {
		this.logDAO = logMapper;
	}
	
	@Override
	public int addLog(LogBean log) {
		return logDAO.insertNewLog(log);
	}

	@Override
	public List<LogBean> findLogsByTableAndTarget(String tableName, String targetDb, int pageNum, int pageSize) {
		if (tableName.length() > 0 && targetDb.length() == 0) {
			return logDAO.selectLogsByTable(tableName, pageNum, pageSize);
		} else if (tableName.length() == 0 && targetDb.length() > 0) {
			return logDAO.selectLogsByTargetDb(targetDb, pageNum, pageSize);
		}
		return logDAO.selectLogsByTableAndTarget(tableName, targetDb, pageNum, pageSize);
	}

	@Override
	public List<LogBean> findAllLogs(int pageNum, int pageSize) {
		return logDAO.selectAllLogsByPages(pageNum, pageSize);
	}

	@Override
	public int findFirstFromLog() {
		return logDAO.selectFirstFromLog();
	}

	@Override
	public int findLogsPageCount(String tableName, String targetDb) {
		int logsCount = logDAO.selectLogsCount(tableName, targetDb);
		int pageCount = (logsCount % 20 == 0) ? (logsCount / 20) : (logsCount / 20 + 1);
		return pageCount;
	}

}
