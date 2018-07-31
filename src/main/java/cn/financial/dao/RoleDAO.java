package cn.financial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.Role;
/**
 * 角色
 * @author gs
 * 2018/3/13
 */
public interface RoleDAO {
    /**
     * 查询所有角色/根据roleName查询
     * @return
     */
    List<Role> listRole(@Param("roleName")String roleName, @Param("rName")String rName);
    /**
     * 根据roleId查询角色
     * @param userId
     * @return
     */
    Role getRoleById(String roleId);
    /**
     * 新增角色
     * @param user
     * @return
     */
    Integer insertRole(Role role);
    /**
     * 修改角色
     * @param user
     * @return
     */
    Integer updateRole(Role role);
    /**
     * 删除角色
     * @param userId
     * @return
     */
    Integer deleteRole(String roleId);
}
