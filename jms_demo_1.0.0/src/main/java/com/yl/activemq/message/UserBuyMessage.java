package com.yl.activemq.message;

import java.math.BigDecimal;

public class UserBuyMessage extends BaseMessage{

	private static final long serialVersionUID = 1L;

	public UserBuyMessage(String queueName) {
		super(queueName);
	}
	
	private Long buyId;
	
	private BigDecimal buyMoney;

	public Long getBuyId() {
		return buyId;
	}

	public void setBuyId(Long buyId) {
		this.buyId = buyId;
	}

	public BigDecimal getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(BigDecimal buyMoney) {
		this.buyMoney = buyMoney;
	}
	
}
