package com.yl.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;

public interface Consumer {
	
	public void handler(Message msg) throws JMSException;
	
	public String getName();
	
	
}
