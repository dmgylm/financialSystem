package cn.financial.dao;

import java.util.List;
import java.util.Map;

import cn.financial.model.OrganizationMove;

/**
 * 组织架构信息记录Dao层
 * 
 * @author C.s
 */
public interface OrganizationMoveDao {
	/**
	 * 接口(新增组织架构信息记录表)
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