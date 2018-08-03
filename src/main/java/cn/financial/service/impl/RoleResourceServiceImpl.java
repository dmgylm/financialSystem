package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.RoleResourceDAO;
import cn.financial.model.RoleResource;
import cn.financial.model.UserRole;
import cn.financial.service.RoleResourceService;
import cn.financial.service.UserRoleService;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;

/**
 * 角色资源关联表
 * @author gs
 * 2018/3/14
 */
@Service("RoleResourceServiceImpl")
public class RoleResourceServiceImpl implements RoleResourceService{
    @Autowired
    private RoleResourceDAO roleResourceDao;
    @Autowired
    private UserRoleService userRoleService;
    /**
     * 查询所有/根据角色id查对应的功能权限
     * @return
     */
    @Override
    public List<RoleResource> listRoleResource(String rId) {
        if(rId == null || rId.equals("")){
            return null;
        }
        return roleResourceDao.listRoleResource(rId);
    }
    /**
     * 新增
     * @return
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer insertRoleResource(RoleResource roleResource) {
        JSONArray sArray = JSON.parseArray(roleResource.getsId());
        int roleResourceList = 0;
        RoleResource roleResources = null;
        for (int i = 0; i < sArray.size(); i++) {
            JSONObject object = (JSONObject) sArray.get(i);
            String resourceIdStr = (String)object.get("resourceId");//获取key-resourceId键值
            System.out.println("resouceId:==="+resourceIdStr);
            if(resourceIdStr!=null && !"".equals(resourceIdStr)){
                roleResources = new RoleResource();
                roleResources.setId(UuidUtil.getUUID());
                roleResources.setsId(resourceIdStr);
                roleResources.setrId(roleResource.getrId());
                roleResourceList = roleResourceDao.insertRoleResource(roleResources);
            }
        }
        return roleResourceList;
    }
    /**
     * 删除
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer deleteRoleResource(String rId) {
        return roleResourceDao.deleteRoleResource(rId);
    }
    /**
     * 修改
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer updateRoleResource(RoleResource roleResource) {
        int roleResourceList = 0;
        int roleResourceDelete = roleResourceDao.deleteRoleResource(roleResource.getrId());//删除
        if(roleResourceDelete > 0){
            JSONArray sArray = JSON.parseArray(roleResource.getsId());
            RoleResource roleResources = null;
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String resourceIdStr = (String)object.get("resourceId");//获取key-resourceId键值
                System.out.println("resouceId:==="+resourceIdStr);
                if(resourceIdStr!=null && !"".equals(resourceIdStr)){
                    roleResources = new RoleResource();
                    roleResources.setId(UuidUtil.getUUID());
                    roleResources.setsId(resourceIdStr);
                    roleResources.setrId(roleResource.getrId());
                    roleResourceList = roleResourceDao.updateRoleResource(roleResources);//修改（新增数据）
                }
            }
        }else{
            return -3;
        }
        return roleResourceList;
    } 
    
    /**
     * 根据用户名查询对应角色功能权限信息
     * @param roleId
     * @return
     */
    public List<JSONObject> roleResourceList(String userName){
        List<JSONObject> jsonObject = new ArrayList<>();
        Map<String, TreeNode<RoleResource>> map = new HashMap<>();
        List<UserRole> userRole = userRoleService.listUserRole(userName, null);//根据用户名查询对应角色信息
        if(userRole.size()>0){//用户可能拥有多个角色，需要去重
            for(UserRole list:userRole){
              //根据角色id查询对应功能权限关联信息（必须勾选父节点，父节点相当于查看权限）
                List<RoleResource> roleResource = roleResourceDao.listRoleResource(list.getrId());
                List<TreeNode<RoleResource>> nodes = new ArrayList<>();
                if(!CollectionUtils.isEmpty(roleResource)){
                    for (RoleResource rss : roleResource) {
                        TreeNode<RoleResource> node = new TreeNode<>();
                        node.setId(rss.getCode().toString());//当前code
                        String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
                        node.setParentId(b);//父id
                        node.setName(rss.getName());//功能权限名称
                        node.setPid(rss.getsId());//当前权限id
                        //node.setOrgType(rss.getPermssion());//权限信息
                        // node.setNodeData(rss);
                        nodes.add(node);
                        map.put(node.getPid(), node);
                    }
                }
            }
         }
        List<TreeNode<RoleResource>> roleList=new ArrayList<>();
        for (TreeNode<RoleResource> item : map.values()) {
            roleList.add(item);
        }
        if(TreeNode.buildTree(roleList) != null){
            jsonObject.add((JSONObject) JSONObject.toJSON(TreeNode.buildTree(roleList)));
        }
        return jsonObject;
     }
}
 