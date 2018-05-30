package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.formula.functions.Replace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.service.OrganizationService;
import cn.financial.service.StatisticJsonService;
import cn.financial.util.FormulaUtil;

/**
 * 统计service实现层
 * 
 * @author gh 2018/5/7
 *
 */
@Service("StatisticServiceImpl")
public class StatisticJsonServiceImpl implements StatisticJsonService {
	
    @Autowired
    private OrganizationService organizationService;

    
    /*
     * 将符合规则的内容字段进行统计(non-Javadoc)
     * @see cn.financial.service.StatisticService#getStatic(net.sf.json.JSONArray)
     */
	@SuppressWarnings("unchecked")
	public JSONObject getStatic(JSONObject model ,List<JSONObject> valueList) {
		JSONArray addJar = new JSONArray();
		Map<String,Object> params = new HashMap<String, Object>();
		for(int k=0;k<valueList.size();k++) {
			JSONObject valueJson = valueList.get(k);
			Iterator<String> it = model.keys();
			while (it.hasNext()) {
				String modelKey = it.next();
				JSONArray modelArr = model.getJSONArray(modelKey);
				JSONObject valueObj = valueJson.getJSONObject(modelKey);
				for (int i = 0; i < modelArr.size(); i++) {
					JSONObject rowjar = JSONObject.fromObject(modelArr.get(i));
					//判断输入是否是需要整合的
					Integer type = rowjar.getInt("type");
					if (type ==2 ||type == 4){
						//还要判断是否存在值，不存在直接跳过
						if(valueObj.toString() != "null"){
							String key = rowjar.getString("key");
							double value = 0 ;
							if(rowjar.containsKey("value")) {
								value = rowjar.getDouble("value");
							}
							value += valueObj.getDouble(key);
							//将数据添加到新json里
							rowjar.put("value", value);
							key = key.replaceAll("\\.", "_");
							params.put(key, value);
						}
					}
					addJar.add(rowjar);
				}
				model.put(modelKey, addJar);
				addJar.clear();
			}
		}
		
		//第二次进行数据公式计算循环
		Iterator<String> ct = model.keys();
		while (ct.hasNext()) {
			String modelKey = ct.next();
			JSONArray modelArr = model.getJSONArray(modelKey);
			for (int i = 0; i < modelArr.size(); i++) {
				JSONObject rowjar = JSONObject.fromObject(modelArr.get(i));
				//判断输入是否是需要整合的
				Integer type = rowjar.getInt("type");
				String formula = rowjar.getString("reallyFormula");
				formula = formula.replaceAll("\\.", "_");
				if (type ==3&&!formula.contains("SUM")){
					rowjar.put("value", FormulaUtil.calculationByFormula(params,formula));
				}
				addJar.add(rowjar);
			}
			model.put(modelKey, addJar);
			addJar.clear();
		}
		
		return model;
	}

	/*
	 * 将前台选择的分级进行处理(non-Javadoc)
	 * @see cn.financial.service.StatisticService#getSelect(net.sf.json.JSONArray)
	 */
	@Override
	public JSONArray getSelect(JSONArray jsonArray) {
		JSONArray total = new JSONArray();
		JSONArray ja = new JSONArray();
		Map<Object, String> selmap = new HashMap<Object, String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jid =JSONObject.fromObject(jsonArray.getString(i));
			//判断是否全选，非全选不用走查询步骤，直接下一步，因为json里会包含非全选的结构
			//当前1为全选
			if(jid.get("sel").equals(1)){
				//将当前id下所有子节点全部拿出来,父节点不会包含在内
				ja = JSONArray.fromObject(organizationService.TreeByIdForSon(jid.getString("id")));
				ja = childSel(ja);
				for (int j = 0; j < ja.size(); j++) {
					JSONObject idson = JSONObject.fromObject(ja.get(j));
					JSONObject jdata =JSONObject.fromObject(idson.get("nodeData")); 
					//放入map里，相同的id自动过滤整合
					selmap.put(idson.get("id"), jdata.getString("id"));
				}
			}
		}
		System.out.println(selmap);
		//上面处理完全部后，直接从表里查询对应的统计json数据
		for (Object code : selmap.keySet()) {
			 String selval = selmap.get(code);
			 //查询对应表里记录后整合成jsonarray
			 total.add("");
		}
		return total;
	}
	
	/**
	 * 循环查找底层树
	 * @param ja
	 * @return
	 */
	public JSONArray childSel(JSONArray ja){
		JSONArray jar = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo =JSONObject.fromObject(ja.get(i));
			if(!jo.get("children").toString().equals("[]")){
				JSONArray jc = JSONArray.fromObject(jo.get("children"));
				for (int j = 0; j < jc.size(); j++) {
					JSONObject jch = JSONObject.fromObject(jc.get(j));
					jar.add(jch);
				}
			}else{
				jar.add(ja);
				return ja;
			}
		}
		jar = childSel(jar);
		return jar;
	}

}
