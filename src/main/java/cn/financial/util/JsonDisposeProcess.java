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

	/**
	 * 简化Json数据
	 * 将其简化为只有Key和Value数据
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject simplifyJson(String json,String jsonStr) {
		JSONObject obj = JSONObject.fromObject(jsonStr);
		JSONObject newObj = new JSONObject();
		for(Iterator<String> iter = obj.keys();iter.hasNext();) {
			String key = iter.next();
			JSONArray arr = obj.getJSONArray(key);
			JSONObject newArr = new JSONObject();
			newArr =jsondetail.generateSimplifyJson(json,arr,key);
			newObj.put(key, newArr);
		}
		return newObj;
	}


}
