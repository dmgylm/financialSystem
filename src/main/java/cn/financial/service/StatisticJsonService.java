package cn.financial.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;


public interface StatisticJsonService {

	/**
     * 统计所有数量
     * @return
     */
	JSONObject jsonCalculation(JSONObject cachemodel ,List<JSONObject> valuemode);
	
}
