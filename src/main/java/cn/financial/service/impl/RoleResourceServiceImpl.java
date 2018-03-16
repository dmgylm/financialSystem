package cn.financial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.RoleResourceDAO;
import cn.financial.model.RoleResource;
import cn.financial.service.RoleResourceService;

/**
 * 角色资源关联表
 * @author gs
 * 2018/3/14
 */
@Service("RoleResourceServiceImpl")
public class RoleResourceServiceImpl implements RoleResourceService{
    @Autowired
    private RoleResourceDAO roleResourceDao;
    /**
     * 查询所有
     * @return
     */
    @Override
    public List<RoleResource> listRoleResource() {
        return roleResourceDao.listRoleResource();
    }
    /**
     * 新增
     * @return
     */
    @Override
    public Integer insertRoleResource(RoleResource roleResource) {
        return roleResourceDao.insertRoleResource(roleResource);
    } 

}
 