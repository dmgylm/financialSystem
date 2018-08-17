package cn.springmvc.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.financial.util.ExcelReckonUtils;
import cn.financial.util.JsonConvertProcess;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExcelReckonTest {
	
	public static void main(String[] args) throws Exception {
		String text = JsonConvertProcess.readFileContent("C:/Users/Admin/Desktop/wxsy.txt");
		JSONObject json = JSONObject.parseObject(text);
		ExcelReckonUtils ert = new ExcelReckonUtils();
		//生成Excel
		JsonConvertProcess jcp = new JsonConvertProcess();
		Map<Integer, Map<Integer, JSONObject>> trMap = jcp.assembleData(json);
		trMap = jcp.sortTableRowMap(trMap);//排序行和列
		HSSFWorkbook wb = ert.generateExcelByFormula(trMap);
		OutputStream os = new FileOutputStream(new File("D:/tmp/sss.xls"));
		wb.write(os);
		//计算并读取Excel
		JSONArray list = ert.computeExcelToJson(trMap,wb);
		Map<String,List<JSONObject>> dataMap = JsonConvertProcess.assembleTableData(list);
		System.out.println(JSONObject.toJSON(dataMap).toString());
	}

}
