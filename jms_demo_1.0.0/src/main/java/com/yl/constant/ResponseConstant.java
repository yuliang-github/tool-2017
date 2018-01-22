package com.yl.constant;

public class ResponseConstant {

	public static enum RET {
		
		BLANK_QUEUE_NAME(1,"队列名不能为空"),
		BLANK_MSG_ID(2,"消息ID不能为空"),
		SYS_ERROR(99,"系统异常"),
		LINK_FAILER(3,"连接消息服务器异常")
		;
		
		private Integer code;
		
		private String message;
		
		RET(Integer code,String message){
			this.code = code;
			this.message = message;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
}
