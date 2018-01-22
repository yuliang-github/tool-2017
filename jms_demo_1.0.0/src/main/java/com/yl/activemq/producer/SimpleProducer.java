package com.yl.activemq.producer;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.yl.utils.ActiveMQUtils;
import com.yl.utils.PropertiesReader;

/**
 * 简单的消息生产者
 * @author alex
 */
public class SimpleProducer {

	private static final Logger log = Logger.getLogger(SimpleProducer.class);
	
	 private static final String SIMPLE_QUEUE_NAME="SIMPLE_QUEUE_NAME";
	
	/**
	 * 发送消息
	 * @param body 对象类型消息 务必实现序列化接口
	 */
	public void sendMsg(Serializable body){
		//获取连接
		Connection conn = null;
		try {
			conn = ActiveMQUtils.getConnection();;
			if(conn == null){
				log.info("连接ActiveMq服务失败,请重试!");
				return;
			}
			// 开启连接
			conn.start();
			// 开启回话 参数1：是否开启事务 参数2：会话的应答模式
			Session session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
			// 创建队列
			Destination queue = session.createQueue(PropertiesReader.getStringValue(SIMPLE_QUEUE_NAME));
			// 创建消息生产者
			MessageProducer producer = session.createProducer(queue);
			//发送
			ObjectMessage message = session.createObjectMessage();
			message.setObject(body);
			producer.send(message);
			log.info("消息发送成功");
		} catch (Exception e) {
			log.error("消息发送失败",e);
		}finally {
		}
	}
	
}
