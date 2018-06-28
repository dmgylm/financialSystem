package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.financial.dao.RoleResourceDAO;
import cn.financial.dao.UserDAO;
import cn.financial.dao.UserRoleDAO;
import cn.financial.model.RoleResource;
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
    private RoleResourceDAO roleResourceDao;
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
    public Set<String> findRoles(String username) {
        List<UserRole> userRole = userRoleDao.listUserRole(username);
        Set<String> roles = new HashSet<String>();
        for(int i=0;i<userRole.size();i++){
            roles.add(userRole.get(i).getRoleName());
            System.out.println("角色-----------"+userRole.get(i).getRoleName());
        }
        return roles;
    }
    //权限
    public Set<String> findPermissions(String username) {
        Set<String> permissions = new HashSet<String>();
        try {
            if(username!=null && !"".equals(username)){
                List<UserRole> userRole = userRoleDao.listUserRole(username);//根据用户名查询对应角色信息
                List<RoleResource> roleResource = new ArrayList<RoleResource>();
                if(userRole.size()>0){
                    for(UserRole list:userRole){
                        roleResource.addAll(roleResourceDao.listRoleResource(list.getrId())); //根据角色id查询对应功能权限信息
                    }  
                }
                for(int i=0;i<roleResource.size();i++){
                    if(!StringUtils.isEmpty(roleResource.get(i).getPermssion())){
                        permissions.add(roleResource.get(i).getPermssion());
                        System.out.println("权限-----------"+roleResource.get(i).getPermssion());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissions;
    }
    
}
 