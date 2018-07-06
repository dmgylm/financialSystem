package cn.financial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import cn.financial.dao.UserRoleDAO;
import cn.financial.model.UserRole;
import cn.financial.service.UserRoleService;


@Service("UserRoleServiceImpl")
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    private UserRoleDAO userRoleDao;
    /**
     * 查询所有
     * @return
     */
    @Override
    public List<UserRole> listUserRole(String name) {
        return userRoleDao.listUserRole(name);
    }
    /**
     * 新增
     * @return
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer insertUserRole(UserRole user) {
        return userRoleDao.insertUserRole(user);
    }
    /**
     * 删除
     * @return
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer deleteUserRole(String uId) {
        return userRoleDao.deleteUserRole(uId);
    }
    /**
     * 修改
     * @return
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer updateUserRole(UserRole user) {
        return userRoleDao.updateUserRole(user);
    }
}
 