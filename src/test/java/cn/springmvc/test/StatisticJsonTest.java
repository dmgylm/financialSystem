package cn.springmvc.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;















import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.BusinessData;
import cn.financial.model.Organization;
import cn.financial.service.BusinessDataService;
import cn.financial.service.StatisticJsonService;
import cn.financial.util.HttpClient3;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml" ,"classpath:spring/spring-redis.xml"})
public class StatisticJsonTest {

    @Autowired
    private StatisticJsonService service;
    
    @Autowired
    private BusinessDataService businessDataService;
	
    @Test
    public void businessDataTest() {
    	
    	List<String> typeIdList =new ArrayList<String>();
    	typeIdList.add("740d5f676b084c00a4d9a1af194b0699");
    	typeIdList.add("d0f956cb5f5546dca91509ccde2a6e74");
    	typeIdList.add("37dee02571284bbd89c4ae3e92e0547b");
    	
        Map<Object, Object> map = new HashMap<>();
        map.put("typeId", typeIdList);
        map.put("startYear", "2018");
        map.put("endYear", "2018");
        map.put("startMonth", "5");
        map.put("endMonth", "5");
    	List<BusinessData> BusinessDataList = businessDataService.listBusinessDataByIdAndDate(map);
    	System.out.println(BusinessDataList.size());
    	
    }
    
    
    @Test
    public void StatisticJsonTest() {
    	
    	JSONArray orgId = new JSONArray();
    	orgId.add("0303e625211c47ee86efb8007053186b");
    	orgId.add("04668940dfa947cd8a1d95e32e503fc8");
    	
		//获取所选机构
		List<Organization> codeSonList = service.companyList(orgId);
		//获取最底层数据
		List<String> typeIdList = service.typeIdList(codeSonList);
		//获取底层对应数据的集合
		List<BusinessData> businessDataList = service.valueList("2018/5","2018/5",typeIdList);
    	
		System.out.println(service.jsonCalculation("0", "66ba42fc04c547318d68d08700770e92", businessDataList));
    	
    }
    
    @Test
    public void StatisticCacheTest() {
    	String caCheUuid = UuidUtil.getUUID();
    	JSONArray orgId = new JSONArray();
    	orgId.add("0303e625211c47ee86efb8007053186b");
    	orgId.add("04668940dfa947cd8a1d95e32e503fc8");
    	
		//获取所选机构
		List<Organization> codeSonList = service.companyList(orgId);
		//获取最底层数据
		List<String> typeIdList = service.typeIdList(codeSonList);
		//获取底层对应数据的集合
		List<BusinessData> businessDataList = service.valueList("2018/5","2018/5",typeIdList);
		//获取对应公司列表
 		Map<String,List<String>> companyList =  service.companyCacheList(codeSonList);
		
    	//存入缓存
 		service.staticInfoMap(companyList,businessDataList,caCheUuid);
		System.out.println(caCheUuid);
    	
    }
    
    @Test
    public void getCacheTest() {
		
    	//存入缓存
    	Map<String, Map<String, String>> ja = service.staticInfoMap(null,null,"bf4f24b367014c839ad3704b3c17f46f");
		System.out.println(ja);
    	
    }
    
    
    @Test
    public void StatisticJsonHTMLTest() throws Exception {
    	
		HttpClient3 httpClient = new HttpClient3();
    	JSONArray orgId = new JSONArray();
    	orgId.add("0303e625211c47ee86efb8007053186b");
    	orgId.add("04668940dfa947cd8a1d95e32e503fc8");
		
		Map<String , String> params = new HashMap<String, String>();
		params.put("reportType", "0");
		params.put("businessType", "66ba42fc04c547318d68d08700770e92");
		params.put("startDate", "2018/5");
		params.put("endDate", "2018/5");
		params.put("orgId", orgId.toString());
		
		String responseStr = httpClient.doPost("http://localhost:8080/financialSys/statistic/staticJson;JSESSIONID=1a5157ce-7db3-489a-9f4a-45c81e720642", params);
		System.out.println(responseStr);
    	
    	
    }
    
	
}
