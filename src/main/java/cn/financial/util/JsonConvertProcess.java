package cn.financial.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.util.HtmlGenerate.MapKeyComparator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonConvertProcess {
	
	private Map<Integer, Map<Integer, JSONObject>> rowMap = new LinkedHashMap<Integer, Map<Integer,JSONObject>>();
	
	//预算表中的Label单元格字段
	private JSONArray bugdetLabelArr = new JSONArray();
		
	/**
	 * 将数据Json和模板Json进行合并
	 * @param templateJson 模板Json
	 * @param dataJson 数据Json
	 * @return
	 */
	public static JSONObject mergeJson(JSONObject templateJson, JSONObject dataJson) {
		for(Iterator<String> iter = templateJson.keySet().iterator();iter.hasNext();){
			String key = iter.next();
			Object templateObj = templateJson.get(key);
			JSONObject shortData = dataJson.getJSONObject(key);
			if (templateObj instanceof JSONArray) {
				JSONArray longLst = (JSONArray) templateObj;
				if(longLst!=null && shortData!=null) {
					mergeDetail(longLst,shortData);
				}
			} else if (templateObj instanceof JSONObject) {
				JSONObject longData = (JSONObject) templateObj;
				mergeJson(longData, shortData);
			}
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
				if(!StringUtils.isValid(longKey)) {
					continue;
				}
				if(dataJson.containsKey(longKey)) {
					Object shortValue = dataJson.get(longKey);
					longJson.put("value", shortValue);
				}
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
	
	public static JSONObject simplifyJson(String jsonStr) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		return simplifyJson(json);
	}

	/**
	 * 简化Json数据
	 * 将其简化为只有Key和Value数据
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject simplifyJson(JSONObject json) {
		JSONObject newObj = new JSONObject();
		for(Iterator<String> iter = json.keySet().iterator();iter.hasNext();) {
			String key = iter.next();
			Object obj = json.get(key);
			if (obj instanceof JSONArray) {
				JSONArray arr = (JSONArray) obj;
				JSONObject newArr = new JSONObject();
				newArr = generateSimplifyJson(arr);
				newObj.put(key, newArr);
			} else if(obj instanceof JSONObject) {
				JSONObject no = simplifyJson((JSONObject) obj);
				newObj.put(key,no);
			}
			
		}
		return newObj;
	}

	/**
	 * 生成简化数据
	 * @param arr
	 * @param newObj
	 * @return
	 */
	public static JSONObject generateSimplifyJson(JSONArray arr) {
		JSONObject nJson = new JSONObject();
		for(int i=0;i<arr.size();i++) {
			Object obj = arr.get(i);
			if (obj instanceof JSONArray) {
				generateSimplifyJson((JSONArray) obj);
			} else if(obj instanceof JSONObject){
				JSONObject json = (JSONObject)obj;
				if(json.containsKey("type")) {
					if(json.getInteger("type")==HtmlGenerate.BOX_TYPE_LABEL) {
						continue;
					}
					
					Integer type = json.getInteger("type");
					if (type == HtmlGenerate.BOX_TYPE_INPUT
							|| type == HtmlGenerate.BOX_TYPE_FORMULA
							|| type == HtmlGenerate.BOX_TYPE_BUDGET) {
						if(json.containsKey("key")) {
							String key = json.getString("key");
							Object value = 0;
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
	
	/**
	 * 生成每月预算JSON模板数据
	 * @param jsonObj
	 * @return
	 */
	public JSONObject generateMonthlyBudgetJson(String json) {
		JSONObject jsonObj = JSONObject.parseObject(json);
		return generateMonthlyBudgetJson(jsonObj);
		
	}
	
	/**
	 * 生成每月预算JSON模板数据
	 * @param jsonObj
	 * @return
	 */
	public JSONObject generateMonthlyBudgetJson(JSONObject jsonObj) {
		bugdetLabelArr.clear();
		
		JSONObject budgetJson = new JSONObject();
		for(Iterator<String> iter = jsonObj.keySet().iterator();iter.hasNext();) {
			String key = iter.next();
			JSONArray arr = jsonObj.getJSONArray(key);
			JSONObject newArr = new JSONObject();
			
			generateModuleBudget(arr,newArr);
			
			for(Iterator<String> it = newArr.keySet().iterator();it.hasNext();){
				String month = it.next();
				JSONObject monthData = budgetJson.getJSONObject(month);
				if(monthData == null) {
					monthData = new JSONObject();
				}
				JSONArray modelData = monthData.getJSONArray(key);
				if(modelData == null) {
					modelData = new JSONArray();
				}
				JSONArray monthArr = newArr.getJSONArray(month);
				modelData.addAll(monthArr);
				monthData.put(key, modelData);
				
				budgetJson.put(month, monthData);
			}
		}
		budgetJson.put("title", bugdetLabelArr);
		return budgetJson;
	}
	
	private void generateModuleBudget(JSONArray arr, JSONObject newArr) {
		for(int i=0;i<arr.size();i++) {
			Object obj = arr.get(i);
			if (obj instanceof JSONArray) {
				generateModuleBudget((JSONArray) obj, newArr);
			} else if(obj instanceof JSONObject){
				JSONObject json = (JSONObject)obj;
				String key = json.getString("key");
				Integer type = json.getInteger("type");
				
				if (HtmlAnalysis.isLabel(type)) {
					bugdetLabelArr.add(json);
				} else {
					String month = getMonthForKey(key);
					JSONArray monthArr = newArr.getJSONArray(month);
					if(monthArr == null) {
						monthArr = new JSONArray();
					}
					monthArr.add(json);
					newArr.put(month, monthArr);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String value = "jfdjk1yue";
		System.out.println(getMonthForKey(value));
	}
	
	public static String getMonthForKey(String value) {
		//从字符串中抽取出月份
		 String pattern = "([1-9]{1}|1[0-2]{1})yue{1}$";
		 Pattern r = Pattern.compile(pattern);
		 Matcher matcher = r.matcher(value);
		 String month = "";
		 if(matcher.find()) {
			 month = value.substring(matcher.start());
		 }
		return month.replace("yue", "");
	}
	
	/**
	 * 根据JsonArray转换成模块化的Json格式
	 * 并在此步骤替换其真实公式
	 * @param array 要组装的Json数组
	 * @return
	 * @throws FormulaAnalysisException 
	 */
	public static Map<String,List<JSONObject>> assembleTableData(JSONArray array) throws FormulaAnalysisException {
		Map<String,List<JSONObject>> dataMap = new HashMap<String, List<JSONObject>>();
		for(int i=0;i<array.size();i++) {
			JSONObject json = array.getJSONObject(i);
			String key = json.getString("key");
			String formula = json.getString("formula");
//			String reallyFormula = FormulaUtil.replaceFormula(array,formula);
//			String reallyFormula = FormulaUtil.getReallyFormulaByKey(array,key);
//			json.put("reallyFormula", reallyFormula);
			if(StringUtils.isValid(key) && key.indexOf(HtmlAnalysis.Separate_Modular)>0) {
				String modelName = key.split(HtmlAnalysis.Separate_Modular)[0];
				List<JSONObject> subjectlst = dataMap.get(modelName);
				if(subjectlst == null) {
					subjectlst = new ArrayList<JSONObject>();
					dataMap.put(modelName, subjectlst);
				}
				subjectlst.add(json);
			} else {//将标题加入
				List<JSONObject> subjectlst = dataMap.get("title");
				if(subjectlst == null) {
					subjectlst = new ArrayList<JSONObject>();
					dataMap.put("title", subjectlst);
				}
				subjectlst.add(json);
			}
		}
		//System.out.println("subjectlst~"+subjectlst);
		return dataMap;
	}
	//用Excel算好的json 和原始模板进行合并
	public JSONObject merge(JSONObject dataMjo,Map<String,List<JSONObject>> assembleTableData) {
		JSONObject newBudgetHtml=new JSONObject();
		JSONObject ja=new JSONObject();
			for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
				String keyIncome = iterIncome.next();
				Object objIncome = dataMjo.get(keyIncome);
				if (keyIncome.equals("title")) {
					ja.put(keyIncome, dataMjo.get(keyIncome));
				} else {
					if (objIncome instanceof JSONArray) {
						newBudgetHtml = mergeHtml(dataMjo,assembleTableData);
					} else {
						JSONObject longData = (JSONObject) objIncome;
						newBudgetHtml = mergeHtml(longData, assembleTableData);
					}
					ja.put(keyIncome, newBudgetHtml);
				}
			}
			return ja;
		
	}
	/**
	 * 生成行数据
	 * @param json
	 * @return
	 */
	public Map<Integer, Map<Integer, JSONObject>> assembleData(
			JSONObject json) {
		for(Iterator<String> iter = json.keySet().iterator();iter.hasNext();) {
			String key = iter.next();
			Object obj = json.get(key);
			if(obj instanceof JSONArray) {
				JSONArray ja = (JSONArray)obj;
				for(int i=0;i<ja.size();i++) {
					putRowToMap(rowMap,ja.getJSONObject(i));
				}
			} else if(obj instanceof JSONObject) {
				assembleData((JSONObject) obj);
			}
		}
		
		return rowMap;
	}
	
	/**
	 * 
	 * @param trMap
	 * @param obj
	 */
	private void putRowToMap(Map<Integer, Map<Integer, JSONObject>> trMap,
			JSONObject obj) {
		if(!obj.containsKey("row")) {
			return;
		}
		Integer rowNum = obj.getInteger("row");
		Integer colNum = obj.getInteger("col");
		Map<Integer, JSONObject> row = trMap.get(rowNum);
		if(row==null) {
			row = new HashMap<Integer, JSONObject>();
			trMap.put(rowNum, row);
		}
		row.put(colNum, obj);
	}
	
	/**
	 * 将该Map通过Key进行排序
	 * @param map
	 * @return
	 */
	public Map<Integer, Map<Integer,JSONObject>> sortTableRowMap(
			Map<Integer, Map<Integer,JSONObject>> map) {
		Map<Integer, Map<Integer,JSONObject>> sortMap = new TreeMap<Integer, Map<Integer,JSONObject>>(
				new MapKeyComparator());
		
		sortMap.putAll(map);
		for(Iterator<Integer> iter = sortMap.keySet().iterator();iter.hasNext();) {
			Integer row = iter.next();
			Map<Integer, JSONObject> colMap = sortMap.get(row);
			colMap = sortMapByKey(colMap);
			sortMap.put(row, colMap);
		}
		return sortMap;
	}
	
	/**
	 * 将该Map通过Key进行排序
	 * @param colMap
	 * @return
	 */
	private Map<Integer, JSONObject> sortMapByKey(
			Map<Integer, JSONObject> colMap) {
		Map<Integer, JSONObject> sortMap = new TreeMap<Integer, JSONObject>(
				new MapKeyComparator());
		
		sortMap.putAll(colMap);
		return sortMap;
	}
	
	public JSONObject mergeHtml (JSONObject dataMjo,Map<String,List<JSONObject>> mo) {
		
		JSONObject newBudgetHtml=new JSONObject();
		JSONObject obj = new JSONObject();
		for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
			String keyIncome = iterIncome.next();
			
			if(mo.containsKey(keyIncome)) {
				List<JSONObject> ljo=mo.get(keyIncome);
			
			Object objIncome = dataMjo.get(keyIncome);
			JSONArray jsoA = new JSONArray();
			if (objIncome instanceof JSONArray) {
			JSONArray longLst = (JSONArray) objIncome;
				for (int j = 0; j < longLst.size(); j++) {
					 obj = new JSONObject();
					JSONObject longJson = longLst.getJSONObject(j);
					for (Iterator<String> iters = longJson.keySet().iterator(); iters.hasNext();) {
						String keysIncomes = iters.next();
						Object objsIncome = longJson.get(keysIncomes);
						obj.put(keysIncomes, objsIncome);
						double value= 0.0;
						if(Integer.parseInt(longJson.get("type").toString())==HtmlGenerate.BOX_TYPE_FORMULA) {//判断当前类型是不是公式
							
							 for (int i = 0; i < ljo.size(); i++) {
								 JSONObject jo=ljo.get(i);
									if(jo.get("key").equals(longJson.get("key"))) {
										 value= Double.parseDouble(jo.get("value").toString());
										 break;
									}
								}
						}
						obj.put("value", value);
					}
					jsoA.add(obj);;
				}
			}
		newBudgetHtml.put(keyIncome, jsoA);
		}
		}
		return newBudgetHtml ;
	}
}
