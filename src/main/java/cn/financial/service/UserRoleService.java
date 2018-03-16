package cn.financial.service;

import java.util.List;

import cn.financial.model.UserRole;


public interface UserRoleService {
    /**
     * 查询所有/根据用户查对应角色
     * @return
     */
    List<UserRole> listUserRole(String name);
    /**
     * 新增
     * @param user
     * @return
     */
    Integer insertUserRole(UserRole user);
}
