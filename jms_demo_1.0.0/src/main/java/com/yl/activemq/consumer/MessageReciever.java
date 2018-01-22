package com.yl.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;

import com.yl.activemq.message.BaseMessage;
import com.yl.activemq.producer.MessageClient;

public class MessageReciever {
	
	private static final long SLEEP_TIME = 1000*10;
	
	private static final Logger log = Logger.getLogger(MessageReciever.class);
	
	private BaseConsumer consumer;
	
	private BaseMessage message;
	
	private boolean flag = true;
	
	public MessageReciever(BaseConsumer conusmer,BaseMessage message){
		this.consumer = conusmer;
		this.message = message;
	}
	
	private Thread executor = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				while(true){
					hand();
				}
			} catch (Exception e) {
				log.error("消息消费异常",e);
			}
		}
	});
	
	public boolean start(){
		executor.setName(consumer.getName());
		boolean ret = false;
		if(flag){
			executor.start();
			ret = true;
		}
		return ret;
	}
	
	public boolean shutDown(){
		boolean ret = false;
		if(flag){
			flag = false;
		}
		return ret;
	}
	
	public void hand(){
		Message msg = MessageClient.getInstance().recieve(message);
		System.out.println("拉取消息:"+msg);
		if(msg != null){
			try {
				consumer.handler(msg);
			} catch (JMSException e) {
				log.error("消息消费异常",e);
			}
		}else{
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				log.error("消息拉取异常",e);
			}
		}
	}
	
}
