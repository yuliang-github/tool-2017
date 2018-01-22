package com.yl.response;

public class JmsResponse {

	public static final Integer SUCCESS_CODE = 0;
	public static final Integer SUB_SUCCESS_CODE = 000;
	
	private Integer code = SUCCESS_CODE;
	
	private Integer subCode = SUB_SUCCESS_CODE;
	
	private String message;

	
	public boolean isSuccess(){
		return SUCCESS_CODE.intValue() == 
				(this.code == null ? 0:this.code.intValue());
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getSubCode() {
		return subCode;
	}

	public void setSubCode(Integer subCode) {
		this.subCode = subCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "JmsResponse [code=" + code + ", subCode=" + subCode + ", message=" + message + "]";
	}
	
	
}
