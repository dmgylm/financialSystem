package cn.springmvc.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.DataModule;
import cn.financial.service.DataModuleService;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml"})
public class DataModuleTest {

	@Autowired
	private DataModuleServiceImpl dataModuleService;
	
	@Test
	public void getListDataModules(){
		
		Map<Object, Object> map=new HashMap<Object, Object>();
		//map.put("moduleName", "");
		
		List<DataModule> dataModules=dataModuleService.listDataModule(map);
		for(DataModule dataModule:dataModules){
			System.out.println(dataModule);
		}
		//return dataModules;
	}
	
	@Test
	public void insertDataModules(){
		DataModule dataModule=new DataModule();
		dataModule.setId(UuidUtil.getUUID());
		dataModule.setModuleName("车险预算表");
		dataModule.setFounder("test");
		dataModule.setStatue(dataModule.STATUS_CONSUMED);
		dataModuleService.insertDataModule(dataModule);
		
	}
}
