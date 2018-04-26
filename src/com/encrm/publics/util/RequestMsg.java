package com.encrm.publics.util;


/**
 * requestMsg消息
 */
public class RequestMsg {

	public static final Integer STATUS_OK=1;
	public static final Integer STATUS_ERROR=0;
	private Integer status;
	
	private Object content;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	
}
