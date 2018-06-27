package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.OrganizationMove;

/**
 * 组织架构信息Service接口层
 * @author C.s
 */
public interface OrganizationMoveService {
	
	/**
	 * 新增
	 * @param organizationMove
	 * @return
	 */
	Integer saveOrganizationMove(OrganizationMove organizationMove);
	
	/**
	 * 查询
	 * @param map
	 * @return
	 */
	List<OrganizationMove> listOrganizationMoveBy(Map<Object, Object> map);

}