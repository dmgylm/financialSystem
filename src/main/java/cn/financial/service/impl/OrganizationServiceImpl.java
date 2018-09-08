package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.OrganizationDAO;
import cn.financial.dao.OrganizationMoveDao;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.OrganizationMove;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.BusinessDataInfoService;
import cn.financial.service.BusinessDataService;
import cn.financial.service.OrganizationService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.StringUtils;
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
	//private Logger logger = Logger.getLogger(OrganizationServiceImpl.class);
	protected Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private OrganizationMoveDao moveDao;
    
    @Autowired
    private UserOrganizationServiceImpl userorganization;
    
    @Autowired
    private BusinessDataService businessDataService;	
    
	@Autowired
	private DataModuleServiceImpl dataModuleServiceImpl;
	
	@Autowired
	private BusinessDataInfoService businessDataInfoService;
    
    
    
 
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
//            Map<Object, Object> mapbro = new HashMap<>();
//            mapbro.put("parentId", org.get(0).getCode());
//            List<Organization> list = organizationDAO.listAllOrganizationBy(mapbro);
//            JSONObject jsonTree= treeByIdForSon(parentOrgId);
//            JSONArray jsonarray=JSONArray.parseArray(jsonTree.get("children").toString());
            String parentCode = org.get(0).getCode();
            // 若存在兄弟节点，则兄弟节点的code找到该节点的code
//            if(jsonarray.size()>0){
//            	 List<String> codes = new ArrayList<String>();
//            	  for(int i=0;i<jsonarray.size();i++){
//            		  JSONObject jsonss=JSONObject.parseObject(jsonarray.get(i).toString());
//            		  String codeList= jsonss.get("id").toString();
//            		  codes.add(codeList);
//            	  }
//            	  code = UuidUtil.getCodeByBrother(parentCode, codes);
//            }
//         // 若不存在兄弟节点，那code就是父节点的code加上01
//            else{
//            	 code = org.get(0).getCode() + "01";
//            }
            
            List<Organization> orglst = listOrgByParentCode(parentCode,null);
    		code = UuidUtil.assembleOrgCode(parentCode, orglst);
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
    public List<Organization> listOrganizationBy(String orgName,String createTime,String updateTime,String id,String code,String uId,String parentId,Integer orgType,String orgkey) {
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
         if (null !=orgkey && !"".equals(orgkey)) {
             map.put("orgkey", orgkey);
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
    @Cacheable(value = "organizationValue", key = "'orga_key_treeById_'+#id")
    public  JSONObject treeByIdForSon(String id) {
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
                Organization organizationList = new Organization();
                if(organization.getOrgPlateId()!=null && !organization.getOrgPlateId().equals("")){
                     organizationList = listOrganizationBy(null, null, null, null, null, null, null, null, organization.getOrgPlateId()).get(0);
                }
                TreeNode<Organization> node = new TreeNode<>();
                node.setId(organization.getCode());
                node.setParentId(organization.getParentId());
                node.setName(organization.getOrgName());
                node.setOrgkeyName(organizationList.getOrgName());
                node.setOrgType(organization.getOrgType().toString());
                node.setHis_permission(organization.getHis_permission());
                node.setPid(organization.getId());
                node.setOrgPlateId(organization.getOrgPlateId());
                node.setOrgKey(organization.getOrgkey());
                nodes.add(node);
            }
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
            return jsonObject;
        }
        return null;
    }
    /**
     * 根据id查询该节点下有没有部门级别
     */
    @Override
    public  Integer treeByIdForSonList(String id) {
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
                    getOrganizationSonList2(departList, list, organizationByIds.get(0).getCode());
                }
            }
        }
        int a=0;
        if (!CollectionUtils.isEmpty(list)) {
            for (Organization organization : list) {
            	    if(organization.getOrgType()==3){
            	    	 a=1;
            	    }
             }
         }
		return a;
 
    }
    /**
     * 根据id查询该节点下有没有公司级别
     */
    @Override
    public  Integer treeByIdForSonSum(String id) {
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
                    getOrganizationSonList2(departList, list, organizationByIds.get(0).getCode());
                }
            }
        }
        int a=0;
        if (!CollectionUtils.isEmpty(list)) {
            for (Organization organization : list) {
            	    if(organization.getOrgType()==2){
            	    	 a=1;
            	    }
             }
         }
		return a;
 
    }    /**
     * 根据id查询该节点下有没有公司级别
     */
    @Override
    public  Integer treeByIdForSonOryType(String id) {
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
                    getOrganizationSonList2(departList, list, organizationByIds.get(0).getCode());
                }
            }
        }
        int a=0;
        if (!CollectionUtils.isEmpty(list)) {
            for (Organization organization : list) {
            	    if(organization.getOrgType()==2){
            	    	int sum=treeByIdForSonList(organization.getId());
            	    	 if(sum==1){
            	    		 a=1;
            	    	 }
            	    }
             }
         }
		return a;
 
    }
    /**
     * 根据id查询该节点下的部门级别
     */
    @Override
    public  void treeByIdForSonShow(Map<String, String> map1, Map<String, String> map2) {
    	 Iterator<String> it = map1.keySet().iterator();
         while(it.hasNext()){
           String key = it.next();
           if(map2.containsKey(key)){
             String beforeId=map1.get(key);//移动之前的id
             String afterId =map2.get(key);
             saveid(beforeId,afterId);
            }
          }
   
    }
    /**
     * 根据之后id查询该节点下的部门级别 
     */
    @Override
    public Map<String, String> treeByIdForSonAfter(String id) {
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
                    getOrganizationSonList2(departList, list, organizationByIds.get(0).getCode());
                }
            }
        }
        Map<String, String> mapList = new HashMap<String, String>();
        if (!CollectionUtils.isEmpty(list)) {
            for (Organization organization : list) {
            	    if(organization.getOrgType()==3){
            	    	String pid=organization.getId();
            	    	String orgname=organization.getOrgName();
            	    	mapList.put(orgname,pid);
            	    }
             }
         }
		return mapList;
 
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
//        List<Organization> listSon = organizationDAO.listAllOrganizationBy(map);
//        JSONObject jsonTree= treeByIdForSon(parentOrgId);
//        JSONArray jsonarray=JSONArray.parseArray(jsonTree.get("children").toString());
        // 若存在兄弟节点，则兄弟节点的code找到该节点的code
