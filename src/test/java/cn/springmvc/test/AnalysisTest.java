package cn.springmvc.test;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.util.HtmlAnalysis;
import cn.financial.util.HtmlGenerate;

public class AnalysisTest {
	
	public static void main(String[] args) throws FormulaAnalysisException {
		AnalysisExcelFormula aef = new AnalysisExcelFormula("D:/Project/fmss/管理报表模板-文字公式版/xxxxxx.xls");
		String json = aef.analysisExcel();
//		System.out.println(json);
		HtmlGenerate hg = new HtmlGenerate();
		String html = hg.generateHtml(json,HtmlGenerate.HTML_TYPE_TEMPLATE);
		System.out.println(html);
		Integer firstRowNum = null;//横向标题前缀
		Integer secondRowNum = 3;//横向标题后缀
		Integer firstColNum = 1;//纵向标题前缀
		Integer secondColNum = 2;//纵向标题后缀
		HtmlAnalysis htmlAnalysis = new HtmlAnalysis(html, firstRowNum, secondRowNum, firstColNum, secondColNum);
		String str = htmlAnalysis.analysis();
		System.out.println(str);
	}

}
