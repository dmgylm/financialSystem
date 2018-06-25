package cn.financial.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.financial.dao.DataModuleDao;
import cn.financial.model.DataModule;
import cn.financial.service.DataModuleService;

@Service("DataModuleServiceImpl")
public class DataModuleServiceImpl implements DataModuleService{

	@Autowired
	private DataModuleDao dataModuleDao;
	
	
	@Override
	public List<DataModule> listDataModule(Map<Object, Object> map) {
		return dataModuleDao.listDataModule(map);
	}

	@Override
	public Integer insertDataModule(DataModule dataModule) {
		return dataModuleDao.insertDataModule(dataModule);
	}

	@Override
	public Integer updateDataModule(String dataModuleId) {
		return dataModuleDao.updateDataModule(dataModuleId);
	}

	@Cacheable(value="dataModule",key="'dataModule_'+#dataModuleId")
	@Override
	public DataModule getDataModule(String dataModuleId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", dataModuleId);
		return dataModuleDao.getDataModule(params);
	}

	@Cacheable(value="dataModule",key="'newestDataModule_'+#reportType+#businessType")
	@Override
	public DataModule getDataModule(String reportType, String businessType) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("reportType", reportType);
		params.put("businessType", businessType);
		return dataModuleDao.getDataModule(params);
	}

	
}