//        if(jsonarray.size()>0){
//        	 List<String> codes = new ArrayList<String>();
//        	  for(int i=0;i<jsonarray.size();i++){
//        		  JSONObject jsonss=JSONObject.parseObject(jsonarray.get(i).toString());
//        		  String codeList= jsonss.get("id").toString();
//        		  codes.add(codeList);
//        	  }
//        	 code = UuidUtil.getCodeByBrother(orgParent.get(0).getCode(), codes);
//        }
//        // 若不存在兄弟节点，那code就是父节点的code加上01
//        else{
//       	 code = org.get(0).getCode() + "01";
//       }
        List<Organization> orglst = listOrgByParentCode(orgParent.get(0).getCode(),null);
		code = UuidUtil.assembleOrgCode(orgParent.get(0).getCode(), orglst);
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
        int sum=0;
        List<Organization> listset = listOrganizationBy(null,null,null,id,null,null,null,null,null);
        String codes=listset.get(0).getCode();
        List<Organization> listcode=organizationDAO.listCode(codes);
        while (iterator.hasNext()) {
            Organization orga = iterator.next();
            // 停用
            Integer intde = organizationDAO.deleteOrganizationByStatus(orga.getId());
            if(sum==0){
            	for(Organization ls:listcode){
            		if(ls.getoId().equals(orga.getId())){
            			userorganization.deleteUserOrganization(ls.getuId());
                    	UserOrganization lists=new UserOrganization();
                		lists.setoId(new_id);
                		lists.setuId(ls.getuId());//用户id
                		lists.setId(UuidUtil.getUUID());
                        organizationDAO.saveUserOrganization(lists);
            		}
            	}
            	sum++;
            }
          
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
                    for(Organization ls:listcode){
                		if(ls.getoId().equals(orga.getId())){
                			userorganization.deleteUserOrganization(ls.getuId());
                        	UserOrganization lists=new UserOrganization();
                    		lists.setoId(new_id1);
                    		lists.setuId(ls.getuId());//用户id
                    		lists.setId(UuidUtil.getUUID());
                            organizationDAO.saveUserOrganization(lists);
                		}
                		
                	}
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
/*        while (iterator.hasNext()) {
        	 Organization orga = iterator.next();
            List<UserOrganization> listsize= userorganization.listUserOrganizations(orga.getId());
            if(listsize.size()>0){
            	userorganization.deleteUserOrganization(user.getId());
            	UserOrganization lists=new UserOrganization();
        		lists.setoId(new_id);
        		lists.setuId(user.getId());//用户id
        		lists.setId(UuidUtil.getUUID());
                organizationDAO.saveUserOrganization(lists);
            }
        }*/
        
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
            	JSONObject json=treeByIdForSon(organizationByIds.get(0).getId());
            	JSONArray jsonarray=JSONArray.parseArray(json.get("children").toString());
            	 if(jsonarray.size()!=0){
            		 flag = true;
                 }
               /* Map<Object, Object> mapp = new HashMap<>();
                mapp.put("parentId", organizationByIds.get(0).getCode());
                List<Organization> list = organizationDAO.listOrganizationBy(mapp);
                if (!CollectionUtils.isEmpty(list)) {
                    flag = true;
                }*/
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
	
	   /**
	    * 1.通过节点id查询对应his_permission
	    * 2.将code等于his_permission的数据查询对应的数据集合
	    */
	@Override
	public List<BusinessData> listBusinessList(String reportType,String startDate, String endDate,
			List<String> ids) {
		List<Organization> list = organizationDAO.listOrganization(ids);
		List<BusinessData> businessDataList = new ArrayList<BusinessData>();
		List<String> listmap = new ArrayList<String>();
		for (Organization organization : list) {
			String his_permission = organization.getHis_permission();
			String[] hps = his_permission.split(",");// 分割逗号
			listmap.addAll(Arrays.asList(hps));// 所有的his_permission存到listmap当中
		}

		// 分隔传过来的开始结束时间
		String[] startYAndM = startDate.split("/");
		String[] endYAndM = endDate.split("/");
		
		Map<Object, Object> map = new HashMap<>();
		map.put("codeId", listmap);
		map.put("startYear", startYAndM[0]);
		map.put("endYear", endYAndM[0]);
		map.put("startMonth", startYAndM[1]);
		map.put("endMonth", endYAndM[1]);
		
		Integer tpye = 0;
		if(DataModule.REPORT_TYPE_PROFIT_LOSS.equals(reportType)||DataModule.REPORT_TYPE_PROFIT_LOSS_SUMMARY.equals(reportType)){
			tpye = 1;
		}
		if(DataModule.REPORT_TYPE_BUDGET.equals(reportType)||DataModule.REPORT_TYPE_BUDGET_SUMMARY.equals(reportType)){
			tpye = 2;
		}
		map.put("sId", tpye);
		businessDataList= businessDataService.listBusinessDataByIdAndDateList(map);

		List<BusinessData> result = new ArrayList<BusinessData>();
		for (int i = 0; i < businessDataList.size(); i++) {
			if(businessDataList.get(i).getsId()==tpye)
			{
				result.add(businessDataList.get(i));
			}
		}
		
		return result;

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
	 /**
     * 根据id查询该节点极其下的所有子节点集合
     */
    @Override
    @Cacheable(value = "organizationValue", key = "'orga_key_listson_'+#id")
	public List<Organization> treeByIdForSons(String id) {
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

                 getOrganizationSonList2(departList, list, organizationByIds.get(0).getCode());
             }
         }
        return list;
		
	}
   /**
    * 添加预算以及副表信息
    * @param beforeId
    * @param aferId
    */
    public void saveid(String beforeId,String aferId){
    	Organization org = getCompanyNameBySon(aferId);// 获取对应部门的公司
	    String businessId=UuidUtil.getUUID();
		BusinessData selectBusinessDataById = businessDataService.selectBusinessDataByType(beforeId);// 查询对应id的数据
		BusinessData businessData = new BusinessData();
		businessData.setId(businessId); // id自动生成
		businessData.setoId(org.getId());
		businessData.setTypeId(aferId);
		businessData.setUpdateTime(selectBusinessDataById.getUpdateTime());
		businessData.setuId(selectBusinessDataById.getuId());
		businessData.setYear(selectBusinessDataById.getYear());
		businessData.setMonth(selectBusinessDataById.getMonth());
		businessData.setStatus(selectBusinessDataById.getStatus());
		businessData.setDelStatus(selectBusinessDataById.getDelStatus());
		businessData.setsId(selectBusinessDataById.getsId());
		businessData.setDataModuleId(selectBusinessDataById.getDataModuleId());
		businessData.setVersion(selectBusinessDataById.getVersion() + 1);
		businessDataService.insertBusinessData(businessData); // 新增一条一模一样的新数据
		selectBusinessDataById.setDelStatus(0);
		businessDataService.updateBusinessDataDelStatus(selectBusinessDataById);//将以前的数据delstatus改为0
		BusinessDataInfo businessInfo=businessDataInfoService.selectBusinessDataById(selectBusinessDataById.getId());
		BusinessDataInfo businfo=new BusinessDataInfo();
		String busId=UuidUtil.getUUID();
		businfo.setId(busId);
		businfo.setBusinessDataId(businessId);  
		businfo.setInfo(businessInfo.getInfo());
		businfo.setuId(businessInfo.getuId());
	    businessDataInfoService.insertBusinessDataInfo(businfo);
	    businessInfo.setDelStatus(0);
	    businessDataInfoService.updateBusinessDataInfoDelStatus(businessInfo);
   }

	@Override
	public List<Organization> listAllOrganizationBy(Map<Object, Object> map) {
	    return organizationDAO.listAllOrganizationBy(map);
	}

	/**
	 * 添加或修改或移动组织架构时检查数据的合法性
	 * 
	 * @param bean
	 * @param saveOrMove false:保存
	 * @return
	 */
	public ResultUtils checkOrgData(Organization bean, String parentId,boolean isMove) {
		return checkOrgData(bean, parentId, null, isMove);
	}
	
	/**
	 * 添加或修改或移动组织架构时检查数据的合法性
	 * 
	 * @param bean
	 * @param saveOrMove false:保存
	 * @param upOrgType 修改的所有子节点最高级别类型
	 * @return
	 */
	public ResultUtils checkOrgData(Organization bean, String parentId,Integer upOrgType,boolean isMove) {
		String returnCode = null;
		ResultUtils result = new ResultUtils();
//		String parentId = bean.getParentId();
		Integer orgType = bean.getOrgType();
		String orgId = bean.getId();
		String orgName = bean.getOrgName();
		JSONObject subOrgJson = treeByIdForSon(parentId);
		//部门下面不能添加节点
		if(Organization.ORG_TYPE_DEPARTMENT==subOrgJson.getInteger("orgType")) {
			if(isMove) {
				returnCode = ElementConfig.DEPER_MOBILE;
			} else {
				returnCode = ElementConfig.DEPER_REMOVE;
			}
			ElementXMLUtils.returnValue(returnCode, result);
			return result;
		}
		JSONArray childrens = subOrgJson.getJSONArray("children");
		//检查子级是否有同名节点
		for(int i=0;childrens != null && i<childrens.size();i++){
			JSONObject node = childrens.getJSONObject(i);
			String nodeName = node.getString("name");
			String nodeId = node.getString("pid");
			if(!StringUtils.isValid(orgId)) {
				if(StringUtils.isValid(orgName) && orgName.equals(nodeName)) {
					ElementXMLUtils.returnValue(ElementConfig.NAMELY_NOSAME,
							result);
					return result;
				}
			} else {
				//编辑时不检查自己
				if(!orgId.equals(nodeId) && StringUtils.isValid(orgName) && orgName.equals(nodeName)) {
					ElementXMLUtils.returnValue(ElementConfig.NAMELY_NOSAME,
							result);
					return result;
				}
			}
		}
		
		//获取修改/新增的父节点的所有父节点
		List<Organization> parentNodes = listTreeByIdForParent(parentId);
		int companyQty = 0;
		int plateQty = 0;
		int departmentQty = 0;
		//计算该节点以后各属性节点的数量
		for(int i=0;i<parentNodes.size();i++) {
			Organization node = parentNodes.get(i);
			Integer nodeType = node.getOrgType();
			//公司
			if(nodeType==Organization.ORG_TYPE_COMPANY) {
				companyQty ++;
				bean.setCompany(node.getId());//设置其所属公司,为后面发送消息做准备
			}
			//板块
			if(nodeType==Organization.ORG_TYPE_PLATE) {
				plateQty ++;
			}
			//部门
			if(nodeType==Organization.ORG_TYPE_DEPARTMENT) {
				departmentQty ++;
			}
		}
		
		if (departmentQty > 0) {//部门下面不能添加节点
			if(isMove) {
				returnCode = ElementConfig.DEPER_MOBILE;
			} else {
				returnCode = ElementConfig.DEPER_REMOVE;
			}
			ElementXMLUtils.returnValue(returnCode, result);
			return result;
		}
		
		//处理节点为部门
		if(orgType==Organization.ORG_TYPE_DEPARTMENT) {
			if (companyQty != 1) {//部门上级必须有公司级别
				ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY, result);
				return result;
			}
		}
		//处理节点为公司
		if(orgType==Organization.ORG_TYPE_COMPANY) {
			if (companyQty > 0) {//公司下面不能添加公司
				ElementXMLUtils.returnValue(ElementConfig.COMPANY_COMPANY, result);
				return result;
			}
			if (plateQty != 1) {//公司上级必须有板块级别
				ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATE, result);
				return result;
			}
			//检查是否有重名公司
			List<Organization> companylst = getCompany();
			Map<String,Organization> companyMap = new HashMap<String, Organization>();
			for(Organization comp:companylst) {
				String companyName = comp.getOrgName();
				if(companyMap.get(companyName)!=null) {
					ElementXMLUtils.returnValue(ElementConfig.COMPANY_REPEAT, result);
					return result;
				}
				companyMap.put(companyName,comp);
			}
			Organization organization = companyMap.get(orgName);
			if(organization!=null && !organization.getId().equals(bean.getId())) {
				ElementXMLUtils.returnValue(ElementConfig.COMPANY_REPEAT, result);
				return result;
			}
			companyQty ++ ;
		}
		//处理节点为业务板块
		if(orgType==Organization.ORG_TYPE_PLATE) {
			if (companyQty > 0) {//板块上级不能有公司级别
				ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATELEVEL, result);
				return result;
			}
			if (plateQty > 0 ) {//板块下面不能添加板块
				ElementXMLUtils.returnValue(ElementConfig.PLATE_PLATELEVEL, result);
				return result;
			}
			plateQty++;
		}
		//upOrgType不为空,表示需要检查被修改节点的子节点人所有父级关系
		//保证一条线上有板块/公司和部门
		if(upOrgType != null) {
			if(upOrgType==Organization.ORG_TYPE_DEPARTMENT) {
				if (companyQty != 1) {
					ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY, result);
					return result;
				}
			} else if(upOrgType==Organization.ORG_TYPE_COMPANY) {
				if (plateQty != 1) {//公司上级必须有板块级别
					ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATE, result);
					return result;
				}
			}
		}
		ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
		return result;
	}

	/**
	 * 移动组织架构
	 */
	public ResultUtils moveOrg(String id, String parentId, String userId) {
		ResultUtils result = new ResultUtils();
		try {
			JSONObject sourceOrgJson = treeByIdForSon(id);
		    JSONObject targetOrgJson = treeByIdForSon(parentId);
		    Integer orgType = getNodeType(sourceOrgJson);
		    Organization bean = new Organization();
		    bean.setId(sourceOrgJson.getString("pid"));// 组织结构id
		    bean.setOrgName(sourceOrgJson.getString("name"));
		    bean.setParentId(parentId);
		    bean.setOrgType(orgType);
		    result = checkOrgData(bean,parentId,true);
		    if (!"200".equals(result.getResultCode())) {
				return result;
			}
		    doMoveOrg(sourceOrgJson,targetOrgJson,userId);
		} catch (Exception e) {
			logger.error("",e);
			ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
		}
		return result;
	}

	private void doMoveOrg(JSONObject sourceOrgJson, JSONObject targetOrgJson,String userId) {
		String targetCode = targetOrgJson.getString("id");//id即code
//		JSONArray targetChildrens = targetOrgJson.getJSONArray("children");
		
//		List<String> listCode = new ArrayList<String>();
//		String targetNodeCode = null;
//		if(targetChildrens != null && targetChildrens.size()>0) {
//			for(int i=0;i<targetChildrens.size();i++) {
//				JSONObject node = targetChildrens.getJSONObject(i);
//				String targetSubCode = node.getString("id");//id即code
//				listCode.add(targetSubCode);
//			}
//			targetNodeCode = UuidUtil.getCodeByBrother(targetCode, listCode);
//		} else {
//			targetNodeCode = targetCode + "01";
//		}
		//查询下一级节点,包括停用状态的
		List<Organization> orglst = listOrgByParentCode(targetCode,null);
		String targetNodeCode = UuidUtil.assembleOrgCode(targetCode, orglst);
		doSaveMoveOrgByRecursion(sourceOrgJson,targetNodeCode,targetCode,userId);
	}
	
	private List<Organization> listOrgByParentCode(String parentCode,Integer status) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("parentCode", parentCode);
		if(status!=null) {
			params.put("status", status);
		}
		return organizationDAO.listOrgByParentCode(params);
	}

	@Transactional
	private void doSaveMoveOrgByRecursion(JSONObject json,
			String nodeCode,String parentCode,String userId) {
		String oldOrgId = json.getString("pid");
		String code = json.getString("id");//id即code
//		String parenId = json.getString("parentId");
		String orgName = json.getString("name");
		String orgKey = json.getString("orgKey");
		String orgType = json.getString("orgType");
		String his_permission = json.getString("his_permission");
		String orgPlateId = json.getString("orgPlateId");
		JSONArray childrens = json.getJSONArray("children");
		//将原来的code前缀替换成新的前缀
//		code = code.replaceFirst(parenId, parentCode);
		Organization bean = new Organization();
		String newId = UuidUtil.getUUID();
		bean.setId(newId);
		bean.setCode(nodeCode);
		bean.setOrgkey(orgKey);
		bean.setOrgName(orgName);
		Integer orgTypeInt = Integer.parseInt(orgType);
		bean.setOrgType(orgTypeInt);
		bean.setHis_permission(his_permission+","+nodeCode);
		bean.setOrgPlateId(orgPlateId);
		bean.setParentId(parentCode);
		bean.setuId(userId);
		organizationDAO.saveOrganization(bean);
		//停用移动前的组织架构
		organizationDAO.deleteOrganizationByStatus(oldOrgId);
		
		//如果节点为部门,则copy预算数据
		if(Organization.ORG_TYPE_DEPARTMENT==orgTypeInt) {
			copyBudgetData(oldOrgId,newId);
		}
		
		//保存组织架构移动记录
		OrganizationMove organizationMove = new OrganizationMove();
        organizationMove.setId(UuidUtil.getUUID());
        organizationMove.setHis_Id(oldOrgId);
        organizationMove.setNew_Id(newId);
        organizationMove.setModifier(userId);
        organizationMove.setNewParent_Id(parentCode);
        moveDao.saveOrganizationMove(organizationMove);
		//更新用户组织架构关联表数据
        userorganization.updateUOOrgByOrgId(oldOrgId,newId);
        
        for(int i=0;childrens!=null && i<childrens.size();i++) {
        	JSONObject children = childrens.getJSONObject(i);
        	String subNodeCode = children.getString("id");
        	subNodeCode = nodeCode + subNodeCode.substring(subNodeCode.length()-2);
        	doSaveMoveOrgByRecursion(children, subNodeCode, nodeCode, userId);
        }
        
        
	}

	/**
	 * 根据组织架构ID copy预算数据
	 * @param oldOrgId
	 * @param newOrgId
	 */
	private void copyBudgetData(String oldOrgId, String newOrgId) {
		BusinessData oldBudget = businessDataService.selectBusinessDataByType(oldOrgId);
		if(oldBudget == null) {
			return;
		}
		BusinessData bean = new BusinessData();
		Organization newCompany = getCompanyNameBySon(newOrgId);// 获取对应部门的公司
		String budgetId = UuidUtil.getUUID();
		bean.setId(budgetId);
		bean.setoId(newCompany.getId());
		bean.setTypeId(newOrgId);
		bean.setuId(oldBudget.getuId());
		bean.setYear(oldBudget.getYear());
		bean.setMonth(oldBudget.getMonth());
		bean.setStatus(oldBudget.getStatus());
		bean.setDelStatus(oldBudget.getDelStatus());
		bean.setsId(oldBudget.getsId());
		bean.setDataModuleId(oldBudget.getDataModuleId());
		bean.setVersion(oldBudget.getVersion()+1);
		bean.setUpdateTime(oldBudget.getUpdateTime());
		//创建主表信息
		businessDataService.insertBusinessData(bean);
		//删除之前的数据
		oldBudget.setDelStatus(0);
		businessDataService.updateBusinessDataDelStatus(oldBudget);
		
		//创建从表数据
		BusinessDataInfo oldBudgetInfo = businessDataInfoService.selectBusinessDataById(oldBudget.getId());
		BusinessDataInfo newBudgetInfo=new BusinessDataInfo();
		String newBudgetInfoId=UuidUtil.getUUID();
		newBudgetInfo.setId(newBudgetInfoId);
		newBudgetInfo.setBusinessDataId(budgetId);  
		newBudgetInfo.setInfo(oldBudgetInfo.getInfo());
		newBudgetInfo.setuId(oldBudgetInfo.getuId());
	    businessDataInfoService.insertBusinessDataInfo(newBudgetInfo);
	    //删除之前的Info数据
	    oldBudgetInfo.setDelStatus(0);
	    businessDataInfoService.updateBusinessDataInfoDelStatus(oldBudgetInfo);
	}
	
	/**
	 * 获取当前及所有子节点所属类型,以板块/公司/部门为顺序依次判断
	 * @param sourceOrgJson
	 * @return
	 */
	public Integer getNodeType(JSONObject orgJson){
		Set<Integer> set = new HashSet<Integer>();
		getNodeType(orgJson,set);
		if(set.contains(Organization.ORG_TYPE_PLATE)) {
			return Organization.ORG_TYPE_PLATE;
		} else if(set.contains(Organization.ORG_TYPE_COMPANY)) {
			return Organization.ORG_TYPE_COMPANY;
		} else if(set.contains(Organization.ORG_TYPE_DEPARTMENT)) {
			return Organization.ORG_TYPE_DEPARTMENT;
		} else {
			return Organization.ORG_TYPE_SUMMARY;
		}
	}
	
	public void getNodeType(JSONObject orgJson,Set<Integer> set) {
		Integer orgType = orgJson.getInteger("orgType");
		if(Organization.ORG_TYPE_PLATE == orgType
				|| Organization.ORG_TYPE_COMPANY == orgType
				|| Organization.ORG_TYPE_DEPARTMENT == orgType){
			set.add(orgType);
		}
		JSONArray childrens = orgJson.getJSONArray("children");
		for (int i = 0; childrens != null && i < childrens.size(); i++) {
			JSONObject subNode = childrens.getJSONObject(i);
			getNodeType(subNode,set);
		}
	}
	
	
	public static void main(String[] args) {
//		JSONObject map = new JSONObject();
//		map.put("orgType", Organization.ORG_TYPE_SUMMARY);
//		JSONArray array1 = new JSONArray();
//		JSONObject a2 = new JSONObject();
//		a2.put("orgType", Organization.ORG_TYPE_DEPARTMENT);
//		array1.add(a2);
//
//		JSONObject a1 = new JSONObject();
//		a1.put("orgType", Organization.ORG_TYPE_COMPANY);
//		array1.add(a1);
//
//		
//		map.put("children", array1);
//		
//		OrganizationServiceImpl service = new OrganizationServiceImpl();
//		Integer nodeType = service.getNodeType(map);
//		System.out.println(nodeType);
		
	}

	@Transactional(rollbackFor=Exception.class)
	public ResultUtils doUpdateOrg(String id, String orgName, Integer orgType,String userId) {

		ResultUtils result = new ResultUtils();
    	try {
    		
    		JSONObject json = treeByIdForSon(id);
    		JSONArray curSubNode = json.getJSONArray("children");
    		//如果修改为部门节点,则先检查其下有没有节点存在
    		if(Organization.ORG_TYPE_DEPARTMENT==orgType && curSubNode.size()>0) {
    			ElementXMLUtils.returnValue(ElementConfig.DEPER_REMOVE, result);
    			return result;
    		}
    		
    		Integer oldOrgType = json.getInteger("orgType");
    		
    		//忽略被修改的节点,判断其节点下所有子节点最高级别
    		json.put("orgType", Organization.ORG_TYPE_SUMMARY);
        	Integer upOrgType = getNodeType(json);
        	//将节点属性改为修改后的, 用于判断父级节点是否合法
        	json.put("orgType", orgType);
        	
        	if(upOrgType==orgType && oldOrgType!=orgType) {
        		if(orgType==Organization.ORG_TYPE_COMPANY) {
        			ElementXMLUtils.returnValue(ElementConfig.COMPANY_COMPANY, result);
        			return result;
        		} else if(orgType==Organization.ORG_TYPE_PLATE) {
        			ElementXMLUtils.returnValue(ElementConfig.PLATE_PLATELEVEL, result);
        			return result;
        		}
        	}
        	
        	if(upOrgType==Organization.ORG_TYPE_DEPARTMENT) {
        		
        	} else if(upOrgType==Organization.ORG_TYPE_COMPANY) {
        		
        	}
        	
        	//修改节点为公司,以下节点类型为板块
        	if(orgType==Organization.ORG_TYPE_COMPANY && upOrgType == Organization.ORG_TYPE_PLATE) {
        		ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATELEVEL, result);
        		return result;
        		
        	}
        	
        	String parentId = json.getString("parentId");
        	Organization bean = new Organization();
    		bean.setId(id);// 组织结构id
    		bean.setOrgName(orgName);
    		bean.setOrgType(orgType);
    		bean.setParentId(parentId);
    		
    		bean.setuId(userId);// 提交人id

    		Organization parentNode = getOrgByCode(parentId);
    		if(parentNode == null) {
    			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
    			return result;
    		}
    		// 新增时检查数据合法性
    		result = checkOrgData(bean,parentNode.getId(),upOrgType,false);
    		// 数据不通过, 则直接返回检查结果
    		if (!"200".equals(result.getResultCode())) {
    			return result;
    		}
    		int saveResult = updateOrganizationById(orgName,id,orgType,userId);
    		if (saveResult == 1) {// 1 为保存成功
    			// 如果将节点类型改为部门,则生成预算数据
    			if(Organization.ORG_TYPE_DEPARTMENT == orgType) {
    				String orgPlateId = json.getString("orgPlateId");
    				DataModule dataModule = dataModuleServiceImpl.getDataModule(DataModule.REPORT_TYPE_BUDGET, orgPlateId);
    				Organization department = getOrgById(id);
    				Calendar c = Calendar.getInstance();
    				int year = c.get(Calendar.YEAR);
    				businessDataService.createBusinessData(department , year, dataModule);
    				// 预算生成成功, 则向组织节点发送消息
    				Organization company = getOrgById(bean.getCompany());
    				businessDataService.createBunsinessDataMessage(year, company , department);
    				ElementXMLUtils.returnValue(ElementConfig.BUDGET_GENERATE, result);
    			}
    			//如果修改之前的节点类型为部门,则把对应的预算表删除
    			if(Organization.ORG_TYPE_DEPARTMENT == oldOrgType) {
    				BusinessData budget = businessDataService.selectBusinessDataByType(id);
    				if(budget != null) {
    					budget.setDelStatus(0);
    					businessDataService.updateBusinessDataDelStatus(budget);
    				}
    			}
    		}
		} catch (Exception e) {
			logger.error("",e);
			ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
		}
		return result;
	}

	private Organization getOrgByCode(String code) {
		return organizationDAO.getOrgByCode(code);
	}

	public Organization getOrgById(String id) {
		List<Organization> list = listOrganizationBy(null, null, null, id, null, null, null, null, null);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
}
