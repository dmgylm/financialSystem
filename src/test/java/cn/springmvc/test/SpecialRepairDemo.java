package cn.springmvc.test;

import java.io.File;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.util.HtmlAnalysis;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;

public class SpecialRepairDemo {
	
	//1: 解析Excel,得到Json, 用于Html生成
	
/*	public static void main(String[] args) {
		String filepath="C:/Users/ellen/Downloads/数据模板/专车预算.xls";
		Integer modelColNum = 1;
		Integer itemColNum = 2;
		Integer prefixRowNum = null;
		Integer suffixRowNum = 1;
		Integer BUDGET_CELL = 0; //预算
		//Integer BUDGET_CELL = 4;//损益
    	AnalysisExcelFormula aef = new AnalysisExcelFormula(filepath, modelColNum, itemColNum, prefixRowNum, suffixRowNum, BUDGET_CELL);
		String html = aef.analysisExcel();
		System.out.println(html);
	}*/
	
	//2: 用(1)Json去生成Html, 生成完后对Html格式进行修改(主要是隐藏单元格和公式的配置检查)
	
	/*public static void main(String[] args) {
		HtmlGenerate gh = new HtmlGenerate();
		String jsonStr = JsonConvertProcess.readFileContent("C:/Users/ellen/Downloads/数据模板/专车预算的json.txt");
		String html = gh.generateHtml(jsonStr, HtmlGenerate.HTML_TYPE_TEMPLATE);
		System.out.println(html);
	}*/
	
	//3: 用(2)生成的Html进行解析,得到一个Json模板数据
	
	public static void main(String[] args) {
		HtmlAnalysis ha = new HtmlAnalysis(new File("C:/Users/ellen/Downloads/数据模板/专车预算.html"));
		try {
			String json = ha.analysis();
			System.out.println(json);
		} catch (FormulaAnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
