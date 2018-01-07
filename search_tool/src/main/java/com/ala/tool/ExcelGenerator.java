package com.ala.tool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ala.annotation.NeedEncrypt;
import com.ala.utils.JDBCUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ExcelGenerator<T> {

	public static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static String XLS = ".xls";
	private static String XLSX = ".xlsx";
	
	/**
	 * 支持.XLS .XLSX两种格式的文件
	 * @param title 工作表名
	 * @param headers 标题
	 * @param pojo 封装数据的POJO类型 需要解密的属性记得加上@NeedEncrypt的注解
	 * @param desPath 目标路径
	 * @param sql 查询语句
	 * @param params 查询参数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void generate(String title, String[] headers,Class pojo, String desPath,String sql,Object...params) {
		OutputStream out = null;
		try {
			//查询数据库
			QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
			List<Object> dataSet = null;
			if(params == null || params.length == 0){
				dataSet = qr.query(sql,new BeanListHandler<>(pojo));
			}else{
				dataSet = qr.query(sql,new BeanListHandler<>(pojo),params);
			}
			if(dataSet == null || dataSet.size() == 0){
				System.err.println("亲,查询结果为空呢！这可没办法给你生成数据表呢");
				return;
			}
			//创建工作簿
			Workbook wb = null;
			// 创建一个WorkBook 对应一个Excel
			if (desPath.endsWith(XLS)) {
				wb = new HSSFWorkbook();
			} else if (desPath.endsWith(XLSX)) {
				wb = new XSSFWorkbook();
			} else {
				String ex = desPath.substring(desPath.lastIndexOf("."));
				throw new RuntimeException("不支持输出到：" + ex + "格式的文件");
			}
			// 创建一个工作表
			Sheet sheet = wb.createSheet(title);
			// 创建样式
			CellStyle style = wb.createCellStyle();
			// 设置样式
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			// 生成一个字体
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 12);
			// 把字体应用到当前的样式
			style.setFont(font);
			// 生成并设置另一个样式
			CellStyle style2 = wb.createCellStyle();
			style2.setAlignment(CellStyle.ALIGN_CENTER);
			style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			// 生成另一个字体
			Font font2 = wb.createFont();
			// 把字体应用到当前的样式
			style2.setFont(font2);
			// 创建标题
			// 创建表头
			Row head = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = head.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			// 创建内容
			Iterator<Object> it = dataSet.iterator();
			for (int i = 1; i <= dataSet.size(); i++) {
				// 创建行
				Row row = sheet.createRow(i);
				// 反射获取属性
				Object t = it.next();
				Class<? extends Object> clazz = t.getClass();
				Field[] fields = clazz.getDeclaredFields();
				int j = 0;
				for (Field field : fields) {
					// 创建表格
					Cell cell = row.createCell(j);
					// 设置样式
					cell.setCellStyle(style2);
					j++;
					String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase()
							+ field.getName().substring(1);
					// 获取get方法
					Method getMethod = clazz.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					String cellValue = "";
					if (value != null) {
						// 获取字段注解
						NeedEncrypt needEncrypt = field.getDeclaredAnnotation(NeedEncrypt.class);
						cellValue = String.valueOf(value);
						if (value instanceof Date) {
							cellValue = sdf.format(value);
						}
						if(value instanceof BigDecimal){
							DecimalFormat df = new DecimalFormat("#,##0.00");
							df.setRoundingMode(RoundingMode.HALF_UP);
							cellValue = df.format(value);
						}
						if (needEncrypt != null) {
							if (needEncrypt.need()) {
								// 需要解密 获取解密类型
								Class type = needEncrypt.type();
								Object o = type.newInstance();
								if (o instanceof EncryptServiceImpl) {
									EncryptServiceImpl service = (EncryptServiceImpl) o;
									cellValue = service.aes128Decrypt(String.valueOf(value));
								} else {
									throw new RuntimeException("暂不支持：" + type + "类型的解密方法");
								}
							}
						}
					}
					cell.setCellValue(cellValue);
				}
			}
			// 输出
			out = new FileOutputStream(desPath);
			wb.write(out);
			System.out.println("亲！Excel导出成功，快去看看吧！记得给5星好评哟！");
		} catch (Exception e) {
			System.out.println("亲！很遗憾，Excel导出失败！不要给差评哟！");
			System.out.println(e);
		} finally {
			try {
				// 关流
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 传入JSON文件 生成Excel表格 支持.XLS .XLSX格式的文件
	 * 
	 * @param title 工作簿名
	 * @param headers 表头 需与字段对应
	 * @param jsonPath JSON文件路径 
	 * @param desPath 输出文件路径
	 * @param needEncrypt 需要解密的JSON字段索引位置 从0开始
	 */
	@SuppressWarnings("rawtypes")
	public static void generateFromJson(String title, String[] headers, String jsonPath,int[] needEncrypt,String desPath) {
		OutputStream out = null;
		try {
			// 创建一个WorkBook 对应一个Excel
			Workbook wb = null;
			if (desPath.endsWith(XLS)) {
				wb = new HSSFWorkbook();
			} else if (desPath.endsWith(XLSX)) {
				wb = new XSSFWorkbook();
			} else {
				String ex = desPath.substring(desPath.lastIndexOf("."));
				throw new RuntimeException("不支持输出到：" + ex + "格式的文件");
			}
			// 创建一个工作簿
			Sheet sheet = wb.createSheet(title);
			// 创建样式
			CellStyle style = wb.createCellStyle();
			// 设置样式
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			// 生成一个字体
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 12);
			// 把字体应用到当前的样式
			style.setFont(font);
			// 生成并设置另一个样式
			CellStyle style2 = wb.createCellStyle();
			style2.setAlignment(CellStyle.ALIGN_CENTER);
			style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			// 生成另一个字体
			Font font2 = wb.createFont();
			// 把字体应用到当前的样式
			style2.setFont(font2);
			// 创建标题
			// 创建表头
			Row head = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = head.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			// 解析JSON数据
			JSONArray dataArray = JSONArray.fromObject(JsonUtils.ReadFile(jsonPath));
			int index = 1;
			for (Object o : dataArray) {
				// 创建行
				Row row = sheet.createRow(index);
				index++;
				JSONObject jsonObject = JSONObject.fromObject(o);
				Set keySet = jsonObject.keySet();
				Iterator it = keySet.iterator();
				int i = 0;
				boolean flag = false;
				while (it.hasNext()) {
					for (int needEnc : needEncrypt) {
						if(needEnc == i){
							flag = true;
						}
					}
					Object key = it.next();
					Object value = jsonObject.get(key);
					String valueStr = String.valueOf(value);
					if(flag){
						//解密
						EncryptServiceImpl service = new EncryptServiceImpl();
						valueStr = service.aes128Decrypt(valueStr);
						flag = false;
					}
					// 创建表格
					Cell cell = row.createCell(i);
					i++;
					if (value instanceof Date) {
						valueStr = sdf.format(value);
					}
					if(value instanceof BigDecimal){
						DecimalFormat df = new DecimalFormat("#,##0.00");
						df.setRoundingMode(RoundingMode.HALF_UP);
						valueStr = df.format(value);
					}
					cell.setCellValue(valueStr);
					cell.setCellStyle(style2);
				}
			}
			// 输出
			out = new FileOutputStream(desPath);
			wb.write(out);
			System.out.println("亲！Excel导出成功，快去看看吧！记得给5星好评哟！");
		} catch (Exception e) {
			System.out.println("亲！很遗憾，Excel导出失败！不要给差评哟！");
			System.out.println(e.getMessage());
		} finally {
			try {
				// 关流
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public  void generateByList(String title, String[] headers,Collection<T> dataSet, String desPath) {
		OutputStream out = null;
		try {
			//创建工作簿
			Workbook wb = null;
			// 创建一个WorkBook 对应一个Excel
			if (desPath.endsWith(XLS)) {
				wb = new HSSFWorkbook();
			} else if (desPath.endsWith(XLSX)) {
				wb = new XSSFWorkbook();
			} else {
				String ex = desPath.substring(desPath.lastIndexOf("."));
				throw new RuntimeException("不支持输出到：" + ex + "格式的文件");
			}
			// 创建一个工作表
			Sheet sheet = wb.createSheet(title);
			// 创建样式
			CellStyle style = wb.createCellStyle();
			// 设置样式
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			// 生成一个字体
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 12);
			// 把字体应用到当前的样式
			style.setFont(font);
			// 生成并设置另一个样式
			CellStyle style2 = wb.createCellStyle();
			style2.setAlignment(CellStyle.ALIGN_CENTER);
			style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			// 生成另一个字体
			Font font2 = wb.createFont();
			// 把字体应用到当前的样式
			style2.setFont(font2);
			// 创建标题
			// 创建表头
			Row head = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = head.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			// 创建内容
			Iterator<T> it = dataSet.iterator();
			for (int i = 1; i <= dataSet.size(); i++) {
				// 创建行
				Row row = sheet.createRow(i);
				// 反射获取属性
				T t = it.next();
				Class<? extends Object> clazz = t.getClass();
				Field[] fields = clazz.getDeclaredFields();
				int j = 0;
				for (Field field : fields) {
					// 创建表格
					Cell cell = row.createCell(j);
					// 设置样式
					cell.setCellStyle(style2);
					j++;
					String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase()
							+ field.getName().substring(1);
					// 获取get方法
					Method getMethod = clazz.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					String cellValue = "";
					if (value != null) {
						// 获取字段注解
						NeedEncrypt needEncrypt = field.getDeclaredAnnotation(NeedEncrypt.class);
						cellValue = String.valueOf(value);
						if (value instanceof Date) {
							cellValue = sdf.format(value);
						}
						if(value instanceof BigDecimal){
							DecimalFormat df = new DecimalFormat("#,##0.00");
							df.setRoundingMode(RoundingMode.HALF_UP);
							cellValue = df.format(value);
						}
						if (needEncrypt != null) {
							if (needEncrypt.need()) {
								// 需要解密 获取解密类型
								Class type = needEncrypt.type();
								Object o = type.newInstance();
								if (o instanceof EncryptServiceImpl) {
									EncryptServiceImpl service = (EncryptServiceImpl) o;
									cellValue = service.aes128Decrypt(String.valueOf(value));
								} else {
									throw new RuntimeException("暂不支持：" + type + "类型的解密方法");
								}
							}
						}
					}
					cell.setCellValue(cellValue);
				}
			}
			// 输出
			out = new FileOutputStream(desPath);
			wb.write(out);
			System.out.println("亲！Excel导出成功，快去看看吧！记得给5星好评哟！");
		} catch (Exception e) {
			System.out.println("亲！很遗憾，Excel导出失败！不要给差评哟！");
			System.out.println(e);
		} finally {
			try {
				// 关流
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
