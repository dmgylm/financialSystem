package cn.springmvc.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.financial.util.HttpClient3;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml","classpath:spring/spring-redis.xml"})
public class DataModuleTest {

	@Autowired
	private DataModuleServiceImpl dataModuleService;
	
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * 查询测试
	 */
	@Test
	public void getListDataModules(){
		
		/*Map<Object, Object> map=new HashMap<Object, Object>();
		map.put("moduleName", "车险预算表");
		
		map.put("dataModuleId", "");
		
		map.put("reportType", "");
		map.put("businessType", "");
		
		List<DataModule> dataModules=dataModuleService.listDataModule(map);
		for(DataModule dataModule:dataModules){
			System.out.println(dataModule);
		}
		System.out.println(dataModules.size());
		//return dataModules;
		 * 
*/	
		
		HttpClient3 http = new HttpClient3();
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("moduleName", "车险预算表");
		//params.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MjE0MzU0NTY2OTB9.dBOCTsvN0fo44hg_pZv8y-MfTzYv23Lo7XGVcezs4gA");
		try {
			System.out.println(JSONObject.fromObject(params));
			String res = http.doPost("http://localhost:8080/financialSys/dataModule/dataModuleList",params);
			System.out.println(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据id 查询配置模板
	 */
	@Test
	public void test(){
		try {
			HttpClient3 http = new HttpClient3();
			
			Map<String,String> params = new HashMap<String, String>();
			params.put("dataModuleId", "16421fc812b14385aa62c01c834b4079");
			//params.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MjE0MzU0NTY2OTB9.dBOCTsvN0fo44hg_pZv8y-MfTzYv23Lo7XGVcezs4gA");
			System.out.println(JSONObject.fromObject(params));
			String res = http.doPost("http://localhost:8080/financialSys/dataModule/getDataModule",params);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据报表类型及业务板块查询最新版本模板
	 */
	@Test
	public void getNewestDataModule(){
		HttpClient3 http = new HttpClient3();
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("reportType", "PROFIT_LOSS");
		params.put("businessType", "3acea0c85d584448b37b0ca05cc34b6f");
		//params.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MjE0MzU0NTY2OTB9.dBOCTsvN0fo44hg_pZv8y-MfTzYv23Lo7XGVcezs4gA");
		try {
			System.out.println(JSONObject.fromObject(params));
			String res = http.doPost("http://localhost:8080/financialSys/dataModule/getNewestDataModule",params);
			System.out.println(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 插入数据测试
	 */
	@Test
	public void insertDataModules(){
		DataModule bean = new DataModule();
		bean.setId(UuidUtil.getUUID());
		bean.setVersionNumber("1");
		bean.setReportType(DataModule.REPORT_TYPE_PROFIT_LOSS);
		//bean.setBusinessType("3acea0c85d584448b37b0ca05cc34b6f");  //保险
		//bean.setBusinessType("b1503ff8da124fa3bce0bf07f16f56f6");//车管家
		//bean.setBusinessType("19d5dd69d5b543ffb83b199f231eb2d3");//技术服务
		//bean.setBusinessType("1d467a74038840e5b617c82d1b421de6");//融资租赁
		//bean.setBusinessType("82f94b3089eb496eaf2ed6ca73fb2946");//专车
		//bean.setBusinessType("f9176b82a56c46e8a0fe6c3379f39de9");//爱车贷
		//bean.setBusinessType("92c6ea3666c148349e9f6842fe2e7bf4");//维修
		bean.setBusinessType("b9d14004e8b342b190fab1eeb2c3dd2c");//集团总部
		
		bean.setModuleData("");
		bean.setStatue(DataModule.STATUS_CONSUMED);
		bean.setModuleName(getDataModuleName(bean.getReportType(),bean.getBusinessType()));
		dataModuleService.insertDataModule(bean);
		
	}
	private String getDataModuleName(String reportType, String businessType) {
		Organization org = organizationService.getOrgaByKey(businessType);
		String reportTypeName = DataModule.getReprtTypeName(reportType);
		return org.getOrgName() + reportTypeName;
	}
	
	@Test
	public void editDataModuleTest(){
		String reportType ="PROFIT_LOSS";  //报表类型  损益
		String businessType="1d467a74038840e5b617c82d1b421de6";  //业务板块（对应为组织架构orgkey
		String html="<html><head> <style>.display_none{display:none}</style></head> <body> <table border='1'>  <tr>   <td colspan='5'><input name='2017年盛大汽车服务连锁月度损益表（总裁办）' value='2017年盛大汽车服务连锁月度损益表（总裁办）' class='title'></td>   <td class='display_none'><input class='input'></td><td class='display_none'><input class='input'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input class='input'></td>   <td><input class='input'></td>  </tr>  <tr>   <td class='display_none'><input class='input'></td>   <td class='display_none'><input class='input'></td>   <td class='display_none'><input class='input'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input class='input'></td>   <td><input class='input'></td>  </tr>  <tr>   <td rowspan='2'><input name='序号' value='序号' class='title'></td>   <td colspan='2' rowspan='2'><input name='项目' value='项目' class='title'></td>   <td class='display_none'><input class='input'></td>   <td colspan='4'><input name='1月' value='1月' class='title'></td>   <td><input class='budget'></td>   <td><input class='input'></td>   <td><input class='input'></td>  </tr>  <tr>   <td class='display_none'><input class='input'></td>   <td class='display_none'><input class='input'></td>   <td class='display_none'><input class='input'></td>   <td><input name='本月实际' value='本月实际' class='title'></td>   <td><input name='本月预算' value='本月预算' class='title'></td>   <td><input name='完成率%' value='完成率%' class='title'></td>   <td><input name='占比%' value='占比%' class='title'></td>  </tr>  <tr>   <td><input name='一、' value='一、' class='title'></td>   <td><input name='总费用' value='总费用' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input value='=营业费用_合计本月实际+管理费用_合计本月实际+财务费用_合计本月实际' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=总费用_本月实际/总费用_本月预算' class='formula'></td>   <td><input class='input'></td>  </tr>  <tr>   <td><input name='1、' value='1、' class='title'></td>   <td rowspan='25'><input name='营业费用' value='营业费用' class='title'></td>   <td><input name='合计' value='合计' class='title'></td>   <td><input value='=SUM(营业费用_其中：工资本月实际:营业费用_其他本月实际)' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_合计本月实际/营业费用_合计本月预算' class='formula'></td>   <td><input value='=营业费用_合计本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='1）' value='1）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='工资' value='工资' class='title'></td>   <td><input value='=营业费用_其中：工资本月实际+营业费用_业务提成本月实际' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_工资本月实际/营业费用_工资本月预算' class='formula'></td>   <td><input value='=营业费用_工资本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='2）' value='2）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='其中：工资' value='其中：工资' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_其中：工资本月实际/营业费用_其中：工资本月预算' class='formula'></td>   <td><input value='=营业费用_其中：工资本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='3）' value='3）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='业务提成' value='业务提成' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_业务提成本月实际/营业费用_业务提成本月预算' class='formula'></td>   <td><input value='=营业费用_业务提成本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='4）' value='4）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='社会保险费' value='社会保险费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_社会保险费本月实际/营业费用_社会保险费本月预算' class='formula'></td>   <td><input value='=营业费用_社会保险费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='5）' value='5）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='住房公积金' value='住房公积金' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_住房公积金本月实际/营业费用_住房公积金本月预算' class='formula'></td>   <td><input value='=营业费用_住房公积金本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='6）' value='6）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='劳务费' value='劳务费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_劳务费本月实际/营业费用_劳务费本月预算' class='formula'></td>   <td><input value='=营业费用_劳务费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='7）' value='7）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='救援费用' value='救援费用' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_救援费用本月实际/营业费用_救援费用本月预算' class='formula'></td>   <td><input value='=营业费用_救援费用本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='8）' value='8）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='信息服务费' value='信息服务费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_信息服务费本月实际/营业费用_信息服务费本月预算' class='formula'></td>   <td><input value='=营业费用_信息服务费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='9）' value='9）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='广告费' value='广告费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_广告费本月实际/营业费用_广告费本月预算' class='formula'></td>   <td><input value='=营业费用_广告费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='10）' value='10）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='咨询费' value='咨询费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_咨询费本月实际/营业费用_咨询费本月预算' class='formula'></td>   <td><input value='=营业费用_咨询费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='11）' value='11）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='印刷制作费' value='印刷制作费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_印刷制作费本月实际/营业费用_印刷制作费本月预算' class='formula'></td>   <td><input value='=营业费用_印刷制作费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='12）' value='12）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='活动费' value='活动费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_活动费本月实际/营业费用_活动费本月预算' class='formula'></td>   <td><input value='=营业费用_活动费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='13）' value='13）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='办公费' value='办公费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_办公费本月实际/营业费用_办公费本月预算' class='formula'></td>   <td><input value='=营业费用_办公费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='14）' value='14）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='汽车费用' value='汽车费用' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_汽车费用本月实际/营业费用_汽车费用本月预算' class='formula'></td>   <td><input value='=营业费用_汽车费用本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='15）' value='15）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='通讯费' value='通讯费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_通讯费本月实际/营业费用_通讯费本月预算' class='formula'></td>   <td><input value='=营业费用_通讯费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='16）' value='16）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='市内交通费' value='市内交通费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_市内交通费本月实际/营业费用_市内交通费本月预算' class='formula'></td>   <td><input value='=营业费用_市内交通费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='17）' value='17）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='差旅费' value='差旅费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_差旅费本月实际/营业费用_差旅费本月预算' class='formula'></td>   <td><input value='=营业费用_差旅费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='18）' value='18）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='快递费' value='快递费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_快递费本月实际/营业费用_快递费本月预算' class='formula'></td>   <td><input value='=营业费用_快递费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='19）' value='19）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='福利费' value='福利费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_福利费本月实际/营业费用_福利费本月预算' class='formula'></td>   <td><input value='=营业费用_福利费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='20）' value='20）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='低值易耗品' value='低值易耗品' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_低值易耗品本月实际/营业费用_低值易耗品本月预算' class='formula'></td>   <td><input value='=营业费用_低值易耗品本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='21）' value='21）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='房租及水电费' value='房租及水电费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_房租及水电费本月实际/营业费用_房租及水电费本月预算' class='formula'></td>   <td><input value='=营业费用_房租及水电费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='22）' value='22）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='折旧费用' value='折旧费用' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_折旧费用本月实际/营业费用_折旧费用本月预算' class='formula'></td>   <td><input value='=营业费用_折旧费用本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='23）' value='23）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='资产摊销' value='资产摊销' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_资产摊销本月实际/营业费用_资产摊销本月预算' class='formula'></td>   <td><input value='=营业费用_资产摊销本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='24）' value='24）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='其他' value='其他' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业费用_其他本月实际/营业费用_其他本月预算' class='formula'></td>   <td><input value='=营业费用_其他本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='2、' value='2、' class='title'></td>   <td rowspan='24'><input name='管理费用' value='管理费用' class='title'></td>   <td><input name='合计' value='合计' class='title'></td>   <td><input value='=SUM(管理费用_工资本月实际:管理费用_其他本月实际)' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_合计本月实际/管理费用_合计本月预算' class='formula'></td>   <td><input value='=管理费用_合计本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='1）' value='1）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='工资' value='工资' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_工资本月实际/管理费用_工资本月预算' class='formula'></td>   <td><input value='=管理费用_工资本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='2）' value='2）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='社会保险费' value='社会保险费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_社会保险费本月实际/管理费用_社会保险费本月预算' class='formula'></td>   <td><input value='=管理费用_社会保险费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='3）' value='3）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='住房公积金' value='住房公积金' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_住房公积金本月实际/管理费用_住房公积金本月预算' class='formula'></td>   <td><input value='=管理费用_住房公积金本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='4）' value='4）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='劳务费' value='劳务费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_劳务费本月实际/管理费用_劳务费本月预算' class='formula'></td>   <td><input value='=管理费用_劳务费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='5）' value='5）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='招聘培训费' value='招聘培训费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_招聘培训费本月实际/管理费用_招聘培训费本月预算' class='formula'></td>   <td><input value='=管理费用_招聘培训费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='6）' value='6）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='广告费' value='广告费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_广告费本月实际/管理费用_广告费本月预算' class='formula'></td>   <td><input value='=管理费用_广告费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='7）' value='7）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='咨询费' value='咨询费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_咨询费本月实际/管理费用_咨询费本月预算' class='formula'></td>   <td><input value='=管理费用_咨询费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='8）' value='8）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='办公费' value='办公费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_办公费本月实际/管理费用_办公费本月预算' class='formula'></td>   <td><input value='=管理费用_办公费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='9）' value='9）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='汽车费用' value='汽车费用' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_汽车费用本月实际/管理费用_汽车费用本月预算' class='formula'></td>   <td><input value='=管理费用_汽车费用本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='10）' value='10）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='业务招待费' value='业务招待费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_业务招待费本月实际/管理费用_业务招待费本月预算' class='formula'></td>   <td><input value='=管理费用_业务招待费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='11）' value='11）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='通讯费' value='通讯费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_通讯费本月实际/管理费用_通讯费本月预算' class='formula'></td>   <td><input value='=管理费用_通讯费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='12）' value='12）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='市内交通费' value='市内交通费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_市内交通费本月实际/管理费用_市内交通费本月预算' class='formula'></td>   <td><input value='=管理费用_市内交通费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='13）' value='13）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='差旅费' value='差旅费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_差旅费本月实际/管理费用_差旅费本月预算' class='formula'></td>   <td><input value='=管理费用_差旅费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='14）' value='14）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='快递费' value='快递费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_快递费本月实际/管理费用_快递费本月预算' class='formula'></td>   <td><input value='=管理费用_快递费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='15）' value='15）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='福利费' value='福利费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_福利费本月实际/管理费用_福利费本月预算' class='formula'></td>   <td><input value='=管理费用_福利费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='16）' value='16）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='工会经费' value='工会经费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_工会经费本月实际/管理费用_工会经费本月预算' class='formula'></td>   <td><input value='=管理费用_工会经费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='17）' value='17）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='低值易耗品' value='低值易耗品' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_低值易耗品本月实际/管理费用_低值易耗品本月预算' class='formula'></td>   <td><input value='=管理费用_低值易耗品本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='18）' value='18）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='房租及水电费' value='房租及水电费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_房租及水电费本月实际/管理费用_房租及水电费本月预算' class='formula'></td>   <td><input value='=管理费用_房租及水电费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='19）' value='19）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='折旧费用' value='折旧费用' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_折旧费用本月实际/管理费用_折旧费用本月预算' class='formula'></td>   <td><input value='=管理费用_折旧费用本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='20）' value='20）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='资产摊销' value='资产摊销' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_资产摊销本月实际/管理费用_资产摊销本月预算' class='formula'></td>   <td><input value='=管理费用_资产摊销本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='21）' value='21）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='管理税费' value='管理税费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_管理税费本月实际/管理费用_管理税费本月预算' class='formula'></td>   <td><input value='=管理费用_管理税费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='22）' value='22）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='审计年检费' value='审计年检费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_审计年检费本月实际/管理费用_审计年检费本月预算' class='formula'></td>   <td><input value='=管理费用_审计年检费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='23）' value='23）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='其他' value='其他' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=管理费用_其他本月实际/管理费用_其他本月预算' class='formula'></td>   <td><input value='=管理费用_其他本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='3、' value='3、' class='title'></td>   <td rowspan='4'><input name='财务费用' value='财务费用' class='title'></td>   <td><input name='合计' value='合计' class='title'></td>   <td><input value='=财务费用_手续费本月实际-财务费用_利息收入本月实际+财务费用_利息支出本月实际' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=财务费用_合计本月实际/财务费用_合计本月预算' class='formula'></td>   <td><input value='=财务费用_合计本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='1）' value='1）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='手续费' value='手续费' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=财务费用_手续费本月实际/财务费用_手续费本月预算' class='formula'></td>   <td><input value='=财务费用_手续费本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='2）' value='2）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='利息收入' value='利息收入' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=财务费用_利息收入本月实际/财务费用_利息收入本月预算' class='formula'></td>   <td><input value='=财务费用_利息收入本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='3）' value='3）' class='title'></td>   <td class='display_none'><input class='input'></td>   <td><input name='利息支出' value='利息支出' class='title'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=财务费用_利息支出本月实际/财务费用_利息支出本月预算' class='formula'></td>   <td><input value='=财务费用_利息支出本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='二、' value='二、' class='title'></td>   <td><input name='营业外净收入' value='营业外净收入' class='title'></td>   <td><input class='input'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=营业外净收入_本月实际/营业外净收入_本月预算' class='formula'></td>   <td><input value='=营业外净收入_本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='三、' value='三、' class='title'></td>   <td><input name='利润总额' value='利润总额' class='title'></td>   <td><input class='input'></td>   <td><input value='=营业外净收入_本月实际-总费用_本月实际' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=利润总额_本月实际/利润总额_本月预算' class='formula'></td>   <td><input value='=利润总额_本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='四、' value='四、' class='title'></td>   <td><input name='所得税' value='所得税' class='title'></td>   <td><input class='input'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=所得税_本月实际/所得税_本月预算' class='formula'></td>   <td><input value='=所得税_本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input name='五、' value='五、' class='title'></td>   <td><input name='净利润' value='净利润' class='title'></td>   <td><input class='input'></td>   <td><input value='=利润总额_本月实际-所得税_本月实际' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=净利润_本月实际/净利润_本月预算' class='formula'></td>   <td><input value='=净利润_本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input class='input'></td>   <td><input name='股票交易利润' value='股票交易利润' class='title'></td>   <td><input class='input'></td>   <td><input class='input'></td>   <td><input class='budget'></td>   <td><input value='=股票交易利润_本月实际/股票交易利润_本月预算' class='formula'></td>   <td><input value='=股票交易利润_本月实际/总费用_本月实际' class='formula'></td>  </tr>  <tr>   <td><input class='input'></td>   <td><input name='净利润合计' value='净利润合计' class='title'></td>   <td><input class='input'></td>   <td><input value='=净利润_本月实际+股票交易利润_本月实际' class='formula'></td>   <td><input class='budget'></td>   <td><input value='=净利润合计_本月实际/净利润合计_本月预算' class='formula'></td>   <td><input value='=净利润合计_本月实际/总费用_本月实际' class='formula'></td>  </tr> </table></body></html>";
		Integer firstRowNum=null;
		Integer secondRowNum=3; 
		Integer firstColNum=1; 
		Integer secondColNum=2;
		/*try {
			Calendar c = Calendar.getInstance();
			long s = c.getTimeInMillis();
			System.out.println("start:"+s);
			
			dataModuleService.editDataModule(reportType,businessType,html,firstRowNum,secondRowNum,firstColNum,secondColNum);
			
			c = Calendar.getInstance();
			long e = c.getTimeInMillis();
			System.out.println("end:"+e);
			System.out.println(s-e);
		} catch (FormulaAnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		HttpClient3 http = new HttpClient3();
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("reportType", reportType);
		params.put("businessType", businessType);
		params.put("html", html);
		params.put("firstRowNum", String.valueOf(firstRowNum));
		params.put("secondRowNum", String.valueOf(secondRowNum));
		params.put("firstColNum", String.valueOf(firstColNum));
		params.put("secondColNum", String.valueOf(secondColNum));
		//params.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MjE0MzU0NTY2OTB9.dBOCTsvN0fo44hg_pZv8y-MfTzYv23Lo7XGVcezs4gA");
		try {
			System.out.println(JSONObject.fromObject(params));
			String res = http.doPost("http://localhost:8080/financialSys/dataModule/editDataModule",params);
			System.out.println(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getDataById(){
		/*Map<Object, Object> map=new HashMap<Object, Object>();
		map.put("dataModuleId", "");*/
		DataModule dataModules=dataModuleService.getDataModule("3a9f5f27581c41158de915ef3cc653bc");
		/*for(DataModule dataModule:dataModules){
			System.out.println(dataModule);
		}
		System.out.println(dataModules.size());*/
		System.out.println(dataModules.getModuleName());
	}
}
