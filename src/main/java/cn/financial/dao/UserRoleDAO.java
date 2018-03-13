package cn.financial.dao;

import java.util.List;

import cn.financial.model.UserRole;

/**
 * 用户角色关联表
 * @author gs
 * 2018/3/13
 */
public interface UserRoleDAO {
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
