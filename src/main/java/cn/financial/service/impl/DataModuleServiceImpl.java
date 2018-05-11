package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Integer deleteDataModule(String dataModuleId) {
		return dataModuleDao.deleteDataModule(dataModuleId);
	}

	
}
