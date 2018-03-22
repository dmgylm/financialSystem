package cn.financial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.Resource;
/**
 * 资源权限表
 * @author gs
 * 2018/3/15
 */
public interface ResourceDAO {
    /**
     * 查询所有
     * @return
     */
    List<Resource> listResource();
    /**
     * 根据id/name查询
     * @param resourceId
     * @return
     */
    Resource getResourceById(@Param("id") String resourceId,@Param("code") String code);
    /**
     * 新增
     * @param resource
     * @return
     */
    Integer insertResource(Resource resource);
    /**
     * 修改
     * @param resource
     * @return
     */
    Integer updateResource(Resource resource);
    /**
     * 删除
     * @param resourceId
     * @return
     */
    Integer deleteResource(String resourceId);
}
