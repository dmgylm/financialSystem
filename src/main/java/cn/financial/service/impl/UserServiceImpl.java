package cn.financial.service.impl;

import java.awt.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.UserDAO;
import cn.financial.model.User;
import cn.financial.service.UserService;


@Service("UserServiceImpl")
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserDAO userDAO;
     
    public int insertUser(User user) {
        return userDAO.insertUser(user);
    }

    public User selectUserById(Integer userId) {
	return userDAO.selectUserById(userId);
    }
 
}
 