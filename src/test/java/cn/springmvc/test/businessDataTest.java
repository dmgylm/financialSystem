package cn.springmvc.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.BusinessData;
import cn.financial.model.DataModule;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.util.FormulaUtil;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
		"classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml",
		"classpath:spring/spring-cache.xml", "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml" })
public class businessDataTest {

	@Autowired
	private BusinessDataServiceImpl businessDataService;
	@Autowired
	private DataModuleServiceImpl dataModuleServiceImpl;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 新增损益数据
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@Test
	public void insertStatement() throws UnsupportedEncodingException, ParseException {
		String info = "第wu个";
		info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
		BusinessData businessData = new BusinessData();
		businessData.setId(UuidUtil.getUUID());
		businessData.setoId("5");
		businessData.setInfo(info);
		businessData.setTypeId("5");
		businessData.setuId("9685618f583c416ab835683d1eba09ea");
		businessData.setYear(2018);
		businessData.setMonth(3);
		businessData.setStatus(1);
		businessData.setDelStatus(2);
		businessData.setsId(1);
		Integer i = businessDataService.insertBusinessData(businessData);
		System.out.println(i);
	}

	/**
	 * 根据传入的map查询相应的损益表数据 不传就是查询 全部
	 */
	@Test
	public void listStatementBy() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("year", 2018);
		map.put("month", 4);
		map.put("sId", 1);
		List<BusinessData> list = businessDataService.listBusinessDataBy(map);
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			System.out.println("开始" + list.get(i).getId() + "--" + list.get(i).getoId() + "--" + list.get(i).getInfo()
					+ "--" + "--" + list.get(i).getTypeId() + "--" + list.get(i).getuId() + "--" + list.get(i).getYear()
					+ "--" + list.get(i).getMonth() + "--" + list.get(i).getStatus());

		}
	}

	/**
	 * 根据ID查询损益信息
	 */
	@Test
	public void getStatementById() {
		String id = "ca85a6e99b8949a886ee50efac06ab06";
		BusinessData statement = businessDataService.selectBusinessDataById(id);
		System.out.println("开始" + statement.getId() + "--" + statement.getoId() + "--" + statement.getInfo() + "--"
				+ "--" + statement.getTypeId() + "--" + statement.getuId() + "--" + statement.getYear() + "--"
				+ statement.getMonth() + "--" + statement.getStatus());
	}

	/**
	 * 根据条件修改损益信息
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@Test
	public void updateStatement() throws UnsupportedEncodingException, ParseException {
		String info = "第2个";
		info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
		BusinessData statement = new BusinessData();
		statement.setId("2");
		statement.setoId("5");
		statement.setInfo(info);
		statement.setTypeId("5");
		statement.setuId("9685618f583c416ab835683d1eba09ea");
		statement.setYear(2018);
		statement.setMonth(3);
		statement.setStatus(1);
		statement.setsId(1);
		Integer i = businessDataService.updateBusinessData(statement);
		System.out.println("结果" + i);
	}

	/**
	 * 伪删除（根据条件删除损益信息）
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@Test
	public void deleteStatement() {
		Integer i = businessDataService.deleteBusinessData("2");
		System.out.println("结果" + i);
	}

	@Test
	public void ti() throws IOException {
		//需要参数，前端传来的HTML，业务表的id，状态（保存还是提交）
		//String table;
		File input = new File("C:/Users/admin/Desktop/测试html.txt");
		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		Elements inputHtml = doc.select("input");// 获取HTML所有input属性
		/* System.out.println(inputHtml); */
		DataModule dm = dataModuleServiceImpl.getDataModule("74742b82645e4b93a1dcaa8697edce86");// 获取原始模板
		JSONObject dataMjo = JSONObject.parseObject(dm.getModuleData());// 获取损益表数据模板
		Map<String,Object>mo=new HashMap<String, Object>();
		for (int i = 0; i < inputHtml.size(); i++) {//解析HTML获取所有input  name和value值
			mo.put(inputHtml.get(i).attr("name"), inputHtml.get(i).val());
			
		}
		JSONObject newBudgetHtml=dgkey(dataMjo, mo);
		
		System.out.println(newBudgetHtml);
	}

	public JSONObject dgkey(JSONObject dataMjo, Map<String,Object> mo) {
		JSONObject newBudgetHtml=new JSONObject();
		for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
			String keyIncome = iterIncome.next();
			Object objIncome = dataMjo.get(keyIncome);
			if (objIncome instanceof JSONArray) {
				newBudgetHtml.put(keyIncome, mergeHtml(dataMjo, mo));
			} else {
				JSONObject longData = (JSONObject) objIncome;
				newBudgetHtml.put(keyIncome, mergeHtml(longData, mo));
			}
		}
		System.out.println(newBudgetHtml);
		return newBudgetHtml;
	}
	/**
	 * 根据前端传来的表格进行合并
	 * @param dataMjo
	 * @param mo
	 * @return
	 */
	public JSONObject mergeHtml (JSONObject dataMjo,Map<String,Object> mo) {
		JSONObject newBudgetHtml=new JSONObject();
		JSONObject obj = new JSONObject();
		for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
			String keyIncome = iterIncome.next();
			Object objIncome = dataMjo.get(keyIncome);
			JSONArray jsoA = new JSONArray();
			if (objIncome instanceof JSONArray) {
			JSONArray longLst = (JSONArray) objIncome;
				for (int j = 0; j < longLst.size(); j++) {
					 obj = new JSONObject();
					JSONObject longJson = longLst.getJSONObject(j);
					for (Iterator<String> iters = longJson.keySet().iterator(); iters.hasNext();) {
						String keysIncomes = iters.next();
						Object objsIncome = longJson.get(keysIncomes);
						obj.put(keysIncomes, objsIncome);
						
						if(mo.containsKey(longJson.get("key"))) {
							Double value= 0.0;
							if(Integer.parseInt(longJson.get("type").toString())==HtmlGenerate.BOX_TYPE_FORMULA) {//判断当前类型是不是公式
								 value=FormulaUtil.calculationByFormula(mo,longJson.get("reallyFormula").toString());
							}else {
								value= (Double) mo.get(longJson.get("key"));
							}
							obj.put("value", value);
						}
					}
					jsoA.add(obj);;
				}
			}
		newBudgetHtml.put(keyIncome, jsoA);
		}
		return newBudgetHtml ;
	}
}
