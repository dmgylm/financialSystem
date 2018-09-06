package cn.springmvc.test;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;

import cn.financial.service.RedisCacheService;
import cn.financial.service.RedisTestService;
//import cn.financial.util.RedisUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml","classpath:spring/spring-redis.xml"})

public class RedisTest {
	
//	@Autowired
//	public RedisUtils red;
	
	@Autowired
	public RedisTestService rts;
	
	@Autowired
	private RedisCacheService cache;
	
	
	@Test
	public void testRedisCache(){
//		cache.put("testCache", "test1", 233333);//添加缓存
//		cache.put("testCache", "test2", 233333);//添加缓存
//		cache.remove("testCache", "test1");//删除单个缓存KEY
		cache.removeAll("organizationValue");//删除整个Cache
	}

	@Test
	public void test(){
//		System.out.println(getData());
//		red.insert("test1", getData().toString());
		JSONArray j = rts.getData();
		System.out.println(j);
	}
	
}
