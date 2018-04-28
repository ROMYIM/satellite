package com.iptv.satellite.domain.db;
/**
*@author:   yim
*@date:  2018年3月14日上午11:47:07
*@description:   对应于epg数据库中各表的实体类
*/

import java.math.BigInteger;

public class EpgBean {
	
	private BigInteger id;
	private Integer frequency;
	private Integer serviceId;
	private String channelName;
	private String startTime;
	private Integer duration;
	private String programName;
	private String language;
	private String shortDescription;
	private String extendedDescription;
	private String satellite;
	
	public void setExtendedDescription(String extendedDescription) {
		this.extendedDescription = extendedDescription;
	}
	
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getExtendedDescription() {
		return extendedDescription;
	}
	public void setExtendedDecription(String extendedDescription) {
		this.extendedDescription = extendedDescription;
	}
}
