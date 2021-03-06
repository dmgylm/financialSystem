package cn.financial.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.BusinessData;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.response.ResultUtils;

/**
 * 组织结构service接口层
 * 
 * @author zlf 2018/3/9
 *
 */
public interface OrganizationService {

	/**
	 * 新增组织架构(事务方式,包含组织架构入库及预算表数据处理)
	 * @param orgName
	 * @param orgType
	 * @param parentOrgId
	 * @param userId
	 * @return
	 */
	ResultUtils addOrgAndBusiniseData(String orgName,Integer orgType,String parentOrgId,String userId);
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
    List<Organization> listOrganizationBy(String orgName,String createTime,String updateTime,String id,String code,String uId,String parentId,Integer orgType,String orgkey);
   

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
    Integer updateOrganizationById(String orgName,String id,Integer orgType,String uId);

    /**
     * 接口（根据ID删除组织结构信息(先判断此节点下是否存在未停用的子节点，若存在，则返回先删除子节点;否则继续停用此节点)）
     * 
     * @param id
     * @return
     */
    Integer deleteOrganizationById(String id, User user);

    /**
     * 接口（伪删除 <根据组织结构ID修改状态为0，即已删除> ）,停用(级联停用，将此节点下的所有子节点停用)
     * 
     * @param id
     * @return
     */
    Integer deleteOrganizationByStatusCascade(String uId, String id);

    /**
     * 根据id查询该节点下的所有子节点,构建成树
     * 
     * @param id
     * @return
     */
    JSONObject treeByIdForSon(String id);
    /**
     * 根据id查询该节点下有没有部门级别
     * 
     * @param id
     * @return
     */
    Integer treeByIdForSonList(String id);
    /**
     * 根据id查询该节点下有没有公司级别
     * 
     * @param id
     * @return
     */
    Integer treeByIdForSonSum(String id);
    /**
     * 根据id查询该节点下的公司级别有没有部门级别
     * 
     * @param id
     * @return
     */
    Integer treeByIdForSonOryType(String id);
    /**
     * 根据id查询该节点下的部门级别 ,并且增加预算表生成
     * 
     * @param id
     * @param listId 
     * @return
     */
    void treeByIdForSonShow( Map<String, String> before, Map<String, String> map);
    /**
     * 根据id查询该节点下的部门级别 
     * 
     * @param id
     * @return
     */
    Map<String, String> treeByIdForSonAfter(String id);
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
    Integer moveOrganization(User user, String id, String parentOrgId);

    /**
     * 根据条件判断是否该节点存在子节点
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
    
    /**
     * 获取所有部门
     * @return
     */
    List<Organization> getDep();
    
    /**
     * 根据某个节点，查询到父级的某个节点
     * @param id
     * @param orgKey
     * @return
     */
    Organization getOrgaUpFromOne(String id, String orgKey);
    
    /**
     * 根据orgKey查询节点
     * @param orgKey
     * @return
     */
    Organization getOrgaByKey(String orgKey);

    /**
     * 获取组织结构为公司的数据
     * 
     * @return
     */
    List<Organization> getCompany();

    List<Organization> listOrganization(List<String> ids);

    List<Organization>  treeByIdForSons(String getoId);

	List<BusinessData> listBusinessList(String reportType,String startDate, String endDate, List<String> ids);

	ResultUtils checkOrgData(Organization bean, String parentId,boolean isMove);
	
	ResultUtils checkOrgData(Organization bean,String parentId,Integer upOrgType,boolean isMove);

	ResultUtils moveOrg(String id, String parentId, String userId);

	Integer getNodeType(JSONObject json);

	ResultUtils doUpdateOrg(String id, String orgName, Integer orgType,String userId);
	
	Organization getOrgById(String id);

/*    List<Organization> listOrganizationcode(List<String> listmap);*/
    

}
