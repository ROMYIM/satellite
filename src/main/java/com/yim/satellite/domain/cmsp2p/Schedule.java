package com.yim.satellite.domain.cmsp2p;

import java.util.Date;

/**
*@author:   yim
*@date:  2018年3月14日上午11:19:04
*@description:   cmsp2p.schedule
*/
public class Schedule {

	private Integer id;
	private Date airEndTime;
	private String channelCode;
	private Integer channelId;
	private String cpContentId;
	private Integer cpSpid;
	private Date deleteTime;
	private String descriprion;
	private String duration;
	private String objectId;
	private String programName;
	private Integer releaseStatus;
	private Date releaseTime;
	private Date startTime;
	private Integer status;
	private String storageDuration;
	private String replay;
	private String language;
	private String satellite;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getAirEndTime() {
		return airEndTime;
	}
	public void setAirEndTime(Date airEndTime) {
		this.airEndTime = airEndTime;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public String getCpContentId() {
		return cpContentId;
	}
	public void setCpContentId(String cpContentId) {
		this.cpContentId = cpContentId;
	}
	public Integer getCpSpid() {
		return cpSpid;
	}
	public void setCpSpid(Integer cpSpid) {
		this.cpSpid = cpSpid;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public String getDescriprion() {
		return descriprion;
	}
	public void setDescriprion(String descriprion) {
		this.descriprion = descriprion;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public Integer getReleaseStatus() {
		return releaseStatus;
	}
	public void setReleaseStatus(Integer releaseStatus) {
		this.releaseStatus = releaseStatus;
	}
	public Date getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStorageDuration() {
		return storageDuration;
	}
	public void setStorageDuration(String storageDuration) {
		this.storageDuration = storageDuration;
	}
	public String getReplay() {
		return replay;
	}
	public void setReplay(String replay) {
		this.replay = replay;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
}
