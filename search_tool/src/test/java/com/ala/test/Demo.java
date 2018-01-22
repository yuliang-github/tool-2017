package com.ala.test;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.ala.search.pojo.UserRankPojo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class Demo {
	
	@Test
	public void test01() throws IOException, InterruptedException{
		CountDownLatch latch = new CountDownLatch(2);
		ExecutorService es = new ThreadPoolExecutor(2,2,60L,
				TimeUnit.SECONDS,new ArrayBlockingQueue<>(2),new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r,ThreadPoolExecutor executor) {
				Thread t = new Thread(r);
				System.out.println(t.getName()+"线程被拒绝");
			}
		});
		Runnable r1= new TimerTask() {
			@Override
			public void run() {
				System.out.println("线程:"+1+"开始休眠");
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("线程:"+1+"休眠结束");
				latch.countDown();
			}
		};
		es.execute(r1);
		Runnable r2= new TimerTask() {
			@Override
			public void run() {
				System.out.println("线程:"+2+"开始休眠");
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("线程:"+2+"休眠结束");
				latch.countDown();
			}
		};
		es.execute(r2);
		es.shutdown();
		latch.await();
		System.out.println("程序运行结束");
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void test02() {
		UserRankPojo pojo = new UserRankPojo();
		pojo.setCellphone("123");
		pojo.setLastinvesttime(new Date());
		//pojo.setMoney(BigDecimal.ZERO);
		pojo.setRank(1);
		//pojo.setUserid(567890L);
		String string = JSON.toJSONString(pojo);
		System.out.println(string);
		
		String str = "{lastinvesttime:1515492895029,money:0,rank:1,userid:567890}";
		JSONObject jsonObject = JSON.parseObject(str);
		UserRankPojo javaObject = JSON.toJavaObject(jsonObject,UserRankPojo.class);
		System.err.println(javaObject);
		
		Map map = JSON.parseObject(str,new TypeReference<Map>(){});
		System.out.println(map);
		
		UserRankPojo mapa = JSON.parseObject(str,new TypeReference<UserRankPojo>(){});
		System.out.println(mapa);
		
	}
}
