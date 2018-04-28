package com.iptv.satellite.util;
/**
*@author:   yim
*@date:  2018年3月20日上午11:16:28
*@description:   卫星工具类。根据您epg各表名，service_id，frequency计算schedule表中的satellite字段的值
*/
public class SatelliteUtil {
	public static String getSatelliteAngle(String tableName) {
		char c = tableName.charAt(tableName.length() - 1);
		switch (c) {
		case 'e':
			return tableName.substring(0, tableName.length() - 1);
		case 'w':
			Integer angle = Integer.valueOf(tableName.substring(0, tableName.length() - 1));
			return String.valueOf(3600 - angle);
		default:
			return tableName;
		}
	}
}
