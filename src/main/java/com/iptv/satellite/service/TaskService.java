package com.iptv.satellite.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.iptv.satellite.constant.CronType;
import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.LogBean;
import com.iptv.satellite.domain.model.EpgModel;
import com.iptv.satellite.domain.model.EpgTableModel;
import com.iptv.satellite.service.ICmsService;
import com.iptv.satellite.service.IEpgService;
import com.iptv.satellite.service.ILogService;
import com.iptv.satellite.util.BeanUtil;
import com.iptv.satellite.util.FormatUtil;

/**
*@author:   yim
*@date:  2018年3月14日下午2:22:43
*@description:   定时任务配置
*/
@Service
public class TaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
	
	/**
	 * 存放epg各个表的临时变量
	 * 存放epg数据源中各个表的表名（便于计算schedule的satellite）以及当前最新数据的id（便于下次更新库的查询）
	 * 存放epg各表中要更新的数据以及更新标志
	 */
	private static EpgTableModel[] epgTableModels;
	
	/**
	 * 对epg数据源几个卫星表操作的接口
	 * 封装查询epg的方法
	 */
	private final IEpgService epgService;
	
	/**
	 * 对日志表操作的接口
	 * 封装查询和添加日志的方法
	 */
	private final ILogService logService;

	/**
	 * 数据源服务操作接口
	 * 用于获取在线数据源服务列表
	 */
	private final DataSourceService dataSourceService;
	
	/**
	 * 任务调度者
	 * 用于开启定时任务和间隔任务
	 */
	private final ThreadPoolTaskScheduler taskScheduler;

	/**
	 * 任务执行者
	 * 任务开始后，任务执行者会从线程池中分配一个工作线程执行具体操作
	 */
	private final Executor taskExecutor;
	
	/**
	 * 对定时任务操作的接口
	 * 用于关闭定时任务
	 */
	private ScheduledFuture<?> timingSchedule;
	
	/**
	 * 对间隔任务操作的接口
	 * 用于关闭间隔任务
	 */
	private ScheduledFuture<?> intervalSchedule;
	
	/**
	 * 记录间隔时间
	 */
	private String intervalTime;
	
	/**
	 * 记录定时时间
	 */
	private String timingTime;
	
	/**
	 * 间隔任务
	 */
	private Runnable intervalTask = new UpdateCmsTask();
	
	/**
	 * 定时任务
	 */
	private Runnable timingTask = new UpdateCmsTask();
	
	/**
	 * epgTableModel初始化
	 */
	public void initEpgTableModels() {
		List<String> epgTableNameList = dataSourceService.getEpgTableNameList();
		epgTableModels = new EpgTableModel[epgTableNameList.size()];
		for (int i = 0; i < epgTableNameList.size(); i++) {
			epgTableModels[i] = new EpgTableModel(epgTableNameList.get(i));
		}
	}

	/**
	 * 获取epg库中的表名列表
	 */
	public List<String> getEpgTableNameList() {
		List<String> epgTableNameList = new ArrayList<>();
		if (epgTableModels != null && epgTableModels.length > 0) {
			for (EpgTableModel epgTableModel : epgTableModels) {
				epgTableNameList.add(epgTableModel.getTableName());
			}
		}
		return epgTableNameList;
	}

	/**
	 * 构造器。添加各种服务的依赖
	 */
	@Autowired
	public TaskService(ILogService logService, IEpgService epgService, DataSourceService dataSourceService, 
			ThreadPoolTaskScheduler taskScheduler, Executor taskExecutor) {
		this.logService = logService;
		this.epgService = epgService;
		this.dataSourceService = dataSourceService;
		this.taskScheduler = taskScheduler;
		this.taskExecutor = taskExecutor;
		initEpgTableModels();
	}
	
	//获取间隔时间
	public String getIntervalTime() {
		return intervalTime;
	}
	
	//获取定时时间
	public String getTimingTime() {
		return timingTime;
	}

	/**
	 * @return the dataSourceService
	 */
	public DataSourceService getDataSourceService() {
		return dataSourceService;
	}
	
	/**
	 * 中断指定任务（定时或间隔）
	 * @param scheduledFuture  指定要中断的任务操作接口
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void stopTask(ScheduledFuture<?> scheduledFuture) throws InterruptedException, ExecutionException {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);                 //指定任务的中断操作
		}
	}

	/**
	 * 启动间隔任务
	 * @param intervalTime  间隔的时间段
	 * @return  启动任务的结果。成功为true，失败为false
	 */
	public boolean startIntervalTask(String intervalTime) {  
		if (this.intervalTime == null || !this.intervalTime.equals(intervalTime)) {
			try {
				//计算间隔任务首次执行时
				long period = FormatUtil.timeToPeriod(intervalTime);                                                    
				Date firstExecuteTime = new Date(System.currentTimeMillis() + period);
				//停止当前间隔任务
				stopTask(intervalSchedule);   
				//按设置的时间重新启动间隔任务                                                                     
				intervalSchedule = taskScheduler.scheduleAtFixedRate(intervalTask, firstExecuteTime, period);
				this.intervalTime = intervalTime;
			} catch (InterruptedException | ExecutionException | NumberFormatException e) {
				LOGGER.debug(e.getMessage());                                                                                
				return false;
			}
		}
		return true;                                                      
	}                                                                                  

	/**
	 * 启动定时任务
	 * @param timingCron  执行时间的cron表达式
	 * @return    启动任务的结果
	 */
	public boolean startTimingTask(String timingTime) {
		//生成cron表达式
		String timingCron = FormatUtil.timeToCron(timingTime, CronType.TIMING);                          
		try {
			//先中断当前的定时任务
			stopTask(timingSchedule);                                                                                                                           
		} catch (InterruptedException | ExecutionException e) {
			//中断失败直接返回
			e.printStackTrace();                                                                                                                                        
			return false;
		}
		
		//以触发器的形式开启新的定时任务
		timingSchedule = taskScheduler.schedule(timingTask, new Trigger() {                 
			//根据表达式计算下次执行时间                                                                                                                                                                              
			@Override                                                                                                                                                          //根据执行时间的cron表达式生成执行时间
			public Date nextExecutionTime(TriggerContext triggerContext) {
				CronTrigger trigger = new CronTrigger(timingCron);
				Date nextExcutionTime = trigger.nextExecutionTime(triggerContext);
				return nextExcutionTime;
			}
		});
		//接受前段设置的时间
		this.timingTime = timingTime;
		return true;
	}

	/**
	 * 重置任务
	 * @return 重置任务的结果
	 */
	public boolean resetTask() {
		try {
			//停止当期任务，时间重置
			stopTask(timingSchedule);
			stopTask(intervalSchedule);
			intervalTime = null;
			timingTime = null;
			//重新初始化获取epg数据中的表集合
			initEpgTableModels();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @author yim
	 * @description  更新cms数据库和p2p数据库的任务类
	 */
	private class UpdateCmsTask implements Runnable {
		
		/**
		 * 每次删除重复的条件匹配数目
		 */
		private static final int EACH_DELETE_COUNT = 1000;
		
		/**
		 * 每次插入schedule的数目
		 */
		private static final int EACH_INSERT_COUNT = 1000;
		
		/**
		 * 针对epg的某一个表对远程数据库的的schedule表进行更新
		 * 先删除旧数据，再插入新数据
		 * 计算每一次操作的时间和时长，记录到日志表中
		 * @param scheduleService              schedule表的操作接口
		 * @param epgs                           需要插入的epg新数据  
		 * @param epgModels                用于匹配需要删除的条件数据
		 * @param log                             用于记录操作的日志
		 * @return                                    生成的操作日志
		 */
		private void updateCms(ICmsService scheduleService, List<EpgBean> epgs, List<EpgModel> epgModels, LogBean log) {
			StringBuilder infoStringBuilder = new StringBuilder(30);
			infoStringBuilder.append(Thread.currentThread().getName()).append(":").append(log.getTargetDataBase()).append("执行服务:").append(log.getTableName());
			LOGGER.info(infoStringBuilder.toString()); 
			long startTime = System.currentTimeMillis();                           //开始操作的时间戳
			scheduleService.deleteOldFromEpg(epgModels, log, EACH_DELETE_COUNT);
			scheduleService.addNewIntoSchedule(epgs, log, EACH_INSERT_COUNT);
			long endTime = System.currentTimeMillis();                       //结束操作的时间戳
			log.setDuration(endTime - startTime);                                   //计算用时
			logService.addLog(log);
		}

		/**
		 * 任务执行的程序
		 */
		@Override
		public void run() {   
			
			long totalStartTime = System.currentTimeMillis();                                                                                                                                                    
			LOGGER.info("开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(totalStartTime)); 
			
			/**
			 * 根据当前最大id值与记录中的最大id值进行对比
			 * 查询epg各表中是否有数据更新
			 * 有新数据就存入并设置更新标志
			 */
			for (int i = 0; i < epgTableModels.length; i++) {                                                                                                                                                                               
				String tableName = epgTableModels[i].getTableName();
				BigInteger maxId = epgService.findMaxIdFromEpg(tableName);  
				if (maxId != null && maxId.compareTo(epgTableModels[i].getCurrentMaxId()) > 0) {
					epgTableModels[i].setEpgBeans(epgService.findNewDataFromEpg(tableName, epgTableModels[i].getCurrentMaxId()));                                  //获取新增的数据
					epgTableModels[i].setEpgModels(epgService.findOldFromNewData(tableName, epgTableModels[i].getCurrentMaxId()));        //获取用于删除匹配的条件数据
					epgTableModels[i].setCurrentMaxId(maxId); 
					epgTableModels[i].setUpdateFlag(true);
				} else {
					epgTableModels[i].setUpdateFlag(false);
					epgService.findFirstFromEpg();
					logService.findFirstFromLog();
					logService.addLog(new LogBean(tableName, "无更新操作"));
				}
				                                 
			}
			
			/**
			 * 根据每一个远程数据库进行更新
			 * 每一个远程数据库更新分配一个线程
			 * 对每一个远程数据库更新都会根据epgTableModel查询epg各表是否有数据更新
			 */
			for (String serviceName : dataSourceService.getRuntimeServiceList()) {
				ICmsService scheduleService = BeanUtil.getBean(serviceName, ScheduleService.class);
				if (scheduleService != null) {
					taskExecutor.execute(new Runnable(){
					
						@Override
						public void run() {
							long startTime = System.currentTimeMillis();                                                                                                                                                    
							LOGGER.info("开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(startTime));   
							for (EpgTableModel epgTableModel : epgTableModels) {
								if (epgTableModel.getUpdateFlag()) {
									updateCms(scheduleService, epgTableModel.getEpgBeans(), epgTableModel.getEpgModels(), new LogBean(epgTableModel.getTableName(), scheduleService.getDataSourceName()));
								} else {
									/**
									 * 为防止数据库连接超时断开，在没有数据更新的情况下对每一个数据库做一个空的查询操作
									 */
									scheduleService.findFirstFromSchedule();
								}
							}
							long endTime = System.currentTimeMillis();                                                                                                                                                             //结束时间
							long duration = endTime - startTime;                                                                                                                                                        //计算用时
							LOGGER.info(scheduleService.getDataSourceName() + "持续时间：" + new SimpleDateFormat("mm:ss.SSS").format(duration));
						}
					});
				} else {
					LOGGER.info("无法获取服务");
				}
			}
		}
	}

}
