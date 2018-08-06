package cn.financial.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.financial.model.BusinessDataBuild;

@Service
public interface BusinessDataBuildService {
	
	Integer saveBusinessDataBuild(BusinessDataBuild businessDataBuild);
	
	Integer deleteBusinessDataBuild();
	
	Integer updateBusinessDataBuild(Map<Object, Object> map);
	
	List<BusinessDataBuild> listBusinessDataBuild(Map<Object, Object> map);

}
