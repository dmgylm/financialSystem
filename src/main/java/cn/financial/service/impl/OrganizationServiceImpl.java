package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

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
        Organization org = organizationDAO.getOrganizationById(parentOrgId);
        // 找到兄弟节点信息
        List<Organization> list = organizationDAO.listByParentId(org.getCode());
        // 若存在兄弟节点，则兄弟节点的code找到该节点的code
        if (null != list && list.size() != 0) {
            List<String> codes = new ArrayList<String>();
            for (Organization orga : list) {
                codes.add(orga.getCode());
            }
            code = UuidUtil.getCodeByBrother(org.getCode(), codes);
        }
        // 若不存在兄弟节点，那code就是父节点的code加上01
        if (list != null && list.size() == 0) {
            code = org.getCode() + "01";
        }
        organization.setParentId(org.getCode());// 父id
        organization.setCode(code); // 该组织机构节点的序号，两位的，比如（01；0101，0102）
        organization.setHis_permission(code); // 新增时，历史权限id就是此节点的code
        return organizationDAO.saveOrganization(organization);
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
    public Organization getOrganizationById(String id) {
        return organizationDAO.getOrganizationById(id);
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
     * 伪删除 <根据组织结构ID修改状态为0，即已删除>,停用(单条停用)
     */
    public Integer deleteOrganizationByStatus(String id) {
        return organizationDAO.deleteOrganizationByStatus(id);
    }

    /**
     * 伪删除 <根据组织结构ID修改状态为0，即已删除>,停用(级联停用，将此节点下的所有子节点停用)
     */
    public Integer deleteOrganizationByStatusCascade(String id) {
        // 根据id查询到该节点信息
        Organization org = organizationDAO.getOrganizationById(id);
        // 根据该节点的code查询到其下所有子节点信息的集合
        List<Organization> list = organizationDAO.listTreeByCodeForSon(org.getCode());
        Integer i = 0;
        Iterator<Organization> iterator = list.iterator();
        while (iterator.hasNext()) {
            Organization orga = iterator.next();
            i = organizationDAO.deleteOrganizationByStatus(orga.getId());
        }
        return i;
    }

    /**
     * 根据id,或者name查询该节点下的所有子节点,构建成树
     */
    @Override
    public String listTreeByNameOrIdForSon(Map<Object, Object> map) {
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        List<Organization> list = organizationDAO.listTreeByCodeForSon(organizationByIds.get(0).getCode());
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
        jsonStr = jsonObject.toString();
        return jsonStr;
    }

    /**
     * 根据parentid查询节点信息
     */
    @Override
    public List<Organization> listByParentId(String parentId) {
        return organizationDAO.listByParentId(parentId);
    }

    /**
     * 根据id,或者name查询该节点的所有父节点
     */
    @Override
    public List<Organization> listTreeByNameOrIdForParent(Map<Object, Object> map) {
        List<Organization> organizationByIds = organizationDAO.listOrganizationBy(map);
        List<Organization> list = organizationDAO.listTreeByCodeForParent(organizationByIds.get(0).getCode());
        return list;
    }

    /**
     * 移动组织机构
     * 
     * @id 要移动的节点的id
     * @parentOrgId 将要移动到其下的节点的id
     */
    @Transactional
    @Override
    public Integer moveOrganization(String id, String parentOrgId) {
        // 根据id查询到该节点信息
        Organization org = organizationDAO.getOrganizationById(id);
        // 根据该节点的code查询到其下所有子节点信息的集合
        List<Organization> list = organizationDAO.listTreeByCodeForSon(org.getCode());
        // 这里已经是要移动的节点及其子节点的集合
        // list.add(org);

        /*
         * 接下来是将要移动的几点按原来的机构新增到现在的父节点上,
         * 先将该节点新增，并修改其code，parentId，his_permission；然后再新增其子节点
         */
        // 这里是移动后的code
        String code = null;
        // 先判断新的父节点下是否有子节点
        Organization orgParent = organizationDAO.getOrganizationById(parentOrgId);
        // 得到新父节点的子节点
        List<Organization> listSon = organizationDAO.listByParentId(orgParent.getCode());
        if (null != listSon && listSon.size() != 0) {
            List<String> codes = new ArrayList<String>();
            for (Organization orgaSon : listSon) {
                codes.add(orgaSon.getCode());
            }
            code = UuidUtil.getCodeByBrother(orgParent.getCode(), codes);
        } else {
            code = orgParent.getCode() + "01";
        }
        Organization organization = new Organization();
        organization.setId(UuidUtil.getUUID());
        organization.setOrgName(org.getOrgName());
        organization.setuId(org.getuId());// ??????????????????????????????????????
        organization.setCode(code);
        organization.setParentId(orgParent.getCode());
        organization.setHis_permission(org.getHis_permission() + "," + code);
        organizationDAO.saveOrganization(organization);

        /*
         * 停用要移动的节点及其所有的子节点
         */
        Iterator<Organization> iterator = list.iterator();
        while (iterator.hasNext()) {
            Organization orga = iterator.next();
            organizationDAO.deleteOrganizationByStatus(orga.getId());

            /*
             * 添加子节点
             */
            if (orga.getId() != id && !id.equals(orga.getId())) {
                // 子节点的code
                String code1 = orga.getCode().replaceFirst(org.getCode(), organization.getCode());
                String parentId1 = orga.getParentId().replaceFirst(org.getCode(), organization.getCode());
                Organization organization1 = new Organization();
                organization1.setId(UuidUtil.getUUID());
                organization1.setOrgName(orga.getOrgName());
                organization1.setuId(orga.getuId());// ??????????????????????????????????????
                organization1.setCode(code1);
                organization1.setParentId(parentId1);
                organization1.setHis_permission(orga.getHis_permission() + "," + code1);
                organizationDAO.saveOrganization(organization1);
            }
        }
        return Integer.valueOf(1);
    }

}
