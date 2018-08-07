package cn.springmvc.test;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.util.HtmlAnalysis;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;

public class AnalysisDemo {
	
	//1: 解析Excel,得到Json, 用于Html生成
	
//	public static void main(String[] args) {
//		String filepath = "D:/Project/fmss/Excel报表/2018年管理报表模板（20180319）-最新/2018年管理报表模板（20180319）-最新/维修业务损益.xls";
//		Integer modelColNum = 1;
//		Integer itemColNum = 2;
//		Integer prefixRowNum = null;
//		Integer suffixRowNum = 3;
//		Integer BUDGET_CELL = 4;
//		AnalysisExcelFormula aef = new AnalysisExcelFormula(filepath, modelColNum, itemColNum, prefixRowNum, suffixRowNum, BUDGET_CELL);
//		String html = aef.analysisExcel();
//		System.out.println(html);
//	}
	
	//2: 用(1)Json去生成Html, 生成完后对Html格式进行修改(主要是隐藏单元格和公式的配置检查)
	
//	public static void main(String[] args) {
//		HtmlGenerate gh = new HtmlGenerate();
//		String jsonStr = JsonConvertProcess.readFileContent("C:/Users/Admin/Desktop/wxsy.txt");
//		String html = gh.generateHtml(jsonStr, HtmlGenerate.HTML_TYPE_TEMPLATE);
//		System.out.println(html);
//	}
	
	//3: 用(2)生成的Html进行解析,得到一个Json模板数据
	
	public static void main(String[] args) {
		HtmlAnalysis ha = new HtmlAnalysis(new File("C:/Users/Admin/Desktop/wxsy.html"));
		try {
			String json = ha.analysis();
			System.out.println(json);
		} catch (FormulaAnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
