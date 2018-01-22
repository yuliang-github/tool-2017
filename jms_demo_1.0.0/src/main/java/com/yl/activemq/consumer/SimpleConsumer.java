package com.yl.activemq.consumer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.yl.activemq.message.SimpleMessage;
import com.yl.utils.ActiveMQUtils;
import com.yl.utils.PropertiesReader;

/**
 * 简单的消息消费者
 * @author alex
 */
public class SimpleConsumer {
	
	private static final Logger log = Logger.getLogger(SimpleConsumer.class);
	
	 private static final String SIMPLE_QUEUE_NAME="SIMPLE_QUEUE_NAME";
	
	 public void consume(){
		 Connection conn = null;
		 try{
			// 获取连接
		    conn = ActiveMQUtils.getConnection();
			 if(conn == null){
				 log.info("连接ActiveMq服务失败,请重试!");
				 return;
			 }
			 // 开启连接
			 conn.start();
			 // 开启回话
			 Session session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
			 // 回话创建目标
			 Destination queue = session.createQueue(PropertiesReader.getStringValue(SIMPLE_QUEUE_NAME));
			 // 创建消息消费者
			 MessageConsumer consumer = session.createConsumer(queue);
			 // 消费消息
			 consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
					ObjectMessage message = (ObjectMessage)msg;
					try {
						SimpleMessage object = (SimpleMessage) message.getObject();
						System.out.println("消息消费成功:"+object);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		 }catch (Exception e){
			 log.error("消息处理失败",e);
		 }finally {
//			if(conn != null){
//				try {
//					conn.close();
//				} catch (JMSException e) {
//					e.printStackTrace();
//				}
//			}
		}
		 
	 }
}
