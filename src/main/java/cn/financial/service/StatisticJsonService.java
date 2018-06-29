package cn.financial.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public interface StatisticJsonService {

	/**
     * 统计所有数量
     * @return
     */
	JSONObject jsonCalculation(String reportType, String businessType, String startDate,String endDate,JSONArray orgId);

	JSONObject findModel(String reportType, String businessType);

	List<JSONObject> findList(String startDate, String endDate,JSONArray orgId);

}
