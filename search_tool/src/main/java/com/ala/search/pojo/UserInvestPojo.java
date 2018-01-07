package com.ala.search.pojo;

import java.math.BigDecimal;

import com.ala.annotation.NeedEncrypt;

public class UserInvestPojo {

	private long uid;
	
	private BigDecimal investMoney;
	
	private BigDecimal redeemMoney;
	
	private BigDecimal newlyMoney;
	
	@NeedEncrypt
	private String name;
	
	@NeedEncrypt
	private String cellphone;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public BigDecimal getInvestMoney() {
		return investMoney;
	}

	public void setInvestMoney(BigDecimal investMoney) {
		this.investMoney = investMoney;
	}

	public BigDecimal getRedeemMoney() {
		return redeemMoney;
	}

	public void setRedeemMoney(BigDecimal redeemMoney) {
		this.redeemMoney = redeemMoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public BigDecimal getNewlyMoney() {
		return newlyMoney;
	}

	public void setNewlyMoney(BigDecimal newlyMoney) {
		this.newlyMoney = newlyMoney;
	}
	
	
}
