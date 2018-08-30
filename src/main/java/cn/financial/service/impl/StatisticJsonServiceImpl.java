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

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.model.BusinessData;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.service.BusinessDataService;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.StatisticJsonService;
import cn.financial.util.ExcelReckonUtils;
import cn.financial.util.FormulaUtil;
import cn.financial.util.JsonConvertProcess;
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
	 * 获取所选数据集合
	 */
	@Override
	public List<BusinessData> BusList(String startDate, String endDate,JSONArray orgId) {
		//将json里数据转换为list类型
		String js=JSONObject.toJSONString(orgId, SerializerFeature.WriteClassName);
		List<String> orgList =JSONObject.parseArray(js, String.class) ;
		//获取选中的子节点数据
		List<BusinessData> BusList = organizationService.listBusinessList(startDate, endDate, orgList);
		if(BusList==null){
			return null;
		}
		return BusList;
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
//		//开始数据计算
//		Map<String,Object> item = valueListSum(valueList);
//		//进行模板填写
//		Iterator<String> it = model.keySet().iterator();
//		while (it.hasNext()) {
//			JSONArray json = new JSONArray();
//			String modelKey = it.next();
//			if(model.get(modelKey) instanceof JSONObject){
//				JSONObject downValve = model.getJSONObject(modelKey);
//				Iterator<String> dt = downValve.keySet().iterator();
//				while (dt.hasNext()) {
//					String downKey = dt.next();
//					downValve = completeMap(downValve,downKey,item,json);
//				}
//				model.put(modelKey, downValve);
//			}else{
//				model = completeMap(model,modelKey,item,json);
//			}
//		}
    	//开始数据计算
    	JSONObject item = valueListNewSum(valueList);
    	//进行模板填写
    	model = JsonConvertProcess.mergeJson(model,item);
		//模板公式计算
		ExcelReckonUtils eru = new ExcelReckonUtils();
		String staticMD =null;
		try {
			staticMD = eru.computeByExcel(model.toString());
		} catch (FormulaAnalysisException e) {
			e.printStackTrace();
		}
		
		SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect; 
		String bytes = JSON.toJSONString(staticMD,feature);  
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
	
	/**
	 * 将底层数据分组统计保存
	 * @param businessDataList
	 * @return
	 */

	@Override
	@JSONField(serialize = true)
	@SuppressWarnings("unchecked")
	public String jsonCalculationCollect(String reportType, String businessType,List<BusinessData> businessDataList){
		//获取模板
//		JSONObject model =JSONObject.parseObject(JsonConvertProcess.readFileContent("C:/Users/mzj/Desktop/budget_new.txt"));
		JSONObject model = findModel(reportType,businessType);
		if(model==null){
			return null;
		}
		Map<String,List<BusinessData>> groupdata = new HashMap<String, List<BusinessData>>();
		//取出id来进行查询
//		List<String> dataId = new ArrayList<String>();
//		for (int i = 0; i < businessDataList.size(); i++) {
//			dataId.add(businessDataList.get(i).getId());
//		}
		JSONArray dataJar = dataModuleService.dataModuleById(reportType,businessDataList);//获取分组对应标识
		Map<String,String> kal = (Map<String, String>) dataJar.get(0);//获取分组数据
		Map<String,String> mol = (Map<String, String>) dataJar.get(1);//获取模板数据
		//分组保存不同模板对应数据集
		for (int i = 0; i < businessDataList.size(); i++) {
			String bid = businessDataList.get(i).getId();
			if(kal.containsKey(bid)){
				String dm = kal.get(bid);
				List<BusinessData> bd = new ArrayList<BusinessData>();
				if(groupdata.containsKey(dm)){
					bd = groupdata.get(dm);
				}
				bd.add(businessDataList.get(i));
				groupdata.put(dm, bd);
			}
		}
		JSONObject groupmap = new JSONObject();
		//计算对应数据集，重新整合
		Iterator<String> it = groupdata.keySet().iterator();
		while (it.hasNext()) {
			String groupname = it.next();
			List<BusinessData> sumdata= groupdata.get(groupname);
			//获取数据
			List<JSONObject> valueList = new ArrayList<JSONObject>();
	    	//将查询得来的数据整合添加
	    	for (int j = 0; j < sumdata.size(); j++) {
	    		valueList.add(JSONObject.parseObject(sumdata.get(j).getInfo()));
			}
			//开始数据计算
			JSONObject groupsum = valueListNewSum(valueList);
			groupmap.put(groupname, groupsum);
		}

		//将数据集填入模板
		Map<String,JSONObject> modelDataStatic = new HashMap<String, JSONObject>();
		//填入模板数据,返回模板对应数据集
		Iterator<String> mod = mol.keySet().iterator();
		while (mod.hasNext()) {
			//获取模板
			String modelLogoName = mod.next();
			JSONObject modelData =JSONObject.parseObject(mol.get(modelLogoName));
			//获取模板对应数据集
			JSONObject trueMap = new JSONObject();
			if(groupmap.containsKey(modelLogoName)){
				trueMap = groupmap.getJSONObject(modelLogoName);
			}
			String modelJson = JsonConvertProcess.mergeJson(modelData,trueMap).toString();
			//模板公式计算
			ExcelReckonUtils eru = new ExcelReckonUtils();
			String reward =null;
			try {
				reward = eru.computeByExcel(modelJson);
			} catch (FormulaAnalysisException e) {
				e.printStackTrace();
			}
			//模板添加
			modelDataStatic.put(modelLogoName,JSONObject.parseObject(reward));
//			modelDataStatic.put(modelLogoName,JSONObject.parseObject(modelJson));
		}
		//重构模板及对应标识，取出需要的结果
		Iterator<String> returnData = modelDataStatic.keySet().iterator();
		while (returnData.hasNext()) {
			//获取模板
			String modelLogoName = returnData.next();
			JSONObject jsonValve = modelDataStatic.get(modelLogoName);

			//分解模板取值
			JSONObject modelDataSum = new JSONObject();
			Iterator<String> jd = jsonValve.keySet().iterator();
			while (jd.hasNext()) {
				String modelKeyJD = jd.next();
				if(jsonValve.get(modelKeyJD) instanceof JSONObject){
					JSONObject downValve = jsonValve.getJSONObject(modelKeyJD);
					Iterator<String> dt = downValve.keySet().iterator();
					while (dt.hasNext()) {
						String downKey = dt.next();
						JSONArray modelArr = downValve.getJSONArray(downKey);
						for (int i = 0; i < modelArr.size(); i++) {
							JSONObject rowjar = modelArr.getJSONObject(i);
							//判断输入是否是需要整合的
							String itemKey = rowjar.getString("key");
							Double value = rowjar.getDouble("value");
							if(value==null){
								value = 0.0;
							}
							modelDataSum.put(itemKey, value);
						}
					}
				}else{
					JSONArray modelArr = jsonValve.getJSONArray(modelKeyJD);
					for (int i = 0; i < modelArr.size(); i++) {
						JSONObject rowjar = modelArr.getJSONObject(i);
						//判断输入是否是需要整合的
						Double value = rowjar.getDouble("value");
						String itemKey = rowjar.getString("key");
						modelDataSum.put(itemKey, value);
					}
				}
			}
			modelDataStatic.put(modelLogoName, modelDataSum);
		}
		//最后进行的汇总模板填写
		Iterator<String> staticModel = model.keySet().iterator();
		while (staticModel.hasNext()) {
			JSONArray json = new JSONArray();
			String modelKey = staticModel.next();
			if(model.get(modelKey) instanceof JSONObject){
				JSONObject downValve = model.getJSONObject(modelKey);
				Iterator<String> dt = downValve.keySet().iterator();
				while (dt.hasNext()) {
					String downKey = dt.next();
					downValve = completeStaticMap(downValve,downKey,modelDataStatic,json);
				}
				model.put(modelKey, downValve);
			}else{
				model = completeStaticMap(model,modelKey,modelDataStatic,json);
			}
		}
		
		//汇总模板公式计算
		ExcelReckonUtils eru = new ExcelReckonUtils();
		String staticMD =null;
		try {
			staticMD = eru.computeByExcel(model.toString());
		} catch (FormulaAnalysisException e) {
			e.printStackTrace();
		}
		
		return staticMD;
	}
	
	//进行汇总整合数据
	private JSONObject completeStaticMap(JSONObject downValve, String downKey,
			Map<String, JSONObject> modelDataStatic, JSONArray json) {
		
		JSONArray modelArr = downValve.getJSONArray(downKey);
		for (int i = 0; i < modelArr.size(); i++) {
			JSONObject rowjar = modelArr.getJSONObject(i);
			//判断输入是否是需要整合的
			Integer type = rowjar.getInteger("type");
			String formula = rowjar.getString("formula"); 
			
			if (type ==3){
				if(formula.contains("★")){
					int wuSum = formula.length()-formula.replaceAll("★", "").length();
					if(wuSum>1){
						//当★大于1时
						String[] moreDown = formula.split("[\\+\\-\\*\\/()]");
						Map<String,Object> item = new HashMap<String, Object>();
//						Double formulaRS = 0.0;
						for (int j = 0; j < moreDown.length; j++) {
							String[] setDown = moreDown[j].split("★");
//							if(setDown.length==1){
//								System.out.println(formula);
//							}
							String logoKey = setDown[0];
							String logoValue = setDown[1];
							if(modelDataStatic.containsKey(logoKey)){
								JSONObject staticData = modelDataStatic.get(logoKey);
								if(staticData.containsKey(logoValue)){
									Double amount = staticData.getDouble(logoValue);
									moreDown[j] = moreDown[j].replaceAll("★", "_");
									item.put(moreDown[j], amount);
//									if(amount==null){
//										amount =0.0;
//									}
//									formulaRS += amount;
								}
							}
						}
						formula = formula.replaceAll("★", "_");
						rowjar.put("value",FormulaUtil.calculationByFormula(item,formula));
					}else{
						//当★为1时
						String[] setDown = formula.split("★");
						String logoKey = setDown[0];
						String logoValue = setDown[1];
						if(modelDataStatic.containsKey(logoKey)){
							JSONObject staticData = modelDataStatic.get(logoKey);
							if(staticData.containsKey(logoValue)){
								//将数据添加到新json里
								rowjar.put("value", staticData.get(logoValue));
							}else{
								rowjar.put("value", 0);
							}
							
						}
					}

				}
			}
			json.add(rowjar);
		}
		downValve.put(downKey, json);
		return downValve;
	}
	
	//差分添加
	public JSONObject addDataJson(String downKey,JSONObject downValve){
		JSONObject modelDataSum = new JSONObject();
		JSONArray modelArr = downValve.getJSONArray(downKey);
		for (int i = 0; i < modelArr.size(); i++) {
			JSONObject rowjar = modelArr.getJSONObject(i);
			//判断输入是否是需要整合的
			String itemKey = rowjar.getString("key");
			Double value = rowjar.getDouble("value");
			if(value==null){
				value = 0.0;
			}
			modelDataSum.put(itemKey, value);
		}
		
		return modelDataSum;
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
	
	//汇总分段的计算方法
	public JSONObject valueListNewSum(List<JSONObject> valueList){
		JSONObject group = new JSONObject();
		for (int k = 0; k < valueList.size(); k++) {
			JSONObject valueJson = valueList.get(k);
			Iterator<String> it = valueJson.keySet().iterator();
			while (it.hasNext()) {
				String Jsonkey = it.next();//分组开始名
				JSONObject item = new JSONObject();
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
				group.put(Jsonkey, item);
			}
		}
		
		SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect; 
		String bytes = JSON.toJSONString(group,feature);  
		return (JSONObject) JSONObject.parse(bytes);

	}

}
