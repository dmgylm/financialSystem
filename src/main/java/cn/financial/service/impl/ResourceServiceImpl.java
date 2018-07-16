package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.ResourceDAO;
import cn.financial.model.Resource;
import cn.financial.model.RoleResource;
import cn.financial.service.ResourceService;
import cn.financial.util.TreeNode;


@Service("ResourceServiceImpl")
public class ResourceServiceImpl implements ResourceService{
    @Autowired
    private ResourceDAO resourceDao;
    /**
     * 查询全部角色
     */
    @Override
    public List<Resource> listResource(){
        return resourceDao.listResource();
    }
    /**
     * 根据id/code查询角色
     */
    @Override
    public Resource getResourceById(String resourceId, String code) {
        return resourceDao.getResourceById(resourceId, code);
    }
    /**
     * 新增角色
     */
    @Override
    public Integer insertResource(Resource resource) {
        if(resource.getPermssion() == null || resource.getPermssion().equals("")){
            return -1;
        }
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
        if(resourceId == null || resourceId.equals("")){
            return -1;
        }
        return resourceDao.deleteResource(resourceId);
    }
    
    //角色权限关联构建成树(勾选子节点不用勾选父节点)
    /*public JSONObject resourceList(List<RoleResource> roleResource){
        List<TreeNode<Resource>> nodes = new ArrayList<>();
        List<Resource> listre = new ArrayList<>();
        for (int i = 0; i < roleResource.size(); i++) {
            String resourceId =roleResource.get(i).getsId();
            //当前的resource节点
            Resource resource = resourceDao.getResourceById(resourceId, null);
            listre.add(resource);
            String[] split = resource.getParentId().split("/");
            for (int j = 0; j < split.length; j++) {
                Resource resource1 = resourceDao.getResourceById(null, split[j]);
                listre.add(resource1);
            }
        }
        if(!CollectionUtils.isEmpty(listre)){
            for (int i = 0; i < listre.size() - 1; i++) {
                for (int j = listre.size() - 1; j > i; j--) {
                    if (listre.get(j).getId().equals(listre.get(i).getId())) {
                        listre.remove(j);
                    }
                }
            }  
            for (Resource resource : listre) {
                TreeNode<Resource> node = new TreeNode<>();
                node.setId(resource.getCode().toString());
                String b=resource.getParentId().substring(resource.getParentId().lastIndexOf("/")+1);
                node.setParentId(b);
                node.setName(resource.getName());
                nodes.add(node);
            }
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
        return jsonObject;
    }*/
    
}
 