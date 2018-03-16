package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.OrganizationDAO;
import cn.financial.model.Organization;
import cn.financial.service.OrganizationService;
import cn.financial.util.TreeNode;

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
    public Integer saveOrganization(Organization organization) {
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
     * 伪删除 <根据组织结构ID修改状态为0，即已删除>
     */
    public Integer deleteOrganizationByStatus(String id) {
        return organizationDAO.deleteOrganizationByStatus(id);
    }

    /**
     * 根据id查询该节点下的所有子节点,构建成树
     */
    @Override
    public String listTreeByOrgId(String id) {
        Organization organizationById = organizationDAO.getOrganizationById(id);
        List<Organization> list = organizationDAO.listTreeByOrgCode(organizationById.getCode());
        List<TreeNode<Organization>> nodes = new ArrayList<>();
        String jsonStr = "";
        for (Organization organization : list) {
            TreeNode<Organization> node = new TreeNode<>();
            node.setId(organization.getId().toString());
            node.setParentId(organization.getParentId().toString());
            node.setText(organization.getOrgName());
            node.setNodeData(organization);
            nodes.add(node);
        }
        JSONObject jsonObject = JSONObject.fromObject(TreeNode.buildTree(nodes));
        jsonStr = jsonObject.toString();
        return jsonStr;
    }
}
