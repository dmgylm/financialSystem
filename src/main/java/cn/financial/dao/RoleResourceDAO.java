package cn.financial.dao;

import java.util.List;

import cn.financial.model.RoleResource;

/**
 * 角色资源关联表
 * @author gs
 * 2018/3/14
 */
public interface RoleResourceDAO {
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
