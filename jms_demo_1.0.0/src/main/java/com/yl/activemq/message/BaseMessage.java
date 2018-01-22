package com.yl.activemq.message;

import java.io.Serializable;

public class BaseMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String queueName;

	private Long msgId;
	
	public BaseMessage(String queueName){
		this.queueName = queueName;
				
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	
	
}
