package com.ala.search.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class UserBuyPojo {

	private long uid;
	
	private BigDecimal money;
	
	private long voucherId;
	
	private String productType;
	
	private Date buyTime;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public long getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(long voucherId) {
		this.voucherId = voucherId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	
	
	
}
