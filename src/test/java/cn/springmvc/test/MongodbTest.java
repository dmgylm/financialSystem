package cn.springmvc.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongodbTest {
	
	public static void main(String[] args) {
		Calendar start = Calendar.getInstance();
		System.out.println("start:"+start.getTimeInMillis());
		MongoClient mongoClient = new MongoClient("192.168.1.3");

		MongoDatabase db = mongoClient.getDatabase("test");
		MongoCollection<Document> doc = db.getCollection("dataTest");

		final List<String> list = new ArrayList<String>();
		FindIterable<Document> iter = doc.find();
		Calendar end = Calendar.getInstance();
		System.out.println("end:"+end.getTimeInMillis());
		System.out.println("findTime:"+(end.getTimeInMillis()-start.getTimeInMillis()));
		iter.forEach(new Block<Document>() {
			public void apply(Document _doc) {
				list.add(_doc.get("value").toString());
			}
		});
		System.out.println(list.size());
		end = Calendar.getInstance();
		System.out.println("end:"+end.getTimeInMillis());
		System.out.println(end.getTimeInMillis()-start.getTimeInMillis());
	}

}
