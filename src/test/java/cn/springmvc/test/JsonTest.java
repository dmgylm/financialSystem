package cn.springmvc.test;



import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

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

	
}
