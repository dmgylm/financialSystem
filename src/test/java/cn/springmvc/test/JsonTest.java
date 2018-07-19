package cn.springmvc.test;



import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import cn.financial.controller.DataModuleController;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;
import cn.financial.util.JsonDataProcess;

public class JsonTest {
	
    @Test
	 public  void  shortJson(){
		String path = "C:/Users/whg/Desktop/解析后文件.txt";
		JSONObject newObj = JSONObject.parseObject(JsonConvertProcess.readFileContent(path));
		String paths="C:/Users/whg/Desktop/解析后文件测试.txt";
		JSONObject json=JSONObject.parseObject(JsonConvertProcess.readFileContent(paths));
		String jsons=JsonConvertProcess.readFileContent(paths).toString();//传过来 的数据先用文件代替
		JSONObject mergin=JsonConvertProcess.mergeJson(newObj,json);
		JSONObject simplifyJson=JsonDataProcess.simplifyJson(mergin.toString(),jsons);
		System.out.println(simplifyJson);
	}
    
    public void sss(){
//    	DataModuleController.class
    }
    
    @Test
    public  void  simplifyJson(){
    	String path = "C:/Users/Admin/Desktop/ffff.txt";
    	JSONObject newObj = JSONObject.parseObject(JsonConvertProcess.readFileContent(path));
    	JSONObject jsons=JsonConvertProcess.simplifyJson(newObj);
    	System.out.println(jsons);
    }

    @Test
    public  void  mergeJson(){
    	String path = "C:/Users/Admin/Desktop/测试模板.txt";
    	JSONObject newObj = JSONObject.parseObject(JsonConvertProcess.readFileContent(path));
    	String shortPath = "C:/Users/Admin/Desktop/测试简化.txt";
    	JSONObject shortObj = JSONObject.parseObject(JsonConvertProcess.readFileContent(shortPath));
    	JSONObject jsons=JsonConvertProcess.mergeJson(newObj,shortObj);
    	System.out.println(jsons);
    }
    
    @Test
    public  void  generateHtml(){
    	try {
    		String path = "C:/Users/Admin/Desktop/车管家预算.txt";
        	String s = JsonConvertProcess.readFileContent(path);
        	JSONObject jsonObj = JSONObject.parseObject(s);
        	HtmlGenerate htmlGenerate = new HtmlGenerate();
        	String json = htmlGenerate.generateHtml(jsonObj, HtmlGenerate.HTML_TYPE_PREVIEW);
        	System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	
}
