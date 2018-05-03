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
    /**
     * 删除
     * @param rId
     * @return
     */
    Integer deleteRoleResource(@Param("rId") String rId);
    /**
     * 修改
     * @param roleResource
     * @return
     */
    Integer updateRoleResource(RoleResource roleResource);
}
