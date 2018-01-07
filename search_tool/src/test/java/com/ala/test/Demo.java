package com.ala.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

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
	
	@Test
	public void test02() {
		List<String> list = new ArrayList<>(2);
		list.add("01");
		list.add("02");
		list.add("03");
		String s = null;
		Optional<String> ofNullable = Optional.ofNullable(s);
		System.err.println(list);
	}
}
