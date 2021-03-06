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

import cn.financial.dao.RoleResourceDAO;
import cn.financial.dao.UserDAO;
import cn.financial.dao.UserRoleDAO;
import cn.financial.model.RoleResource;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.service.UserService;
import cn.financial.util.UuidUtil;
import cn.financial.util.shiro.PasswordHelper;


@Service("UserServiceImpl")
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserDAO userDao;
    @Autowired
    private UserRoleDAO userRoleDao;
    @Autowired
    private RoleResourceDAO roleResourceDao;
    @Autowired
    private PasswordHelper passwordHelper;
    
    /**
     * 查询全部/多条件查询用户列表
     */
    @Override
    public List<User> listUser(Map<Object, Object> map) {
        return userDao.listUser(map);
    }
    /**
     * 查询全部/多条件查询用户列表  总条数
     */
    @Override
    public Integer listUserCount(Map<Object, Object> map) {
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
        if(userId == null || userId.equals("")){
          return null;  
        }
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
        if(user.getRealName() == null || user.getRealName().equals("")){
            return -1;
        }else{
            //加密密码
            passwordHelper.encryptPassword(user);
            return userDao.insertUser(user); 
        }
    }
    /**
     * 修改用户
     */
    @Override
    public Integer updateUser(User user) {
        if(user.getId() == null || user.getId().equals("")){//用户id
            return -1;
        }
        if(user.getPwd()!=null && !user.getPwd().equals("")){
            if(!user.getPwd().matches(User.REGEX)){//密码规则校验
                return -2;
            } 
            user.setSalt(UuidUtil.getUUID());
            user.setExpreTime(UuidUtil.expreTime());//添加密码到期时间
            passwordHelper.encryptPassword(user);//加密密码
        }
        return userDao.updateUser(user);
    }
    /**
     * 删除用户
     */
    @Override
    public Integer deleteUser(String userId){
        if(userId == null || userId.equals("")){
            return -1;
        }
        return userDao.deleteUser(userId);
    }
    /**
     * 根据组织架构名称查询用户列表信息
     */
    @Override
    public List<User> listUserOrgName(Map<Object, Object> map) {
        return userDao.listUserOrgName(map);
    }
    /**
     * 根据组织架构名称查询用户列表信息总条数
     */
    @Override
    public Integer listUserOrgNameCount(Map<Object, Object> map) {
        return userDao.listUserOrgNameCount(map);
    }
    /**
     * 根据组织架构id查询用户列表信息
     */
    @Override
    public List<User> listUserOrgOId(Map<Object, Object> map) {
        return userDao.listUserOrgOId(map);
    }
    //角色
    @Cacheable(value = "find_Roles_Permissions", key = "'findRoles_name_'+#username")
    public Set<String> findRoles(String username) {
        List<UserRole> userRole = userRoleDao.listUserRole(username, null);
        Set<String> roles = new HashSet<String>();
        if(!CollectionUtils.isEmpty(userRole)){
            for(int j=0;j<userRole.size();j++){
                roles.add(userRole.get(j).getRoleName());
            }
            System.out.println("角色-----------"+roles);
        }
        return roles;
    }
    //权限
    @Cacheable(value = "find_Roles_Permissions", key = "'findPermissions_name_'+#username")
    public Set<String> findPermissions(String username) {
        Set<String> permissions = new HashSet<String>();
        try {
            if(username!=null && !"".equals(username)){
                List<UserRole> userRole = userRoleDao.listUserRole(username, null);//根据用户名查询对应角色信息
                List<RoleResource> roleResource = new ArrayList<>();
                HashSet<Object> twoList = new HashSet<>();
                if(userRole.size()>0){//用户可能拥有多个角色，需要去重权限信息
                    for(UserRole list:userRole){
                        //根据角色id查询对应功能权限关联信息（必须勾选父节点，父节点相当于查看权限）
                        roleResource.addAll(roleResourceDao.listRoleResource(list.getrId()));
                    }
                    if(!CollectionUtils.isEmpty(roleResource)){
                        for(RoleResource roleRes : roleResource){
                            twoList.add(roleRes.getPermssion());
                        }
                    }
                }
                if(!CollectionUtils.isEmpty(twoList)){
                    for(Object itemStr : twoList){
                        permissions.add(itemStr.toString());
                    }
                    System.out.println("权限-----------"+permissions);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissions;
    }
    
    
    /*public static void addOrgTypes(JSONObject json,Set<String> permissions){
        if(json==null || json.equals("")){
            return;
        }
        if(json.containsKey("orgType") && StringUtils.isNotEmpty(json.getString("orgType"))){
            permissions.add(json.getString("orgType"));
        }
        if(json.containsKey("children") && CollectionUtils.isNotEmpty(json.getJSONArray("children"))){
            for (Object item : json.getJSONArray("children")) {
                JSONObject  itemChildren = (JSONObject)JSONObject.toJSON(item);
                if(itemChildren.containsKey("orgType") && StringUtils.isNotEmpty(itemChildren.getString("orgType"))){
                    permissions.add(itemChildren.getString("orgType"));
                }
                addOrgTypes(itemChildren,permissions);
            }
        }
         
    }*/   
 
    public static void addItem(List<JSONObject>  obj,List<JSONObject> object){
        //获取第二个list的所有pid
        HashSet<Object> twoList = new HashSet<>();
        for (JSONObject item : object) {
            twoList.add(item.getString("pid"));
            addItems(item, twoList);
        }
        //System.out.println("--------"+JSON.toJSONString(twoList));
        //循环匹配
        for (JSONObject item : obj) {
            item.put("mathc", false);
            if(twoList.contains(item.getString("pid"))){
                item.put("mathc", true);
            }
            checkItems(item, twoList);
        }
    }
    
    //循环匹配
    public static void checkItems(JSONObject object,HashSet<Object> obj){
        if(object!=null && CollectionUtils.isNotEmpty(object.getJSONArray("children"))){
            for (Object item : object.getJSONArray("children")) {
                JSONObject  itemChildrens = (JSONObject)JSONObject.toJSON(item);
                for (Object itemStr : obj) {
                    itemChildrens.put("mathc", false);
                    //System.out.println("------"+itemStr+"========="+itemChildrens.getString("pid"));
                   if(itemChildrens.getString("pid").equals(itemStr)){
                       itemChildrens.put("mathc", true);
                       break;
                   }
                }
                checkItems(itemChildrens, obj);
             }
            
        }
    }
    
    public static void addItems(JSONObject object,HashSet<Object> obj){
        if(object!=null && CollectionUtils.isNotEmpty(object.getJSONArray("children"))){
            for (Object item : object.getJSONArray("children")) {
               JSONObject  itemChildrens = (JSONObject)JSONObject.toJSON(item);
               obj.add(itemChildrens.getString("pid"));
               addItems(itemChildrens, obj);
            }
            
        }
        
    }
}
 