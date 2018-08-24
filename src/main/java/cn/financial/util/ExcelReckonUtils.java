package cn.financial.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.model.DataModule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExcelReckonUtils {
	
	private Map<String,String> keyMap ;
	
	private String MODULE_KEY_SPLIT = "★";
	
	private HSSFFormulaEvaluator evaluator ;
	
	private HSSFSheet sheet ;
	
	/**
	 * 通过Excel计算公式的值
	 * @param json 需要计算的Json数据(模板+数据格式)
	 * @return 计算完成的Json
	 * @throws FormulaAnalysisException 异常:公式找不到对应值
	 */
	public String computeByExcel(String json) throws FormulaAnalysisException{
		JSONObject jsonObj = JSONObject.parseObject(json);
		//生成Excel
		JsonConvertProcess jcp = new JsonConvertProcess();
		Map<Integer, Map<Integer, JSONObject>> trMap = jcp.assembleData(jsonObj);
		trMap = jcp.sortTableRowMap(trMap);//排序行和列
		HSSFWorkbook wb = generateExcelByFormula(trMap);
		evaluator = wb.getCreationHelper().createFormulaEvaluator();
		sheet = wb.getSheetAt(0);
		jsonObj = computeExcelToJson(jsonObj);
//		Map<String,List<JSONObject>> dataMap = JsonConvertProcess.assembleTableData(list);
		return jsonObj.toString();
	}
	
	private JSONObject computeExcelToJson(JSONObject jsonObj) {
		JSONObject json = new JSONObject();
		for(Iterator<String> iter = jsonObj.keySet().iterator();iter.hasNext();) {
			String key = iter.next();
			Object obj = jsonObj.get(key);
			if (obj instanceof JSONArray) {
				JSONArray arr = (JSONArray) obj;
				JSONArray array = new JSONArray();
				for(int i=0;i<arr.size();i++) {
					JSONObject jsonO = arr.getJSONObject(i);
					jsonO = computeCellValue(jsonO);
					array.add(jsonO);
				}
				json.put(key, arr);
			} else {
				JSONObject jsonO = (JSONObject) obj;
				jsonO = computeCellValue(jsonO);
				json.put(key, jsonO);
			}
		}
		return json;
	}
	
	
	private JSONObject computeCellValue(JSONObject jsonO) {
		if(jsonO.containsKey("row") && jsonO.containsKey("col") && jsonO.containsKey("type")) {
			Integer rowNum = jsonO.getInteger("row");
			Integer celNum = jsonO.getInteger("col");
			Integer type = jsonO.getInteger("type");
			if(type == HtmlGenerate.BOX_TYPE_FORMULA) {
				HSSFRow row = sheet.getRow(rowNum);
				HSSFCell cell = row.getCell(celNum);
				Double value = evaluator.evaluate(cell).getNumberValue();
				jsonO.put("value", value);
			}
		} else {
			jsonO = computeExcelToJson(jsonO);
		}
		return jsonO;
	}

	public String computeByExcelToBusin(String json,JSONObject datamodul,String reportType) throws FormulaAnalysisException{
		JSONObject jsonObj = JSONObject.parseObject(json);
		ExcelReckonUtils ert = new ExcelReckonUtils();
		//生成Excel
		JsonConvertProcess jcp = new JsonConvertProcess();
		Map<Integer, Map<Integer, JSONObject>> trMap = jcp.assembleData(jsonObj);
		trMap = jcp.sortTableRowMap(trMap);//排序行和列
		HSSFWorkbook wb = ert.generateExcelByFormula(trMap);
		
		JSONArray list = ert.computeExcelToJson(trMap,wb);
		Map<String,List<JSONObject>> dataMap = JsonConvertProcess.assembleTableData(list);
		if(reportType.equals(DataModule.REPORT_TYPE_PROFIT_LOSS)) {//损益
			return JSONObject.toJSON(dataMap).toString();
		}else if(reportType.equals(DataModule.REPORT_TYPE_BUDGET)) {//预算
			return jcp.merge(datamodul,dataMap).toString();
		}
			return null;
	
	}
	public JSONArray computeExcelToJson(Map<Integer, Map<Integer, JSONObject>> trMap, HSSFWorkbook wb) {
		HSSFSheet sheet = wb.getSheetAt(0);
		JSONArray array = new JSONArray();
		HSSFFormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		for(Iterator<Integer> iter = trMap.keySet().iterator();iter.hasNext();) {
			Integer rowNum = iter.next();
			Map<Integer, JSONObject> celMap = trMap.get(rowNum);
			HSSFRow row = sheet.getRow(rowNum);
			for(Iterator<Integer> celIterator = celMap.keySet().iterator();celIterator.hasNext();) {
				Integer celNum = celIterator.next();
				JSONObject obj = celMap.get(celNum);
				Integer type = obj.getInteger("type");
				if(type==HtmlGenerate.BOX_TYPE_FORMULA) {
					HSSFCell cell = row.getCell(celNum);
					Double value = evaluator.evaluate(cell).getNumberValue();
					obj.put("value", value);
				}
				array.add(obj);
			}
		}
		return array;
	}

	public HSSFWorkbook generateExcelByFormula(Map<Integer, Map<Integer, JSONObject>> trMap) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		
		
		keyMap = convertKeyTo(trMap);
		
		for(Iterator<Integer> iter = trMap.keySet().iterator();iter.hasNext();) {
			Integer rowNum = iter.next();
			Map<Integer, JSONObject> celMap = trMap.get(rowNum);
			HSSFRow row = sheet.createRow(rowNum);
			for(Iterator<Integer> celIterator = celMap.keySet().iterator();celIterator.hasNext();) {
				Integer celNum = celIterator.next();
				JSONObject obj = celMap.get(celNum);
				@SuppressWarnings("unused")
				String key = obj.getString("key");
				Integer type = obj.getInteger("type");
				Integer rowspan = obj.getInteger("rowspan");
				Integer colspan = obj.getInteger("colspan");
				if(rowspan!=1 || colspan!=1) {
					@SuppressWarnings("deprecation")
					CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum+rowspan-1, celNum, celNum+colspan-1);
					sheet.addMergedRegion(cellRangeAddress);
				}
				HSSFCell cell = row.createCell(celNum);
//				Double value = 0D;
				Double value = rowNum.doubleValue();
				if(type == HtmlGenerate.BOX_TYPE_INPUT ||type == HtmlGenerate.BOX_TYPE_BUDGET) {
					if(obj.containsKey("value")) {
						value = obj.getDoubleValue("value");
					}
					cell.setCellValue(value);
				} else if(type == HtmlGenerate.BOX_TYPE_FORMULA){
						if(obj.containsKey("value")) {
							value = obj.getDoubleValue("value");
						}
						cell.setCellValue(value);
						String formula = obj.getString("formula");
						if(!StringUtils.isValid(formula)) {
							continue;
						}
						String excelFormula = replaceFormulaToStr(formula,value);
						cell.setCellType(Cell.CELL_TYPE_FORMULA);
						cell.setCellFormula(excelFormula);
				} else {
					cell.setCellValue(obj.getString("name"));//显示类直接展示Name属性值
				}
				
				
			}
		}
		return wb;
	}
	
	private boolean isDouble(String value){
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException  e) {
			return false;
		}
	}

	private String replaceFormulaToStr(String formula,Double oldValue) {
		String[] attrs = FormulaUtil.splitFormula(formula);
		for(int i=0;attrs!=null && i<attrs.length;i++) {
			String attr = attrs[i];
			String excelFormula = null;
			if(isDouble(attr)) {
				excelFormula = attr;
			} else {
				excelFormula = keyMap.get(attr);
			}
			if(attr.indexOf(MODULE_KEY_SPLIT)<0 && excelFormula==null) {
				System.out.println(111);
			}
			if(attr.indexOf(MODULE_KEY_SPLIT)>0) {
				excelFormula = String.valueOf(oldValue);
			}
			formula = FormulaUtil.replaceFirst(formula,attr,excelFormula);
		}
		return formula;
	}

	private Map<String, String> convertKeyTo(
			Map<Integer, Map<Integer, JSONObject>> trMap) {
		Map<String, String> keyMap = new HashMap<String, String>();
		for(Iterator<Integer> iter = trMap.keySet().iterator();iter.hasNext();) {
			Integer rowNum = iter.next();
			Map<Integer, JSONObject> celMap = trMap.get(rowNum);
			for(Iterator<Integer> celIterator = celMap.keySet().iterator();celIterator.hasNext();) {
				Integer celNum = celIterator.next();
				JSONObject obj = celMap.get(celNum);
				String key = obj.getString("key");
				Integer row = obj.getInteger("row");
				Integer col = obj.getInteger("col");
				String value = excelColIndexToStr(col+1)+(row+1);
				keyMap.put(key, value);
			}
		}
		return keyMap;
	}
	
	/**
     * Excel column index begin 1
     * @param columnIndex
     * @return
     */
    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }

	public  void ss() throws Exception {
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		int rownum = 0;
		HSSFRow row = sheet.createRow(rownum);
		int column = 0;
		HSSFCell cell1 = row.createCell(column++);
		HSSFCell cell2 = row.createCell(column++);
		HSSFCell cell3 = row.createCell(column++);
		cell3.setCellType(Cell.CELL_TYPE_FORMULA);
		cell3.setCellFormula("A1+B1");
		cell1.setCellValue(1);
		cell2.setCellValue(2);
		System.out.println(cell3.getNumericCellValue());
		File file = new File("D:/tmp/sss.xls");
		OutputStream os = new FileOutputStream(file);
		wb.write(os );
		
		InputStream is = new FileInputStream(file);
		HSSFWorkbook inwb = new HSSFWorkbook(is);
		
		HSSFFormulaEvaluator evaluator = inwb.getCreationHelper().createFormulaEvaluator();
		HSSFSheet insheet = inwb.getSheetAt(0);
		HSSFRow inrow = insheet.getRow(0);
		int cellnum = 0;
		HSSFCell incell1 = inrow.getCell(cellnum++);
		HSSFCell incell2 = inrow.getCell(cellnum++);
		HSSFCell incell3 = inrow.getCell(cellnum++);
		
		System.out.println(incell1.getNumericCellValue());
		System.out.println(incell2.getNumericCellValue());
		System.out.println(evaluator.evaluate(incell3).getNumberValue());
		
	}

	

}
