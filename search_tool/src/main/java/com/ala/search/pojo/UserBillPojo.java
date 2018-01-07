package com.ala.search.pojo;

import java.math.BigDecimal;

import com.ala.annotation.NeedEncrypt;
import com.ala.tool.EncryptServiceImpl;

public class UserBillPojo {

	private long UserId;
	
	private String Name;
	
	@NeedEncrypt
	private String Cellphone;
	
	private String sex;
	
	private BigDecimal rate;
	
	private BigDecimal accInvest;
	
	private BigDecimal onionProfit;
	
	private BigDecimal interestProfit;

	public long getUserId() {
		return UserId;
	}

	public void setUserId(long userId) {
		UserId = userId;
	}

	public String getName() {
		EncryptServiceImpl service = new EncryptServiceImpl();
		String name = service.aes128Decrypt(Name);
		if(name.length() < 4){
			name = name.substring(0,1);
		}
		return name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCellphone() {
		return Cellphone;
	}

	public void setCellphone(String cellphone) {
		Cellphone = cellphone;
	}

	public String getSex() {
		// WOMEN(0), MAN(1), UNDEFINE(-1);
		switch (sex) {
		case "0":
			sex =  "女";
			break;
		case "1":
			sex =  "男";
			break;
		default:
			sex =  "未知";
			break;
		}
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getAccInvest() {
		return accInvest;
	}

	public void setAccInvest(BigDecimal accInvest) {
		this.accInvest = accInvest;
	}

	public BigDecimal getOnionProfit() {
		return onionProfit;
	}

	public void setOnionProfit(BigDecimal onionProfit) {
		this.onionProfit = onionProfit;
	}

	public BigDecimal getInterestProfit() {
		return interestProfit;
	}

	public void setInterestProfit(BigDecimal interestProfit) {
		this.interestProfit = interestProfit;
	}
	
	
	
}
