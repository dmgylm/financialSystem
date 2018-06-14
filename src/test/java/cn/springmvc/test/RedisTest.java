package cn.springmvc.test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.financial.service.impl.MongoDBServiceImpl;
import cn.financial.util.RedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml","classpath:conf/spring-redis.xml"})

public class RedisTest {
	
	@Autowired
	public RedisUtils red;
	@Autowired
	public MongoDBServiceImpl mongoDBService;
	
	@Test
	public void test(){
//		System.out.println(getData());
		red.insert("test1", getData().toString());
		System.out.println(red.getData("test1"));
	}
	
	public JSONArray getData(){
		MongoClient client = new MongoClient("192.168.105.201",27017);
		MongoCollection<Document> col = client.getDatabase("school").getCollection("personnel");
		MongoCursor<Document> cur = col.find().iterator();
		
		JSONArray json = new JSONArray();
		while(cur.hasNext()){
			Document doc = cur.next();
			json.add(doc);
		}
		return json;
	}
}
