package cn.financial.service;

import java.util.List;

import cn.financial.model.RoleResource;


public interface RoleResourceService {
    /**
     * 查询所有/根据角色id查对应的功能权限
     * @return
     */
    List<RoleResource> listRoleResource(String rId);
    /**
     * 新增
     * @param roleResource
     * @return
     */
    Integer insertRoleResource(RoleResource roleResource);
    /**
     * 删除
     * @param rId
     * @return
     */
    Integer deleteRoleResource(String rId);
    /**
     * 修改
     * @param roleResource
     * @return
     */
    Integer updateRoleResource(RoleResource roleResource);
}
