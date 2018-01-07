package com.ala.search.pojo;

import java.util.Date;

import com.ala.annotation.NeedEncrypt;

public class UserPojo{
	
	private Long id;
	
	@NeedEncrypt
    private String name;
	@NeedEncrypt
    private String cellphone;

    private Integer sex;

    private Date createtime;

    private String usertype;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
    
	public static enum UserType{
		Invest("INVEST"), PersonLoan("PERSON_LOAN"), EnterpriseLoan("ENTERPRISE_LOAN");
		private String value;
		UserType(String str){
			this.value = str;
		}
		
		public String get(){
			return value;
		}
	}

	public static enum Sex {
		WOMEN(0), MAN(1), UNDEFINE(-1);
		private int value;

		Sex(int value) {
			this.value = value;
		}

		public int get() {
			return value;
		}
	}
}
