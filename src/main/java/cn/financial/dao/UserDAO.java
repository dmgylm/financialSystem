package cn.financial.dao;

import java.awt.List;

import cn.financial.model.User;


public interface UserDAO {
    public int insertUser(User user);
    
    public User selectUserById(Integer userId);
 
}
