package com.yl.utils;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class ActiveMQUtils {

private static final Logger log = Logger.getLogger(ActiveMQUtils.class);
	
	private static final String USERNAME = "ACTIVEMQ_USERNAME";
	private static final String PASSWORD = "ACTIVEMQ_PASSWORD";
	private static final String BROKER_URL = "ACTIVEMQ_BROKER_URL";
	
	private static Connection conn = null;
	
	private ActiveMQUtils(){
		
	}
	
	/**
	 * 获取连接工厂
	 */
	private static class ConnectionFactoryProvider {
		public static final ConnectionFactory factory = new ActiveMQConnectionFactory(PropertiesReader.getStringValue(USERNAME),
				PropertiesReader.getStringValue(PASSWORD),PropertiesReader.getStringValue(BROKER_URL));
	}
	
	/**
	 * 获取mq连接
	 */
	public static Connection getConnection(){
		try {
			if(conn == null){
				synchronized (ActiveMQUtils.class) {
					if(conn == null){
						conn = ConnectionFactoryProvider.factory.createConnection();
					}
				}
			}
		}catch(Exception e){
			log.error("获取连接异常",e);
		}
		return conn;
	}
	
	public static Session getSession(){
		Session session = null;
		try {
			// 获取连接
			Connection conn = getConnection();
			if(conn != null){
				// 开启连接
				conn.start();
				// 开启会话
				session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
			}
		} catch (Exception e) {
			log.error("开启会话异常",e);
		}
		return session;
	}
	
}
