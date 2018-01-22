package com.yl.activemq.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserLoginMessage extends BaseMessage{
	
	private static final long serialVersionUID = 1L;

	public UserLoginMessage(String queueName) {
		super(queueName);
	}
	
	private Long uid;
	
	private String userName;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public Object clone(){
		Object cloneObject = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			// 将对象序列化成到输出流中
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			// this代表方法调用者 将this输出到BOS中
			oos.writeObject(this);
			
			// 从对象输出流中读取对象
			bis = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bis);
			cloneObject = ois.readObject();
		} catch (Exception e) {
			System.err.println(e);
		}finally {
			try {
				if(bos != null){
					bos.close();
				}
				if(oos != null){
					oos.close();
				}
				if(bis != null){
					bis.close();
				}
				if(ois != null){
					ois.close();
				}
			} catch (Exception e2) {
				System.err.println(e2);
			}
		}
		return cloneObject;
	}

	@Override
	public String toString() {
		return "UserLoginMessage [uid=" + uid + ", userName=" + userName + "]";
	}
}
