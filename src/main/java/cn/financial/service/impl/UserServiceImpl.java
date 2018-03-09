package cn.financial.service.impl;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.UserDAO;
import cn.financial.model.User;
import cn.financial.service.UserService;
import cn.financial.util.UuidUtil;


@Service("UserServiceImpl")
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserDAO userDao;
    /**
     * 查询全部
     */
    public java.util.List<User> listUser() {
        return userDao.listUser();
    }
    /**
     * 根据name查询
     */
    public Integer countUserName(String name, String pwd) {
        return userDao.countUserName(name, pwd);
    }
    /**
     * 根据id查询
     */
    public User getUserById(String userId){
        return userDao.getUserById(userId);
    }
    /**
     * 新增用户
     */
    public Integer insertUser(String name, String realName, String pwd, Integer privilege, Date createTime, Date updateTime, String oId) {
        User user = new User(UuidUtil.getUUID(), name, realName, pwd, privilege, createTime, updateTime, oId);
        return userDao.insertUser(user);
    }
    /**
     * 修改用户
     */
    public Integer updateUser(String userId, String name, String realName, String pwd, Integer privilege, Date createTime, Date updateTime, String oId) {
        User user = new User(userId, name, realName, pwd, privilege, createTime, updateTime, oId);
        return userDao.updateUser(user);
    }
    /**
     * 删除用户
     */
    public Integer deleteUser(String userId){
        return userDao.deleteUser(userId);
    }
}
 