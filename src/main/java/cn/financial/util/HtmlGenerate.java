package cn.financial.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.CharEncoding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 根据Json数据生成Html
 * @author Admin
 *
 */
public class HtmlGenerate {
	
	public HtmlGenerate(){}
	
	/**
	 * 显示页面是否需要在td中添加id属性
	 * @param isPreviewNeedId
	 */
	public HtmlGenerate(boolean isPreviewNeedId){
		this.isPreviewNeedId = isPreviewNeedId;
	}
	
	
	
	private boolean isPreviewNeedId = false;
	
	/**
	 * 预算编辑是否需要禁止输入
	 */
	private boolean isDisableBudgetInput = false;
	
	/**
	 * 预算编辑禁止输入判断
	 */
	public void disableBudgetInput(){
		isDisableBudgetInput = true;
	}
	
	/**
	 * 预算编辑都可以输入
	 */
	public void enableBudgetInput(){
		isDisableBudgetInput = false;
	}
	
	private static String NONE_DISPLAY_CLASS="display_none";
	
	/**
	 * HTML类型:配置模板
	 */
	public static int HTML_TYPE_TEMPLATE = 1;

	/**
	 * HTML类型:录入页面
	 */
	public static int HTML_TYPE_INPUT = 2;

	/**
	 * HTML类型:查看页面
	 */
	public static int HTML_TYPE_PREVIEW = 3;
	/**
	 * 11 输入框类型(label)
	 */
	public static int BOX_TYPE_LABEL = 11;
	/**
	 * 12  输入框类型(模块)
	 */
	public static int BOX_TYPE_MODULE = 12;
	/**
	 * 13 输入框类型(科目)
	 */
	public static int BOX_TYPE_ITEM = 13;
	/**
	 * 14 输入框类型(主标题)
	 */
	public static int BOX_TYPE_MAINTITLE = 14;
	/**
	 * 15 输入框类型(子标题)
	 */
	public static int BOX_TYPE_SUBTITLE = 15;
	/**
	 * 2 输入框类型(录入框)
	 */
	public static int BOX_TYPE_INPUT = 2;
	/**
	 * 3 输入框类型(公式)
	 */
	public static int BOX_TYPE_FORMULA = 3;
	/**
	 * 4 输入框类型(预算)
	 */
	public static int BOX_TYPE_BUDGET = 4;
	
	public static String CLASS_LABEL = "title";// Label所带Class
	public static String CLASS_MODULE = "module";// 模块所带Class
	public static String CLASS_ITEM = "item";// 科目所带Class
	public static String CLASS_MAINTITLE = "maintitle";// 主标题所带Class
	public static String CLASS_SUBTITLE = "subtitle";// 子标题所带Class
	public static String CLASS_INPUT = "input";// Input所带Class
	public static String CLASS_FORMULA = "formula";// 公式所带Class
	public static String CLASS_BUDGET = "budget";// 公式所带Class

