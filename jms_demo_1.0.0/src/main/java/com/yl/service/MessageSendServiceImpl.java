package com.yl.service;

import javax.annotation.PostConstruct;

import com.yl.activemq.consumer.BaseConsumer;
import com.yl.activemq.consumer.MessageReciever;
import com.yl.activemq.consumer.UserBuyConsumer;
import com.yl.activemq.consumer.UserLoginConsumer;
import com.yl.activemq.message.BaseMessage;
import com.yl.activemq.producer.MessageClient;
import com.yl.response.JmsResponse;

public class MessageSendServiceImpl implements MessageSendService{
	
	private static MessageReciever userLoginRecieve;
	
	private static MessageReciever userBuyRecieve;
	
	@PostConstruct
	public void init(){
		BaseConsumer userLoginConsumer = new UserLoginConsumer();
		BaseMessage message = new BaseMessage("test_user_login");
		message.setQueueName("test_user_login");
		userLoginRecieve = new MessageReciever(userLoginConsumer,message);
		userLoginRecieve.start();
		
		
		BaseConsumer userBuyConsumer = new UserBuyConsumer();
		message = new BaseMessage("test_user_buy");
		userBuyRecieve = new MessageReciever(userBuyConsumer, message);
		userBuyRecieve.start();
		
	}
	
	@Override
	public JmsResponse send(BaseMessage message) {
		/*
		 * 此处消息入库 设为初始状态
		 * 因为演示模式  此处不进行此操作
		 * 生产中可考虑入库
		 */
		JmsResponse response = MessageClient.getInstance().send(message);
		if(response.isSuccess()){
			/*
			 * 此处更改消息状态为成功发送 
			 */
		}else{
			/*
			 * 此处更改消息状态为发送失败
			 * response.getMessage();
			 * 插入失败原因
			 */
		}
		return response;
	}

}
