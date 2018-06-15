package cn.springmvc.test;


import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.financial.service.RedisTestService;
import cn.financial.service.impl.MongoDBServiceImpl;
//import cn.financial.util.RedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml","classpath:conf/spring-redis.xml"})

public class RedisTest {
	
//	@Autowired
//	public RedisUtils red;
	@Autowired
	public MongoDBServiceImpl mongoDBService;
	@Autowired
	public RedisTestService rts;
	
	@Test
	public void test(){
//		System.out.println(getData());
//		red.insert("test1", getData().toString());
		JSONArray j = rts.getData();
		System.out.println(j);
	}
	
}
