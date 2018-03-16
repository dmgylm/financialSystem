package cn.financial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.ResourceDAO;
import cn.financial.model.Resource;
import cn.financial.service.ResourceService;


@Service("ResourceServiceImpl")
public class ResourceServiceImpl implements ResourceService{
    @Autowired
    private ResourceDAO resourceDao;
    /**
     * 查询全部角色
     */
    @Override
    public List<Resource> listResource() {
        return resourceDao.listResource();
    }
    /**
     * 根据id查询角色
     */
    @Override
    public Resource getResourceById(String resourceId) {
        return resourceDao.getResourceById(resourceId);
    }
    /**
     * 新增角色
     */
    @Override
    public Integer insertResource(Resource resource) {
        return resourceDao.insertResource(resource);
    }
    /**
     * 修改角色
     */
    @Override
    public Integer updateResource(Resource resource) {
        return resourceDao.updateResource(resource);
    }
    /**
     * 删除角色
     */
    @Override
    public Integer deleteResource(String resourceId) {
        return resourceDao.deleteResource(resourceId);
    }
    
}
 