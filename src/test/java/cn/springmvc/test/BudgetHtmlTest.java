package cn.springmvc.test;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.util.HtmlAnalysis;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;

@RunWith(JUnit4.class)
public class BudgetHtmlTest {
	
	@Test
	public void genBudgetTempJson(){
		String path = "D:/Project/fmss/Excel报表/2018年管理报表模板（20180319）-最新/2018年管理报表模板（20180319）-最新/车管家业务预算.xls";
		AnalysisExcelFormula aef = new AnalysisExcelFormula(path );
		String json = aef.analysisExcel();
//		System.out.println(json);
		
		HtmlGenerate hg = new HtmlGenerate();
		String html = hg.generateHtml(json, HtmlGenerate.HTML_TYPE_TEMPLATE);
		System.out.println(html);
		
		HtmlAnalysis ha = new HtmlAnalysis(html, 1, 2, 2, 3);
		try {
			String htmlJson = ha.analysis();
//			System.out.println(htmlJson);
//			String generateHtml = hg.generateHtml(htmlJson, HtmlGenerate.HTML_TYPE_INPUT);
//			System.out.println(generateHtml);
		} catch (FormulaAnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void analysisHtml(){
		HtmlAnalysis ha = new HtmlAnalysis(new File("C:/Users/Admin/Desktop/Html budget.html"), null, 2, 1, 2);
		try {
			String json = ha.analysis();
			System.out.println(json);
//			JSONObject jsonObj = JSONObject.parseObject(json);
			
//			JsonConvertProcess jcp = new JsonConvertProcess();
//			JSONObject budgetJson = jcp.generateMonthlyBudgetJson(jsonObj);//生成预算模板数据
//			
//			JSONObject simplifyJson = JsonConvertProcess.simplifyJson(budgetJson);
//			System.out.println(simplifyJson);
//			System.out.println(budgetJson);
//			System.out.println(json);
//			HtmlGenerate hg = new HtmlGenerate();
//			String html = hg.generateHtml(budgetJson.toJSONString(), HtmlGenerate.HTML_TYPE_TEMPLATE);
//			System.out.println(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
