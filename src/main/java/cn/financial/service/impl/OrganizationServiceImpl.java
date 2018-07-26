package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.OrganizationDAO;
import cn.financial.dao.OrganizationMoveDao;
import cn.financial.model.Organization;
import cn.financial.model.OrganizationMove;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.service.OrganizationService;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;

/**
 * 组织结构service实现层
 * 
 * @author zlf 2018/3/9
 *
 */
@Service("OrganizationServiceImpl")
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
	private Logger logger = Logger.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private OrganizationMoveDao moveDao;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private UserOrganizationServiceImpl userorganization;
 
    /**
     * 新增组织结构
     */
    @CacheEvict(value = "organizationValue", allEntries = true)
    public Integer saveOrganization(Organization organization, String parentOrgId) {
        String code = null;
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("code", organization.getParentId());
        // 找到父节点信息
        Map<Object, Object> mapparentOrgId = new HashMap<>();
        mapparentOrgId.put("id", parentOrgId);
        List<Organization> org = organizationDAO.listOrganizationBy(mapparentOrgId);
        if (!CollectionUtils.isEmpty(org)) {
            // 找到兄弟节点信息
            Map<Object, Object> mapbro = new HashMap<>();
            mapbro.put("parentId", org.get(0).getCode());
            List<Organization> list = organizationDAO.listAllOrganizationBy(mapbro);
            // 若存在兄弟节点，则兄弟节点的code找到该节点的code
            if (!CollectionUtils.isEmpty(list)) {
                List<String> codes = new ArrayList<String>();
                for (Organization orga : list) {
                    codes.add(orga.getCode());
                }
                code = UuidUtil.getCodeByBrother(org.get(0).getCode(), codes);
            }
            // 若不存在兄弟节点，那code就是父节点的code加上01
            else {
                code = org.get(0).getCode() + "01";
            }
            organization.setParentId(org.get(0).getCode());// 父id
            organization.setCode(code); // 该组织机构节点的序号，两位的，比如（01；0101，0102）
            organization.setHis_permission(code); // 新增时，历史权限id就是此节点的code
            organization.setOrgkey(UuidUtil.getUUID());// 与模版对应的一个唯一值
            organization.setOrgPlateId(org.get(0).getOrgPlateId());// 板块Id
            return organizationDAO.saveOrganization(organization);
        } else {
            return 0;
        }
    }

    /**
     * 根据条件查询组织结构信息
     */
    // @Cacheable(value = "organizationValue" , key = "'orga_key_listby_'+#map.toString()")
    public List<Organization> listOrganizationBy(String orgName,String createTime,String updateTime,String id,String code,String uId,String parentId,Integer orgType) {
    	Map<Object, Object> map=new HashMap<Object, Object>();
    	if (null !=orgName && !"".equals(orgName)) {
        		map.put("orgName", orgName);
         }
         if (null !=createTime && !"".equals(createTime)) {
        	map.put("createTime",createTime);
         }
         if (null !=updateTime && !"".equals(updateTime)) {
        	map.put("updateTime",updateTime);
         }
         if (null !=id && !"".equals(id)) {
        	map.put("id", id);
         }
         if (null !=code && !"".equals(code)) {
        	map.put("code", code);
         }
         if (null !=uId && !"".equals(uId)) {
        	map.put("uId", uId);
         }
         if (null !=parentId && !"".equals(parentId)) {
        	map.put("parentId", parentId);
         }
         if (null !=orgType && !"".equals(orgType)) {
        	map.put("orgType", orgType);
         }
    
    	return organizationDAO.listOrganizationBy(map);
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     */
    @CacheEvict(value = "organizationValue", allEntries = true)
    public Integer updateOrganizationById(String orgName,String id,Integer orgType,String uId) {
    	Map<Object, Object> map=new HashMap<Object, Object>();
    	 if (null !=orgName && !"".equals(orgName)) {
    		 map.put("orgName", orgName);
         }
     	 if (null !=id && !"".equals(id)) {
     		 map.put("id", id);
         }
     	 if (null !=orgType && !"".equals(orgType)) {
         	map.put("orgType", orgType);
         }
     	 if (null !=uId && !"".equals(uId)) {
     		map.put("uId", uId);
         }
        return organizationDAO.updateOrganizationById(map);
    }

    /**
     * 根据ID删除组织结构信息(先判断此节点下是否存在未停用的子节点，若存在，则返回先删除子节点;否则继续停用此节点)
     */
    @CacheEvict(value = "organizationValue", allEntries = true)
    public Integer deleteOrganizationById(String id, User user) {
        Integer resultInt = 0;
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> org = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(org)) {
            // 修改此节点的操作人为当前登录人
            Map<Object, Object> idmap = new HashMap<>();
            idmap.put("id", id);
            idmap.put("uId", user.getId());
            Integer upInteg = organizationDAO.updateOrganizationById(idmap);
            if (Integer.valueOf(1).equals(upInteg)) {
                resultInt = organizationDAO.deleteOrganizationByStatus(id);
            }
        }
        return resultInt;
    }

    /**
     * 停用(级联停用，将此节点下的所有子节点停用)，根据组织结构ID修改状态为0，即已停用
     */
    @Transactional
    @CacheEvict(value = "organizationValue", allEntries = true)
    public Integer deleteOrganizationByStatusCascade(String uId, String id) {
        Integer i = 0;
        // 所有的组织结构
        List<Organization> departList = organizationDAO.listOrganizationBy(new HashMap<Object, Object>());
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> org = organizationDAO.listOrganizationBy(map);
        List<Organization> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(org)) {
            list.add(org.get(0));
            if (!CollectionUtils.isEmpty(departList)) {
                // 递归在所有的组织结构中找到我们需要的组织结构极其下所有子结构
                getOrganizationSonList2(departList, list, org.get(0).getCode());
            }
        }
        Iterator<Organization> iterator = list.iterator();
        while (iterator.hasNext()) {
            Organization orga = iterator.next();
            Map<Object, Object> idmap = new HashMap<>();
            idmap.put("id", orga.getId());
            idmap.put("uId", uId);
            // 先修改uid
            Integer idupdate = organizationDAO.updateOrganizationById(idmap);
            if (Integer.valueOf(1).equals(idupdate)) {
                i = organizationDAO.deleteOrganizationByStatus(orga.getId());
            }
        }
        return i;
    }

    /**
     * 根据id查询该节点下的所有子节点,构建成树
     */
    @Override
    @Cacheable(value = "organizationValue", key = "'orga_key_treeById_'+#id")
    public  JSONObject TreeByIdForSon(String id) {
        List<Organization> list = new ArrayList<>();
        // 所有的组织结构
        List<Organization> departList = organizationDAO.listOrganizationBy(new HashMap<Object, Object>());
        if (id == null || "".equals(id)) {
            list.addAll(departList);
        } else {
            // 根据id查询到该节点信息
            Map<Object, Object> map = new HashMap<>();
            map.put("id", id);
            // 当前节点
            List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
            if (!CollectionUtils.isEmpty(organizationByIds)) {
                list.add(organizationByIds.get(0));
                if (!CollectionUtils.isEmpty(departList)) {
                    // sql函数递归查询
                    // list =
                    // organizationDAO.listTreeByCodeForSon(organizationByIds.get(0).getCode());
                    // java代码递归查询
                    // getOrganizationSonList(list,
                    // organizationByIds.get(0).getCode());
                    // 递归在所有的组织结构中找到我们需要的组织结构极其下所有子结构
                    getOrganizationSonList2(departList, list, organizationByIds.get(0).getCode());
                }
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            List<TreeNode<Organization>> nodes = new ArrayList<>();
            for (Organization organization : list) {
                Organization organizationList = organizationService.getOrgaByKey(organization.getOrgPlateId());
                TreeNode<Organization> node = new TreeNode<>();
                node.setId(organization.getCode());
                node.setParentId(organization.getParentId().toString());
                node.setName(organization.getOrgName());
                node.setOrgkeyName(organizationList.getOrgName());
                node.setOrgType(organization.getOrgType().toString());
                // node.setNodeData(organization);
                node.setPid(organization.getId());
                node.setOrgPlateId(organization.getOrgPlateId());
                nodes.add(node);
            }
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
            return jsonObject;
        }
        return null;
    }

    /**
     * 根据id查询该节点极其下的所有子节点集合
     */
    @Override
    @Cacheable(value = "organizationValue", key = "'orga_key_listson_'+#id")
    public List<Organization> listTreeByIdForSon(String id) {
        List<Organization> list = new ArrayList<>();
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        // 所有的组织结构
        List<Organization> departList = organizationDAO.listOrganizationBy(new HashMap<Object, Object>());
        // 当前节点
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(organizationByIds)) {
            list.add(organizationByIds.get(0));
            if (!CollectionUtils.isEmpty(departList)) {
                // sql函数递归查询
                // list =
                // organizationDAO.listTreeByCodeForSon(organizationByIds.get(0).getCode());
                // java代码递归查询
                // getOrganizationSonList(list,
                // organizationByIds.get(0).getCode());
                // 递归在所有的组织结构中找到我们需要的组织结构极其下所有子结构
                getOrganizationSonList2(departList, list, organizationByIds.get(0).getCode());
            }
        }
        return list;
    }

    /**
     * @Description: 在所有的组织结构中循环递归找到我们需要的组织结构极其下所有子结构
     * @param departList
     * @param result
     */
    public void getOrganizationSonList2(List<Organization> departList, List<Organization> result, String code) {
        try {
            if (!CollectionUtils.isEmpty(departList)) {
                Iterator<Organization> iterator = departList.iterator();
                while (iterator.hasNext()) {
                    Organization o = iterator.next();
                    if (code.equals(o.getParentId())) {
                        result.add(o);
                        getOrganizationSonList2(departList, result, o.getCode());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 递归查询组织机构
     * @param @param
     *            departList
     * @param @param
     *            departId 设定文件
     * @return
     */
    public void getOrganizationSonList(List<Organization> departList, String departId) {
        try {
            Map<Object, Object> map = new HashMap<>();
            map.put("parentId", departId);
            List<Organization> list = organizationDAO.listOrganizationBy(map);
            if (!CollectionUtils.isEmpty(list)) {
                Iterator<Organization> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Organization organization = iterator.next();
                    departList.add(organization);
                    getOrganizationSonList(departList, organization.getCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id查询该节点的所有父节点
     */
    @Override
    @Cacheable(value = "organizationValue", key = "'orga_key_listparent_'+#id")
    public List<Organization> listTreeByIdForParent(String id) {
        List<Organization> list = new ArrayList<>();
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        // 所有的组织结构
        List<Organization> departList = organizationDAO.listOrganizationBy(new HashMap<Object, Object>());
        // 当前节点
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(organizationByIds)) {
            list.add(organizationByIds.get(0));
            if (!CollectionUtils.isEmpty(departList)) {
                // 递归在所有的组织结构中找到我们需要的节点及所有其父节点
                getOrganizationParentList(departList, list, organizationByIds.get(0).getParentId());
            }
        }
        return list;
    }

    /**
     * 根据id查询该节点及所有其父节点
     * 
     * @param departList
     * @param result
     * @param code
     */
    public void getOrganizationParentList(List<Organization> departList, List<Organization> result, String parentcode) {
        try {
            if (!CollectionUtils.isEmpty(departList)) {
                Iterator<Organization> iterator = departList.iterator();
                while (iterator.hasNext()) {
                    Organization o = iterator.next();
                    if (parentcode.equals(o.getCode())) {
                        result.add(o);
                        getOrganizationParentList(departList, result, o.getParentId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动组织机构
     * 
     * @id 要移动的节点的id
     * @parentOrgId 将要移动到其下的节点的id
     */
    @CacheEvict(value = "organizationValue", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer moveOrganization(User user, String id, String parentOrgId) {
    	//保存code，id键值对
    	Map<Object, Object> icMap = new HashMap<Object, Object>();
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> org = organizationDAO.listOrganizationBy(map);
        if (CollectionUtils.isEmpty(org)) {
            return Integer.valueOf(0);
        }
        // 根据该节点的code查询到其下所有子节点信息的集合(要移动的节点及其子节点的集合)
        List<Organization> list = listTreeByIdForSon(id);// organizationDAO.listTreeByCodeForSon(org.get(0).getCode());
        /*
         * 接下来是将要移动的几点按原来的机构新增到现在的父节点上,
         * 先将该节点新增，并修改其code，parentId，his_permission；然后再新增其子节点
         */
        // 这里是移动后的code
        String code = null;
        // 先判断新的父节点下是否有子节点
        map = new HashMap<>();
        map.put("id", parentOrgId);
        List<Organization> orgParent = organizationDAO.listOrganizationBy(map);
        if (CollectionUtils.isEmpty(orgParent)) {
            return Integer.valueOf(0);
        }
        // 得到新父节点的子节点
        map = new HashMap<>();
        map.put("parentId", orgParent.get(0).getCode());
        List<Organization> listSon = organizationDAO.listAllOrganizationBy(map);
        if (!CollectionUtils.isEmpty(listSon)) {
            List<String> codes = new ArrayList<String>();
            for (Organization orgaSon : listSon) {
                codes.add(orgaSon.getCode());
            }
            code = UuidUtil.getCodeByBrother(orgParent.get(0).getCode(), codes);
        } else {
            code = orgParent.get(0).getCode() + "01";
        }
        Organization organization = new Organization();
        String new_id = UuidUtil.getUUID();
        organization.setId(new_id);
        organization.setOrgName(org.get(0).getOrgName());
        organization.setuId(user.getId());
        organization.setCode(code);
        organization.setParentId(orgParent.get(0).getCode());
        organization.setHis_permission(org.get(0).getHis_permission() + "," + code);
        organization.setOrgkey(org.get(0).getOrgkey());
        organization.setOrgType(org.get(0).getOrgType());
        organization.setOrgPlateId(orgParent.get(0).getOrgPlateId());
        Integer saveInteger = organizationDAO.saveOrganization(organization);
        
        icMap.put(code, new_id);//新节点的code和id
        
        List<UserOrganization> listsize= userorganization.listUserOrganization(user.getId());
        if(listsize.size()>0){
        	userorganization.deleteUserOrganization(user.getId());
        	UserOrganization lists=new UserOrganization();
    		lists.setoId(new_id);
    		lists.setuId(user.getId());//用户id
    		lists.setId(UuidUtil.getUUID());
            organizationDAO.saveUserOrganization(lists);
        }else{
        	UserOrganization lists=new UserOrganization();
    		lists.setoId(new_id);
    		lists.setuId(user.getId());//用户id
    		lists.setId(UuidUtil.getUUID());
    		organizationDAO.saveUserOrganization(lists);
        }
        
        // 父节点新增成功后，在organization_move表中记录
        if (Integer.valueOf(1).equals(saveInteger)) {
            OrganizationMove organizationMove = new OrganizationMove();
            organizationMove.setId(UuidUtil.getUUID());
            organizationMove.setHis_Id(org.get(0).getId());
            organizationMove.setNew_Id(new_id);
            organizationMove.setModifier(user.getId());
            organizationMove.setNewParent_Id(parentOrgId);
            moveDao.saveOrganizationMove(organizationMove);
        }

        /*
         * 停用要移动的节点及其所有的子节点
         */
        Iterator<Organization> iterator = list.iterator();
        while (iterator.hasNext()) {
            Organization orga = iterator.next();
            // 停用
            Integer intde = organizationDAO.deleteOrganizationByStatus(orga.getId());
            if (Integer.valueOf(1).equals(intde)) {
                /*
                 * 添加子节点
                 */
                if (orga.getId() != id && !id.equals(orga.getId())) {
                    // 子节点的code
                    String code1 = orga.getCode().replaceFirst(org.get(0).getCode(), organization.getCode());
                    String parentId1 = orga.getParentId().replaceFirst(org.get(0).getCode(), organization.getCode());
                    Organization organization1 = new Organization();
                    String new_id1 = UuidUtil.getUUID();
                    organization1.setId(new_id1);
                    organization1.setOrgName(orga.getOrgName());
                    organization1.setuId(user.getId());
                    organization1.setCode(code1);
                    organization1.setParentId(parentId1);
                    organization1.setHis_permission(orga.getHis_permission() + "," + code1);
                    organization1.setOrgkey(orga.getOrgkey());
                    organization1.setOrgType(orga.getOrgType());
                    organization1.setOrgPlateId(orgParent.get(0).getOrgPlateId());
                    Integer saveOrganization1 = organizationDAO.saveOrganization(organization1);
                    
                    icMap.put(code1, new_id1);
                    
                    // 子节点新增成功后，在organization_move表中记录
                    if (Integer.valueOf(1).equals(saveOrganization1)) {
                        OrganizationMove organizationMove = new OrganizationMove();
                        organizationMove.setId(UuidUtil.getUUID());
                        organizationMove.setHis_Id(orga.getId());
                        organizationMove.setNew_Id(new_id1);
                        organizationMove.setModifier(user.getId());
                        organizationMove.setNewParent_Id(icMap.get(parentId1).toString());
                        moveDao.saveOrganizationMove(organizationMove);
                    }
                }
            }
        }
        return Integer.valueOf(1);
    }

    /**
     * 根据条件判断是否该节点存在子节点
     */
    @Override
    public Boolean hasOrganizationSon(Map<Object, Object> map) {
        Boolean flag = false;
        // 查询到当前节点
        if (!map.isEmpty() && map.size() > 0) {
            List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
            if (!CollectionUtils.isEmpty(organizationByIds)) {
                Map<Object, Object> mapp = new HashMap<>();
                mapp.put("parentId", organizationByIds.get(0).getCode());
                List<Organization> list = organizationDAO.listOrganizationBy(mapp);
                if (!CollectionUtils.isEmpty(list)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 根据公司以下节点的id，查询到该节点所属公司
     */
    @Override
    public Organization getCompanyNameBySon(String id) {
        if (id != null && !"".equals(id)) {
            List<Organization> parent = listTreeByIdForParent(id);
            if (!CollectionUtils.isEmpty(parent)) {
                for (Organization o : parent) {
                    Integer orgType = o.getOrgType();
                    if (orgType == 2) {
                        return o;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取所有部门
     * 
     * @return
     */
    @Override
    @Cacheable(value = "organizationValue", key = "'orga_key_alldepart'")
    public List<Organization> getDep() {
        /*List<Organization> result = new ArrayList<>();
        // 得到所有组织结构
        List<Organization> orgaAll = organizationDAO.listOrganizationBy(new HashMap<>());
        if (!CollectionUtils.isEmpty(orgaAll)) {
            aaa: for (Organization or : orgaAll) {
                for (Organization organization : orgaAll) {
                    if (or.getCode().equals(organization.getParentId())) {
                        continue aaa;
                    }
                }
                result.add(or);
            }
        }
        return result;*/
        Map<Object, Object> map = new HashMap<>();
        map.put("orgType", 3);
        List<Organization> result = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(result)) {
            return result;
        }
        return null;
    }

    /**
     * 根据某个节点，查询到父级的某个节点
     */
    @Override
    public Organization getOrgaUpFromOne(String id, String orgKey) {
        if ("".equals(orgKey) || orgKey == null || orgKey.length() == 0) {
            return null;
        }
        // 该节点及其父节点的集合
        List<Organization> listTreeByIdForParent = listTreeByIdForParent(id);
        if (!CollectionUtils.isEmpty(listTreeByIdForParent)) {
            for (Organization organization : listTreeByIdForParent) {
                if (organization.getOrgkey().equals(orgKey)) {
                    return organization;
                }
            }
        }
        return null;
    }
    

    @Override
    public List<Organization> getCompany() {
        return organizationDAO.getCompany();
    }


   /**
    * 1.通过节点id查询对应his_permission
    * 2.将code等于his_permission的数据查询对应的id
    * 3.根据id查询该节点极其下的所有子节点集合
    */
	@Override
	public List<Organization> listOrganization(List<String> ids) {
		  List<Organization> list = organizationDAO.listOrganization(ids);
		  List<Organization> codeSonList = new ArrayList<Organization>();
	      List<String> listmap = new ArrayList<String>();
	      for (Organization organization : list) {
	          String his_permission = organization.getHis_permission();
	          String[] hps = his_permission.split(",");// 分割逗号
	          listmap.addAll(Arrays.asList(hps));// 所有的his_permission存到listmap当中
	      }
	      // 查询对应的节点的数据
	     List<Organization> listshow = organizationDAO.listOrganizationcode(listmap);
	     //根据每个节点id查询对应的子节点数据
	     for (int i = 0; i < listshow.size(); i++) {
	   	 List<Organization> codeSon =listTreeByIdForSon(listshow.get(i).getId());
			for (int j = 0; j < codeSon.size(); j++) {
				codeSonList.add(codeSon.get(j));
			}
		}

	     
	      return codeSonList;
	
	}
	 /*   public List<Organization> listOrganizationcode(List<String> listmap) {
    return organizationDAO.listOrganizationcode(listmap);
     }
     public List<Organization> listOrganization(List<String> list) {
        return organizationDAO.listOrganization(list);
    }*/

	@Override
	public Organization getOrgaByKey(String orgKey) {
		Map<Object, Object> map = new HashMap<>();
        map.put("orgkey", orgKey);
        List<Organization> listOrg =organizationDAO.listAllOrganizationBy(map);
		if(!CollectionUtils.isEmpty(listOrg)){
			return listOrg.get(0);
		}
		return null;
	}


	
}
