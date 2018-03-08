package cn.financial.dao;

import java.awt.List;
import java.util.Date;

import cn.financial.model.User;

/**
 * 
 * @author gs
 * 2018/3/7
 */
public interface UserDAO {
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
    int insertUser(User user);
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
    int updateUser(User user);
    /**
     * 删除用户
     * @param userId
     * @return
     */
    int deleteUser(String userId);
}
