package cn.springmvc.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSONObject;

import cn.financial.util.FormulaUtil;
import cn.financial.util.HanyuToPingyinUtils;
import cn.financial.util.StringUtils;

/**
 * 解析Excel模板
 * @author zcp
 *
 */
public class AnalysisExcelFormula {
	
	private File file;
	
	private HSSFWorkbook wb;
	
	private Map<String, Merged1> mergedMap;//该Sheet中合并的单元格集合
	
	private Map<String,String> rowKeyMap = new HashMap<String, String>();//行标题
	
	private Map<String,String> colKeyMap = new HashMap<String, String>();//列标题
	
	private Map<String,List<Map<String,Object>>> jm = new HashMap<String, List<Map<String,Object>>>();
	
	private static String Separate_Modular = "_";//模块和科目间的间隔字符串
	private static String Separate_X_Y = "";//科目和列标题(本月实际)间的间隔字符串

	private static String firstRowKey = "";//行标题前缀
	private static String firstCelKey = "";//列标题前缀
	
	private static int BOX_TYPE_LABEL = 1;//输入框类型(label)
	private static int BOX_TYPE_INPUT = 2;//输入框类型(录入框)
	private static int BOX_TYPE_FORMULA = 3;//输入框类型(公式)
	private static int BOX_TYPE_BUDGET = 4;//输入框类型(预算)
	
	private static int BUDGET_CELL = 0;//预算列
	
