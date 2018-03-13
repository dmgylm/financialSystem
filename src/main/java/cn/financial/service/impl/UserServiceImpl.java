package cn.financial.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.UserDAO;
import cn.financial.model.User;
import cn.financial.service.UserService;


@Service("UserServiceImpl")
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserDAO userDao;
    /**
     * 查询全部
     */
    @Override
    public java.util.List<User> listUser() {
        return userDao.listUser();
    }
    /**
     * 根据name查询
     */
    @Override
    public Integer countUserName(String name, String pwd) {
        return userDao.countUserName(name, pwd);
    }
    /**
     * 根据id查询
     */
    @Override
    public User getUserById(String userId){
        return userDao.getUserById(userId);
    }
    /**
     * 新增用户
     */
    @Override
    public Integer insertUser(User user) {
        return userDao.insertUser(user);
    }
    /**
     * 修改用户
     */
    @Override
    public Integer updateUser(User user) {
        return userDao.updateUser(user);
    }
    /**
     * 删除用户
     */
    @Override
    public Integer deleteUser(String userId){
        return userDao.deleteUser(userId);
    }
}
 