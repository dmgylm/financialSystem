package cn.financial.service;

import net.sf.json.JSONArray;

public interface StatisticService {

    /**
     * 统计所有选择级下数量
     * @return
     */
	JSONArray getStatic(JSONArray jsonArray);

}
