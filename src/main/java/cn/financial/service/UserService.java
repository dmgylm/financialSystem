package cn.financial.service;

import java.util.List;

import cn.financial.model.User;


public interface UserService {
    /**
     * 查询所有用户
     * @return
     */
    List<User> listUser();
    /**
     * 根据name查询用户
     * @param name
     * @return
     */
    Integer countUserName(String name ,String pwd);
    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    User getUserById(String userId);
    /**
     * 新增用户
     * @param name
     * @param pwd
     * @param privilege
     * @param createTime
     * @param updateTime
     * @param oId
     * @return
     */
    Integer insertUser(User user);
    /**
     * 修改用户
     * @param name
     * @param pwd
     * @param privilege
     * @param createTime
     * @param updateTime
     * @param oId
     * @return
     */
    Integer updateUser(User user);
    /**
     * 删除用户
     * @param userId
     * @return
     */
    Integer deleteUser(String userId);
	 
}
