package cn.financial.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.exception.FormulaAnalysisException;

/**
 * 解析模板页面Html代码,将其转换成对应的Json格式
 * @author zcp
 *
 */
public class HtmlAnalysis {
	
	private Document doc;
	private static String CLASS_NONE_DISPLAY_NAME = "display_none";
	
	private static String modelRowKey = "";
	private static String prefixCelKey = "";
	private static String Separate_Modular = "_";//模块和科目间的字符间隔
	private static String Separate_X_Y = "";//科目和横向标题间(本月实际)的字符间隔
	
	private Map<String,String> rowKeyMap = new HashMap<String, String>();
	private Map<String,String> colKeyMap = new HashMap<String, String>();
	
	private String REPLACE_REGX = "[：\\%/]";
	
	/**
	 * 构造方法
	 * @param file Html文件
	 * @param prefixRowNum 横向标题前缀
	 * @param suffixCelNum 横向标题后缀
	 * @param modelColNum 模块列号
	 * @param itemColNum 科目称号
	 */
	public HtmlAnalysis(File file) {
		try {
			this.doc = Jsoup.parse(file,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造方法
	 * @param html Html代码字符串
	 * @param prefixCelNum 横向标题前缀
	 * @param suffixCelNum 横向标题后缀
	 * @param modelColNum 模块列号
	 * @param itemColNum 科目称号
	 */
	public HtmlAnalysis(String html) {
		this.doc = Jsoup.parse(html);
	}

	public static void main(String[] args) {
		String filename = "C:/Users/Admin/Desktop/xxxxxfffff.html";
//		String filename = "C:/Users/Admin/Desktop/test.html";
		File file = new File(filename);
		HtmlAnalysis htmlAnalysis = new HtmlAnalysis(file);
		String html = null;
		try {
			html = htmlAnalysis.analysis();
		} catch (FormulaAnalysisException e) {
			e.printStackTrace();
		}
		System.out.println(html);
		
	}
	
	/**
	 * 解析该Html
	 * @return 返回解析后的Json字符串
	 * @throws FormulaAnalysisException 
	 */
	public String analysis() throws FormulaAnalysisException {
		Elements tables = doc.getElementsByTag("table");
		Element table = tables.get(0);
		JSONArray list = analysisTable(table);
		Map<String,List<JSONObject>> dataMap = assembleTableData(list);
		return JSONObject.toJSON(dataMap).toString();
	}
	
	/**
	 * 根据JsonArray转换成模块化的Json格式
	 * 并在此步骤替换其真实公式
	 * @param array 要组装的Json数组
	 * @return
	 * @throws FormulaAnalysisException 
	 */
	private Map<String,List<JSONObject>> assembleTableData(JSONArray array) throws FormulaAnalysisException {
		Map<String,List<JSONObject>> dataMap = new HashMap<String, List<JSONObject>>();
		for(int i=0;i<array.size();i++) {
			JSONObject json = array.getJSONObject(i);
			String key = json.getString("key");
			String reallyFormula = FormulaUtil.getReallyFormulaByKey(array,key);
			json.put("reallyFormula", reallyFormula);
			if(StringUtils.isValid(key) && key.indexOf(Separate_Modular)>0) {
				String modelName = key.split(Separate_Modular)[0];
				List<JSONObject> subjectlst = dataMap.get(modelName);
				if(subjectlst == null) {
					subjectlst = new ArrayList<JSONObject>();
					dataMap.put(modelName, subjectlst);
				}
				subjectlst.add(json);
			} else {//将标题加入
				List<JSONObject> subjectlst = dataMap.get("title");
				if(subjectlst == null) {
					subjectlst = new ArrayList<JSONObject>();
					dataMap.put("title", subjectlst);
				}
				subjectlst.add(json);
			}
		}
		
		return dataMap;
	}

	/**
	 * 解析前端Table中的数据
	 * @param table 要解析的Table
	 * @return
	 */
	private JSONArray analysisTable(Element table) {
		JSONArray list = new JSONArray();
		Elements trs = table.select("tr");
		for(int rowNum=0;rowNum<trs.size();rowNum++) {
			Element tr = trs.get(rowNum);
			Elements tds = tr.select("td");
			for(int colNum=0;colNum<tds.size();colNum++){
				Element td = tds.get(colNum);
				
				boolean tdDisplay = td.hasClass(CLASS_NONE_DISPLAY_NAME);
				String rowspan = td.attr("rowspan");
				String colspan = td.attr("colspan");
		
				Elements inputs = td.select("input");
				String inputValue = null;
				Integer inputboxType = HtmlGenerate.BOX_TYPE_LABEL;
				Set<String> inputClassSet = null;//该控件所有类名
				if(inputs!=null && inputs.size()>0) {
					Element input = inputs.get(0);
					inputValue = input.attr("value");
					inputClassSet = input.classNames();
					if(input.hasClass(HtmlGenerate.CLASS_FORMULA)) {//公式
						inputboxType = HtmlGenerate.BOX_TYPE_FORMULA;
					} else if(input.hasClass(HtmlGenerate.CLASS_INPUT)) {//输入框
						inputboxType = HtmlGenerate.BOX_TYPE_INPUT;
					} else if(input.hasClass(HtmlGenerate.CLASS_BUDGET)) {//预算
						inputboxType = HtmlGenerate.BOX_TYPE_BUDGET;
					} else if(input.hasClass(HtmlGenerate.CLASS_MODULE)) {//模块
						inputboxType = HtmlGenerate.BOX_TYPE_MODULE;
					} else if(input.hasClass(HtmlGenerate.CLASS_ITEM)) {//科目
						inputboxType = HtmlGenerate.BOX_TYPE_ITEM;
					} else if(input.hasClass(HtmlGenerate.CLASS_MAINTITLE)) {//主标题
						inputboxType = HtmlGenerate.BOX_TYPE_MAINTITLE;
					} else if(input.hasClass(HtmlGenerate.CLASS_SUBTITLE)) {//子标题
						inputboxType = HtmlGenerate.BOX_TYPE_SUBTITLE;
					} else if(input.hasClass(HtmlGenerate.CLASS_LABEL)) {//普通Label
						inputboxType = HtmlGenerate.BOX_TYPE_LABEL;
					}
				}
				addRowAndColKey(inputValue, inputClassSet,rowNum, colNum);
				
				JSONObject tdJson = setJsonData(inputboxType,inputValue,rowNum,colNum,rowspan,colspan,!tdDisplay);
				if(tdJson!=null) {
					list.add(tdJson);
				}
			}
		}
		return list;
	}

	/**
	 * 组装单个Json
	 * @param inputboxType 控件类型 
	 * 			BOX_TYPE_LABEL = 1;//输入框类型(label)
	 * 			BOX_TYPE_INPUT = 2;//输入框类型(录入框)
	 * 			BOX_TYPE_FORMULA = 3;//输入框类型(公式)
	 * @param inputValue 控件的值(Label值或公式)
	 * @param rowNum 该控件所在的行:从0开始
	 * @param colNum 该控件所在的列:从0开始
	 * @param rowspan 该控件的rowspan
	 * @param colspan 该控件的colspan
	 * @return
	 */
	private JSONObject setJsonData(Integer inputboxType, String inputValue,
			int rowNum, int colNum,String rowspan,String colspan,boolean tdDisplay) {
		JSONObject json = new JSONObject();
		String formula = "";
		String formulaCN = "";
		String name = "";
		String rowKey = rowKeyMap.get(rowNum+"");
		String colKey = colKeyMap.get(colNum+"");
		if(!StringUtils.isValid(rowKey)) {
			rowKey = "";
		}
		if(!StringUtils.isValid(colKey)) {
			colKey = "";
		}
		
//		if("管理费用".equals(inputValue)) {
//			System.out.println(222);
//		}
		if(inputboxType==HtmlGenerate.BOX_TYPE_FORMULA) {
			if(inputValue.startsWith("=")) {
				inputValue = inputValue.substring(1);
			}
			formulaCN = inputValue;
			formula = HanyuToPingyinUtils.hanyuToPinyin(inputValue);
		} else if(inputboxType==HtmlGenerate.BOX_TYPE_LABEL) {
//			if(!StringUtils.isValid(inputValue)) {
//				return null;
//			}
			name = inputValue;
		}
		
		String key = rowKey + Separate_X_Y + colKey;
//		if(StringUtils.isValid(rowKey) && StringUtils.isValid(colKey)) {
//			name = key;
//		}
		if(!StringUtils.isValid(name)) {
			name = key;
		}
		key = removeSpecialChar(key);
		json.put("key", HanyuToPingyinUtils.hanyuToPinyin(key));
		
		formula = removeSpecialChar(formula);
		json.put("formula", formula);
		json.put("formulaCN", formulaCN);
		json.put("row", rowNum);
		json.put("col", colNum);
		json.put("name", name);
		json.put("display", tdDisplay);
		json.put("rowspan", StringUtils.isValid(rowspan)?rowspan:1);
		json.put("colspan", StringUtils.isValid(colspan)?colspan:1);
		json.put("type", inputboxType);
		return json;
	}

	/**
	 * 替换value中的非法字符  此处为:和%
	 * @param value 要替换的字符串
	 * @return
	 */
	private String removeSpecialChar(String value) {
		if(value!=null) {
			value = value.replaceAll(REPLACE_REGX,"" );
		}
		return value;
	}
	
	/**
	 * 添加行和列标题到全局变量中
	 * @param cellValue
	 * @param rowNum
	 * @param colNum
	 */
	private void addRowAndColKey(String cellValue,Set<String> classSet, int rowNum,int colNum) {
		if(cellValue==null) {
			cellValue = "";
		}
		cellValue = cellValue.trim();
		if(classSet.contains(HtmlGenerate.CLASS_MODULE) && StringUtils.isValid(cellValue)) {
			modelRowKey = cellValue;
		}
		if(classSet.contains(HtmlGenerate.CLASS_ITEM)) {
			String key = modelRowKey + Separate_Modular + cellValue;
			rowKeyMap.put(rowNum+"", key);
		}
		if(classSet.contains(HtmlGenerate.CLASS_MAINTITLE)) {
			prefixCelKey = cellValue;
		}
		if(classSet.contains(HtmlGenerate.CLASS_SUBTITLE)) {
			String key = prefixCelKey + cellValue;
			colKeyMap.put(colNum+"", key);
		}
	}
	
}
