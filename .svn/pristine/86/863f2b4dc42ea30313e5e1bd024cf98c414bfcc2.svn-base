package com.yim.satellite.config;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.yim.satellite.domain.epg.Epg;
import com.yim.satellite.service.IEpgService;

/**
*@author:   yim
*@date:  2018年3月14日下午2:22:43
*@description:   
*/
@Component
public class ScheduleConfig implements SchedulingConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleConfig.class);
	private static final String DEFAULT_CRON = "0/30 * * * * ?";
	private String intervalCron = DEFAULT_CRON;
	private String timingCron = "0 12 * * * ?";
	
	@Autowired
	private IEpgService epgService;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		taskRegistrar.addTriggerTask(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LOGGER.info("测试间隔任务");
				List<Epg> epgs = epgService.findEpgFrom_1055e();
				LOGGER.info(String.valueOf(epgs.size()));
			}
		}, new Trigger() {
			
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				// TODO Auto-generated method stub
				CronTrigger trigger = new CronTrigger(intervalCron);
				Date nextExcutionTime = trigger.nextExecutionTime(triggerContext);
				return nextExcutionTime;
			}
		});
		
		taskRegistrar.addCronTask(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LOGGER.info("测试定时任务");
				List<Epg> epgs = epgService.findEpgFrom_1055e();
				LOGGER.info(String.valueOf(epgs.size()));
			}
		}, timingCron);
	}

	public void setIntervalCron(String intervalCron) {
		this.intervalCron = intervalCron;
	}

	public void setTimingCron(String timingCron) {
		this.timingCron = timingCron;
	}

}
