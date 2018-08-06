package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.BusinessDataBuildDao;
import cn.financial.model.BusinessDataBuild;
import cn.financial.service.BusinessDataBuildService;

@Service("BusinessDataBuildServiceImpl")
public class BusinessDataBuildServiceImpl implements BusinessDataBuildService{
	
	@Autowired
	private BusinessDataBuildDao dao;
	
	@Override
	public Integer saveBusinessDataBuild(BusinessDataBuild businessDataBuild) {
		return dao.saveBusinessDataBuild(businessDataBuild);
	}
	
	@Override
	public Integer deleteBusinessDataBuild() {
		return dao.deleteBusinessDataBuild();
	}
	
	@Override
	public Integer updateBusinessDataBuild(Map<Object, Object> map) {
		return dao.updateBusinessDataBuild(map);
	}
	
	@Override
	public List<BusinessDataBuild> listBusinessDataBuild(String id){
		return dao.listBusinessDataBuild(id);
	}

}
