package com.iptv.satellite.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Async;
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
	 */
	private static EpgTableModel[] epgTableModels = new EpgTableModel[14];
	
	/**
	 * 对epg数据源几个卫星表操作的接口
	 * 封装查询epg的方法
	 */
	private final IEpgService epgService;
	
	/**
	 * 对cms数据源操作的接口
	 * 封装查询、删除、添加schedule（cms数据源的表）方法
	 */
	private final ICmsService cmsService;
	
	/**
	 * 对p2p数据源操作的接口
	 * 封装查询、删除、添加schedule（p2p数据源的表）方法
	 */
	private final ICmsService cmsp2pService;
	
	/**
	 * 对日志表操作的接口
	 * 封装查询和添加日志的方法
	 */
	private final ILogService logService;
	
	/**
	 * 任务调度者
	 * 用于开启定时任务和间隔任务
	 */
	private final ThreadPoolTaskScheduler taskScheduler;
	
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
	 * 日历，计算定时与间隔运行时间的相差时间
	 */
	private Calendar calendar = Calendar.getInstance();
	
	/**
	 * 间隔任务
	 */
	private Runnable intervalTask = new UpdateCmsTask();
	
	/**
	 * 定时任务
	 */
	private Runnable timingTask = new UpdateCmsTask();
	
	/**
	 * 静态代码块
	 * epgTableModel初始化
	 */
	static {
		epgTableModels[0] = new EpgTableModel("1005E");
		epgTableModels[1] = new EpgTableModel("1055E");
		epgTableModels[2] = new EpgTableModel("130E");
		epgTableModels[3] = new EpgTableModel("1320E");
		epgTableModels[4] = new EpgTableModel("160E");
		epgTableModels[5] = new EpgTableModel("192E");
		epgTableModels[6] = new EpgTableModel("300W");
		epgTableModels[7] = new EpgTableModel("430W");
		epgTableModels[8] = new EpgTableModel("580W");
		epgTableModels[9] = new EpgTableModel("6875C");
		epgTableModels[10] = new EpgTableModel("700W");
		epgTableModels[11] = new EpgTableModel("70W");
		epgTableModels[12] = new EpgTableModel("850E");
		epgTableModels[13] = new EpgTableModel("910W");
	}

	@Autowired
	public TaskService(ILogService logService, IEpgService epgService, 
			@Qualifier("cmsService")ICmsService cmsService, @Qualifier("cmsp2pService")ICmsService cmsp2pService, 
			ThreadPoolTaskScheduler taskScheduler) {
		this.logService = logService;
		this.epgService = epgService;
		this.cmsService = cmsService;
		this.cmsp2pService = cmsp2pService;
		this.taskScheduler = taskScheduler;
	}
	
	/**
	 * 生成任务调度者
	 * 设置线程池大小为2
	 * 把任务调度者声明为bean让spring管理
	 * @return 任务调度者实例
	 */
	@Bean("taskScheduler")
	public ThreadPoolTaskScheduler createTaskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(2);
		return taskScheduler;
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
				long period = FormatUtil.timeToPeriod(intervalTime);                                                    //间隔的时间转为毫秒， 只取小时部分做转化
				Date firstExecuteTime = new Date(System.currentTimeMillis() + period);
				calendar.setTime(firstExecuteTime);
				int firstExecuteMinute = calendar.get(Calendar.MINUTE);
				int firstExecuteHour = calendar.get(Calendar.HOUR_OF_DAY);
				int timingHour = Integer.valueOf(timingTime.substring(0, 2));
				int timingMinute = Integer.valueOf(timingTime.substring(3));
				stopTask(intervalSchedule);                                                                             //先中断当前的间隔任务
				if (Math.abs(firstExecuteHour - timingHour) > 0 || Math.abs(firstExecuteMinute - timingMinute) > 5) {
					intervalSchedule = taskScheduler.scheduleAtFixedRate(intervalTask, firstExecuteTime, period);
				}
				this.intervalTime = intervalTime;
			} catch (InterruptedException | ExecutionException | NumberFormatException e) {
				LOGGER.debug(e.getMessage());                                                                                //中断失败直接返回
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
		String timingCron = FormatUtil.timeToCron(timingTime, CronType.TIMING);                          //生成cron表达式
		try {
			stopTask(timingSchedule);                                                                                                                           //先中断当前的定时任务
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();                                                                                                                                        //中断失败直接返回
			return false;
		}
		
		timingSchedule = taskScheduler.schedule(timingTask, new Trigger() {                 //以触发器的形式开启新的定时任务
			                                                                                                                                                                                    
			@Override                                                                                                                                                          //根据执行时间的cron表达式生成执行时间
			public Date nextExecutionTime(TriggerContext triggerContext) {
				CronTrigger trigger = new CronTrigger(timingCron);
				Date nextExcutionTime = trigger.nextExecutionTime(triggerContext);
				return nextExcutionTime;
			}
		});
		this.timingTime = timingTime;
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
		 * 针对epg的某一个表对（cms或p2p）的schedule表进行更新
		 * @param scheduleService              schedule表的操作接口
		 * @param logService                         日志表操作接口
		 * @param epgs                           需要插入的epg新数据  
		 * @param epgModels                用于匹配需要删除的条件数据
		 * @param log                             用于记录操作的日志
		 * @return                                    生成的操作日志
		 */
		@Async
		private void updateCms(ICmsService scheduleService, ILogService logService, List<EpgBean> epgs, List<EpgModel> epgModels, LogBean log) {
			long startTime = System.currentTimeMillis();                           //开始操作的时间戳
			scheduleService.deleteOldFromEpg(epgModels, log, EACH_DELETE_COUNT);
			scheduleService.addNewIntoSchedule(epgs, log, EACH_INSERT_COUNT);
			LOGGER.info(log.getTableName());
			long endTime = System.currentTimeMillis();                       //结束操作的时间戳
			log.setDuration(endTime - startTime);                                   //计算用时
			logService.addLog(log);
		}

		/**
		 * 任务执行的程序
		 */
		@Override
		public void run() {                        
			long totalStartTime = System.currentTimeMillis();                                                                                                                                                    //本次任务的开始时间
			LOGGER.info("开始时间：" + new SimpleDateFormat("HH:mm:ss:SSS").format(totalStartTime));      
			for (int i = 0; i < epgTableModels.length; i++) {                                                                                                                                                            //对epg的每个表进行相同操作                    
				String tableName = epgTableModels[i].getTableName();
				BigInteger maxId = epgService.findMaxIdFromEpg(tableName);                                                                                                                    //找出该表中最新数据的游标
				
				/**
				 * 如果该表有数据更新
				 * 先查询表中新的数据以及新数据中需要删除的条件数目
				 * 分别对cms数据库和p2p数据库进行更新操作，并生成相应的日志
				 * 把日志添加到日志表中
				 */
				if (maxId != null && maxId.compareTo(epgTableModels[i].getCurrentMaxId()) > 0) {                                                                            //判断该表中是否有数据更新
					List<EpgBean> epgs = epgService.findNewDataFromEpg(tableName, epgTableModels[i].getCurrentMaxId());                                 //获取新增的数据
					List<EpgModel> epgModels = epgService.findOldFromNewData(tableName, epgTableModels[i].getCurrentMaxId());         //获取用于删除匹配的条件数据
					
				} else {
					/**
					 * 为防止数据库连接超时断开，在没有数据更新的情况下对每一个数据库做一个空的查询操作
					 */
					epgService.findFirstFromEpg();
					cmsService.findFirstFromSchedule();
					cmsp2pService.findFirstFromSchedule();
					logService.findFirstFromLog();
					LogBean logBean = new LogBean(epgTableModels[i].getTableName(), "无更新操作");
					logService.addLog(logBean);
				}
				
				epgTableModels[i].setCurrentMaxId(maxId);                                                                                                                                                                //更新游标
			}

			long totalEndTime = System.currentTimeMillis();                                                                                                                                                             //结束时间
			long totalDuration = totalEndTime - totalStartTime;                                                                                                                                                        //计算用时
			LOGGER.info("持续时间：" + new SimpleDateFormat("mm:ss.SSS").format(totalDuration));
		}
	}

}