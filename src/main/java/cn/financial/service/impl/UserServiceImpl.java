package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.UserDAO;
import cn.financial.dao.UserRoleDAO;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.service.UserService;
import cn.financial.util.shiro.PasswordHelper;


@Service("UserServiceImpl")
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserDAO userDao;
    @Autowired
    private UserRoleDAO userRoleDao;
    @Autowired
    private RoleResourceServiceImpl roleResourceService;
    @Autowired
    private PasswordHelper passwordHelper;
    
    /**
     * 查询全部/多条件查询用户列表
     */
    @Override
    public java.util.List<User> listUser(Map<Object, Object> map) {
        return userDao.listUser(map);
    }
    /**
     * 查询全部/多条件查询用户列表  总条数
     */
    @Override
    public java.util.List<User> listUserCount(Map<Object, Object> map) {
        return userDao.listUserCount(map);
    }
    /**
     * 根据name/jobNumber查询
     */
    @Override
    public Integer countUserName(String name, String jobNumber) {
        return userDao.countUserName(name, jobNumber);
    }
    /**
     * 根据id查询
     */
    @Override
    public User getUserById(String userId){
        return userDao.getUserById(userId);
    }
    /**
     * 根据name查询
     */
    @Override
    public User getUserByName(String name){
        return userDao.getUserByName(name);
    }
    /**
     * 新增用户
     */
    @Override
    public Integer insertUser(User user) {
        //加密密码
        passwordHelper.encryptPassword(user);
        return userDao.insertUser(user);
    }
    /**
     * 修改用户
     */
    @Override
    public Integer updateUser(User user) {
        //加密密码
        if(user.getPwd()!=null && !user.getPwd().isEmpty()){
            passwordHelper.encryptPassword(user);
        }
        return userDao.updateUser(user);
    }
    /**
     * 删除用户
     */
    @Override
    public Integer deleteUser(String userId){
        return userDao.deleteUser(userId);
    }
    //角色
    @Cacheable(value = "find_Roles_Permissions", key = "'findRoles_name_'+#username")
    public Set<String> findRoles(String username) {
        List<UserRole> userRole = userRoleDao.listUserRole(username);
        Set<String> roles = new HashSet<String>();
        if(!CollectionUtils.isEmpty(userRole)){
            for(int j=0;j<userRole.size();j++){
                roles.add(userRole.get(j).getRoleName());
                System.out.println("角色-----------"+userRole.get(j).getRoleName());
            }
        }
        return roles;
    }
    //权限
    @Cacheable(value = "find_Roles_Permissions", key = "'findPermissions_name_'+#username")
    public Set<String> findPermissions(String username) {
        Set<String> permissions = new HashSet<String>();
        try {
            if(username!=null && !"".equals(username)){
                List<JSONObject> jsonObject = roleResourceService.roleResourceList(username);
                if(!CollectionUtils.isEmpty(jsonObject)){
                    for (JSONObject item : jsonObject) {
                        this.addOrgTypes(item, permissions);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissions;
    }
    
    
    public static void addOrgTypes(JSONObject json,Set<String> permissions){
        if(json==null || json.equals("")){
            return;
        }
        if(json.containsKey("children") && CollectionUtils.isNotEmpty(json.getJSONArray("children"))){
            for (Object item : json.getJSONArray("children")) {
                JSONObject  itemChildren=(JSONObject)JSONObject.toJSON(item);
                if(itemChildren.containsKey("orgType") && org.apache.commons.lang.StringUtils.isNotEmpty(itemChildren.getString("orgType"))){
                    permissions.add(itemChildren.getString("orgType"));
                    System.out.println("权限-----------"+itemChildren.getString("orgType"));
                }
                addOrgTypes(itemChildren,permissions);
            }
        }
         
    }
    
}
 