package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.List;

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
        return roleResourceDao.listRoleResource(rId);
    }
    /**
     * 根据sort排序
     * @return
     */
    @Override
    public List<RoleResource> listRoleResourceSort(List<String> rId) {
        return roleResourceDao.listRoleResourceSort(rId);
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
        List<TreeNode<RoleResource>> nodes = new ArrayList<>();
        List<UserRole> userRole = userRoleService.listUserRole(userName, null);//根据用户名查询对应角色信息
        if(userRole.size()>0){//用户可能拥有多个角色
            List<String>  resourceSort = new ArrayList<>();
            for(UserRole list:userRole){
                resourceSort.add(list.getrId()); 
            }
            //根据角色id查询对应功能权限关联信息（必须勾选父节点，父节点相当于查看权限）
            List<RoleResource> roleResource = roleResourceDao.listRoleResourceSort(resourceSort);
            if(!CollectionUtils.isEmpty(roleResource)){
                for (RoleResource rss : roleResource) {
                    TreeNode<RoleResource> node = new TreeNode<>();
                    if(rss.getName().equals("录入中心")){
                        node.setLink("/recodercenter/index");
                        node.setIcon("iconfont icon-luruzhongxin");
                        node.setOrgkeyName("recodercenter");
                    }
                    if(rss.getName().equals("资金流水")){
                        node.setLink("/financeflow/index");
                        node.setIcon("iconfont icon-zijinliushui");
                        node.setOrgkeyName("financeflow");
                    }
                    if(rss.getName().equals("汇总中心")){
                        node.setLink("/summarycenter/index");
                        node.setIcon("iconfont icon-huizongzhongxin");
                        node.setOrgkeyName("summarycenter");
                    }
                    if(rss.getName().equals("消息中心")){
                        node.setLink("/infocenter/index");
                        node.setIcon("iconfont icon-xiaoxizhongxin");
                        node.setOrgkeyName("infocenter");
                    }
                    if(rss.getName().equals("系统设置")){
                        node.setOrgType("Y");//是按钮
                        node.setIcon("iconfont icon-xitongshezhi");
                    }
                    if(rss.getName().equals("用户权限设置")){
                        node.setLink("/setting/permissions-settings");
                        node.setIcon("anticon anticon-appstore-o");
                        node.setOrgkeyName("permissions-settings");
                    }
                    if(rss.getName().equals("组织架构管理")){
                        node.setLink("/setting/organization-management");
                        node.setIcon("anticon anticon-rocket");
                        node.setOrgkeyName("organization-management");
                    }
                    if(rss.getName().equals("数据模板配置")){
                        node.setLink("/setting/data-configure/index");
                        node.setIcon("anticon anticon-rocket");
                        node.setOrgkeyName("data-configure");
                    }
                    if(rss.getName().equals("角色设置")){
                        node.setLink("/setting/role-setting");
                        node.setIcon("anticon anticon-rocket");
                        node.setOrgkeyName("role-setting");
                    }
                    if(rss.getName().equals("修改密码")){
                        node.setLink("/setting/change-pw");
                        node.setIcon("anticon anticon-rocket");
                        node.setOrgkeyName("change-pw");
                    }
                    node.setId(rss.getCode().toString());//当前code
                    String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
                    node.setParentId(b);//父id
                    node.setName(rss.getName());//功能权限名称
                    node.setPid(rss.getsId());//当前权限id
                    nodes.add(node);
                }
            }
         }
        List<String>  nodeList = new ArrayList<>();
        List<TreeNode<RoleResource>>  roles = new ArrayList<TreeNode<RoleResource>>();
        //去除多个角色重复功能权限数据
        if(!CollectionUtils.isEmpty(nodes)){
            for (TreeNode<RoleResource> item : nodes) {
                if(!nodeList.contains(item.getPid())){
                        roles.add(item);
                        nodeList.add(item.getPid());
                }
                for (TreeNode<RoleResource> items : item.getChildren()) {
                    if(!nodeList.contains(items.getPid())){
                        roles.add(items);
                        nodeList.add(items.getPid());
                    }
                }
            }
        }
        jsonObject.add((JSONObject) JSONObject.toJSON(TreeNode.buildTree(roles)));
        return jsonObject;
     }
    
    
}
 