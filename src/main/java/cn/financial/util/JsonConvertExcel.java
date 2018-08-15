package cn.financial.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonConvertExcel {

	private static Map<Integer, Map<Integer, JSONObject>> rowMap = new HashMap<Integer, Map<Integer, JSONObject>>();
/**
 * 
 * @param 业务子表info数据和模板数据合并后的json 
 * @return Workbook (excel)
 * @throws IOException
 */
	public  static Workbook getExcel(JSONObject Json,String sheetName) throws IOException {
		Map<Integer, Map<Integer, JSONObject>> trMap = assembleData(Json);
		trMap = sortTableRowMap(trMap);
		return assembleTable(trMap, sheetName);
	}

	private static Map<Integer, Map<Integer, JSONObject>> assembleData(JSONObject array) {
		for (Iterator<String> iter = array.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			Object obj = array.get(key);
			if (obj instanceof JSONArray) {
				JSONArray ja = (JSONArray) obj;
				for (int i = 0; i < ja.size(); i++) {
					putRowToMap(rowMap, ja.getJSONObject(i));
				}
			} else if (obj instanceof JSONObject) {
				assembleData((JSONObject) obj);
			}
		}

		return rowMap;
	}

	/**
	 * 将该Map通过Key进行排序
	 * 
	 * @param map
	 * @return
	 */
	private static Map<Integer, Map<Integer, JSONObject>> sortTableRowMap(Map<Integer, Map<Integer, JSONObject>> map) {
		Map<Integer, Map<Integer, JSONObject>> sortMap = new TreeMap<Integer, Map<Integer, JSONObject>>(
				new MapKeyComparator());

		sortMap.putAll(map);
		return sortMap;
	}

	/**
	 * 
	 * @param trMap
	 * @param obj
	 */
	private static void putRowToMap(Map<Integer, Map<Integer, JSONObject>> trMap, JSONObject obj) {
		if (!obj.containsKey("row")) {
			return;
		}
		Integer rowNum = obj.getInteger("row");
		Integer colNum = obj.getInteger("col");
		Map<Integer, JSONObject> row = trMap.get(rowNum);
		if (row == null) {
			row = new HashMap<Integer, JSONObject>();
			trMap.put(rowNum, row);
		}
		row.put(colNum, obj);
	}

	static class MapKeyComparator implements Comparator<Integer> {
		public int compare(Integer value1, Integer value2) {
			return value1 - value2;
		}
	}

	/**
	 * 组装Table
	 * 
	 * @param trMap
	 * @param table
	 * @throws IOException
	 */
	private static Workbook assembleTable(Map<Integer, Map<Integer, JSONObject>> trMap,String sheetName) throws IOException {
		Workbook wb = new XSSFWorkbook();
		if(("").equals(sheetName)) {
			sheetName="sheet1";
		}
		Sheet sheet = wb.createSheet(sheetName);
		XSSFCellStyle ztStyle = (XSSFCellStyle) wb.createCellStyle();

		ztStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		ztStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		ztStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		ztStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		ztStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

		// 创建字体对象
		Font ztFont = wb.createFont();
		ztFont.setFontName("宋体");
		ztFont.setFontHeightInPoints((short) 10); // 将字体大小设置为18px
		ztStyle.setFont(ztFont);

		Row row1;
		List<CellRangeAddress> lc = new ArrayList<CellRangeAddress>();
		for (Iterator<Integer> rowIter = trMap.keySet().iterator(); rowIter.hasNext();) {
			Integer rowNum = rowIter.next();
			Map<Integer, JSONObject> row = trMap.get(rowNum);

			row1 = sheet.createRow(rowNum);
			for (Iterator<Integer> colIter = row.keySet().iterator(); colIter.hasNext();) {
				Integer colNum = colIter.next();
				JSONObject col = row.get(colNum);

				int firstRow = Integer.parseInt(col.get("row").toString());
				int lastRow = firstRow;
				int firstCol = Integer.parseInt(col.get("col").toString());
				int lastCol = firstCol;
				if (Integer.parseInt(col.get("rowspan").toString()) != 1) {
					lastRow = firstRow + Integer.parseInt(col.get("rowspan").toString()) - 1;
				}
				if (Integer.parseInt(col.get("colspan").toString()) != 1) {
					lastCol = firstCol + Integer.parseInt(col.get("colspan").toString()) - 1;
				}
				CellRangeAddress cellr = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
				lc.add(cellr);
				Cell cell = row1.createCell(colNum);
				boolean display = (boolean) col.get("display");
			/*	if (col.get("name").equals("") && col.containsKey("value") && display == true) {
					if (Integer.parseInt(col.get("type").toString()) == HtmlGenerate.BOX_TYPE_LABEL
							|| Integer.parseInt(col.get("type").toString()) == HtmlGenerate.BOX_TYPE_MODULE
							|| Integer.parseInt(col.get("type").toString()) == HtmlGenerate.BOX_TYPE_ITEM
							|| Integer.parseInt(col.get("type").toString()) == HtmlGenerate.BOX_TYPE_MAINTITLE
							|| Integer.parseInt(col.get("type").toString()) == HtmlGenerate.BOX_TYPE_SUBTITLE) {
						cell.setCellValue(col.get("name").toString());
					} else {
						cell.setCellValue(col.get("value").toString());
					}
				} else {
					cell.setCellValue(col.get("name").toString());
				}*/
				
				if(display==true) {
					if(Integer.parseInt(col.get("type").toString())==HtmlGenerate.BOX_TYPE_INPUT
							||Integer.parseInt(col.get("type").toString())==HtmlGenerate.BOX_TYPE_FORMULA
							||Integer.parseInt(col.get("type").toString())==HtmlGenerate.BOX_TYPE_BUDGET) {
						if(col.containsKey("value")) {
							cell.setCellValue(col.get("value").toString());
						}else{
							cell.setCellValue(0);
						}
					}else {
						cell.setCellValue(col.get("name").toString());
					}
				}
				cell.setCellStyle(ztStyle);
				if (Integer.parseInt(col.get("type").toString()) == 3) {
					XSSFCellStyle ztStyles = (XSSFCellStyle) wb.createCellStyle();
					ztStyles.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
					ztStyles.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
					ztStyles.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
					ztStyles.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
					ztStyles.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
					ztStyles.setFillPattern(CellStyle.SOLID_FOREGROUND);
					ztStyles.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex()); // 前景色;
					ztStyles.setFont(ztFont);
					cell.setCellStyle(ztStyle); // 样式应用到该单元格上
					cell.setCellStyle(ztStyles);

				}
			}
		}
		for (int i = 0; i < lc.size(); i++) {
			sheet.addMergedRegion(lc.get(i));

		}
		// sheet=getCell(trMap,sheet);
		return wb;
	}

}
