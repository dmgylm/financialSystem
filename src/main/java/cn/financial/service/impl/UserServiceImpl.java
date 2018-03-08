package cn.financial.service.impl;

import java.awt.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.UserDAO;
import cn.financial.model.Organization;
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
    public int listUserById(String name) {
        return userDao.listUserById(name);
    }
    /**
     * 新增用户
     */
    public int insertUser(String name, String pwd, Integer privilege, Date createTime, Date updateTime, String oId) {
        return userDao.insertUser(UuidUtil.strToUser(UuidUtil.getUUID(),name, pwd, privilege, createTime, updateTime, oId));
    }
    /**
     * 修改用户
     */
    public int updateUser(String userId, String name, String pwd, Integer privilege, Date createTime, Date updateTime, String oId) {
        return userDao.updateUser(UuidUtil.strToUser(userId,name, pwd, privilege, createTime, updateTime, oId));
    }
    /**
     * 删除用户
     */
    public int deleteUser(String userId) {
        return userDao.deleteUser(userId);
    }
    
}
 