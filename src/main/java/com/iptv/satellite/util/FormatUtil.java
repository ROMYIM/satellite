package com.iptv.satellite.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.iptv.satellite.constant.CronType;
import com.iptv.satellite.domain.db.EpgBean;
import com.iptv.satellite.domain.db.ScheduleBean;

/**
*@author:   yim
*@date:  2018年3月26日下午3:45:05
*@description:   类型，格式转换工具类
*/
public class FormatUtil {
	public static final DecimalFormat NUMBER_FORMATTER = (DecimalFormat) NumberFormat.getInstance();
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 根据指定格式把时间戳转为字符串
	 * @param format  字符串格式
	 * @param timestamp  时间戳
	 * @return  指定格式的字符串
	 */
	public static String formatTimestamp(String format, long timestamp) {
		NUMBER_FORMATTER.applyPattern(format);
		return NUMBER_FORMATTER.format(timestamp);
	}
	
	/**
	 * 把时间字符串转为指定的cron表达式
	 * @param time  时间字符串
	 * @param type  cron表达式类型，定时任务的，还是间隔任务的
	 * @return  返回cron表达式字符串
	 */
	public static String timeToCron(String time, CronType type) {
		String cron = " * * ?";
		StringBuffer stringBuffer = new StringBuffer("0 ");
		Integer hour = Integer.valueOf(time.substring(0, time.indexOf(":")));
		String minute = time.substring(time.indexOf(":") + 1);
		if (type == CronType.TIMING) {
			stringBuffer.append(minute).append(" ").append(hour).append(cron);
		} else if (type == CronType.INTERVAL) {
			stringBuffer.append(" ").append("0 0/").append(hour).append(cron);
		}
		return stringBuffer.toString();
	}

	public static Date timeToDate(String timeString) {
		int hour = Integer.valueOf(timeString.substring(0, 2));
		int minute = Integer.valueOf(timeString.substring(3));
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minute);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 把时间字符串转为指定的时间戳，针对于间隔任务，只截取小时字段做转换
	 * @param time  时间字符串
	 * @return 返回的时间戳
	 */
	public static long hourToPeriod(String time) throws NumberFormatException{
		Long period = Long.valueOf(time);
		return period * 1000 * 60 * 60;
	}
	
	public static String getCmsAirStartTime(String epgStartTime, SimpleDateFormat dateFormatter) throws ParseException {
		long timeStamp = dateFormatter.parse(epgStartTime).getTime() + 28800000;
		return dateFormatter.format(new Date(timeStamp));
	}
	
	public static String getCmsAirEndTime(String cmsAirStartTime, Integer duration, SimpleDateFormat dateFormatter) throws ParseException {
		long timeStamp = dateFormatter.parse(cmsAirStartTime).getTime() + duration * 1000;
		return dateFormatter.format(new Date(timeStamp));
	}
	
	public static String getCmsStartDate(String cmsAirStartTime) {
		StringBuilder stringBuilder = new StringBuilder(19);
		stringBuilder.append(cmsAirStartTime.substring(0, 10)).append(" ").append("00:00:00");
		return stringBuilder.toString();
	}
	
	public static String getCmsStartTime(String cmsAirStartTime) {
		return new StringBuilder(6)
				.append(cmsAirStartTime.substring(11, 13))
				.append(cmsAirStartTime.substring(14, 16))
				.append(cmsAirStartTime.substring(17)).toString();
	}
	
	public static String getCmsDuration(Integer duration) {
		StringBuilder stringBuilder = new StringBuilder(6);
		stringBuilder.append(String.format("%02d", duration / 3600)).append(String.format("%02d", duration % 3600 / 60)).append(String.format("%02d", duration % 60));
		return stringBuilder.toString();
	}
	
	public static ScheduleBean epgToCms(EpgBean epgBean) {
		ScheduleBean scheduleBean = new ScheduleBean();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			scheduleBean.setProgramName(epgBean.getProgramName());
			scheduleBean.setReleaseStatus(1);
			scheduleBean.setDuration(getCmsDuration(epgBean.getDuration()));
			String description = (epgBean.getExtendedDescription() == null || epgBean.getExtendedDescription().length() <= 0) ? epgBean.getShortDescription() : epgBean.getExtendedDescription();
			scheduleBean.setDescription(description);
			scheduleBean.setLanguage(epgBean.getLanguage());
			scheduleBean.setSatellite(epgBean.getSatellite());
			scheduleBean.setChannelCode(epgBean.getChannelName());
			scheduleBean.setReleaseTime(dateFormatter.format(new Date()));
			scheduleBean.setStatus(1);
			scheduleBean.setAirStartTime(getCmsAirStartTime(epgBean.getStartTime(), dateFormatter));
			scheduleBean.setAirEndTime(getCmsAirEndTime(scheduleBean.getAirStartTime(), epgBean.getDuration(), dateFormatter));
			scheduleBean.setStartDate(getCmsStartDate(scheduleBean.getAirStartTime()));
			scheduleBean.setStartTime(getCmsStartTime(scheduleBean.getAirStartTime()));
		} catch (ParseException | NumberFormatException e) {
			e.printStackTrace();
		}
		return scheduleBean;
	}
}
