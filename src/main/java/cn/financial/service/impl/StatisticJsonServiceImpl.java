package cn.financial.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	public JSONObject jsonCalculation(JSONObject model ,List<JSONObject> valueList) {
		
		JSONArray json = new JSONArray();
		Map<String,Object> item = new HashMap<String, Object>();
		for (int k = 0; k < valueList.size(); k++) {
			JSONObject valueJson = valueList.get(k);
			Iterator<String> it = valueJson.keys();
			while (it.hasNext()) {
				String Jsonkey = it.next();
				JSONObject jsonValve = valueJson.getJSONObject(Jsonkey);
				Iterator<String> vt = jsonValve.keys();
				while (vt.hasNext()) {
					String itemKey = vt.next();
					Double valve = jsonValve.getDouble(itemKey);
					itemKey = itemKey.replaceAll("\\.", "_");
					if(item.containsKey(itemKey)){
						valve +=(Double)item.get(itemKey);
					}
					item.put(itemKey, valve);
				}
			}
		}
		
		Iterator<String> it = model.keys();
		while (it.hasNext()) {
			String modelKey = it.next();
			JSONArray modelArr = model.getJSONArray(modelKey);
			for (int i = 0; i < modelArr.size(); i++) {
				JSONObject rowjar = JSONObject.fromObject(modelArr.get(i));
				//判断输入是否是需要整合的
				Integer type = rowjar.getInt("type");
				String itemKey = rowjar.getString("key");
				String formula = rowjar.getString("reallyFormula");
				itemKey = itemKey.replaceAll("\\.", "_");
				formula = formula.replaceAll("\\.", "_");
				if (type ==2 ||type == 4){
					//将数据添加到新json里
					rowjar.put("value", item.get(itemKey));
				}else if(type ==3 && !formula.contains("SUM")){
					rowjar.put("value", FormulaUtil.calculationByFormula(item,formula));
				}
				json.add(rowjar);
			}
			model.put(modelKey, json);
			json.clear();
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
