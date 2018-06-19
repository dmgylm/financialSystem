package cn.springmvc.test;

public class ThreadTest {
	
	public static void main(String[] args) throws Exception {
		ThreadTest tt = new ThreadTest();
		
		MyThread t = tt.new MyThread("t1");
		Thread t1 = new Thread(t);
		Thread t2 = new Thread(t);
		Thread t3 = new Thread(t);
		Thread t4 = new Thread(t);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		System.out.println("线程开始执行");
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		
		System.out.println("线程执行结束");
		System.out.println(t.getTicket());
	}
	
	class MyThread implements Runnable{
		
		private String threadName;
		
		MyThread(String threadName){
			this.threadName = threadName;
			
		}
		
		private Integer ticket =10;
		
		
		@Override
		public void run() {
			try {
				Thread.sleep(2);
				synchronized (ticket) {
					ticket --;
				}
				System.out.println(ticket);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		public Integer getTicket() {
			return ticket;
		}


		public void setTicket(Integer ticket) {
			this.ticket = ticket;
		}
	}

}
