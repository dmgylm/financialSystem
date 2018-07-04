package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.UserOrganizationDAO;
import cn.financial.model.Organization;
import cn.financial.model.UserOrganization;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.util.TreeNode;


@Service("UserOrganizationServiceImpl")
public class UserOrganizationServiceImpl implements UserOrganizationService{
    @Autowired
    private UserOrganizationDAO userOrganizationDao;
    @Autowired
    private OrganizationService organizationService;
    /**
     * 查询所有
     * @return
     */
    @Override
    public List<UserOrganization> listUserOrganization(String uId) {
        return userOrganizationDao.listUserOrganization(uId);
    }
    /**
     * 新增
     * @return
     */
    @Override
    public Integer insertUserOrganization(UserOrganization userOrganization) {
        return userOrganizationDao.insertUserOrganization(userOrganization);
    }
    /**
     * 删除
     */
    @Override
    public Integer deleteUserOrganization(String uId) {
        return userOrganizationDao.deleteUserOrganization(uId);
    }
    /**
     * 修改
     * @return
     */
    @Override
    public Integer updateUserOrganization(UserOrganization userOrganization) {
        return userOrganizationDao.updateUserOrganization(userOrganization);
    }
    
    /**
     * 根据用户id查询该组织架构节点下的所有子节点,构建成树
     * @return
     */
    public List<JSONObject> userOrganizationList(String uId){
        List<UserOrganization> userOrganization = userOrganizationDao.listUserOrganization(uId);
        List<JSONObject> jsonUserOrg = new ArrayList<>();
        JSONObject jsonObject = null;
        if(!CollectionUtils.isEmpty(userOrganization)){
            for (UserOrganization rss : userOrganization) {
                //根据id查询该节点下的所有子节点,构建成树
                jsonObject = organizationService.TreeByIdForSon(rss.getoId());
                jsonUserOrg.add(jsonObject);
            }
        }
        return jsonUserOrg;
    }
    /**
     * 用户组织结构关联表(直接勾选子节点或者全选)
     * @param uId
     * @return
     */
    public JSONObject userOrganizationLists(String uId){
        List<UserOrganization> userOrganization = userOrganizationDao.listUserOrganization(uId);
        List<TreeNode<Organization>> nodes = new ArrayList<>();
        JSONObject jsonObject = null;
        Map<Object, Object> map = new HashMap<Object, Object>();
        List<Organization> listre = new ArrayList<>();
        for (int i = 0; i < userOrganization.size(); i++) {
            String orgId = userOrganization.get(i).getoId();
            //当前id信息
            map.put("id", orgId);
            List<Organization> userOrgId = organizationService.listOrganizationBy(map); 
            listre.addAll(userOrgId);
            //根据当前id查询该节点的所有父节点
            List<Organization> orgIdList = organizationService.listTreeByIdForParent(orgId);
            listre.addAll(orgIdList);
        }
        
        if(!CollectionUtils.isEmpty(listre)){
            //去重
            for (int i = 0; i < listre.size() - 1; i++) {
                for (int j = listre.size() - 1; j > i; j--) {
                    if (listre.get(j).getId().equals(listre.get(i).getId())) {
                        listre.remove(j);
                    }
                }
            } 
            for (Organization organization : listre) {
                TreeNode<Organization> node = new TreeNode<>();
                node.setId(organization.getCode());
                node.setParentId(organization.getParentId());
                node.setName(organization.getOrgName());
                node.setPid(organization.getId());
                nodes.add(node);
            }
            jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
        }
        return jsonObject;
    }
}
 