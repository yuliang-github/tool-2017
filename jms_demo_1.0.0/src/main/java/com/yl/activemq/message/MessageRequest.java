package com.yl.activemq.message;

import java.io.Serializable;

public class MessageRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected Long msgId;
	
	protected String queueName;

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
}
