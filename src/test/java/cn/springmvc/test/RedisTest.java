package cn.springmvc.test;


import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import cn.financial.service.RedisCacheService;
import cn.financial.service.RedisTestService;
import cn.financial.service.impl.MongoDBServiceImpl;
//import cn.financial.util.RedisUtils;
import cn.financial.util.EhcacheUtil;

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
	private RedisCacheService cache;
	
	
	@Test
	public void testRedisCache(){
		cache.put("testCache", "test1", 233333);//添加缓存
		cache.put("testCache", "test2", 233333);//添加缓存
		cache.remove("testCache", "test1");//删除单个缓存KEY
		cache.removeAll("testCache");//删除整个Cache
	}

	@Test
	public void test(){
//		System.out.println(getData());
//		red.insert("test1", getData().toString());
		JSONArray j = rts.getData();
		System.out.println(j);
	}
	
}
