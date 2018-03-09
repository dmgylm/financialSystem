package cn.financial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.User;

/**
 * 用户
 * @author gs
 * 2018/3/7
 */
public interface UserDAO {
    /**
     * 查询所有用户
     * @return
     */
    List<User> listUser(Integer status);
    /**
     * 根据name查询用户
     * @param name
     * @return
     */
    Integer countUserName(String name);
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
