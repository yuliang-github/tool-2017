package com.yl.activemq.message;

import java.io.Serializable;

public class SimpleMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String uid;
	
	private String name;
	
	private String sex;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "SimpleMessage [uid=" + uid + ", name=" + name + ", sex=" + sex + "]";
	}
	
}
