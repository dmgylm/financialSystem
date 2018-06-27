package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.OrganizationMoveDao;
import cn.financial.model.OrganizationMove;
import cn.financial.service.OrganizationMoveService;

/**
 * 组织架构信息Service实现层
 * @author C.s
 */
@Service("OrganizationMoveServiceImpl")
public class OrganizationMoveServiceImpl implements OrganizationMoveService{
	
	@Autowired
	private OrganizationMoveDao omDao;
	
	/**
	 * 新增
	 */
	public Integer saveOrganizationMove(OrganizationMove organizationMove) {
		return omDao.saveOrganizationMove(organizationMove);
	}
	
	/**
	 * 查询
	 */
	public List<OrganizationMove> listOrganizationMoveBy(Map<Object, Object> map) {
        return omDao.listOrganizationMoveBy(map);
    }

}