package com.ala.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.ala.search.pojo.UserBillPojo;
import com.ala.search.pojo.UserBuyPojo;
import com.ala.search.pojo.UserBuyTotalPojo;
import com.ala.search.pojo.UserInvestPojo;
import com.ala.search.pojo.UserPojo;
import com.ala.search.pojo.UserRankPojo;
import com.ala.tool.ExcelGenerator;
import com.ala.tool.ExcelReader;
import com.ala.utils.JDBCUtils;

public class SearchTest {
	
	@Test
	public void test09() throws SQLException,FileNotFoundException{
		String sql = "select Id,Name,Cellphone,Sex,Ct createtime,UserType from T_USER_BASIC where to_days(Ct) = to_days(?)";
		String[] headers = {"用户ID","姓名","手机号","性别","注册时间","用户类别"};
		ExcelGenerator.generate("注册用户",headers,UserPojo.class,"/Users/alex/Public/今日注册用户.xlsx",sql,"2017-12-20");
	}
	
	@Test
	public void test10() throws SQLException,FileNotFoundException{
		String[] headers = {"用户ID","姓名","手机号","性别","注册时间","用户类别"};
		int needEncrypt[] = {1,2};
		ExcelGenerator.generateFromJson("注册用户", headers,"/Users/alex/Public/contact.json",needEncrypt,"/Users/alex/Public/今日注册用户.xls");
	}
	
	@Test
	public void test11(){
		//读取EXCEL表格
		try{
			String filePath = "/Users/alex/Public/1218.xlsx";
			InputStream is = new FileInputStream(new File(filePath));
			Workbook wb = null;
			Set<Long> uidSet = new HashSet<>();
			if(filePath.endsWith(".xls")){
				wb = new HSSFWorkbook(is);
			}else if(filePath.endsWith(".xlsx")){
				wb = new XSSFWorkbook(is);
			}
			//读取工作表
			Sheet sheet = wb.getSheetAt(0);
			//遍历每一行 没有标题  正文从第1行开始
			for(int i=0;i<sheet.getLastRowNum();i++){
				Row row = sheet.getRow(i);
				//获取第一个表格内容
				long uid = Long.parseLong(row.getCell(0).getStringCellValue());
				uidSet.add(uid);
			}
			is.close();
			QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
			List<UserBuyPojo> dataList = new ArrayList<>();
			List<UserBuyTotalPojo> total = new ArrayList<>();
			//根据uid查询数据库
			String sql = "select * from (select buy.Uid uid,InitBuy money,ifnull(VoucherId,0) voucherId,ProductType,BuyTime buyTime "
					+ "from T_PRODUCT_BUY buy where to_days(buy.BuyTime) >= to_days('2017-11-01') "
					+ "and to_days(buy.BuyTime) <= to_days('2017-12-18')) t "
					+ "where money != 0 and Uid = ?";
			String sql21 = "select sum(InitBuy) money from T_PRODUCT_BUY where to_days(BuyTime) >= to_days('2017-11-01') and to_days(BuyTime) <= to_days('2017-12-18')and Uid = ?;";
			String sql22 = "select IdleMoney from T_SYT_ACCOUNT_CHARGE where Uid = ?;";
			String sql23 = "select Cellphone from T_USER_BASIC where Id = ?;";
			
			Iterator<Long> it = uidSet.iterator();
			while(it.hasNext()) {
				Long uid = it.next();
				List<UserBuyPojo> list = qr.query(sql,new BeanListHandler<>(UserBuyPojo.class),uid);
				//UserBuyTotalPojo totalPojo = qr.query(sql2,new BeanHandler<>(UserBuyTotalPojo.class),uid);
				
				UserBuyTotalPojo totalPo = new UserBuyTotalPojo();
				List<BigDecimal> moneys = qr.query(sql21,new ColumnListHandler<BigDecimal>(),uid);
				BigDecimal money = BigDecimal.ZERO;
				if(moneys != null && moneys.get(0) != null){
					money = moneys.get(0).setScale(2,RoundingMode.UP);
				}
				totalPo.setUid(uid);
				totalPo.setMoney(money);
				List<BigDecimal> idleMOneys = qr.query(sql22,new ColumnListHandler<BigDecimal>(),uid);
				BigDecimal idleMoney = BigDecimal.ZERO;
				if(idleMOneys != null && idleMOneys.get(0) != null){
					idleMoney = idleMOneys.get(0).setScale(2,RoundingMode.UP);
				}
				totalPo.setIdleMoney(idleMoney);
				
				List<String> cellPhones = qr.query(sql23,new ColumnListHandler<String>(),uid);
				String cellPhone = "";
				if(cellPhones != null && cellPhones.size() != 0){
					cellPhone = cellPhones.get(0);
				}
				totalPo.setCellphone(cellPhone);
				
				total.add(totalPo);
				
				if(list != null && list.size() != 0){
					for (UserBuyPojo po : list) {
						if(po.getVoucherId() != 0l){
							po.setVoucherId(1l);
						}
						switch (po.getProductType()) {
						case "freshman":
							po.setProductType("新手标");
							break;
						case "halfyear":
							po.setProductType("6月期");
							break;
						case "huoqi":
							po.setProductType("洋葱钱包");
							break;
						case "month":
							po.setProductType("1月期");
							break;
						case "quarter":
							po.setProductType("3月期");
							break;
						case "year":
							po.setProductType("12月期");
							break;
						default:
							po.setProductType("散标");
							break;
						}
					}
					dataList.addAll(list);
				}
			}
			//生成excel
			String[] headers = {"用户ID","投资金额","是否使用满减券","投资产品","投资时间"};
			String desPath = "/Users/alex/Public/用户投资记录表.xlsx";
			ExcelGenerator<UserBuyPojo> excel = new ExcelGenerator<>();
			excel.generateByList("用户投资记录",headers,dataList,desPath);
			
			String[] headers2 = {"用户ID","累计投资金额","闲置资金","电话号码"};
			String desPath2 = "/Users/alex/Public/用户投资记录汇总表.xlsx";
			ExcelGenerator<UserBuyTotalPojo> excel2 = new ExcelGenerator<>();
			excel2.generateByList("用户投资记录汇总",headers2,total,desPath2);
			
		}catch (Exception e) {
			System.err.println(e);
		}
	}
	
