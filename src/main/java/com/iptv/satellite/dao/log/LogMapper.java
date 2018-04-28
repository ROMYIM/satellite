package com.iptv.satellite.dao.log;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iptv.satellite.domain.db.LogBean;

/**
*@author:   yim
*@date:  2018年3月23日下午4:24:56
*@description:   log表持久层接口
*/
public interface LogMapper {
	
	/**
	 * 添加一条新的日志
	 * @param log  日志实例
	 * @return   数据库操作行数
	 */
	int insertNewLog(LogBean log);
	
	/**
	 * 根据epg的表名查询日志
	 * @param tableName epg数据库中的表名
	 * @param pageNum 查询记录的起始数，相当于前端分页的页数
	 * @param pageSize  每次查询最大记录数，相当于前端的页面大小
	 * @return  返回查询结果
	 */
	List<LogBean> selectLogsByTable(@Param("tableName")String tableName, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	/**
	 * 根据目标数据库（cms或p2p）和epg数据库的表名查询日志
	 * @param tableName  epg数据库中的表名
	 * @param targetDb   目标数据库
	 * @param pageNum  查询记录的起始数，相当于前端分页的页数
	 * @param pageSize  每次查询最大记录数，相当于前端的页面大小
	 * @return  返回查询结果
	 */
	List<LogBean> selectLogsByTableAndTarget(@Param("tableName")String tableName, @Param("targetDb")String targetDb, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	/**
	 * 查询日志
	 * @param pageNum  查询记录的起始数，相当于前端分页的页数
	 * @param pageSize  每次查询最大记录数，相当于前端的页面大小
	 * @return  返回查询结果
	 */
	List<LogBean> selectAllLogsByPages(@Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	/**
	 * 根据目标数据库（cms或p2p）查询日志
	 * @param targetDb 目标数据库
	 * @param pageNum   查询记录的起始数，相当于前端分页的页数
	 * @param pageSize  每次查询最大记录数，相当于前端的页面大小
	 * @return  返回查询结果
	 */
	List<LogBean> selectLogsByTargetDb(@Param("targetDb")String targetDb, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
	
	/**
	 * 查询第一条日志的id，防止数据库因空闲时间过长而断开连接
	 * @return 第一条日志的id
	 */
	int selectFirstFromLog();
	
	/**
	 * 查询日志表的日志总数，用于显示分页的总页数
	 * @return 日志记录总数
	 */
	int selectLogsCount(@Param("tableName")String tableName, @Param("targetDb")String targetDb);
}
