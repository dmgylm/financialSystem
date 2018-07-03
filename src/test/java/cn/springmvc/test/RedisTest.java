package cn.springmvc.test;


import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.financial.model.InCopy;
import cn.financial.service.RedisTestService;
import cn.financial.service.impl.InCopyServiceImpl;
import cn.financial.service.impl.MongoDBServiceImpl;
//import cn.financial.util.RedisUtils;
import cn.financial.util.RedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml","classpath:spring/spring-redis.xml"})

public class RedisTest {
	
//	@Autowired
//	public RedisUtils red;
	@Autowired
	public MongoDBServiceImpl mongoDBService;
	@Autowired
	public RedisTestService rts;
	@Autowired
	public InCopyServiceImpl ii;
	@Autowired
	public RedisUtils redisUtils;
	
	
	@Test
	public void test(){
//		System.out.println(getData());
//		red.insert("test1", getData().toString());
		long a = System.currentTimeMillis();
		rts.delete();
//		rts.get1();
		long b = System.currentTimeMillis();
		System.out.println("----------------------------------------------------------------------");
		System.out.println(b-a);
//		System.out.println(t);
	}
	
	@Test
	public void test1(){
//		List<String> listId = ii.getCopyIdList();
		long a = System.currentTimeMillis();
		List<InCopy> list = ii.getCopyId();
		for(InCopy in : list){
			rts.get(in);
		}
		long b = System.currentTimeMillis();
		System.out.println(b-a);
	}
	
	@Test
	public void test2(){
		List<String> a = rts.getListValue("ceshi123");
		System.out.println(a);
	}
	
	@Test
	public void testt(){
		System.gc();
	}
	
}
