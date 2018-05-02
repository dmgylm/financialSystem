package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.Organization;
import net.sf.json.JSONObject;

/**
 * 组织结构service接口层
 * 
 * @author zlf 2018/3/9
 *
 */
public interface OrganizationService {

    /**
     * 接口（新增组织结构）
     * 
     * @param organization
     * @param parentOrgId
     * @return
     */
    Integer saveOrganization(Organization organization, String parentOrgId);

    /**
     * 接口（条件查询组织结构信息）
     * 
     * @param map
     * @return
     */
    List<Organization> listOrganizationBy(Map<Object, Object> map);

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
     * 接口（伪删除 <根据组织结构ID修改状态为0，即已删除> ）,停用(单条停用)
     * 
     * @param id
     * @return
     */
    Integer deleteOrganizationByStatus(String id);

    /**
     * 接口（伪删除 <根据组织结构ID修改状态为0，即已删除> ）,停用(级联停用，将此节点下的所有子节点停用)
     * 
     * @param id
     * @return
     */
    Integer deleteOrganizationByStatusCascade(String id);

    /**
     * 根据id查询该节点下的所有子节点,构建成树
     * 
     * @param id
     * @return
     */
    JSONObject TreeByIdForSon(String id);

    /**
     * 根据id查询该节点下的所有子节点集合
     * 
     * @param id
     * @return
     */
    List<Organization> listTreeByIdForSon(String id);

    /**
     * 根据id查询该节点的所有父节点
     * 
     * @param id
     */
    List<Organization> listTreeByIdForParent(String id);

    /**
     * 移动组织机构
     * 
     * @param id
     * @param parentOrgId
     * @return
     */
    Integer moveOrganization(String id, String parentOrgId);

    /**
     * 根据id或者name判断是否该节点存在子节点（这里的name主要是指公司名称，查询该公司是否有部门；其他节点只能通过id查询）
     * 
     * @param id
     * @return
     */
    Boolean hasOrganizationSon(Map<Object, Object> map);

    /**
     * 根据公司以下节点的id，查询到该节点所属公司
     * 
     * @param id
     * @return
     */
    Organization getCompanyNameBySon(String id);

}
