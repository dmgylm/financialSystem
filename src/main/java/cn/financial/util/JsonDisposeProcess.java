package cn.financial.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 对json文件进行处理
 * @author hsl
 *
 */
public class JsonDisposeProcess {
  @Autowired
  private static  JsonDetail jsondetail;
  private static JsonConvertProcess  jsonconvert;
	 public static void shortJson(){
		String path = "C:/Users/whg/Desktop/解析后文件.txt";
		JSONObject newObj = JSONObject.fromObject(jsonconvert.readFileContent(path));
		String paths="C:/Users/whg/Desktop/解析后文件测试.txt";
		JSONObject json=JSONObject.fromObject(jsonconvert.readFileContent(paths));
		JSONObject mergin=jsonconvert.mergeJson(newObj,json);
		JSONObject simplifyJson=simplifyJson(mergin.toString());
		System.out.println(simplifyJson);
	}

	/**
	 * 简化Json数据
	 * 将其简化为只有Key和Value数据
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject simplifyJson(String jsonStr) {
		JSONObject obj = JSONObject.fromObject(jsonStr);
		JSONObject newObj = new JSONObject();
		for(Iterator<String> iter = obj.keys();iter.hasNext();) {
			String key = iter.next();
			JSONArray arr = obj.getJSONArray(key);
			JSONObject newArr = new JSONObject();
			newArr =jsondetail.generateSimplifyJson(arr,key);
			newObj.put(key, newArr);
		}
		return newObj;
	}


}
