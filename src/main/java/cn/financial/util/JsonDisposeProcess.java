package cn.financial.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

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
	public static  void shortJson(){
		String path = "C:/Users/whg/Desktop/解析后文件.txt";
		JSONObject newObj = simplifyJson(readFileContent(path));
		System.out.println(newObj);
	}
	/**
	 * 获取文件内容
	 * @param path
	 * @return
	 */
	public static String readFileContent(String path){
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(path );
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tmp = null;
			while((tmp = reader.readLine())!=null){
				sb.append(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
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
