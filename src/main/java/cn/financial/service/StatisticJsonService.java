package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.BusinessData;
import cn.financial.model.Organization;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public interface StatisticJsonService {

	/**
	 * 查找模板
	 * @param reportType
	 * @param businessType
	 * @return
	 */
	JSONObject findModel(String reportType, String businessType);

	/**
	 * 查找组织架构公司列表
	 * @param orgId
	 * @return
	 */
	List<Organization> companyList(JSONArray orgId);

	/**
	 * 查找底层数据列表
	 * @param codeSonList
	 * @return
	 */
	List<String> typeIdList(List<Organization> codeSonList);

	/**
	 * 查找底层数据集合
	 * @param startDate
	 * @param endDate
	 * @param typeIdList
	 * @return
	 */
	List<BusinessData> valueList(String startDate, String endDate,
			List<String> typeIdList);

	/**
	 * 经行缓存保存与查找相关操作
	 * @param companyList
	 * @param businessDataList
	 * @param caCheUuid
	 * @return
	 */
	Map<String, Map<String, String>> staticInfoMap(
			Map<String, List<String>> companyList,
			List<BusinessData> businessDataList,String caCheUuid);

	/**
	 * 查找适用于缓存的公司数据
	 * @param codeSonList
	 * @return
	 */
	Map<String, List<String>> companyCacheList(List<Organization> codeSonList);

	/**
	 * 整合完成列表相关数据
	 * @param reportType
	 * @param businessType
	 * @param businessDataList
	 * @return
	 */
	JSONObject jsonCalculation(String reportType, String businessType,
			List<BusinessData> businessDataList);

}
