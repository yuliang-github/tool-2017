package com.ala.tool;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.ala.utils.JDBCUtils;

import net.sf.json.JSONArray;

public class SearchTool<T> {
	
	public List<T> search(String sql,BeanListHandler<T> handler,Object...params) throws SQLException{
		//创建运行对象
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		//执行
		List<T> query;
		if(params == null || params.length == 0){
			query = qr.query(sql, handler);
		}else{
			query = qr.query(sql, handler, params);
		}
		return query;
	}
	
	public JSONArray searchByJsonFile(String filePath){
		//解析JSON文件
		String jsonStr = JsonUtils.ReadFile(filePath);
		//解析
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		return jsonArray;
	}
	
}
