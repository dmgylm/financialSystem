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
     * 根据id查询
     * @param userId
     * @return
     */
    UserRole getUserRoleById(String userId);
    /**
     * 新增
     * @param user
     * @return
     */
    Integer insertUserRole(UserRole user);
}
