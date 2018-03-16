package cn.financial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.RoleResource;

/**
 * 角色资源关联表
 * @author gs
 * 2018/3/14
 */
public interface RoleResourceDAO {
    /**
     * 查询所有/根据角色id查对应的功能权限
     * @return
     */
    List<RoleResource> listRoleResource(@Param("rId") String rId);
    /**
     * 新增
     * @param roleResource
     * @return
     */
    Integer insertRoleResource(RoleResource roleResource);

}
