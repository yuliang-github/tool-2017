package com.ala.search.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.ala.annotation.NeedEncrypt;
import com.ala.tool.EncryptServiceImpl;

public class UserRankPojo {

	private Long userid;
	
	@NeedEncrypt(type=EncryptServiceImpl.class,need=true)
	private String cellphone;

	private BigDecimal money;

	private Integer rank;

	private Date lastinvesttime;

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Date getLastinvesttime() {
		return lastinvesttime;
	}

	public void setLastinvesttime(Date lastinvesttime) {
		this.lastinvesttime = lastinvesttime;
	}

	@Override
	public String toString() {
		return "UserRankPojo [userid=" + userid + ", cellphone=" + cellphone + ", money=" + money + ", rank=" + rank
				+ ", lastinvesttime=" + lastinvesttime + "]";
	}

}
