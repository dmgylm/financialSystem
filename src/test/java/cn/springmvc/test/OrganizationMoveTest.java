package cn.springmvc.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.OrganizationMove;
import cn.financial.service.impl.OrganizationMoveServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml",
        "classpath:spring/spring-cache.xml", "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml" })
public class OrganizationMoveTest {
	
	@Autowired
	private OrganizationMoveServiceImpl service;
	
	//查询
	@Test
	public void listOrganizationMoveBy() {
		Map<Object,Object> map = new HashMap<>();
		map.put("orgkey", "d59aa659dba14120acf151418f48bc14");
        List<OrganizationMove> list = service.listOrganizationMoveBy(map);
        System.out.println(list.get(0).toString());
	}
	
}