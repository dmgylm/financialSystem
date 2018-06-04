package cn.springmvc.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoClientOptionsFactoryBean;

import cn.financial.dao.MongoDBDao;

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
	
//	public static void main(String[] args) throws Exception {
//		Calendar start = Calendar.getInstance();
//		System.out.println("start:"+start.getTimeInMillis());
////		MongoClientOptionsFactoryBean mcof = new MongoClientOptionsFactoryBean();
////		mcof.setConnectTimeout(30000);
////		MongoClientOptions object = mcof.getObject();
//		MongoClient mongoClient = new MongoClient("localhost");
////		MongoClient mongoClient = new MongoClient("192.168.1.3");
//
//		MongoDatabase db = mongoClient.getDatabase("test");
////		MongoCollection<Document> doc = db.getCollection("dataTest");
//		MongoCollection<Document> doc = db.getCollection("netWork");
//
//		final List<JSONObject> list = new ArrayList<JSONObject>();
//		FindIterable<Document> iter = doc.find();
////		iter = iter.skip(9500);
//		Calendar end = Calendar.getInstance();
//		System.out.println("end:"+end.getTimeInMillis());
//		System.out.println("findTime:"+(end.getTimeInMillis()-start.getTimeInMillis()));
//		iter.forEach(new Block<Document>() {
//			public void apply(Document _doc) {
//				String string = null;
//				Document value = (Document)_doc.get("value");
//				string = value.toJson();
//				list.add(JSONObject.fromObject(string));
//			}
//		});
//		
////		iter.into(list);
////		iter.forEach(new Consumer<Document>() {
////			@Override
////			public void accept(Document _doc) {
////				list.add(_doc.get("value").toString());
//////				System.out.println(_doc);
////			}
////		});
//		end = Calendar.getInstance();
//		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
////		JSONArray.fromObject(list);
//		end = Calendar.getInstance();
//		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
//		System.out.println(list.size());
//		end = Calendar.getInstance();
//		System.out.println("end:"+end.getTimeInMillis());
//		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
//		
//	}
	public static void main(String[] args) throws Exception {
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
		
//		iter.into(list);
//		iter.forEach(new Consumer<Document>() {
//			@Override
//			public void accept(Document _doc) {
//				list.add(_doc.get("value").toString());
////				System.out.println(_doc);
//			}
//		});
		
		MongodbTest mt = new MongodbTest();
		final List<JSONObject> list = mt.ss(iter);
//		JSONArray.fromObject(list);
		System.out.println(list.size());
		end = Calendar.getInstance();
		System.out.println("end:"+end.getTimeInMillis());
		System.out.println((end.getTimeInMillis()-start.getTimeInMillis()));
		
	}
	
	public List<JSONObject> ss(FindIterable<Document> iter){
		List<MyThread> list = new ArrayList<MyThread>();
		int pageSize = 1000;
		int count = 10;
		for(int i=0;i<count;i++){
			int start = i*pageSize;
			MyThread t = new MyThread(iter, start, pageSize);
			t.start();
			list.add(t);
		}
		for(MyThread t:list){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
		List<JSONObject> array = new ArrayList<JSONObject>();
		for(MyThread t:list){
			array.addAll(t.getList());
		}
		return array;
	}
	
	class MyThread extends Thread{
		
		private int start;
		private FindIterable<Document> iter;
		private int end;

		MyThread(FindIterable<Document> iter,int start,int end){
			this.iter = iter;
			this.start = start;
			this.end = end;
			
		}
		
		private List<JSONObject> list = new ArrayList<JSONObject>();
		
		@Override
		public void run() {
			iter.skip(start);
			iter.limit(end);
			iter.forEach(new Block<Document>() {
				public void apply(Document _doc) {
					String string = null;
					Document value = (Document)_doc.get("value");
					string = value.toJson();
					list.add(JSONObject.fromObject(string));
				}
			});
		}

		public List<JSONObject> getList() {
			return list;
		}

		public void setList(List<JSONObject> list) {
			this.list = list;
		}
	}

}
