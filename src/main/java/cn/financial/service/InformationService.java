package cn.financial.service;

import org.springframework.stereotype.Service;

import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;
import cn.financial.util.JsonDetail;
import cn.financial.util.JsonDisposeProcess;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class InformationService {
	
	 public JSONArray listbudget(String json){
		 String path = "C:/Users/whg/Desktop/解析后文件.txt";
		 JSONObject newObj = JsonDisposeProcess.simplifyJson(json,JsonDisposeProcess.readFileContent(path));
		 JSONArray jsonarray=JSONArray.fromObject(newObj);
		 return jsonarray;
	 }
	 /**
	  * 处理数据
	  * @param jsons 前端传过来json字符串
	  * @param arr
	  * @param newObj 
	  * @return
	  */
	 public static JSONObject generateSimplifyJson(String jsons,JSONArray arr,String newObj) {
		    JSONObject nJson = new JSONObject();
			for(int i=0;i<arr.size();i++) {
				Object obj = arr.get(i);
				
				if (obj instanceof JSONArray) {
					generateSimplifyJson(jsons,(JSONArray) obj,newObj);
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
							String value=Jsoncombined(jsons,newObj,key);
							nJson.put(key, value);
						}
					}
				}
			}
			return nJson;
		}
		 public static String  Jsoncombined(String json,String object,String key) {
			 JSONObject js=JSONObject.fromObject(json.toString());
			 JSONObject ss=JSONObject.fromObject(js.get(object));
			 String value=ss.get(key).toString();
			return value;
		}
}
