package com.iptv.satellite.service;

import java.math.BigInteger;
import java.util.List;

import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.model.EpgModel;

/**
*@author:   yim
*@date:  2018年3月14日下午4:47:51
*@description:   epg各表的service层接口
*/
public interface IEpgService {
	
	/**
	 * 查找当表中最新数据的id值
	 * @param tableName 表名
	 * @return  返回最新数据的id值
	 */
	BigInteger findMaxIdFromEpg(String tableName);
	
	/**
	 * 在表中查找新添加的数据
	 * @param tableName 表名
	 * @param maxId  旧数据中最大的id值，作为新数据与旧数据的分界
	 * @return  返回表中新增的数据
	 */
	List<EpgBean> findNewDataFromEpg(String tableName, BigInteger maxId);
	
	/**
	 * 在新增的数据中查找作为判定时间重复的条件数据
	 * @param tableName  表名
	 * @param maxId  旧数据中最大的id值，作为新数据与旧数据的分界
	 * @return  返回判定时间重复的条件数据
	 */
	List<EpgModel> findOldFromNewData(String tableName, BigInteger maxId);
	
	/**
	 * 为防止与数据库断开连接而作的非业务逻辑查询操作
	 * @return 返回第一条数据的id值
	 */
	BigInteger findFirstFromEpg();
	
}
