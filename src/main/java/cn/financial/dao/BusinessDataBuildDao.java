package cn.financial.dao;

import java.util.List;
import java.util.Map;

import cn.financial.model.BusinessDataBuild;

public interface BusinessDataBuildDao {
	
	/**
	 * 新增
	 * @param businessDataBuild
	 * @return
	 */
	Integer saveBusinessDataBuild(BusinessDataBuild businessDataBuild);
	
	/**
	 * 清空表
	 * @return
	 */
	Integer deleteBusinessDataBuild();
	
	/**
	 * 修改
	 * @param map
	 * @return
	 */
	Integer updateBusinessDataBuild(Map<Object, Object> map);
	
	/**
	 * 查询
	 * @param map
	 * @return
	 */
	List<BusinessDataBuild> listBusinessDataBuild(Integer status);

}
