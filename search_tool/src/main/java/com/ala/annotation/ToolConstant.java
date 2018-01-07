package com.ala.annotation;

import com.ala.tool.EncryptServiceImpl;

public class ToolConstant {

	public static enum NEED {
		YES(true),NO(false);
		private boolean value;
		NEED(boolean b){
			this.value = b;
		}
		public boolean get() {
			return value;
		}
	};
	
	public static enum TYPE {
		ENCRYPTSERVICE(EncryptServiceImpl.class);
		private Class<?> type;
		TYPE(Class<?> type){
			this.type = type;
		}
		public Class<?> get() {
			return type;
		}
	};

	
}
