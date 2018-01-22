package com.yl.utils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
	
	private static final String ACTIVE_MQ_CONF="/prop/activemq.properties";
	
	private static Properties pro;
	
	static {
		pro = new Properties();
		try {
			pro.load(new BufferedInputStream(ClassLoader.class.getResourceAsStream(ACTIVE_MQ_CONF)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getStringValue(String key){
		String value = pro.getProperty(key);
		return value;
	}
	
}
