package cn.financial.service;

import net.sf.json.JSONArray;

public interface StatisticJsonService {

    /**
     * 统计所有数量
     * @return
     */
	JSONArray getStatic(JSONArray jsonArray);
	
    /**
     * 选择结构相关
     * @return
     */
	JSONArray getSelect(JSONArray jsonArray);

}
