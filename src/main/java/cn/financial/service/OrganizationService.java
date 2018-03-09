package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.Organization;

public interface OrganizationService {

    /**
     * 接口（新增组织结构）
     * 
     * @param map
     * @return
     */
    Integer saveOrganization(Map<Object, Object> map);

    /**
     * 接口（查询所有的组织结构）
     * 
     * @return
     */
    List<Organization> listOrganization();

    /**
     * 接口（条件查询组织结构信息）
     * 
     * @param id
     * @return
     */
    List<Organization> listOrganizationBy(Map<Object, Object> map);

    /**
     * 接口（根据id查询组织结构信息）
     * 
     * @param id
     * @return
     */
    Organization getOrganization(String id);

    /**
     * 接口（根据条件修改组织结构信息）
     * 
     * @param id
     * @return
     */
    Integer updateOrganization(Map<Object, Object> map);

    /**
     * 接口（根据条件删除组织结构信息）
     * 
     * @param id
     * @return
     */
    Integer deleteOrganization(String id);

}
