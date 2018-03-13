package cn.financial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.RoleDAO;
import cn.financial.model.Role;
import cn.financial.service.RoleService;


@Service("RoleServiceImpl")
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDAO roleDao;
    /**
     * 查询全部角色
     */
    @Override
    public List<Role> listRole() {
        return roleDao.listRole();
    }
    /**
     * 根据id查询角色
     */
    @Override
    public Role getRoleById(String roleId) {
        return roleDao.getRoleById(roleId);
    }
    /**
     * 新增角色
     */
    @Override
    public Integer insertRole(Role role) {
        return roleDao.insertRole(role);
    }
    /**
     * 修改角色
     */
    @Override
    public Integer updateRole(Role role) {
        return roleDao.updateRole(role);
    }
    /**
     * 删除角色
     */
    @Override
    public Integer deleteRole(String roleId) {
        return roleDao.deleteRole(roleId);
    }
    
}
 