package com.yl.activemq.producer;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.yl.activemq.message.BaseMessage;
import com.yl.constant.ResponseConstant;
import com.yl.response.JmsResponse;
import com.yl.utils.ActiveMQUtils;

public class MessageClient {
	
	private static final Logger log = Logger.getLogger(MessageClient.class);
	
	private static MessageClient instance = null;
	
	private static Session session = ActiveMQUtils.getSession();
	
	private MessageClient() {
		
	}
	
	private void init(){
		if(instance != null){
			if(session == null){
				session = ActiveMQUtils.getSession();
			}
		}
	}
	
	public static MessageClient getInstance(){
		if(instance == null){
			synchronized (MessageClient.class) {
				if(instance == null){
					instance = new MessageClient();
					instance.init();
				}
			}
		}
		return instance;
	}
	
	
	public JmsResponse send(BaseMessage request){
		JmsResponse ret = new JmsResponse();
		try{
			String queueName = request.getQueueName();
			if(StringUtils.isBlank(queueName)){
				ret.setCode(ResponseConstant.RET.BLANK_QUEUE_NAME.getCode());
				ret.setMessage(ResponseConstant.RET.BLANK_QUEUE_NAME.getMessage());
				return ret;
			}
			if(session == null){
				ret.setCode(ResponseConstant.RET.LINK_FAILER.getCode());
				ret.setMessage(ResponseConstant.RET.LINK_FAILER.getMessage());
			}else{
				Destination des = session.createQueue(queueName);
				MessageProducer producer = session.createProducer(des);
				ObjectMessage msg = session.createObjectMessage();
				msg.setObject(request);
				producer.send(msg);
				/*
				 * 此处入库 消息状态 发送成功
				 */
				ret.setMessage("消息发送成功");
			}
		}catch(Exception e){
			ret.setCode(ResponseConstant.RET.SYS_ERROR.getCode());
			ret.setMessage(ResponseConstant.RET.SYS_ERROR.getMessage());
			log.error("消息发送异常",e);
			/*
			 * 此处修改消息状态为发送失败 标注失败原因
			 */
		}
		return ret;
	}
	
	public Message recieve(BaseMessage request){
		Message message = null;
		try {
			String queueName = request.getQueueName();
			if(StringUtils.isNotBlank(queueName)){
				System.err.println(queueName);
				if(session == null){
					return null;
				}
				Destination dest = session.createQueue(queueName);
				if(dest != null){
					MessageConsumer consumer = session.createConsumer(dest);
					message = consumer.receive(5000);
					consumer.close();
				}
			}
		} catch (Exception e) {
			log.error("消息获取失败",e);
		}
		return message;
	}
}
