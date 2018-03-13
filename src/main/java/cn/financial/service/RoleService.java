package cn.financial.service;

import java.util.List;

import cn.financial.model.Role;

/**
 * 角色
 * @author gs
 * 2018/3/13
 */
public interface RoleService {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> listRole();
    /**
     * 根据id查询角色
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
