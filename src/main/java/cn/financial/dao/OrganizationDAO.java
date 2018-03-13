package cn.financial.dao;

import java.util.List;
import java.util.Map;

import cn.financial.model.Organization;

/**
 * 组织结构Dao层
 * 
 * @author zlf 2018/3/9
 */
public interface OrganizationDAO {

    /**
     * 接口（新增组织结构）
     * 
     * @param organization
     * @return
     */
    Integer saveOrganization(Organization organization);

    /**
     * 接口（查询所有的组织结构）
     * 
     * @return
     */
    List<Organization> listOrganization();

    /**
     * 接口（条件查询组织结构信息）
     * 
     * @param map
     * @return
     */
    List<Organization> listOrganizationBy(Map<Object, Object> map);

    /**
     * 接口（根据id查询组织结构信息）
     * 
     * @param id
     * @return
     */
    Organization getOrganizationById(String id);

    /**
     * 接口（根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id）
     * 
     * @param map
     * @return
     */
    Integer updateOrganizationById(Map<Object, Object> map);

    /**
     * 接口（根据ID删除组织结构信息）
     * 
     * @param id
     * @return
     */
    Integer deleteOrganizationById(String id);

    /**
     * 接口（伪删除 <根据组织结构ID修改状态为0，即已删除> ）
     * 
     * @param id
     * @return
     */
    Integer deleteOrganizationByStatus(String id);

}
