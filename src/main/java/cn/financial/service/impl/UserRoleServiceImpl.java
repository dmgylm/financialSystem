package cn.financial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<UserRole> listUserRole() {
        return userRoleDao.listUserRole();
    }
    /**
     * 根据id查询
     * @return
     */
    @Override
    public UserRole getUserRoleById(String userId) {
        return userRoleDao.getUserRoleById(userId);
    }
    /**
     * 新增
     * @return
     */
    @Override
    public Integer insertUserRole(UserRole user) {
        return userRoleDao.insertUserRole(user);
    }
}
 