package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.financial.model.BusinessData;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.service.BusinessDataService;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.StatisticJsonService;
import cn.financial.util.FormulaUtil;
import cn.financial.util.StringUtils;

/**
 * 统计service实现层
 * 
 * @author gh 2018/5/7
 *
 */
@Service("StatisticServiceImpl")
public class StatisticJsonServiceImpl implements StatisticJsonService {
	
	@Autowired
	private DataModuleService dataModuleService;
	
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private BusinessDataService businessDataService;
	
    
    /**
     * 从传过来的种类里获取模板
     */
	@Override
	public JSONObject findModel(String reportType, String businessType) {
		DataModule bean = dataModuleService.getDataModule(reportType,businessType);
		if(bean!=null){
			return JSONObject.parseObject(bean.getModuleData());
		}
		return null;
	}
    
	/**
	 * 获取所选机构数据集合
	 */
	@Override
	public List<Organization> companyList(JSONArray orgId) {
		//将json里数据转换为list类型
		String js=JSONObject.toJSONString(orgId, SerializerFeature.WriteClassName);
		List<String> orgList =JSONObject.parseArray(js, String.class) ;
		//获取选中的子节点数据
		List<Organization> codeSonList = organizationService.listOrganization(orgList);
		
		return codeSonList;
	}
	
	/**
	 * 获取最底层数据集合
	 * @param codeSonList
	 * @return
	 */
	@Override
	public List<String> typeIdList(List<Organization> codeSonList) {
		List<String> typeIdList = new ArrayList<String>();
	       for (int i = 0; i < codeSonList.size(); i++) {
	        	if(codeSonList.get(i).getOrgType()==3){
	            	//组装到要整体要查询的集合下
	            	typeIdList.add(codeSonList.get(i).getId());
	        	}
			}
		return typeIdList;
	}
	
	/**
	 * 获取底层对应数据的集合(缓存和数据都要使用)
	 */
	@Override
	public List<BusinessData> valueList(String startDate, String endDate,List<String> typeIdList) {
		//分隔传过来的开始结束时间
		String[] startYAndM = startDate.split("/");
		String[] endYAndM = endDate.split("/");
		
		Map<Object, Object> map = new HashMap<>();
		map.put("typeId", typeIdList);
		map.put("startYear", startYAndM[0]);
		map.put("endYear", endYAndM[0]);
		map.put("startMonth", startYAndM[1]);
		map.put("endMonth", endYAndM[1]);
		List<BusinessData> businessDataList = businessDataService.listBusinessDataByIdAndDate(map);
		
		return businessDataList;
	}
	
	
	/**
	 * 获取对应公司数据集合(缓存使用)
	 * @param codeSonList
	 * @return
	 */
	@Override
	public Map<String,List<String>> companyCacheList(List<Organization> codeSonList) {
		Map<String,List<String>> companyList = new HashMap<String, List<String>>();
		//将底层数据id拿出来
        for (int i = 0; i < codeSonList.size(); i++) {
        	if(codeSonList.get(i).getOrgType()==3){
            	List<String> companyListValve = new ArrayList<String>();
            	//重新组装底层id到相应公司下
            	Organization company = organizationService.getCompanyNameBySon(codeSonList.get(i).getId());
            	if(company != null){
                	if(companyList.containsKey(company.getOrgName())){
        				companyListValve = companyList.get(company.getOrgName());
        			}
        			companyListValve.add(codeSonList.get(i).getId());
            		companyList.put(company.getOrgName(), companyListValve);
            	}

        	}
		}
		return companyList;
	}
	
	/**
	 * 进行缓存的存储(缓存使用)
	 */
	@Override
    @Cacheable(value = "staticInfoMap", key = "'staticInfoMap_'+#caCheUuid")
	public Map<String, Map<String, String>> staticInfoMap(Map<String, List<String>> companyList,
			List<BusinessData> businessDataList,String caCheUuid) {
		
    	Map<String,Map<String,String>> redisCacheInfo = new HashMap<String, Map<String,String>>();
    	//将组合成的公司对应数据计算后重新组合
    	Iterator<String> it = companyList.keySet().iterator();
		while (it.hasNext()) {
			List<JSONObject> departmentValueList = new ArrayList<JSONObject>();
			String departKey = it.next();//公司
			List<String> departmentList = companyList.get(departKey);
			for (int i = 0; i < departmentList.size(); i++) {
				for (int k = 0; k < businessDataList.size(); k++) {
					if(businessDataList.get(k).getTypeId().equals(departmentList.get(i))){
						departmentValueList.add(JSONObject.parseObject(businessDataList.get(k).getInfo()));
					}
				}
			}
			//数据统计整理
			Map<String,Object> item = valueListSum(departmentValueList);
			//整合数据格式
			Iterator<String> vt = item.keySet().iterator();
			while (vt.hasNext()) {
		    	Map<String,String> companyMap = new HashMap<String, String>();
				String valueKey = vt.next();//key
				String value = item.get(valueKey).toString();//value
				if(redisCacheInfo.containsKey(valueKey)){
					companyMap = redisCacheInfo.get(valueKey);
				}
				companyMap.put(departKey, value);
				redisCacheInfo.put(valueKey, companyMap);
			}
		}
		
		return redisCacheInfo ;
	}

	
    /*
     * 将符合规则的内容字段进行统计(non-Javadoc)
     * @see cn.financial.service.StatisticService#getStatic(net.sf.json.JSONArray)
     */

