package cn.financial.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.service.OrganizationService;
import cn.financial.service.StatisticJsonService;

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
	@Override
	public JSONArray getStatic(JSONArray jsonArray) {
		Map<Object, Integer> dataint = new HashMap<Object, Integer>();
		JSONArray jar = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject ja = JSONObject.fromObject(jsonArray.get(i));
			Iterator<String> it = ja.keys();
			while (it.hasNext()) {
				Integer sum = 0;
				JSONArray jai = ja.getJSONArray(it.next());
				for (int j = 0; j < jai.size(); j++) {
					JSONObject jni = JSONObject.fromObject(jai.get(j));
						//判断价格或其他参数
						if (jni.get("type").equals(2)){
							//判断是否在map里存在此数据
							if (dataint.containsKey(jni.get("name"))) {
								sum = jni.getInt("type")
										+ dataint.get(jni.get("name"));
							} else {
								sum = jni.getInt("type");
							}
						}
					dataint.put(jni.get("name"), sum);
				}
			}
		}

		// 重新建立新json，将合集数据添加进去
		JSONObject jv = JSONObject.fromObject(jsonArray.get(0));
		JSONArray jain = new JSONArray();
		JSONObject jac = new JSONObject();

		Iterator<String> iv = jv.keys();
		while (iv.hasNext()) {
			String key = iv.next();
			JSONArray jai = jv.getJSONArray(key);
			for (int j = 0; j < jai.size(); j++) {
				JSONObject jni = JSONObject.fromObject(jai.get(j));
				if (dataint.containsKey(jni.get("name"))) {
					Integer vs = dataint.get(jni.get("name"));
					jni.put("value", vs);
				}
				jain.add(jni);
			}
			jac.put(key, jain);
			jain.clear();
		}
		jar.add(jac);
		return jar;
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
		//上面处理玩全部后，直接从表里查询对应的统计json数据
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
