package cn.springmvc.test;

import java.util.concurrent.CountDownLatch;

public class ThreadCountDownTest {
	
	public static void main(String[] args) {
		CountDownLatch cdl = new CountDownLatch(10);
		ThreadCountDownTest tcdt = new ThreadCountDownTest();
		for(int i=0;i<10;i++) {
			MyThread myThread = tcdt.new MyThread(cdl,"线程"+i);
			myThread.start();
			cdl.countDown();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("==================");
	}
	
	class MyThread extends Thread{
		
		private CountDownLatch cdl;
		private String name;

		public MyThread(CountDownLatch cdl,String name){
			this.cdl = cdl;
			this.name = name;
		}
		
		@Override
		public void run() {
			try {
				
				System.out.println("线程["+name+"]准备就绪");
				cdl.await();
				System.out.println("线程["+name+"]开始执行");
				for(int i=0;i<5;i++) {
					Thread.sleep(2000);
					System.out.println("线程["+name+"]第["+i+"]次正在执行!");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}

}
