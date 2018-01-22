package com.yl.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.yl.activemq.message.UserLoginMessage;

public class UserLoginConsumer extends BaseConsumer{
	
	private static final String name = "TEST_USER_LOGIN_THREAD";
	
	@Override
	public void handler(Message msg) throws JMSException {
			if(msg == null){
				return;
			}
			ObjectMessage mes = (ObjectMessage)msg;
			UserLoginMessage message = (UserLoginMessage)(mes.getObject());
			Long uid = message.getUid();
			String userName = message.getUserName();
			System.out.println("UserLoginConsumer:"+uid+"-"+userName);
	}


	@Override
	public String getName() {
		return name;
	}
}
