package com.ala.utils;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtils {
	
	    private static DataSource dataSource = null;
	    static {
	    	try {
	    		//注册驱动
				//连接池
				dataSource = new ComboPooledDataSource();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		public static DataSource getDataSource() {
			return dataSource;
		}
		
		public static DataSource getDataSource(String dataSourceName){
			if(StringUtils.isBlank(dataSourceName)){
				return dataSource;
			}
			return new ComboPooledDataSource(dataSourceName);
		}
		
}
