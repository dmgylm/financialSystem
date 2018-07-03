package cn.financial.service;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.financial.model.InCopy;
import cn.financial.service.impl.InCopyServiceImpl;
import cn.financial.util.DBConnection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mysql.jdbc.Statement;

import freemarker.cache.TemplateCache;

@Service("cacheService")
//@CacheConfig(cacheNames={"baseCache"})
public class RedisTestService {
	
	@Autowired
	public InCopyServiceImpl ii;
	
	@SuppressWarnings("rawtypes")
	public RedisTemplate jedisTemplate;
	
	@SuppressWarnings("unchecked")
	public List<String> getListValue(String key) {

        ListOperations<String, String> list = jedisTemplate.opsForList();
        return jedisTemplate.opsForList().range(key, 0,list.size(key));

    }
	
	
	@Cacheable(value="ceshi11",key="'data11'")
	public JSONArray get1(){
		JSONArray json = new JSONArray();
		JSONObject jo = new JSONObject();
		List<InCopy> list = ii.getCopy();
		for(InCopy sc:list){
			jo.put(sc.getId(), sc.getInfo()/*.replace("\\", "")*/);
//			System.out.println(sc.getId());
		}
		json.add(jo);
		return json;
	}
	
	
//	public List<String> test(){
//		List<InCopy> list = ii.getCopy();
//		List<String> l = new ArrayList<String>();
//		for(InCopy in:list){
//			l.add(in.getId());
//		}
//		return l;
//	}
	@Cacheable(value="ceshi123")
	public JSONArray getAll(){
		JSONArray json = new JSONArray();
		List<InCopy> list = ii.getCopy();
		String a = list.get(0).getInfo();
		json.add(a);
		return json;
	}
	
	@Cacheable(value="ttt",key="#in.getId()")
	public JSONArray get(InCopy in){
		JSONArray json = new JSONArray();
//		JSONObject jo = new JSONObject();
		List<InCopy> list = ii.getCopyById(in.getId());
		InCopy a = list.get(0);
		json.add(a);
//		json.add(jo);
		return json;
	}
	
	@CachePut(value="ceshi123",key="#id")
	public JSONArray update(String id){
		JSONArray json = new JSONArray();
		json.add("5678");
		return json;
	}
	
	@CacheEvict(value="ceshi123", allEntries = true)
	public void delete(){
	}
	
	
	
	
	@Cacheable(value="ceshi",key="'data1'")
	public JSONArray getData(){
		@SuppressWarnings("resource")
		MongoClient client = new MongoClient("192.168.105.201",27017);
		MongoCollection<Document> col = client.getDatabase("school").getCollection("netWork");
		MongoCursor<Document> cur = col.find().iterator();
		
		JSONArray json = new JSONArray();
		while(cur.hasNext()){
			Document doc = cur.next();
			json.add(doc);
		}
		
		return json;
	}
	
	@Cacheable(value="ceshi",key="'data'")
	public Map<String, Object> getData1(){
		@SuppressWarnings("resource")
		MongoClient client = new MongoClient("192.168.105.201",27017);
		MongoCollection<Document> col = client.getDatabase("school").getCollection("ttt");
		MongoCursor<Document> cur = col.find().iterator();
		
		JSONArray json = new JSONArray();
		while(cur.hasNext()){
			Document doc = cur.next();
			json.add(doc);
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", json);
		return dataMap;
	}
	
}
