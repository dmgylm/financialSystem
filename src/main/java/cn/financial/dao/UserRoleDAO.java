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
     * 查询所有/根据用户查对应角色/角色id查询用户角色关联信息
     * @return
     */
    List<UserRole> listUserRole(@Param("name") String name, @Param("rId") String rId);
    /**
     * 新增
     * @param user
     * @return
     */
    Integer insertUserRole(UserRole user);
    /**
     * 删除
     * @param uId
     * @return
     */
    Integer deleteUserRole(@Param("uId") String uId);
    /**
     * 修改
     * @param user
     * @return
     */
    Integer updateUserRole(UserRole user);
}
