package cn.financial.service;


import org.bson.Document;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Service("cacheService")
//@CacheConfig(cacheNames={"baseCache"})
public class RedisTestService {
	
	@Cacheable(value="who",key="'jqq'")
	public JSONArray getData(){
		@SuppressWarnings("resource")
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
