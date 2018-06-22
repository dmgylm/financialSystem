package cn.financial.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonConvertProcess {
	
	/**
	 * 将数据Json和模板Json进行合并
	 * @param templateJson 模板Json
	 * @param dataJson 数据Json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject mergeJson(JSONObject templateJson, JSONObject dataJson) {
		for(Iterator<String> iter = templateJson.keySet().iterator();iter.hasNext();){
			String key = iter.next();
			JSONArray longLst = templateJson.getJSONArray(key);
			JSONObject shortLst = dataJson.getJSONObject(key);
			mergeDetail(longLst,shortLst);
		}
		return templateJson;
	}
	
	/**
	 * 合并明细
	 * @param templateArr 模板Json
	 * @param dataJson 数据Json
	 */
	public static void mergeDetail(JSONArray templateArr, JSONObject dataJson) {
		for(int i=0;i<templateArr.size();i++) {
			JSONObject longJson = templateArr.getJSONObject(i);
			if(longJson.containsKey("key")) {
				String longKey =  longJson.getString("key");
				Object shortValue = dataJson.get(longKey);
				longJson.put("value", shortValue);
			}
		}
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
	public static JSONObject simplifyJson(String json,String jsonStr) {
		JSONObject obj = JSONObject.fromObject(jsonStr);
		JSONObject newObj = new JSONObject();
		for(Iterator<String> iter = obj.keys();iter.hasNext();) {
			String key = iter.next();
			JSONArray arr = obj.getJSONArray(key);
			JSONObject newArr = new JSONObject();
			//newArr = generateSimplifyJson(arr,newArr);
			newArr =JsonDetail.generateSimplifyJson(json,arr,key);
			newObj.put(key, newArr);
		}
		return newObj;
	}

	/**
	 * 生成简化数据
	 * @param arr
	 * @param newObj
	 * @return
	 */
	public static JSONObject generateSimplifyJson(JSONArray arr,JSONObject newObj) {
		JSONObject nJson = new JSONObject();
		for(int i=0;i<arr.size();i++) {
			Object obj = arr.get(i);
			if (obj instanceof JSONArray) {
				generateSimplifyJson((JSONArray) obj,newObj);
			} else if(obj instanceof JSONObject){
				JSONObject json = (JSONObject)obj;
				if(json.containsKey("type")) {
					if(json.getInt("type")==1) {
						continue;
					}
					
					Integer type = json.getInt("type");
					if (type == HtmlGenerate.BOX_TYPE_INPUT
							|| type == HtmlGenerate.BOX_TYPE_FORMULA
							|| type == HtmlGenerate.BOX_TYPE_BUDGET) {
						if(json.containsKey("key")) {
							String key = json.getString("key");
							Object value = i;
							if(json.containsKey("value")) {
								value = json.get("value");
							}
							nJson.put(key, value);//更新值
						}
					}
				}
			}
		}
		return nJson;
	}
}
