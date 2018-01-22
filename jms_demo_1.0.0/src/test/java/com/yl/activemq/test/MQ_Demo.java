package com.yl.activemq.test;

import java.math.BigDecimal;

import org.junit.Test;

import com.yl.activemq.consumer.SimpleConsumer;
import com.yl.activemq.message.SimpleMessage;
import com.yl.activemq.message.UserBuyMessage;
import com.yl.activemq.message.UserLoginMessage;
import com.yl.activemq.producer.SimpleProducer;
import com.yl.response.JmsResponse;
import com.yl.service.MessageSendServiceImpl;

public class MQ_Demo {

	@Test
	public void test01() throws InterruptedException{
		SimpleProducer producer = new SimpleProducer();
		SimpleMessage body = new SimpleMessage();
		body.setUid("007");
		body.setSex("man");
		body.setName("bangde");
		producer.sendMsg(body);
		SimpleConsumer consumer = new SimpleConsumer();
		consumer.consume();
		
		Thread.sleep(10000);
	}
	
	
	@Test
	public void test02() throws InterruptedException{
		MessageSendServiceImpl service = new MessageSendServiceImpl();
		service.init();
		for(int i=1;i<=10;i++){
			UserLoginMessage message = new UserLoginMessage("test_user_login");
			message.setUid(i*111111L);
			message.setUserName("00"+i);
			JmsResponse send = service.send(message);
			System.err.println(send);
		}
		Thread.sleep(1000000);
	}
	
	public static void main(String[] args) throws InterruptedException {
		MessageSendServiceImpl service = new MessageSendServiceImpl();
//		JmsTemplate t = new JmsTemplate();
//		final UserLoginMessage message = new UserLoginMessage("test_user_login");
//		t.send("", new MessageCreator() {
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				ObjectMessage objectMessage = session.createObjectMessage();
//				objectMessage.setObject(message);
//				return objectMessage;
//			}
//		});
		for(int i=1;i<=10;i++){
			UserLoginMessage message = new UserLoginMessage("test_user_login");
			message.setUid(i*111111L);
			message.setUserName("00"+i);
			JmsResponse send = service.send(message);
			System.err.println(send);
		}
		service.init();
		Thread.sleep(1000000);
	}
	
	
	@Test
	public void test03() throws InterruptedException{
		MessageSendServiceImpl service = new MessageSendServiceImpl();
		service.init();
		for(int i=1;i<=10;i++){
			UserBuyMessage message = new UserBuyMessage("test_user_buy");
			
			message.setBuyId(i*22222L);
			message.setBuyMoney(new BigDecimal(i*22222L));
			JmsResponse send = service.send(message);
			System.err.println(send);
		}
		Thread.sleep(1000000);
	}
	
	@Test
	public void test04(){
		UserLoginMessage message = new UserLoginMessage("chole_test");
		message.setMsgId(1234L);
		message.setUid(11111L);
		message.setUserName("miss");
		UserLoginMessage clone = (UserLoginMessage) message.clone();
		
		System.err.println(message == clone);
		
		System.err.println(message);
		
		System.err.println("clone:"+clone);
		
	}
	
	@Test
	public void test05(){
		
		
	}
}
