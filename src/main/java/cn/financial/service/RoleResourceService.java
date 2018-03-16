package cn.financial.service;

import java.util.List;

import cn.financial.model.RoleResource;


public interface RoleResourceService {
    /**
     * 查询所有
     * @return
     */
    List<RoleResource> listRoleResource();
    /**
     * 新增
     * @param roleResource
     * @return
     */
    Integer insertRoleResource(RoleResource roleResource);
}
