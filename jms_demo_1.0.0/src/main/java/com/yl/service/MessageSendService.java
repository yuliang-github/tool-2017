package com.yl.service;
import com.yl.activemq.message.BaseMessage;
import com.yl.response.JmsResponse;

public interface MessageSendService {

	public JmsResponse send(BaseMessage message);
	
}
