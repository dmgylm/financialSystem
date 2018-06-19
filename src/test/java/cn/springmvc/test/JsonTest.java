package cn.springmvc.test;

import net.sf.json.JSONObject;

import org.junit.Test;

import cn.financial.util.JsonConvertProcess;

public class JsonTest {

	@Test
	public void shortJsonTest() {
		try {
			String path = "C:/Users/Admin/Desktop/解析后文件.txt";
			JSONObject newObj = JsonConvertProcess.simplifyJson(JsonConvertProcess.readFileContent(path));
			System.out.println(newObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void mergeJsonTest() {
		String longTxt = JsonConvertProcess.readFileContent("C:/Users/Admin/Desktop/解析后文件.txt");
		String shortTxt = JsonConvertProcess.readFileContent("C:/Users/Admin/Desktop/新解析后文件.txt");
		JSONObject longJson = JSONObject.fromObject(longTxt);
		JSONObject shortJson = JSONObject.fromObject(shortTxt);
		JSONObject mergeJson = JsonConvertProcess.mergeJson(longJson,shortJson);
		System.out.println(mergeJson);
	}
	
}
