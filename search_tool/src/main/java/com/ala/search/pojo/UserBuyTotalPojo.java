package com.ala.search.pojo;

import java.math.BigDecimal;

import com.ala.annotation.NeedEncrypt;

public class UserBuyTotalPojo {

	private long uid;
	
	private BigDecimal money;
	
	private BigDecimal idleMoney;
	
	@NeedEncrypt
	private String cellphone;

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

	public BigDecimal getIdleMoney() {
		return idleMoney;
	}

	public void setIdleMoney(BigDecimal idleMoney) {
		this.idleMoney = idleMoney;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	
	
	
}
