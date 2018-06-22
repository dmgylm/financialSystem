package cn.springmvc.test;

import net.sf.json.JSONObject;

import org.junit.Test;

import cn.financial.util.JsonConvertProcess;
import cn.financial.util.JsonDetail;
import cn.financial.util.JsonDisposeProcess;

public class JsonTest {
	
    @Test
	 public  void  shortJson(){
		String path = "C:/Users/whg/Desktop/解析后文件.txt";
		JSONObject newObj = JSONObject.fromObject(JsonConvertProcess.readFileContent(path));
		String paths="C:/Users/whg/Desktop/解析后文件测试.txt";
		JSONObject json=JSONObject.fromObject(JsonConvertProcess.readFileContent(paths));
		String jsons=JsonConvertProcess.readFileContent(paths).toString();//传过来 的数据先用文件代替
		JSONObject mergin=JsonConvertProcess.mergeJson(newObj,json);
		JSONObject simplifyJson=JsonDisposeProcess.simplifyJson(jsons,mergin.toString());
		System.out.println(simplifyJson);
	}

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
