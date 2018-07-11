package cn.springmvc.test;

import com.alibaba.fastjson.JSONObject;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.util.HtmlAnalysis;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;

public class AnalysisTest {
	
	public static void main(String[] args) throws FormulaAnalysisException {
		AnalysisExcelFormula aef = new AnalysisExcelFormula("D:/Project/fmss/Excel报表/2018年管理报表模板（20180319）-最新/2018年管理报表模板（20180319）-最新/车管家业务损益.xls");
		String json = aef.analysisExcel();
//		System.out.println(json);
		HtmlGenerate hg = new HtmlGenerate();
		String html = hg.generateHtml(json,HtmlGenerate.HTML_TYPE_TEMPLATE);
//		System.out.println(html);
		Integer firstRowNum = null;//横向标题前缀
		Integer secondRowNum = 3;//横向标题后缀
		Integer firstColNum = 1;//纵向标题前缀
		Integer secondColNum = 2;//纵向标题后缀
		HtmlAnalysis htmlAnalysis = new HtmlAnalysis(html, firstRowNum, secondRowNum, firstColNum, secondColNum);
		String str = htmlAnalysis.analysis();
		
//		JSONObject simplifyJson = JsonConvertProcess.simplifyJson(str);
//		System.out.println(simplifyJson);
//		System.out.println(str);
		
		String generateHtml = hg.generateHtml(str, HtmlGenerate.HTML_TYPE_TEMPLATE);
		System.out.println(generateHtml);
	}

}
