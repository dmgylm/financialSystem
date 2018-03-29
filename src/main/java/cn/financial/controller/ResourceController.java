package cn.financial.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.financial.model.Resource;
import cn.financial.service.ResourceService;
import cn.financial.util.UuidUtil;

/**
 * 资源权限表
 * @author gs
 * 2018/3/15
 */
@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;
    
    protected Logger logger = LoggerFactory.getLogger(ResourceController.class);
    /**
     * 查询所有
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public Map<String, Object> listResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<Resource> resource = resourceService.listResource();
            dataMap.put("resourceList", resource);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功");
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
    	return dataMap;
    }
    /**
     * 根据id查询
     * @param request
     * @param response
     * @param resourceId
     * @return
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/resourceById", method = RequestMethod.POST)
    public Map<String, Object> getResourceById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
        	String resourceId = null;
        	if(null != request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId"))) {
        		  resourceId = request.getParameter("resourceId");
        	}
            if(resourceId == null || "".equals(resourceId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "资源id为空");
            }else{
                Resource resource = resourceService.getResourceById(resourceId,"");
                dataMap.put("resourceById", resource);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功");
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            e.printStackTrace();
        }
        return dataMap;
    }
    /**
     * 新增
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Map<String, Object> insertResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try { 
            String name = null;
            String code = null;//父id
            String url = null;
            String permssion = null;
            
            if( null != request.getParameter("name") && !"".equals(request.getParameter("name")) ) {
            	name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            }
            if( null != request.getParameter("code") && !"".equals(request.getParameter("code")) ) {
            	code = request.getParameter("code");//父id
            }
            if( null != request.getParameter("url") && !"".equals(request.getParameter("url")) ) {
            	url = request.getParameter("url");
            }
            if( null != request.getParameter("permssion") && !"".equals(request.getParameter("permssion")) ) {
            	permssion = request.getParameter("permssion");
            }
            Resource parent = resourceService.getResourceById("",code);//根据code查询对应功能权限
            Resource resource = new Resource();
            resource.setId(UuidUtil.getUUID());
            resource.setName(name);
            resource.setUrl(url);
            resource.setPermssion(permssion);
            if(parent != null && !"".equals(parent)){  
                resource.setParentId(parent.getParentId());
            }else{//没数据返回代表不是子节点直接把父节点赋值给parentId
                resource.setParentId("1"); 
            }
            int resourceList = resourceService.insertResource(resource);
            if(resourceList>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "新增成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "新增失败");
            } 

        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 修改
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
        	String name = null;
            String permssion = null;//父id
            String url = null;
            String parentId = null;
            String resourceId = null;
            
            if( null != request.getParameter("name") && !"".equals(request.getParameter("name")) ) {
            	name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            }
            if( null != request.getParameter("permssion") && !"".equals(request.getParameter("permssion")) ) {
            	permssion = request.getParameter("permssion");//父id
            }
            if( null != request.getParameter("url") && !"".equals(request.getParameter("url")) ) {
            	url = request.getParameter("url");
            }
            if( null != request.getParameter("parentId") && !"".equals(request.getParameter("parentId")) ) {
            	parentId = request.getParameter("parentId");
            }
            if( null != request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId")) ) {
            	resourceId = request.getParameter("resourceId");
            }
            Resource resource = new Resource();
            resource.setId(resourceId);
            resource.setName(name);
            resource.setUrl(url);
            resource.setParentId(parentId);
            resource.setPermssion(permssion);
            Integer resourceList = resourceService.updateResource(resource);
            if(resourceList>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "修改成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "修改失败");
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 删除
     * @param request
     * @param response
     * @param resourceId
     */
    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String resourceId = null;
            if( null != request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId")) ) {
            	resourceId = request.getParameter("resourceId");
            }
            if(resourceId == null || "".equals(resourceId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "资源id为空");
            }
            Integer flag = resourceService.deleteResource(resourceId);
            if(flag>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "删除失败");
            } 
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }  
        return dataMap;
    }
}
