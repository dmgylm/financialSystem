package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.User;


public interface UserService {
    /**
     * 查询所有用户/多条件查询用户列表
     * @return
     */
    List<User> listUser(Map<Object, Object> map);
    /**
     * 查询所有用户/多条件查询用户列表  总条数 
     * @return
     */
    Integer listUserCount(Map<Object, Object> map);
    /**
     * 根据name/jobNumber查询用户
     * @param name
     * @return
     */
    Integer countUserName(String name ,String jobNumber);
    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    User getUserById(String userId);
    /**
     * 根据name查询用户
     * @param userName
     * @return
     */
    User getUserByName(String name);
    /**
     * 新增用户
     * @param name
     * @param pwd
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
	/**
	 * 根据组织架构名称查询用户列表信息
	 * @param map
	 * @return
	 */
    List<User> listUserOrgName(Map<Object, Object> map);
    /**
     * 根据组织架构名称查询用户列表信息总条数
     * @param map
     * @return
     */
    Integer listUserOrgNameCount(Map<Object, Object> map);
    /**
     * 根据组织架构id查询用户列表信息
     * @param map
     * @return
     */
    List<User> listUserOrgOId(Map<Object, Object> map);
}
