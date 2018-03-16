package cn.financial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.UserRole;

/**
 * 用户角色关联表
 * @author gs
 * 2018/3/13
 */
public interface UserRoleDAO {
    /**
     * 查询所有/根据用户查对应角色
     * @return
     */
    List<UserRole> listUserRole(@Param("name") String name);
    /**
     * 新增
     * @param user
     * @return
     */
    Integer insertUserRole(UserRole user);

}