	/**
	 * 构造方法,读取指定路径的文件到HSSFWorkbook中
	 * @param filepath
	 */
	public AnalysisExcelFormula(String filepath) {
		this.file = new File(filepath);
		try {
			this.wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String filepath = "D:/Project/fmss/管理报表模板-文字公式版/xxxxxx.xls";
		AnalysisExcelFormula aef = new AnalysisExcelFormula(filepath);
		String json = aef.analysisExcel();
		System.out.println(json);
		
	}
	
	/**
	 * 将字符串转为Ascii码,以逗号分隔,主要用于将Excel中的如D5转化为3_4
	 * @param value
	 * @return
	 */
	public static String stringToAscii(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i != chars.length - 1) {
				sbu.append((int) chars[i]).append(",");
			} else {
				sbu.append((int) chars[i]);
			}
		}
		return sbu.toString();
	}
	
	/**
	 * 解析该Excel
	 * @return
	 */
	public String analysisExcel() {
		Map<String,JSONObject> dataMap = new HashMap<String, JSONObject>();
		HSSFSheet sheet = wb.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		
		mergedMap = getMergedMap(sheet);
		
		for(int i=0;i<lastRowNum;i++) {
			HSSFRow row = sheet.getRow(i);
			if(row==null) {
				continue;
			}
			short lastCellNum = row.getLastCellNum();
			for(int j=0;j<lastCellNum;j++) {
				HSSFCell cell = row.getCell(j);
				JSONObject json = new JSONObject();
				String cellValue = null;
				String cellFormula = null;
				if(cell!=null) {
					int cellType = cell.getCellType();
					if(cellType==HSSFCell.CELL_TYPE_NUMERIC) {
						cellValue = String.valueOf(cell.getNumericCellValue());
					} else if(cellType==HSSFCell.CELL_TYPE_FORMULA) {
						cellFormula = cell.getCellFormula();
					} else if(cellType==HSSFCell.CELL_TYPE_STRING) {
						cellValue = cell.getStringCellValue();
//					} else if(cellType==HSSFCell.CELL_TYPE_BLANK) {//空白单元格
//						continue;
					}
					int type = BOX_TYPE_LABEL;
					if(cellValue!=null && !cellValue.trim().equals("")) {
						type = BOX_TYPE_LABEL;
					} else if(cellValue == null && cellFormula==null) {
						type = BOX_TYPE_INPUT;
					} else if(cellFormula!=null) {
						type = BOX_TYPE_FORMULA;
					}
					if((type==BOX_TYPE_FORMULA || type==BOX_TYPE_INPUT) && j==BUDGET_CELL && BUDGET_CELL != 0) {
						type = BOX_TYPE_BUDGET;
					}
					
					addRowAndColKey(cellValue,j,i);
					
					if(cellValue!=null) {
						cellValue = cellValue.trim();
					}
					
					Merged1 merged = getCellSpan(j , i);
					
					json.put("row", i);
					json.put("col", j);
					json.put("rowspan", merged.getRowspan());
					json.put("colspan", merged.getColspan());
					json.put("name", cellValue);
					json.put("formula", cellFormula);
					
					json.put("type", type);
					dataMap.put(j+"_"+i, json);
				}
			}
		}
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(Iterator<String> iter = dataMap.keySet().iterator();iter.hasNext();) {
			String k = iter.next();
			JSONObject json = dataMap.get(k);
			int row = json.getInteger("row");
			int col = json.getInteger("col");
			String name = null;
			if(json.containsKey("name")) {
				name = json.getString("name");
			}
			
			String key = getKey(row, col);
			if(!StringUtils.isValid(name) && key!=null) {
//				key = key.replaceAll(reKey, "_");
				json.put("name", key);
				json.put("key", HanyuToPingyinUtils.hanyuToPinyin(key));
			}
			
			String tmpkey = null;
			String formula = null;
			
			if(json.containsKey("key")) {
				tmpkey = json.getString("key");
			}
			if(json.containsKey("formula")) {
				formula = json.getString("formula");
			}
			boolean display = false;
			if(StringUtils.isValid(name) || StringUtils.isValid(tmpkey)  || StringUtils.isValid(formula) ) {
				display = true;
				list.add(json);
			}
			
			json.put("display", display);
			replaceFormula(json);
			assembleFinalJson(json);
		}
		return JSONObject.toJSON(jm).toString();
	}
	
	/**
	 * 暂时不用
	 * @param json
	 */
	private void assembleFinalJson(JSONObject json){
		if(!json.containsKey("key") || json.getString("key").indexOf(".")<=0) {
			List<Map<String, Object>> jmlst = jm.get("title");
			if(jmlst==null) {
				jmlst = new ArrayList<Map<String,Object>>();
				jm.put("title", jmlst);
			}
			jmlst.add(json);
			return;
		}
		String key = json.getString("key");
		if(key.indexOf(".")>0) {
			String modelKey = key.split("\\.")[0];
			List<Map<String, Object>> jmlst = jm.get(modelKey);
			if(jmlst==null) {
				jmlst = new ArrayList<Map<String,Object>>();
				jm.put(modelKey, jmlst);
			}
			jmlst.add(json);
			jm.put(modelKey,jmlst);
		}
	}
	
	/**
	 * 根据行与列获取该单元格对应的Key
	 * @param row
	 * @param col
	 * @return
	 */
	private String getKey(int row,int col){
		String rowKey = rowKeyMap.get(row+"");
		String colKey = colKeyMap.get(col+"");
		if(StringUtils.isValid(rowKey) && StringUtils.isValid(colKey)) {
			return rowKey+Separate_X_Y+colKey;
		}
		return null;
	}
	
	/**
	 * 将Excel中的公式替换为中文(该中该以key对应)
	 * @param json
	 */
	private void replaceFormula(JSONObject json) {
		if(!json.containsKey("formula")) {
			json.put("formula", "");
			return;
		}
		String formula = json.getString("formula");
		if(!StringUtils.isValid(formula)) {
			json.put("formula", "");
			return;
		}
		if (formula.indexOf("$") > -1) {//将Excel公式中的$D$5替换成D5
			formula = formula.replace("$", "");
		}
		String[] attrs = FormulaUtil.splitFormula(formula);
		for(String attr:attrs) {
			if (attr.equals("SUM")) {
				continue;
			}
			String colId = attr.substring(0,1);
			String rowId = attr.substring(1);
			Integer tmpColId = Integer.parseInt(stringToAscii(colId))-65;
			Integer tmpRowId = null;
			try {
				tmpRowId = Integer.parseInt(rowId)-1;
			} catch (Exception e) {
			}
			String key = null;
			if(tmpRowId != null) {
				key = getKey(tmpRowId, tmpColId);
			}
			if(key!=null) {
				formula = FormulaUtil.replaceFirst(formula,attr,key);
			}
		}
//		formula = formula.replace(reKey, "_");
		json.put("formula", formula);
	}
	
	/**
	 * 添加行与列标题到全局变量
	 * @param cellValue
	 * @param j
	 * @param i
	 */
	private void addRowAndColKey(String cellValue, int j, int i) {
		if(cellValue==null) {
			cellValue = "";
		}
		cellValue = cellValue.trim();
		if(j==1 && StringUtils.isValid(cellValue)) {
			firstRowKey = cellValue;
		}
		if(j==2) {
			String key = firstRowKey+Separate_Modular+cellValue;
			rowKeyMap.put(i+"", key);
		}
//		if(i==2) {
//			firstCelKey = cellValue;
//		}
		if(i==3) {
			String key = firstCelKey + cellValue;
			colKeyMap.put(j+"", key);
		}
	}

	/**
	 * 根据行和列获取合并的单元格
	 * @param colNum
	 * @param rowNum
	 * @return
	 */
	private Merged1 getCellSpan(int colNum, int rowNum) {
		Merged1 merged = mergedMap.get(colNum + "_" + rowNum);
		if (merged == null) {
			merged = new Merged1(1, 1);
		}
		return merged;
	}

	/**
	 * 获取该Sheet中的合并单元格集合
	 * @param sheet
	 * @return
	 */
	public Map<String, Merged1> getMergedMap(HSSFSheet sheet) {
		int mergedCount = sheet.getNumMergedRegions();
		Map<String, Merged1> mergedMap = new HashMap<String, Merged1>();
		for (int i = 0; i < mergedCount; i++) {
			CellRangeAddress add = sheet.getMergedRegion(i);
			int firstRow = add.getFirstRow();
			int firstColumn = add.getFirstColumn();
			int colspan = add.getLastColumn() - firstColumn +1;
			int rowspan = add.getLastRow() - firstRow +1;
			Merged1 m = new Merged1(colspan, rowspan);
			mergedMap.put(firstColumn + "_" + firstRow, m);
		}
		return mergedMap;
	}

}
