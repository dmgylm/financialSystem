package cn.financial.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.Organization;
import cn.financial.model.UserOrganization;

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
     * 接口（条件查询组织结构信息）
     * 
     * @param map
     * @return
     */
    List<Organization> listOrganizationBy(Map<Object, Object> map);
    
    /**
     * 接口（条件查询组织结构信息(包含状态为0和1)）
     * 
     * @param map
     * @return
     */
    List<Organization> listAllOrganizationBy(Map<Object, Object> map);

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

    /**
     * 根据code查询该节点下的所有子节点,构建成树
     * 
     * @param code
     * @return
     */
    List<Organization> listTreeByCodeForSon(String code);

    /**
     * 根据code查询该节点下的所有父节点
     * 
     * @param code
     */
    List<Organization> listTreeByCodeForParent(String code);
    
    /**
     * 获取组织结构为公司的数据
     * @return
     */
    List<Organization> getCompany();
  
    /**
     * 查询通过id查询的his_permission值
     * @param list
     * @return
     */
    List<Organization> listOrganization(@Param("list") List<String> list);
    
    /**
     * 通过his_permission查询相对应的数据
     * @param code
     * @return
     */
    List<Organization> listOrganizationcode(@Param("code") List<String> code);

	Integer  saveUserOrganization(UserOrganization lists);

	List<Organization> listCode(@Param("code")String code);

	


}
