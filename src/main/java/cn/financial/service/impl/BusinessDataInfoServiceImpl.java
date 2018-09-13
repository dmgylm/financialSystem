package cn.financial.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * 
 * @author lmn
 *
 */
@Service("BusinessDataInfoServiceImpl")
public class BusinessDataInfoServiceImpl implements BusinessDataInfoService {
	@Autowired
	private BusinessDataInfoDao businessDataInfoDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer insertBusinessDataInfo(BusinessDataInfo businessData) {
		return businessDataInfoDao.insertBusinessDataInfo(businessData);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteBusinessDataInfo(String id) {
		return businessDataInfoDao.deleteBusinessDataInfo(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer updateBusinessDataInfo(BusinessDataInfo businessData) {
		return businessDataInfoDao.updateBusinessDataInfo(businessData);
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer updateBusinessDataInfoDelStatus(BusinessDataInfo businessData) {
		return businessDataInfoDao.updateBusinessDataInfoDelStatus(businessData);
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
	 * @param dataMjo
	 *            原始模板
	 * @param mo
	 *            前端传来的HTML
	 * @return 返回合并后的模板
	 */
	public JSONObject dgkey(JSONObject dataMjo, Map<String, Object> mo, String type) {
		JSONObject newBudgetHtml = new JSONObject();
		JSONObject ja = new JSONObject();
		if (type.equals("BUDGET")) {// 预算
			for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
				String keyIncome = iterIncome.next();
				Object objIncome = dataMjo.get(keyIncome);
				if (keyIncome.equals("title")) {
					ja.put(keyIncome, dataMjo.get(keyIncome));
				} else {
					if (objIncome instanceof JSONArray) {
						newBudgetHtml = mergeHtml(dataMjo, mo);
					} else {
						JSONObject longData = (JSONObject) objIncome;
						newBudgetHtml = mergeHtml(longData, mo);
					}
					ja.put(keyIncome, newBudgetHtml);
				}
			}
			return ja;
		} else if (type.equals("PROFIT_LOSS")) {// 损益
			for (Iterator<String> iterIncome = dataMjo.keySet().iterator(); iterIncome.hasNext();) {// 获得损益模板最外层key
				String keyIncome = iterIncome.next();
				Object objIncome = dataMjo.get(keyIncome);
				if (objIncome instanceof JSONArray) {
					newBudgetHtml = mergeHtml(dataMjo, mo);
				} else {
					JSONObject longData = (JSONObject) objIncome;
					newBudgetHtml = mergeHtml(longData, mo);
				}
			}
			return newBudgetHtml;
		}

		return null;
	}

	/**
	 * 根据前端传来的表格进行公式计算之后进行合并
	 * 
	 * @param dataMjo
	 * @param mo
	 * @return
	 */
	public JSONObject mergeHtml(JSONObject dataMjo, Map<String, Object> mo) {
		JSONObject newBudgetHtml = new JSONObject();
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

						if (mo.containsKey(longJson.get("key"))) {
							double value = 0.0;
							if (Integer.parseInt(longJson.get("type").toString()) == HtmlGenerate.BOX_TYPE_FORMULA) {// 判断当前类型是不是公式
								value = FormulaUtil.calculationByFormula(mo, longJson.get("reallyFormula").toString());
							} else {
								value = Double.parseDouble((mo.get(longJson.get("key")).toString()).toString());
							}
							obj.put("value", value);
						}
					}
					jsoA.add(obj);
					;
				}
			}
			newBudgetHtml.put(keyIncome, jsoA);
		}
		return newBudgetHtml;
	}
}
