package cn.springmvc.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoClientOptionsFactoryBean;

import cn.financial.dao.MongoDBDao;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongodbTest {
	
	private ThreadPoolManager executor= ThreadPoolManager.newInstance();
	
	public static void main(String[] args) {
		MongodbTest mt = new MongodbTest();
		mt.test();
//		mt.testThread();
	}
	
	public void test()  {
		Calendar start = Calendar.getInstance();
		System.out.println("start:"+start.getTimeInMillis());
//		MongoClientOptionsFactoryBean mcof = new MongoClientOptionsFactoryBean();
//		mcof.setConnectTimeout(30000);
//		MongoClientOptions object = mcof.getObject();
		MongoClient mongoClient = new MongoClient("localhost");
//		MongoClient mongoClient = new MongoClient("192.168.1.3");

		MongoDatabase db = mongoClient.getDatabase("test");
//		MongoCollection<Document> doc = db.getCollection("dataTest");
		MongoCollection<Document> doc = db.getCollection("netWork");

		final List<JSONObject> list = new ArrayList<JSONObject>();
		FindIterable<Document> iter = doc.find();
//		iter = iter.skip(9500);
		Calendar end = Calendar.getInstance();
		System.out.println("end:"+end.getTimeInMillis());
		System.out.println("findTime:"+(end.getTimeInMillis()-start.getTimeInMillis()));
		iter.forEach(new Block<Document>() {
			public void apply(Document _doc) {
				String string = null;
				Document value = (Document)_doc.get("value");
				string = value.toJson();
				list.add(JSONObject.parseObject(string));
			}
		});
		
//		iter.into(list);
//		iter.forEach(new Consumer<Document>() {
//			@Override
//			public void accept(Document _doc) {
//				list.add(_doc.get("value").toString());
////				System.out.println(_doc);
//			}
//		});
		end = Calendar.getInstance();
		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
//		JSONArray.fromObject(list);
		end = Calendar.getInstance();
		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
		System.out.println(list.size());
		end = Calendar.getInstance();
		System.out.println("end:"+end.getTimeInMillis());
		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
		
	}
	
	public void testThread()  {
		Calendar start = Calendar.getInstance();
		System.out.println("start:"+start.getTimeInMillis());
//		MongoClientOptionsFactoryBean mcof = new MongoClientOptionsFactoryBean();
//		mcof.setConnectTimeout(30000);
//		MongoClientOptions object = mcof.getObject();
		MongoClient mongoClient = new MongoClient("localhost");
//		MongoClient mongoClient = new MongoClient("192.168.1.3");
		
		MongoDatabase db = mongoClient.getDatabase("test");
//		MongoCollection<Document> doc = db.getCollection("dataTest");
		MongoCollection<Document> doc = db.getCollection("netWork");
		
		FindIterable<Document> iter = doc.find();
//		iter = iter.skip(9500);
		Calendar end = Calendar.getInstance();
		System.out.println("end:"+end.getTimeInMillis());
		System.out.println("findTime:"+(end.getTimeInMillis()-start.getTimeInMillis()));
		
		final List<JSONObject> list = execThread(iter);
//		JSONArray.fromObject(list);
		System.out.println(list.size());
		end = Calendar.getInstance();
		System.out.println("end:"+end.getTimeInMillis());
		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
		
	}
	
	public List<JSONObject> execThread(FindIterable<Document> iter){
		List<MyThread> threadlst = new ArrayList<MyThread>();
		int pageSize = 5000;
		int count = 2;
		CountDownLatch cdl = new CountDownLatch(count);
		for(int i=0;i<count;i++){
			int start = i*pageSize;
			MyThread t = new MyThread(cdl,iter, start, pageSize);
			executor.addExecuteTask(t);
			threadlst.add(t);
		}
		System.out.println("线程正在执行");
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("线程正在结束");
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(MyThread t:threadlst){
			list.addAll(t.getList());
		}
		return list;
	}
	
	class MyThread extends Thread{
		
		private int start;
		private FindIterable<Document> iter;
		private int end;
		private CountDownLatch cdl;

		MyThread(CountDownLatch cdl,FindIterable<Document> iter,int start,int end){
			this.cdl = cdl;
			this.iter = iter;
			this.start = start;
			this.end = end;
			
		}
		
		private List<JSONObject> list = new ArrayList<JSONObject>();
		
		@Override
		public void run() {
			try {
				Thread.sleep((long)2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			iter.skip(start);
			iter.limit(end);
			iter.forEach(new Block<Document>() {
				public void apply(Document _doc) {
					String string = null;
					Document value = (Document)_doc.get("value");
					string = value.toJson();
					list.add(JSONObject.parseObject(string));
				}
			});
			cdl.countDown();
			
		}

		public List<JSONObject> getList() {
			return list;
		}

		public void setList(List<JSONObject> list) {
			this.list = list;
		}
	}

}
