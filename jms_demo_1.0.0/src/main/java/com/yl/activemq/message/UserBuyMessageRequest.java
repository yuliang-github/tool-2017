package com.yl.activemq.message;

import java.math.BigDecimal;

public class UserBuyMessageRequest extends MessageRequest{

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	
	private String userName;
	
	private BigDecimal money;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
}