	@Override
	@JSONField(serialize = true)
	public JSONObject jsonCalculation(String reportType, String businessType,List<BusinessData> businessDataList) {
		//获取模板
		JSONObject model = findModel(reportType,businessType);
		if(model==null){
			return null;
		}
//		//获取所选机构
//		List<Organization> codeSonList = companyList(orgId);
//		//获取最底层数据
//		List<String> typeIdList = typeIdList(codeSonList);
//		//获取底层对应数据的集合
//		List<BusinessData> businessDataList = valueList(startDate,endDate,typeIdList);
		//获取数据
		List<JSONObject> valueList = new ArrayList<JSONObject>();
    	//将查询得来的数据整合添加
    	for (int j = 0; j < businessDataList.size(); j++) {
    		valueList.add(JSONObject.parseObject(businessDataList.get(j).getInfo()));
		}
		//开始数据计算
		Map<String,Object> item = valueListSum(valueList);
		//进行模板填写
		Iterator<String> it = model.keySet().iterator();
		while (it.hasNext()) {
			JSONArray json = new JSONArray();
			String modelKey = it.next();
			if(model.get(modelKey) instanceof JSONObject){
				JSONObject downValve = model.getJSONObject(modelKey);
				Iterator<String> dt = downValve.keySet().iterator();
				while (dt.hasNext()) {
					String downKey = dt.next();
					downValve = completeMap(downValve,downKey,item,json);
				}
				model.put(modelKey, downValve);
			}else{
				model = completeMap(model,modelKey,item,json);
			}
		}
		
		SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect; 
		String bytes = JSON.toJSONString(model,feature);  
		return (JSONObject) JSONObject.parse(bytes);
	}
	
	//进行整合数据
	public JSONObject completeMap(JSONObject model,String modelKey,Map<String,Object> item,JSONArray json){
		
		JSONArray modelArr = model.getJSONArray(modelKey);
		for (int i = 0; i < modelArr.size(); i++) {
			JSONObject rowjar = modelArr.getJSONObject(i);
			//判断输入是否是需要整合的
			Integer type = rowjar.getInteger("type");
			String itemKey = rowjar.getString("key");
			String formula = rowjar.getString("reallyFormula");
			if (type ==2 ||type == 4){
				//将数据添加到新json里
				rowjar.put("value", item.get(itemKey));
			}else if(type ==3 && StringUtils.isValid(formula) && !formula.contains("SUM")){
				rowjar.put("value", FormulaUtil.calculationByFormula(item,formula));
			}
			json.add(rowjar);
		}
		model.put(modelKey, json);
		return model;
		
	}
	
	//进行分段的计算方法
	public Map<String,Object> valueListSum(List<JSONObject> valueList){
		
		Map<String,Object> item = new HashMap<String, Object>();
		for (int k = 0; k < valueList.size(); k++) {
			JSONObject valueJson = valueList.get(k);
			Iterator<String> it = valueJson.keySet().iterator();
			while (it.hasNext()) {
				String Jsonkey = it.next();
				JSONObject jsonValve = valueJson.getJSONObject(Jsonkey);
				Iterator<String> vt = jsonValve.keySet().iterator();
				while (vt.hasNext()) {
					String itemKey = vt.next();
					//判断是否为JSONObject
					if(jsonValve.get(itemKey) instanceof JSONObject){
						JSONObject downValve = jsonValve.getJSONObject(itemKey);
						Iterator<String> dt = downValve.keySet().iterator();
						while (dt.hasNext()) {
							String downKey = dt.next();
							Double valve = downValve.getDouble(downKey);
							if(item.containsKey(downKey)){
								valve +=(Double)item.get(downKey);
							}
							item.put(downKey, valve);
						}
					}else{
						Double valve = jsonValve.getDouble(itemKey);
						if(item.containsKey(itemKey)){
							valve +=(Double)item.get(itemKey);
						}
						item.put(itemKey, valve);
					}
				}
			}
		}
		
		return item;
	}

}
