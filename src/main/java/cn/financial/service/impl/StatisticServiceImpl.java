package cn.financial.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import cn.financial.service.StatisticService;

/**
 * 统计service实现层
 * 
 * @author gh 2018/5/7
 *
 */
@Service("StatisticServiceImpl")
public class StatisticServiceImpl implements StatisticService {

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
					//判断是否是手动输入位
					if (jni.get("formula").equals(null)) {
						//判断价格或其他参数
						if (jni.get("type") != null)
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

}
