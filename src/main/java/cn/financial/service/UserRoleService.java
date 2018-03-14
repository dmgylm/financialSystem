package cn.financial.service;

import java.util.List;

import cn.financial.model.UserRole;


public interface UserRoleService {
    /**
     * 查询所有
     * @return
     */
    List<UserRole> listUserRole();
    /**
     * 新增
     * @param user
     * @return
     */
    Integer insertUserRole(UserRole user);
}
