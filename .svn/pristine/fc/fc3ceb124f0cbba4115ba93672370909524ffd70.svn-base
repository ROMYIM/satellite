package com.iptv.satellite.service;

import java.util.List;

import com.iptv.satellite.domain.db.LogBean;

/**
*@author:   yim
*@date:  2018年3月23日下午5:54:32
*@description:   关于日志的service层接口
*/
public interface ILogService {
	
	/**
	 * 添加一个日志记录
	 * @param log  一个日志数据
	 * @return  数据库操作的行数
	 */
	int addLog(LogBean log);
	
	/**
	 * 根据epg的表名和数据库名（cms或p2p）进行分页查找日志记录
	 * @param tableName epg表名
	 * @param targetDb  数据库名
	 * @param pageNum  页码数
	 * @param pageSize  页面大小，查找记录的数目
	 * @return  返回日志记录
	 */
	List<LogBean> findLogsByTableAndTarget(String tableName, String targetDb, int pageNum, int pageSize);
	
	/**
	 * 分页查找记录
	 * @param pageNum  页码数
	 * @param pageSize  页面大小，查找记录的数目
	 * @return  返回日志记录
	 */
	List<LogBean> findAllLogs(int pageNum, int pageSize);
	
	/**
	 * 查询第一条日志，防止数据库因空闲时间过长而断开连接
	 * @return  返回第一条日志的id
	 */
	int findFirstFromLog();
	
	/**
	 * 以20条记录为页面大小，查询指定表名和指定数据库名的日志记录的总页数
	 * @return   日志记录的总页数
	 */
	int findLogsPageCount(String tableName, String targetDb);
}
