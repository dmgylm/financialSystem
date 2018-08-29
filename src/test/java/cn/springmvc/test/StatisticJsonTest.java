package cn.springmvc.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.BusinessData;
import cn.financial.model.Organization;
import cn.financial.model.response.ChildrenObject;
import cn.financial.service.BusinessDataService;
import cn.financial.service.StatisticJsonService;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.HttpClient3;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml","classpath:spring/swagger.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml" ,"classpath:spring/spring-redis.xml"})
public class StatisticJsonTest {

    @Autowired
    private StatisticJsonService service;
    
    @Autowired
    private BusinessDataService businessDataService;
    @Autowired
    private OrganizationServiceImpl orgin;
    @Test
    public void ss(){
    	String id="00a1f4a699c945638e4c6114e9a8448d";
    	GG(orgin.TreeByIdForSon(id));
    }
    public void GG(JSONObject json){
        if(json==null || json.equals("")){
            return;
        }
        JSONArray jsosn=(JSONArray) json.get("children");
        ChildrenObject children = new ChildrenObject();
        children.setId(json.get("id").toString());
        
    }
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
    	orgId.add("088053224bfe404ab97b45efba15d6c0");
    	
		//获取所选机构
		List<Organization> codeSonList = service.companyList(orgId);
		//获取最底层数据
		List<String> typeIdList = service.typeIdList(codeSonList);
		//获取底层对应数据的集合
		List<BusinessData> businessDataList = service.valueList("2015/5","2019/7",typeIdList);
    	
		System.out.println(service.jsonCalculation("BUDGET", "b1503ff8da124fa3bce0bf07f16f56f6", businessDataList)); 
    	
    }
    
    @Test
    public void StatisticCacheTest() {
    	String caCheUuid = UuidUtil.getUUID();
    	JSONArray orgId = new JSONArray();
    	orgId.add("088053224bfe404ab97b45efba15d6c0");
    	
    	System.out.println(orgId);
    	
		//获取所选机构
		List<Organization> codeSonList = service.companyList(orgId);
		//获取最底层数据
		List<String> typeIdList = service.typeIdList(codeSonList);
		//获取底层对应数据的集合
		List<BusinessData> businessDataList = service.valueList("2018/5","2019/7",typeIdList);
		//获取对应公司列表
 		Map<String,List<String>> companyList =  service.companyCacheList(codeSonList);
		
    	//存入缓存
 		service.staticInfoMap(companyList,businessDataList,caCheUuid);
		System.out.println(caCheUuid);
    	
    }
    
    @Test
    public void getCacheTest() {
		
    	//存入缓存
    	Map<String, Map<String, String>> ja = service.staticInfoMap(null,null,"9ee9313edc334232b7a64a60f27ce5dd");
		System.out.println(ja);
    	
    }
    
    @Test
    public void StatisticJsonHTMLTest() throws Exception {
    	
		HttpClient3 httpClient = new HttpClient3();
    	JSONArray orgId = new JSONArray();
    	orgId.add("0303e625211c47ee86efb8007053186b");
    	orgId.add("04668940dfa947cd8a1d95e32e503fc8");
		
		Map<String , String> params = new HashMap<String, String>();
		params.put("reportType", "PROFIT_LOSS");
		params.put("businessType", "66ba42fc04c547318d68d08700770e92");
		params.put("startDate", "2018/5");
		params.put("endDate", "2018/5");
		params.put("orgId", orgId.toString());
		
		String responseStr = httpClient.doPost("http://localhost:8080/financialSystem/statistic/staticJson;JSESSIONID=06773a40-5e62-4ecf-85b9-aa768020e72d&", params);
		System.out.println("responseStr:"+responseStr);
    	
    	
    }
    
    @Test
    public void StatisticBudgetHTMLTest() throws Exception {
    	
		HttpClient3 httpClient = new HttpClient3();
    	JSONArray orgId = new JSONArray();
    	orgId.add("088053224bfe404ab97b45efba15d6c0");
		
		Map<String , String> params = new HashMap<String, String>();
		params.put("reportType", "BUDGET");
		params.put("businessType", "b1503ff8da124fa3bce0bf07f16f56f6");
		params.put("startDate", "2015/5");
		params.put("endDate", "2019/7");
		params.put("orgId", orgId.toString());
		
		String responseStr = httpClient.doPost("http://localhost:8080/financialSystem/statistic/staticJson;JSESSIONID=3d6c459a-02df-42cc-9a19-1f57805131e0", params);
		System.out.println(responseStr);
    	
    	
    }
    
    @Test
    public void StatisticJsonSelectTest() throws Exception {
    	
		HttpClient3 httpClient = new HttpClient3();
		
		Map<String , String> params = new HashMap<String, String>();
		params.put("infoKey", "guanlifeiyong_zhufanggongjijinbenyueshiji");
		params.put("caCheUuid", "9ee9313edc334232b7a64a60f27ce5dd");
		
		String responseStr = httpClient.doPost("http://localhost:8080/financialSystem/statistic/staticInfo;JSESSIONID=24951a30-34a4-46c7-84fe-855db87a017b", params);
		System.out.println(responseStr);
    	
    	
    }
    
    @Test
    public void OrgDataTest() {
    	String orgId = "['22ee8c554c2a4f5383038c3f11ae02ed']";
    	//获取所选机构
 		List<Organization> codeSonList = service.companyList(JSONArray.parseArray(orgId));
		//获取最底层数据
		List<String> typeIdList = service.typeIdList(codeSonList);
   		//获取对应公司列表
// 		Map<String,List<String>> companyList =  service.companyCacheList(codeSonList);
 		
// 		System.out.println(companyList);
    }
    
    @Test
    public void StatisticGroupTest() {
       	String orgId = "['22ee8c554c2a4f5383038c3f11ae02ed']";
       	
       	List<BusinessData> businessDataList = service.BusList("2018/1","2019/12",JSONArray.parseArray(orgId));
       	
//    	//获取所选机构
// 		List<Organization> codeSonList = service.companyList(JSONArray.parseArray(orgId));
//		//获取最底层数据
//		List<String> typeIdList = service.typeIdList(codeSonList);
//		//获取底层对应数据的集合
//		List<BusinessData> businessDataList = service.valueList("2018/1","2019/12",typeIdList);
		
		JSONObject ja =JSONObject.parseObject(service.jsonCalculationCollect("SUMMARY_BUDGET",null,businessDataList));
		HtmlGenerate hg = new HtmlGenerate(true);
		String html = hg.generateHtml(ja,HtmlGenerate.HTML_TYPE_PREVIEW);
		
		System.out.println(html); 
    	
    }
	
}
