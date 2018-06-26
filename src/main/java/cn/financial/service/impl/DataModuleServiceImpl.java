package cn.financial.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.financial.dao.DataModuleDao;
import cn.financial.exception.FormulaAnalysisException;
import cn.financial.model.DataModule;
import cn.financial.service.DataModuleService;
import cn.financial.service.RedisCacheService;
import cn.financial.util.HtmlAnalysis;

@Service("DataModuleServiceImpl")
public class DataModuleServiceImpl implements DataModuleService{

	@Autowired
	private DataModuleDao dataModuleDao;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	/**
	 * 获取模板列表
	 */
	public List<DataModule> listDataModule(Map<Object, Object> map) {
		return dataModuleDao.listDataModule(map);
	}

	public Integer insertDataModule(DataModule dataModule) {
		return dataModuleDao.insertDataModule(dataModule);
	}

	public Integer updateDataModule(String dataModuleId) {
		return dataModuleDao.updateDataModuleState(dataModuleId);
	}

	/**
	 * 根据模板ID获取模板数据
	 */
	@Cacheable(value="dataModule",key="'dataModule_'+#dataModuleId")
	public DataModule getDataModule(String dataModuleId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", dataModuleId);
		return dataModuleDao.getDataModule(params);
	}

	/**
	 * 根据报表类型及业务类型获取最新模板数据
	 */
	@Cacheable(value="dataModule",key="'newestDataModule_'+#reportType+#businessType")
	public DataModule getDataModule(String reportType, String businessType) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("reportType", reportType);
		params.put("businessType", businessType);
		return dataModuleDao.getDataModule(params);
	}

	/**
	 * 编辑模板
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editDataModule(String reportType,
			String businessType, String html, Integer firstRowNum,
			Integer secondRowNum, Integer firstColNum, Integer secondColNum)
			throws FormulaAnalysisException {
		HtmlAnalysis htmlAnalysis = new HtmlAnalysis(html, firstRowNum, secondRowNum, firstColNum, secondColNum);
		String json = htmlAnalysis.analysis();
		DataModule dataModule = getDataModule(reportType,businessType);
		Integer versionNumber = 1;
		if(dataModule != null) {
			versionNumber = Integer.parseInt(dataModule.getVersionNumber());
		}
		dataModule.setStatue(DataModule.STATUS_NOVALID);
		dataModuleDao.updateDataModuleState(dataModule.getId());
		
		DataModule bean = new DataModule();
		bean.setVersionNumber(String.valueOf(versionNumber+1));
		bean.setBusinessType(businessType);
		bean.setReportType(reportType);
		bean.setModuleData(json);
		bean.setStatue(DataModule.STATUS_CONSUMED);
		dataModuleDao.insertDataModule(bean);
		//清除数据模板缓存
		redisCacheService.removeAll("dataModule");
	}
	
}
