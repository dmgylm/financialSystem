package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.OrganizationDAO;
import cn.financial.model.Organization;
import cn.financial.service.OrganizationService;

@Service("OrganizationServiceImpl")
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDAO organizationDAO;

    /**
     * 新增组织结构
     */
    public Integer saveOrganization(Map<Object, Object> map) {
        return organizationDAO.saveOrganization(map);
    }

    /**
     * 查询所有的组织结构
     */
    public List<Organization> listOrganization() {
        return organizationDAO.listOrganization();
    }

    /**
     * 根据条件查询组织结构信息
     */
    public List<Organization> listOrganizationBy(Map<Object, Object> map) {
        return organizationDAO.listOrganizationBy(map);
    }

    /**
     * 根据ID查询组织结构信息
     */
    public Organization getOrganization(String id) {
        return organizationDAO.getOrganization(id);
    }

    /**
     * 根据条件修改组织结构信息
     */
    public Integer updateOrganization(Map<Object, Object> map) {
        return organizationDAO.updateOrganization(map);
    }

    /**
     * 根据条件删除组织结构信息
     */
    public Integer deleteOrganization(String id) {
        return organizationDAO.deleteOrganization(id);
    }
}
