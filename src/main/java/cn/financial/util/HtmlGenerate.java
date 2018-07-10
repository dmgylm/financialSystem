package cn.financial.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
	
	public static int BOX_TYPE_LABEL = 1;//输入框类型(label)
	public static int BOX_TYPE_INPUT = 2;//输入框类型(录入框)
	public static int BOX_TYPE_FORMULA = 3;//输入框类型(公式)
	public static int BOX_TYPE_BUDGET = 4;//输入框类型(预算)
	
	public static String CLASS_LABEL="title";//Label所带Class
	public static String CLASS_INPUT="input";//Input所带Class
	public static String CLASS_FORMULA="formula";//公式所带Class
	public static String CLASS_BUDGET="budget";//公式所带Class

	private static Map<Integer, Map<Integer, JSONObject>> rowMap = new HashMap<Integer, Map<Integer,JSONObject>>();

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
		Map<Integer,Map<Integer,JSONObject>> trMap = assembleData(jsonObj);
		trMap = sortTableRowMap(trMap);
		Document doc = Jsoup.parse("<html> <head></head><style>."+NONE_DISPLAY_CLASS+"{display:none}</style> <body></body></html>");
		doc.outputSettings().charset(CharEncoding.UTF_8).prettyPrint(false);
		Element table = createTable(doc);
		assembleTable(trMap,table,htmlType);
		return doc.html();
	}

	/**
	 * 将该Map通过Key进行排序
	 * @param map
	 * @return
	 */
	private Map<Integer, Map<Integer, JSONObject>> sortTableRowMap(
			Map<Integer, Map<Integer, JSONObject>> map) {
		Map<Integer, Map<Integer, JSONObject>> sortMap = new TreeMap<Integer, Map<Integer, JSONObject>>(
				new MapKeyComparator());

		sortMap.putAll(map);
		return sortMap;
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
				setTableCellAttr(td,col,display,htmlType);
			}
		}
	}
	

	/**
	 * 设置单元格属性
	 * @param td
	 * @param col
	 */
	private void setTableCellAttr(Element td, JSONObject col,Boolean display,Integer htmlType) {
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
		
		if (htmlType == HTML_TYPE_TEMPLATE) {
			generateTemplateContent(td, inputType, name, formula);
		} else if (htmlType == HTML_TYPE_INPUT) {
			generateInputContent(td, inputType, key,name, formula, value);
		} else if (htmlType == HTML_TYPE_PREVIEW) {
			generatePreviewContent(td, inputType, name, value);
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
	private void generatePreviewContent(Element td,int inputType,String name, String value) {
		if (inputType == BOX_TYPE_LABEL) {// Label
			td.text(name);
		} else if (inputType == BOX_TYPE_INPUT) {// input
			td.text(value);
		} else if (inputType == BOX_TYPE_FORMULA) {// formula
			td.text(value);
		} else if (inputType == BOX_TYPE_BUDGET) {// 预算
			td.text(value);
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
			String name, String formula,String value) {
		if(inputType==BOX_TYPE_LABEL) {//Label
			td.text(name);
		} else if(inputType==BOX_TYPE_INPUT) {//input
			Element input = td.appendElement("input");
			input.attr("name",key);
			input.attr("value",value);
			input.addClass("input");
		} else if(inputType==BOX_TYPE_FORMULA){//formula
			td.text(value);
		} else if(inputType==BOX_TYPE_BUDGET) {//预算
			td.text(value);
		}
	}

	/**
	 * 模板页面生成
	 * @param td
	 * @param inputType
	 * @param name
	 * @param formula
	 */
	private void generateTemplateContent(Element td, int inputType,
			String name, String formula) {
		Element input = td.appendElement("input");
		if(inputType==BOX_TYPE_LABEL) {//Label
			input.attr("name",name);
			input.attr("value",name);
			input.addClass(CLASS_LABEL);
		} else if(inputType==BOX_TYPE_INPUT) {//input
//			input.attr("value",col.getString("name"));
			input.addClass(CLASS_INPUT);
		} else if(inputType==BOX_TYPE_FORMULA){//formula
			if(StringUtils.isValid(formula)) {
				input.attr("value","="+formula);
			}
			input.addClass(CLASS_FORMULA);
		} else if(inputType==BOX_TYPE_BUDGET){
			input.addClass(CLASS_BUDGET);
		}
	}

	/**
	 * 生成行数据
	 * @param array
	 * @return
	 */
	private Map<Integer, Map<Integer, JSONObject>> assembleData(
			JSONObject array) {
		for(Iterator<String> iter = array.keySet().iterator();iter.hasNext();) {
			String key = iter.next();
			Object obj = array.get(key);
			if(obj instanceof JSONArray) {
				JSONArray ja = (JSONArray)obj;
				for(int i=0;i<ja.size();i++) {
					putRowToMap(rowMap,ja.getJSONObject(i));
				}
			} else if(obj instanceof JSONObject) {
				assembleData((JSONObject) obj);
			}
		}
		
		return rowMap;
	}


	/**
	 * 
	 * @param trMap
	 * @param obj
	 */
	private void putRowToMap(Map<Integer, Map<Integer, JSONObject>> trMap,
			JSONObject obj) {
		if(!obj.containsKey("row")) {
			return;
		}
		Integer rowNum = obj.getInteger("row");
		Integer colNum = obj.getInteger("col");
		Map<Integer, JSONObject> row = trMap.get(rowNum);
		if(row==null) {
			row = new HashMap<Integer, JSONObject>();
			trMap.put(rowNum, row);
		}
		row.put(colNum, obj);
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
		return table;
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
