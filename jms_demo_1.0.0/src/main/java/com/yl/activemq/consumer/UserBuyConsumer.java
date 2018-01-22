package com.yl.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.yl.activemq.message.UserBuyMessage;

public class UserBuyConsumer extends BaseConsumer{

	private static final String name = "TEST_USER_BUY_THREAD";
	
	@Override
	public void handler(Message msg) throws JMSException {
		if(msg != null){
			/**
			 * 此处修改消息状态为消息获取成功
			 * 后续根据具体业务
			 * 修改消息状态 直至最终状态
			 */
			ObjectMessage message = (ObjectMessage)msg;
			UserBuyMessage body = (UserBuyMessage)message.getObject();
			System.out.println("UserBuyConsumer:"+body.getBuyId()+"-"+body.getBuyMoney());
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