	public static void main(String[] args) {
		String path = "C:/Users/Admin/Desktop/解析后文件.txt";
		File file = new File(path );
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String tmp = null;
			while((tmp = reader.readLine())!=null){
				sb.append(tmp);
			}
//			int htmlType = HTML_TYPE_TEMPLATE;
			int htmlType = HTML_TYPE_PREVIEW;
//			int htmlType = HTML_TYPE_INPUT;
			HtmlGenerate hg = new HtmlGenerate();
			String html = hg.generateHtml(sb.toString(),htmlType);
			System.out.println(html);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据Json字符串对象生成Html
	 * @param jsonStr Json字符串
	 * @param htmlType Html类型 (模板/录入/查看) @see {@link cn.financial.util.HtmlGenerate} HTML_TYPE_TEMPLATE/HTML_TYPE_INPUT/HTML_TYPE_VIEW
	 * @return
	 */
	public String generateHtml(String jsonStr,Integer htmlType){
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		return generateHtml(jsonObj, htmlType);
	}
	
	/**
	 * 根据Json对象生成Html
	 * @param jsonObj Json对象 
	 * @param htmlType Html类型 (模板/录入/查看) @see {@link cn.financial.util.HtmlGenerate} HTML_TYPE_TEMPLATE/HTML_TYPE_INPUT/HTML_TYPE_VIEW
	 * @return
	 */
	public String generateHtml(JSONObject jsonObj,Integer htmlType){
		JsonConvertProcess jcp = new JsonConvertProcess();
		Map<Integer,Map<Integer,JSONObject>> trMap = jcp.assembleData(jsonObj);
		trMap = jcp.sortTableRowMap(trMap);
		Document doc = Jsoup.parse("<html> <head></head><style>."+NONE_DISPLAY_CLASS+"{display:none}</style> <body></body></html>");
		doc.outputSettings().charset(CharEncoding.UTF_8).prettyPrint(false);
		Element table = createTable(doc);
		assembleTable(trMap,table,htmlType);
		return doc.html();
	}

	/**
	 * 组装Table
	 * @param trMap
	 * @param table
	 */
	private void assembleTable(
			Map<Integer, Map<Integer, JSONObject>> trMap, Element table,Integer htmlType) {
		for(Iterator<Integer> rowIter = trMap.keySet().iterator();rowIter.hasNext();) {
			Integer rowNum = rowIter.next();
			Map<Integer, JSONObject> row = trMap.get(rowNum);
			Element tr = table.appendElement("tr");
//			Element tmpTd = tr.appendElement("td");
//			tmpTd.text(""+i);
//			Element keyRow = null;
//			if(i==0) {
//				keyRow = table.appendElement("tr");
//			}
			for(Iterator<Integer> colIter = row.keySet().iterator();colIter.hasNext();) {
				Integer colNum = colIter.next();
				JSONObject col = row.get(colNum);
//				int colRowNum = col.getInt("row");
				
//				int rowspan = col.getInt("rowspan");
//				addMergedList(rowspan+"", colRowNum);
				
				boolean display = true;
				if(col.containsKey("display")) {
					display = col.getBoolean("display");
				}
				
				Element td = tr.appendElement("td");
				setTableCellAttr(td,col,display,htmlType,rowNum,colNum);
			}
		}
	}
	

	/**
	 * 设置单元格属性
	 * @param td
	 * @param col
	 */
	private void setTableCellAttr(Element td, JSONObject col,Boolean display,Integer htmlType,Integer rowNum,Integer colNum) {
		int inputType = col.getInteger("type");
		String name = "";
		String key = "";
		if(col.containsKey("name")) {
			name = col.getString("name");
		}
		
		if(col.containsKey("key")) {
			key = col.getString("key");
		}
		
		String formula = col.getString("formulaCN");
		String value = "";
		if(col.containsKey("value")) {
			value = col.getString("value");
		}
		
		if(HtmlAnalysis.isLabel(inputType) || !isValid(key)) {
			key = generateValidKey(rowNum,colNum);
		}
		
		if (htmlType == HTML_TYPE_TEMPLATE) {
			generateTemplateContent(td, inputType,key, name, formula);
		} else if (htmlType == HTML_TYPE_INPUT) {
			
			generateInputContent(td, inputType, key, name, formula, value, display);
		} else if (htmlType == HTML_TYPE_PREVIEW) {
			generatePreviewContent(td, inputType,key, name, value);
		}
		
		
		
		int colspan = col.getInteger("colspan");
		int rowspan = col.getInteger("rowspan");
		if(colspan!=1) {
			td.attr("colspan",""+colspan);
		}
		if(rowspan!=1) {
			td.attr("rowspan",""+rowspan);
		}
		if(!display) {
			td.addClass(NONE_DISPLAY_CLASS);
		}
	}
	
	/**
	 * 预览页面生成
	 * @param td
	 * @param inputType
	 * @param name
	 * @param value
	 */
	private void generatePreviewContent(Element td,int inputType,String key,String name, String value) {
		if (inputType == BOX_TYPE_INPUT || inputType == BOX_TYPE_FORMULA
				|| inputType == BOX_TYPE_BUDGET) {// input / 预算 /公式
			value = StringUtils.formatNumber(value);//格式化数据
			td.text(value);
			if(isPreviewNeedId) {
				td.attr("id",key);
			}
		} else {//显示类控件,包括模块/科目/主标题/子标题等
			td.text(name);
		}
	}

	/**
	 * 录入页面生成
	 * @param td
	 * @param inputType
	 * @param name
	 * @param formula
	 * @param value
	 */
	private void generateInputContent(Element td, int inputType,String key,
			String name, String formula,String value,Boolean display) {
		if(inputType==BOX_TYPE_INPUT && display) {//input
			Element input = td.appendElement("input");
			input.attr("name",key);
			input.attr("value",value);
			input.attr("id",key);
			input.attr("type","number");
			
			boolean disableInput = isDisable(key);
			
			if(disableInput) {
				input.attr("readonly", "true");
			}
			input.addClass(CLASS_INPUT);
		} else if(inputType==BOX_TYPE_FORMULA){//formula
			td.text(value);
		} else if(inputType==BOX_TYPE_BUDGET) {//预算
			td.text(value);
		} else {//显示类控件,包括模块/科目/主标题/子标题等
			td.text(name);
		}
	}
	
	

	private boolean isDisable(String key) {
		if(!isDisableBudgetInput) {//不需要禁用预算编辑,则直接返回flase
			return false;
		}
		String month = JsonConvertProcess.getMonthForKey(key);
		Integer monthInt = null;
		try {
			monthInt = Integer.parseInt(month);
		} catch (Exception e) {
		}
		
		int disableMonth = TimeUtils.getCurrentMonth();
		int today = TimeUtils.getCurrentDayOfMonth();
		if(today < SiteConst.PROFIT_LOSS_GENERATE_DAY) {
			disableMonth = disableMonth - 1;
		}
		return monthInt != null && monthInt <= disableMonth;
	}

	/**
	 * 模板页面生成
	 * @param td
	 * @param inputType
	 * @param name
	 * @param formula
	 */
	private void generateTemplateContent(Element td, int inputType,String key,
			String name, String formula) {
		Element input = td.appendElement("input");
		input.attr("id",key);//添加ID属性,保证前端编辑后可正常赋值并提交后台
		
		if(inputType==BOX_TYPE_INPUT) {//input
//			input.attr("value",col.getString("name"));
			input.addClass(CLASS_INPUT);
		} else if(inputType==BOX_TYPE_FORMULA){//formula
			if(StringUtils.isValid(formula)) {
				input.attr("value","="+formula);
			}
			input.addClass(CLASS_FORMULA);
		} else if(inputType==BOX_TYPE_BUDGET){
			input.addClass(CLASS_BUDGET);
		} else {//显示类单元格
			input.attr("name",name);
			input.attr("value",name);
			
			if(inputType == BOX_TYPE_LABEL) {
				input.addClass(CLASS_LABEL);
			} else if(inputType == BOX_TYPE_MODULE) {
				input.addClass(CLASS_MODULE);
			} else if(inputType == BOX_TYPE_ITEM) {
				input.addClass(CLASS_ITEM);
			} else if(inputType == BOX_TYPE_MAINTITLE) {
				input.addClass(CLASS_MAINTITLE);
			} else if(inputType == BOX_TYPE_SUBTITLE) {
				input.addClass(CLASS_SUBTITLE);
			} else {
				input.addClass(CLASS_LABEL);
			}
		}
	}

	/**
	 * 创建表格
	 * @param doc
	 * @return
	 */
	private Element createTable(Document doc) {
		Element body = doc.select("body").get(0);
		Element table = body.appendElement("table");
		table.attr("border","1");
		table.attr("id","generate_business_table");
		return table;
	}
	
	private String generateValidKey(int rowNum, int colNum) {
		return "row_col_"+rowNum+"_"+colNum;
	}

	private boolean isValid(String key) {
		key = key.replace(HtmlAnalysis.Separate_Modular, "");
		return StringUtils.isValid(key);
	}

	
	static class MapKeyComparator implements Comparator<Integer>{
	    public int compare(Integer value1, Integer value2) {
	        return value1-value2;
	    }
	}
	
//	static List<HtmlMerged> mergedList = new ArrayList<HtmlMerged>();
//	private void addMergedList(String rowspan,Integer firstRowNum) {
//		if(StringUtils.isValid(rowspan)) {
//			HtmlMerged merged = new HtmlMerged(firstRowNum,firstRowNum+Integer.parseInt(rowspan),null);
//			mergedList.add(merged);
//		}
//	}
	
//	class HtmlMerged {
//
//		private Integer firstRowNum;
//		private Integer lastRowNum;
//		private String text;
//
//		public HtmlMerged(Integer firstRowNum, Integer lastRowNum, String text) {
//			this.firstRowNum = firstRowNum;
//			this.lastRowNum = lastRowNum;
//			this.text = text;
//		}
//
//		public Integer getFirstRowNum() {
//			return firstRowNum;
//		}
//
//		public void setFirstRowNum(Integer firstRowNum) {
//			this.firstRowNum = firstRowNum;
//		}
//
//		public Integer getLastRowNum() {
//			return lastRowNum;
//		}
//
//		public void setLastRowNum(Integer lastRowNum) {
//			this.lastRowNum = lastRowNum;
//		}
//
//		public String getText() {
//			return text;
//		}
//
//		public void setText(String text) {
//			this.text = text;
//		}
//
//	}
}