	@Test
	public void test12(){
		List<Map<Integer, String>> ret = ExcelReader.readExcel("/Users/alex/Public/今日注册用户.xlsx",0,1,1,5,6);
		for (Map<Integer, String> map : ret) {
			Set<Entry<Integer,String>> set = map.entrySet();
			Iterator<Entry<Integer, String>> it = set.iterator();
			while(it.hasNext()){
				Entry<Integer, String> next = it.next();
				System.out.print((next.getKey()+"=="+next.getValue()));
				System.out.println("\t");
			}
			System.out.print("\r\n");
		}
	}
	
	@Test
	public void test13(){
		List<Map<Integer, String>> ret = ExcelReader.readExcel("/Users/alex/Public/今日注册用户.xlsx",0,1,new int[]{1,5,6,9});
		for (Map<Integer, String> map : ret) {
			System.out.println(5+"=="+map.get(5));
		}
	}
	
	@Test
	public void test14() throws SQLException,FileNotFoundException{
		String sql = "select t1.*,t2.Name name,t2.Cellphone cellphone from "
				+ "(select t1.Uid uid,t1.money investMoney,ifnull(t2.money,0) redeemMoney,(t1.money-ifnull(t2.money,0)) newlyMoney from "
				+ "(select Uid,sum(InitBuy) money from T_PRODUCT_BUY "
				+ "where to_days(BuyTime) >= to_days('2017-12-11') and DirectInvest = 1 group by Uid) t1 "
				+ "left join (select Uid,sum(AlreadyMoney) money from T_SYT_REDEEM "
				+ "where to_days(Ct) >= to_days('2017-12-11') group by Uid) t2 on t1.Uid = t2.Uid "
				+ "where (t1.money-ifnull(t2.money,0)) > ?) t1,T_USER_BASIC t2 where t1.uid = t2.Id;";
		String[] headers = {"用户ID","投资金额","赎回金额","新增投资额","姓名","手机号"};
		ExcelGenerator.generate("新增投资大于0用户",headers,UserInvestPojo.class,"/Users/alex/Public/新增投资大于0用户.xlsx",sql,0);
	}
	
	@Test
	public void test15() throws SQLException,FileNotFoundException{
		String sql = "select UserId,CellPhone,Money,Rank,LastInvestTime from T_ACTIVITY_INVEST_RANK where to_days(Ct) = to_days(?);";
		String[] headers = {"用户ID","手机号码","新增投资额","排名","最后一笔投资时间"};
		ExcelGenerator.generate("最终排名榜",headers,UserRankPojo.class,"/Users/alex/Public/最终排名榜.xlsx",sql,new Date());
	}
	
	@Test
	public void test16() throws SQLException,FileNotFoundException{
		String sql = "select t.UserId,b.Name,b.Cellphone,b.Sex,t.rate rate,t.accInvest accInvest,"
				+ "t.onionProfit onionProfit,t.interestProfit interestProfit from "
				+ "(select UserId,truncate(AccInvest,2) accInvest,truncate(AccOnionProfit,2) onionProfit,"
				+ "truncate(AccInterestProfit,2) interestProfit,truncate(DailyInvest,2) dailyInvest,"
				+ "truncate(if(DailyInvest = 0,if((AccOnionProfit+AccInterestProfit) = 0,0.00,100),"
				+ "(AccOnionProfit+AccInterestProfit)/DailyInvest*100),2) rate,ProfitDays profitDays "
				+ "from T_USER_ANNUAL_BILL where AccInvest >= 10000) t,T_USER_BASIC b "
				+ "where t.UserId = b.Id and t.rate >= ?";
		String[] headers = {"用户ID","姓名","手机号码","性别","年化利率","累计投资","洋葱收益","其它收益"};
		ExcelGenerator.generate("最终排名榜",headers,UserBillPojo.class,"/Users/alex/Public/1-02年度账单取数.xlsx",sql,10);
	}
	
}
