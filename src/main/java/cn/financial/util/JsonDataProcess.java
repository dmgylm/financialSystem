package cn.financial.util;

import java.util.Iterator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



public class JsonDataProcess {
	/**
	 * 简化Json数据
	 * 将其简化为只有Key和Value数据
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject simplifyJson(String jsonStr,String json) {
		JSONObject obj = JSONObject.parseObject(jsonStr);
		JSONObject newObj = new JSONObject();
		for(Iterator<String> iter = obj.keySet().iterator();iter.hasNext();) {
			String key = iter.next();
			JSONArray arr = obj.getJSONArray(key);
			JSONObject newArr = new JSONObject();
			newArr =JsonDetail.generateSimplifyJson(json,arr,key);
			newObj.put(key, newArr);
		}
		return newObj;
	}

}
