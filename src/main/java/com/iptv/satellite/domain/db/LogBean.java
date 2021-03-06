package com.iptv.satellite.domain.db;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
*@author:   yim
*@date:  2018年3月23日下午2:10:21
*@description:   对应于日志表的实体类，每条日志记录从epg的某一个表（如1055e），查询记录到schedule表删除和插入的基本信息
*/
public class LogBean {
	
	/**
	 * 日志表的主键，自增
	 */
	private Integer id;
	
	/**
	 * 
	 */
	private String tableName;
	private Integer insertCount;
	private Integer deleteCount;
	private String targetDataBase;
	private String duration;
	private String operateTime;
	
	public LogBean() {
		super();
	}
	
	public LogBean(String tableName, String  targetDataBase) {
		super();
		this.tableName = tableName;
		this.insertCount = 0;
		this.deleteCount = 0;
		this.targetDataBase =  targetDataBase;
		this.duration = "00.000";
		this.operateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Integer getInsertCount() {
		return insertCount;
	}
	
	public void setInsertCount(Integer insertCount) {
		this.insertCount = insertCount;
	}
	
	public Integer getDeleteCount() {
		return deleteCount;
	}
	
	public void setDeleteCount(Integer deleteCount) {
		this.deleteCount = deleteCount;
	}
	
	public String getTargetDataBase() {
		return  targetDataBase;
	}
	public void setTargetDataBase(String  targetDataBase) {
		this. targetDataBase =  targetDataBase;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setDuration(long timeStamp) {
		this.duration = new SimpleDateFormat("ss.SSS").format(timeStamp);
	}
}
