package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.BusinessData;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.service.BusinessDataService;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizaCodeService;
import cn.financial.service.StatisticJsonService;
import cn.financial.util.FormulaUtil;

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
    private OrganizaCodeService organizaCodeService;
    
    @Autowired
    private BusinessDataService businessDataService;
	
    
    /**
     * 从传过来的种类里获取模板
     */
	@Override
	public JSONObject findModel(String reportType, String businessType) {
		DataModule bean = dataModuleService.getDataModule(reportType,businessType);
		return JSONObject.parseObject(bean.getModuleData());
	}
    
	/**
	 * 获取所选机构底层数据集合
	 */
	@Override
	public List<JSONObject> findList(String startDate, String endDate,List<String> orgId) {
		List<JSONObject> valveList = new ArrayList<JSONObject>();
		//分隔传过来的开始结束时间
		String[] startYAndM = startDate.split("/");
		String[] endYAndM = endDate.split("/");
		//获取选中的子节点数据
		List<Organization> codeSonList = organizaCodeService.organization(orgId);
		List<String> typeIdList = new ArrayList<String>();
		//将底层数据id拿出来
        for (int i = 0; i < codeSonList.size(); i++) {
        	typeIdList.add(codeSonList.get(i).getId());
		} 
        //查找对应的数据集合
        Map<Object, Object> map = new HashMap<>();
        map.put("typeId", typeIdList);
        map.put("startYear", startYAndM[0]);
        map.put("endYear", endYAndM[0]);
        map.put("startMonth", startYAndM[1]);
        map.put("endMonth", endYAndM[1]);
    	List<BusinessData> BusinessDataList = businessDataService.listBusinessDataByIdAndDate(map);
    	//将查询得来的数据整合添加
    	for (int j = 0; j < BusinessDataList.size(); j++) {
    		valveList.add(JSONObject.parseObject(BusinessDataList.get(j).getInfo()));
		}
		
		return valveList;
	}
    
    /*
     * 将符合规则的内容字段进行统计(non-Javadoc)
     * @see cn.financial.service.StatisticService#getStatic(net.sf.json.JSONArray)
     */
	public JSONObject jsonCalculation(String reportType, String businessType, String startDate, String endDate, List<String> orgId) {
		//获取模板
		JSONObject model = findModel(reportType,businessType);
		//获取数据
		List<JSONObject> valueList = findList(startDate,endDate,orgId);
		//开始数据计算
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
					Double valve = jsonValve.getDouble(itemKey);
					itemKey = itemKey.replaceAll("\\.", "_");
					if(item.containsKey(itemKey)){
						valve +=(Double)item.get(itemKey);
					}
					item.put(itemKey, valve);
				}
			}
		}
		
		Iterator<String> it = model.keySet().iterator();
		while (it.hasNext()) {
			JSONArray json = new JSONArray();
			String modelKey = it.next();
			JSONArray modelArr = model.getJSONArray(modelKey);
			for (int i = 0; i < modelArr.size(); i++) {
				JSONObject rowjar = modelArr.getJSONObject(i);
				//判断输入是否是需要整合的
				Integer type = rowjar.getInteger("type");
				String itemKey = rowjar.getString("key");
				String formula = rowjar.getString("reallyFormula");
				itemKey = itemKey.replaceAll("\\.", "_");
				formula = formula.replaceAll("\\.", "_");
				if (type ==2 ||type == 4){
					//将数据添加到新json里
					rowjar.put("value", item.get(itemKey));
				}else if(type ==3 && !formula.contains("SUM")){
					rowjar.put("value", FormulaUtil.calculationByFormula(item,formula));
				}
				json.add(rowjar);
			}
			model.put(modelKey, json);
		}
		
		return model;
	}

}
