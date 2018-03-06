package cn.financial.service;

import java.awt.List;

import cn.financial.model.User;


public interface UserService {
	 public int insertUser(User user);
	 public User selectUserById(Integer userId);
	 
}
