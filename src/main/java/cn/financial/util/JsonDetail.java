package cn.financial.util;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class JsonDetail {
	/**
	 * 生成简化数据
	 * @param arr
	 * @param newObj
	 * @return
	 */
	public static JSONObject generateSimplifyJson(String jsonlist,JSONArray arr,String newObj) {

		JSONObject nJson = new JSONObject();
		for(int i=0;i<arr.size();i++) {
			Object obj = arr.get(i);
			if (obj instanceof JSONArray) {
				generateSimplifyJson(jsonlist,(JSONArray) obj,newObj);
			} else if(obj instanceof JSONObject){
				JSONObject json = (JSONObject)obj;
				if(json.containsKey("type")) {
					if(json.getInt("type")==1) {
						continue;
					}					
					Integer type = json.getInt("type");
					if(type == HtmlGenerate.BOX_TYPE_INPUT|| type == HtmlGenerate.BOX_TYPE_FORMULA){
						if(json.containsKey("key")) {
							String key = json.getString("key");
							Object value="0";
							nJson.put(key, value);
						}
					}
					if(type == HtmlGenerate.BOX_TYPE_BUDGET){//预算的,type=4
						String key = json.getString("key");

						String value=jsonCombined(jsonlist,newObj,key);
						nJson.put(key, value);
					}
				}
			}
		}
		return nJson;
	}
	
	public static String jsonCombined(String  json,String object,String key) {
		try {
		    JSONObject js=JSONObject.fromObject(json.toString());
			JSONObject ss=JSONObject.fromObject(js.get(object));
			String value=ss.get(key).toString();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public static  String ss(String json) {
		
		return json;
	}

}
