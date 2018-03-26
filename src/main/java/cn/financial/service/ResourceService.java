package cn.financial.service;

import java.util.List;

import cn.financial.model.Resource;

/**
 * 资源权限表
 * @author gs
 * 2018/3/15
 */
public interface ResourceService {
    /**
     * 查询所有
     * @return
     */
    List<Resource> listResource();
    /**
     * 根据id/code查询
     * @param resourceId
     * @return
     */
    Resource getResourceById(String resourceId, String code);
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
