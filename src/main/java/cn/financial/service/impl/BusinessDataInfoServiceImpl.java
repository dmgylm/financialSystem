package cn.financial.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.BusinessDataDao;
import cn.financial.dao.BusinessDataInfoDao;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.service.BusinessDataInfoService;
import cn.financial.util.FormulaUtil;
import cn.financial.util.HtmlGenerate;

/**
 * 损益表Service
 * @author lmn
 *
 */
@Service("BusinessDataInfoServiceImpl")
public class BusinessDataInfoServiceImpl implements BusinessDataInfoService{
	@Autowired
	private BusinessDataInfoDao businessDataInfoDao;
	@Override
	public Integer insertBusinessDataInfo(BusinessDataInfo businessData) {
		return businessDataInfoDao.insertBusinessDataInfo(businessData);
	}

	@Override
	public Integer deleteBusinessDataInfo(String id) {
		return businessDataInfoDao.deleteBusinessDataInfo(id);
	}

	@Override
	public Integer updateBusinessDataInfo(BusinessDataInfo businessData) {
		return businessDataInfoDao.updateBusinessDataInfo(businessData);
	}

	@Override
	public List<BusinessData> getAll(Map<Object, Object> map) {
		return businessDataInfoDao.getAll(map);
	}

	@Override
	public BusinessDataInfo selectBusinessDataById(String id) {
		return businessDataInfoDao.selectBusinessDataById(id);
	}
	
	/**
	 * 
	 * @param dataMjo 原始模板
	 * @param mo   前端传来的HTML
	 * @return 返回合并后的模板
	 */
	public JSONObject dgkey(JSONObject dataMjo, Map<String,Object> mo) {
		JSONObject newBudgetHtml=new JSONObject();
		for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
			String keyIncome = iterIncome.next();
			Object objIncome = dataMjo.get(keyIncome);
			if (objIncome instanceof JSONArray) {
				newBudgetHtml.put(keyIncome, mergeHtml(dataMjo, mo));
			} else {
				JSONObject longData = (JSONObject) objIncome;
				newBudgetHtml.put(keyIncome, mergeHtml(longData, mo));
			}
		}
		return newBudgetHtml;
	}
	/**
	 * 根据前端传来的表格进行公式计算之后进行合并
	 * @param dataMjo
	 * @param mo
	 * @return
	 */
	public JSONObject mergeHtml (JSONObject dataMjo,Map<String,Object> mo) {
		JSONObject newBudgetHtml=new JSONObject();
		JSONObject obj = new JSONObject();
		for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
			String keyIncome = iterIncome.next();
			Object objIncome = dataMjo.get(keyIncome);
			JSONArray jsoA = new JSONArray();
			if (objIncome instanceof JSONArray) {
			JSONArray longLst = (JSONArray) objIncome;
				for (int j = 0; j < longLst.size(); j++) {
					 obj = new JSONObject();
					JSONObject longJson = longLst.getJSONObject(j);
					for (Iterator<String> iters = longJson.keySet().iterator(); iters.hasNext();) {
						String keysIncomes = iters.next();
						Object objsIncome = longJson.get(keysIncomes);
						obj.put(keysIncomes, objsIncome);
						
						if(mo.containsKey(longJson.get("key"))) {
							Double value= 0.0;
							if(Integer.parseInt(longJson.get("type").toString())==HtmlGenerate.BOX_TYPE_FORMULA) {//判断当前类型是不是公式
								 value=FormulaUtil.calculationByFormula(mo,longJson.get("reallyFormula").toString());
							}else {
								value=Double.valueOf(mo.get(longJson.get("key")).toString());
								
							}
							obj.put("value", value);
						}
					}
					jsoA.add(obj);;
				}
			}
		newBudgetHtml.put(keyIncome, jsoA);
		}
		return newBudgetHtml ;
	}
}
