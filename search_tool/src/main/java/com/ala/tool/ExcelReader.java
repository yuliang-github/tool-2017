package com.ala.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private static final String XLS = ".xls";

	private static final String XLSX = ".xlsx";

	/**
	 * 
	 * @param path
	 *            文件路径
	 * @param rowBegin
	 *            开始行号 从0开始
	 * @param cellNos
	 *            需要获取的列号 从0开始
	 * @param sheetNo
	 *            工作表号 从0开始
	 * @return 返回Map的key为对应列号
	 */
	public static List<Map<Integer, String>> readExcel(String path, int sheetNo, int rowBegin, int... cellNos) {
		// 判断文件格式
		InputStream is = null;
		List<Map<Integer, String>> ret = new ArrayList<>();
		try {
			if (StringUtils.isBlank(path)) {
				throw new RuntimeException("文件名不能为空");
			} else {
				// 创建流
				is = new FileInputStream(path);
				// 创建工作簿
				Workbook wb = null;
				if (path.endsWith(XLS)) {
					wb = new HSSFWorkbook(is);
				} else if (path.endsWith(XLSX)) {
					wb = new XSSFWorkbook(is);
				} else {
					throw new RuntimeException("不支持的文件格式:" + path.substring(path.lastIndexOf(".")));
				}
				// 获取工作表
				Sheet sheet = wb.getSheetAt(sheetNo);
				if (sheet == null) {
					throw new RuntimeException("工作表不存在");
				}
				// 获取行数
				int lastRowNum = sheet.getLastRowNum();
				if (rowBegin > lastRowNum || rowBegin < 0) {
					throw new RuntimeException("行号超出范围");
				} else {
					for (int i = rowBegin; i <= lastRowNum; i++) {
						Row row = sheet.getRow(i);
						short lastCell = row.getLastCellNum();
						short firstCell = row.getFirstCellNum();
						if (cellNos.length == 0) {
							throw new RuntimeException("请选择要获取的列号");
						} else {
							// 每行数据用一个MAP封装
							Map<Integer, String> map = new HashMap<>();
							for (int no : cellNos) {
								if (no < firstCell || no > lastCell) {
									System.err.println("第" + i + "行不存在第" + no + "列");
								} else {
									Cell cell = row.getCell(no);
									String cellValue = "";
									if (cell != null) {
										// 获取列类型
										int cellType = cell.getCellType();
										switch (cellType) {
										case Cell.CELL_TYPE_BLANK:
											break;
										case Cell.CELL_TYPE_BOOLEAN:
											cellValue = String.valueOf(cell.getBooleanCellValue());
											break;
										case Cell.CELL_TYPE_FORMULA:
											cellValue = String.valueOf(cell.getCellFormula());
											break;
										case Cell.CELL_TYPE_NUMERIC:
											cellValue = String.valueOf(cell.getNumericCellValue());
											break;
										case Cell.CELL_TYPE_STRING:
											cellValue = String.valueOf(cell.getStringCellValue());
											break;
										default:
											System.err.println("第" + i + "行第" + no + "列" + "数据错误");
											break;
										}
									}
									map.put(no,cellValue);
								}
							}
							ret.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

}
