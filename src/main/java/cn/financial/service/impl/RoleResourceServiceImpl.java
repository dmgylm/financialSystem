package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.RoleResourceDAO;
import cn.financial.model.RoleResource;
import cn.financial.service.RoleResourceService;
import cn.financial.util.TreeNode;

/**
 * 角色资源关联表
 * @author gs
 * 2018/3/14
 */
@Service("RoleResourceServiceImpl")
public class RoleResourceServiceImpl implements RoleResourceService{
    @Autowired
    private RoleResourceDAO roleResourceDao;
    /**
     * 查询所有/根据角色id查对应的功能权限
     * @return
     */
    @Override
    public List<RoleResource> listRoleResource(String rId) {
        return roleResourceDao.listRoleResource(rId);
    }
    /**
     * 新增
     * @return
     */
    @Override
    public Integer insertRoleResource(RoleResource roleResource) {
        return roleResourceDao.insertRoleResource(roleResource);
    }
    /**
     * 删除
     */
    @Override
    public Integer deleteRoleResource(String rId) {
        return roleResourceDao.deleteRoleResource(rId);
    }
    /**
     * 修改
     */
    @Override
    public Integer updateRoleResource(RoleResource roleResource) {
        return roleResourceDao.updateRoleResource(roleResource);
    } 
    
    /**
     * 根据角色id查询对应功能权限关联信息（必须勾选父节点，父节点相当于查看权限）
     * @param roleId
     * @return
     */
    public JSONObject roleResourceList(String roleId){
        List<RoleResource> roleResource = roleResourceDao.listRoleResource(roleId);
        List<TreeNode<RoleResource>> nodes = new ArrayList<>();
        JSONObject jsonObject = null;
        if(!CollectionUtils.isEmpty(roleResource)){
            for (RoleResource rss : roleResource) {
                TreeNode<RoleResource> node = new TreeNode<>();
                node.setId(rss.getCode().toString());//当前code
                String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
                node.setParentId(b);//父id
                node.setName(rss.getName());//功能权限名称
                node.setPid(rss.getsId());//当前权限id
                // node.setNodeData(rss);
                nodes.add(node);
            }
            jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
        }
        return jsonObject;
    }
}
 