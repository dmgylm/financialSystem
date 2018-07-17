package cn.springmvc.test;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import cn.financial.util.HttpClient3;
import cn.financial.util.JsonConvertProcess;

@RunWith(JUnit4.class)
public class BudgetHtmlTest {
	
	@Test
	public void genBudgetTempJson(){
		String path = "D:/Project/fmss/Excel报表/2018年管理报表模板（20180319）-最新/2018年管理报表模板（20180319）-最新/车管家业务预算.xls";
		Integer prefixRowNum = null;//横向标题前缀
		 Integer suffixRowNum = 2;//横向标题后缀
		 Integer modelColNum = 1;//纵向标题前缀
		 Integer itemColNum = 2;//纵向标题后缀
		 Integer BUDGET_CELL = 0;
		AnalysisExcelFormula aef = new AnalysisExcelFormula(path, modelColNum, itemColNum, prefixRowNum, suffixRowNum, BUDGET_CELL);
		String json = aef.analysisExcel();
//		System.out.println(json);
		
		HtmlGenerate hg = new HtmlGenerate();
		String html = hg.generateHtml(json, HtmlGenerate.HTML_TYPE_TEMPLATE);
		System.out.println(html);
		
		HtmlAnalysis4Excel ha = new HtmlAnalysis4Excel(html, 1, 2, 2, 3);
		try {
//			String htmlJson = ha.analysis();
//			System.out.println(htmlJson);
//			String generateHtml = hg.generateHtml(htmlJson, HtmlGenerate.HTML_TYPE_INPUT);
//			System.out.println(generateHtml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void analysisBudgetHtml(){
		HtmlAnalysis ha = new HtmlAnalysis(new File("C:/Users/Admin/Desktop/Html budget.html"));
		try {
			String json = ha.analysis();
//			System.out.println(json);
			JSONObject jsonObj = JSONObject.parseObject(json);
			
			JsonConvertProcess jcp = new JsonConvertProcess();
			JSONObject budgetJson = jcp.generateMonthlyBudgetJson(jsonObj);//生成预算模板数据
			System.out.println(budgetJson);
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
	
	@Test
	public void analysisHtml(){
		HtmlAnalysis ha = new HtmlAnalysis(new File("C:/Users/Admin/Desktop/PROFIT_LOSS.html"));
//		HtmlAnalysis ha = new HtmlAnalysis(new File("C:/Users/Admin/Desktop/PROFIT_LOSS.html"), 1, 2, 2);
		try {
			String json = ha.analysis();
			System.out.println(json);
			JSONObject jsonObj = JSONObject.parseObject(json);
			
//			JsonConvertProcess jcp = new JsonConvertProcess();
//			JSONObject budgetJson = jcp.generateMonthlyBudgetJson(jsonObj);//生成预算模板数据
//			System.out.println(budgetJson);
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
