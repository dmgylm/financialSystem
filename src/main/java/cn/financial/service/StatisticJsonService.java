package cn.financial.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface StatisticJsonService {

    /**
     * 统计所有数量
     * @return
     */
	JSONObject getStatic(JSONObject cachemodel ,List<JSONObject> valuemode);
	
    /**
     * 选择结构相关
     * @return
     */
	JSONArray getSelect(JSONArray jsonArray);

}
