package com.iptv.satellite.controller;
/**
*@author:   yim
*@date:  2018年3月26日上午9:38:05
*@description:   日志控制层，对日志相关的逻辑操作
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.iptv.satellite.domain.db.LogBean;
import com.iptv.satellite.domain.model.ResponseMessage;
import com.iptv.satellite.domain.model.ResultModel;
import com.iptv.satellite.service.ILogService;
import com.iptv.satellite.service.TaskService;

@RestController
public class HomeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * 任务调度的服务接口，用于修改相关任务的时间设置
	 */
	private final TaskService taskService;
	
	/**
	 * 日志服务接口，用于对日志查询操作
	 */
	private final ILogService logService;

	/**
	 * 任务调度和日志服务的依赖注入
	 */
	@Autowired
	public HomeController(TaskService taskService, ILogService logService) {
		this.taskService = taskService;
		this.logService = logService;
	}
	
	/**
	 * 首页响应，默认查询前20条日志记录，并返回两个任务的设置时间
	 * @param modelAndView
	 * @return  返回首页以及响应的模型对象（日志记录和设置的时间）
	 */
	@RequestMapping("/")
	public ModelAndView index(ModelAndView modelAndView) {
		List<LogBean> logs = logService.findAllLogs(0, 20);
		List<String> serviceStrings = taskService.getDataSourceService().getRuntimeServiceList();
		List<String> dataSourceList = new ArrayList<>(serviceStrings.size());
		for (String serviceName : serviceStrings) {
			dataSourceList.add(serviceName.replace("ScheduleService", ""));
		}
		int pageCount = logService.findLogsPageCount("", "");
		modelAndView.addObject("pageCount", pageCount);
		modelAndView.addObject("timingTime", taskService.getTimingTime());
		modelAndView.addObject("intervalTime", taskService.getIntervalTime());
		modelAndView.addObject("logList", logs).addObject("dataSources", dataSourceList).setViewName("index");
		return modelAndView;
	}
	
	/**
	 * 按条件查询相关日志
	 * @param tableName  要查询epg数据表名
	 * @param targetDb  要查询的目标数据库
	 * @param pageNum  查询的起始下标，用于分页查询
	 * @return  返回查询的结果
	 */
	@RequestMapping(value = "/findLogs", method = RequestMethod.GET)
	public ResultModel findLogs(String tableName, String targetDb, int pageNum, Map<String, Object> map) {
		LOGGER.info(String.valueOf(pageNum));
		ResultModel resultModel = new ResultModel();
		List<LogBean> logBeans = null;
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (tableName.length() == 0 && targetDb.length() == 0) {
			logBeans = logService.findAllLogs(pageNum, 20);
		} else {
			logBeans = logService.findLogsByTableAndTarget(tableName, targetDb, pageNum, 20);
		}
		int pageCount = logService.findLogsPageCount(tableName, targetDb);
		map.put("pageCount", pageCount);
		map.put("logList", logBeans);
		resultModel.setCode(200);
		resultModel.setResult(map);
		return resultModel;
	}
	
	/**
	 * 设置两个任务的执行时间
	 * @param timingTime  定时任务时间参数
	 * @param intervalTime  间隔任务时间参数
	 * @return  响应消息实例，操作成功或操作失败
	 */
	@RequestMapping(value = "setCron", method = RequestMethod.POST)
	public ResponseMessage setCron(String timingTime, String intervalTime) {
		LOGGER.info("定时时间:" + timingTime);
		LOGGER.info("间隔时间:" + intervalTime);
		ResponseMessage message =  new ResponseMessage();
		message.setMessage("操作失败");
		StringBuffer messagStringBuffer = new StringBuffer(14);
		if (timingTime.length() > 0) {	
			if (taskService.startTimingTask(timingTime)) {
				messagStringBuffer.append("定时设置成功！");
			}
		}
		if (intervalTime.length() > 0) {
			if (taskService.startIntervalTask(intervalTime)) {
				messagStringBuffer.append("间隔设置成功！");
			}
		}
		message.setMessage(messagStringBuffer.toString());
		return message;
	}
}
