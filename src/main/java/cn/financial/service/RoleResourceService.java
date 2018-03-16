package cn.financial.service;

import java.util.List;

import cn.financial.model.RoleResource;


public interface RoleResourceService {
    /**
     * 查询所有/根据角色查对应的功能权限
     * @return
     */
    List<RoleResource> listRoleResource(String roleName);
    /**
     * 新增
     * @param roleResource
     * @return
     */
    Integer insertRoleResource(RoleResource roleResource);
}
