package com.iptv.satellite.domain.model;
/**
*@author:   yim
*@date:  2018年3月29日下午6:09:56
*@description:   
*/
public class ResponseMessage {
	private String message;

	public ResponseMessage() {
		super();
	}

	public ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
