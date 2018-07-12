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
     * 查询全部角色/根据roleName查询
     */
    @Override
    public List<Role> listRole(String roleName) {
        return roleDao.listRole(roleName);
    }
    /**
     * 根据roleId查询角色
     */
    @Override
    public Role getRoleById(String roleId) {
        if(roleId == null || roleId.equals("")){
            return null;
        }
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
        if(role.getId() == null || role.getId().equals("")){
            return -1;
        }
        return roleDao.updateRole(role);
    }
    /**
     * 删除角色
     */
    @Override
    public Integer deleteRole(String roleId) {
        if(roleId == null || roleId.equals("")){
            return -1;
        }
        return roleDao.deleteRole(roleId);
    }
    
}
 