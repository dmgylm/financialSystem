package cn.financial.service;

import java.awt.List;
import java.util.Date;

import cn.financial.model.Organization;
import cn.financial.model.User;


public interface UserService {
    /**
     * 查询所有用户
     * @return
     */
    java.util.List<User> listUser();
    /**
     * 根据name查询用户
     * @param name
     * @return
     */
    int listUserById(String name);
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
    int insertUser(String name, String pwd, Integer privilege, Date createTime, Date updateTime, String oId);
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
    int updateUser(String userId, String name, String pwd, Integer privilege, Date createTime, Date updateTime, String oId);
    /**
     * 删除用户
     * @param userId
     * @return
     */
    int deleteUser(String userId);
	 
}
