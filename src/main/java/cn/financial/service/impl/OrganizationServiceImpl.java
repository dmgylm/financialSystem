package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.financial.dao.OrganizationDAO;
import cn.financial.model.Organization;
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
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDAO organizationDAO;

    /**
     * 新增组织结构
     */
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
            return organizationDAO.saveOrganization(organization);
        } else {
            return 0;
        }
    }

    /**
     * 根据条件查询组织结构信息
     */
    public List<Organization> listOrganizationBy(Map<Object, Object> map) {
        return organizationDAO.listOrganizationBy(map);
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     */
    public Integer updateOrganizationById(Map<Object, Object> map) {
        return organizationDAO.updateOrganizationById(map);
    }

    /**
     * 根据ID删除组织结构信息
     */
    public Integer deleteOrganizationById(String id) {
        return organizationDAO.deleteOrganizationById(id);
    }

    /**
     * 停用(级联停用，将此节点下的所有子节点停用)，根据组织结构ID修改状态为0，即已停用
     */
    @Transactional
    public Integer deleteOrganizationByStatusCascade(String uId, String id) {
        Integer i = 0;
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> org = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(org)) {
            // 根据该节点的code查询到其下所有子节点信息的集合
            List<Organization> list = organizationDAO.listTreeByCodeForSon(org.get(0).getCode());
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
        }
        return i;
    }

    /**
     * 根据id查询该节点下的所有子节点,构建成树
     */
    @Override
    public JSONObject TreeByIdForSon(String id) {
        List<Organization> list = new ArrayList<>();
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        list.add(organizationByIds.get(0));
        if (!CollectionUtils.isEmpty(organizationByIds)) {
            // list =
            // organizationDAO.listTreeByCodeForSon(organizationByIds.get(0).getCode());
            getOrganizationSonList(list, organizationByIds.get(0).getCode());
            if (!CollectionUtils.isEmpty(list)) {
                List<TreeNode<Organization>> nodes = new ArrayList<>();
                String jsonStr = "";
                for (Organization organization : list) {
                    TreeNode<Organization> node = new TreeNode<>();
                    node.setId(organization.getCode());
                    node.setParentId(organization.getParentId().toString());
                    node.setText(organization.getOrgName());
                    node.setNodeData(organization);
                    nodes.add(node);
                }
                JSONObject jsonObject = JSONObject.fromObject(TreeNode.buildTree(nodes));
                return jsonObject;
            }
        }
        return null;
    }

    /**
     * 根据id查询该节点下的所有子节点集合
     */
    @Override
    public List<Organization> listTreeByIdForSon(String id) {
        List<Organization> list = new ArrayList<>();
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
         list.add(organizationByIds.get(0));
        if (!CollectionUtils.isEmpty(organizationByIds)) {
//            list = organizationDAO.listTreeByCodeForSon(organizationByIds.get(0).getCode());
            getOrganizationSonList(list, organizationByIds.get(0).getCode());
            if (!CollectionUtils.isEmpty(list)) {
                return list;
            }
        }
        return list;
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
    public List<Organization> listTreeByIdForParent(String id) {
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(organizationByIds)) {
            List<Organization> list = organizationDAO.listTreeByCodeForParent(organizationByIds.get(0).getCode());
            if (!CollectionUtils.isEmpty(list)) {
                return list;
            }
        }
        return null;
    }

    /**
     * 移动组织机构
     * 
     * @id 要移动的节点的id
     * @parentOrgId 将要移动到其下的节点的id
     */
    @Transactional
    @Override
    public Integer moveOrganization(String uId ,String id, String parentOrgId) {
        // 根据id查询到该节点信息
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        List<Organization> org = organizationDAO.listOrganizationBy(map);
        if (CollectionUtils.isEmpty(org)) {
            return Integer.valueOf(0);
        }
        // 根据该节点的code查询到其下所有子节点信息的集合(要移动的节点及其子节点的集合)
        List<Organization> list = listTreeByIdForSon(id);//organizationDAO.listTreeByCodeForSon(org.get(0).getCode());
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
        organization.setId(UuidUtil.getUUID());
        organization.setOrgName(org.get(0).getOrgName());
        organization.setuId(uId);
        organization.setCode(code);
        organization.setParentId(orgParent.get(0).getCode());
        organization.setHis_permission(org.get(0).getHis_permission() + "," + code);
        organizationDAO.saveOrganization(organization);

        /*
         * 停用要移动的节点及其所有的子节点
         */
        Iterator<Organization> iterator = list.iterator();
        while (iterator.hasNext()) {
            Organization orga = iterator.next();
            Map<Object, Object> idmap = new HashMap<>();
            idmap.put("id", orga.getId());
            idmap.put("uId", uId);
            // 先修改uid
            Integer idupdate = organizationDAO.updateOrganizationById(idmap);
            if (Integer.valueOf(1).equals(idupdate)) {
                // 停用
                organizationDAO.deleteOrganizationByStatus(orga.getId());
                /*
                 * 添加子节点
                 */
                if (orga.getId() != id && !id.equals(orga.getId())) {
                    // 子节点的code
                    String code1 = orga.getCode().replaceFirst(org.get(0).getCode(), organization.getCode());
                    String parentId1 = orga.getParentId().replaceFirst(org.get(0).getCode(), organization.getCode());
                    Organization organization1 = new Organization();
                    organization1.setId(UuidUtil.getUUID());
                    organization1.setOrgName(orga.getOrgName());
                    organization1.setuId(uId);
                    organization1.setCode(code1);
                    organization1.setParentId(parentId1);
                    organization1.setHis_permission(orga.getHis_permission() + "," + code1);
                    organizationDAO.saveOrganization(organization1);
                }
            }
        }
        return Integer.valueOf(1);
    }

    /**
     * 根据id判断是否该节点存在子节点根据id或者name判断是否该节点存在子节点（这里的name主要是指公司名称，查询该公司是否有部门；
     * 其他节点只能通过id查询）
     */
    @Override
    public Boolean hasOrganizationSon(Map<Object, Object> map) {
        Boolean flag = false;
        // 查询到当前节点
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(organizationByIds)) {
            Map<Object, Object> mapp = new HashMap<>();
            mapp.put("parentId", organizationByIds.get(0).getCode());
            List<Organization> list = organizationDAO.listOrganizationBy(mapp);
            if (!CollectionUtils.isEmpty(list)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 根据公司以下节点的id，查询到该节点所属公司
     */
    @Override
    public Organization getCompanyNameBySon(String id) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", id);
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        if (!CollectionUtils.isEmpty(organizationByIds)) {
            List<Organization> parent = organizationDAO.listTreeByCodeForParent(organizationByIds.get(0).getCode());
            if (!CollectionUtils.isEmpty(parent)) {
                for (Organization o : parent) {
                    if (o.getOrgName().contains("公司")) {
                        return o;
                    }
                }
            }
        }
        return null;
    }

}
