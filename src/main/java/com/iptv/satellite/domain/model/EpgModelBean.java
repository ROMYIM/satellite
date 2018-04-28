package com.iptv.satellite.domain.model;

/**
*@author:   yim
*@date:  2018年3月16日下午3:10:04
*@description:   用于判断schedule表中时间重复的条件模型类。
*                                 对应每一个satellite判断startDate属性。
*/
public class EpgModelBean {
	
	private Integer id;
	private String startDate;
	private String satellite;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

}
