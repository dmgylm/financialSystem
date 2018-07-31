package cn.financial.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.UserRole;


public interface UserRoleService {
    /**
     * 查询所有/根据用户查对应角色
     * @return
     */
    List<UserRole> listUserRole(String name, String rId);
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
