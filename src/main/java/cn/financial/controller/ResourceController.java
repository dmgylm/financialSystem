package cn.financial.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.financial.model.Resource;
import cn.financial.service.impl.ResourceServiceImpl;
import cn.financial.util.FileUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

/**
 * 资源权限表
 * @author gs
 * 2018/3/15
 */
@Controller
public class ResourceController {
    @Autowired
    ResourceServiceImpl resourceService;
    
    protected Logger logger = LoggerFactory.getLogger(ResourceController.class);
    /**
     * 查询所有用户
     * @param request
     * @param response
     */
    @RequestMapping(value = "/resource/index", method = RequestMethod.POST)
    public void listResource(HttpServletRequest request,HttpServletResponse response){
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
    	FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 根据id查询
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping(value = "/resource/resourceById", method = RequestMethod.POST)
    public void getResourceById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Resource resource = resourceService.getResourceById(request.getParameter("resourceId"));
            dataMap.put("resourceById", resource);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功");
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            e.printStackTrace();
        }
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 新增角色
     * @param request
     * @param response
     */
    @RequestMapping(value = "/resource/insert", method = RequestMethod.POST)
    public void insertResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            Resource resource = new Resource();
            resource.setId(UuidUtil.getUUID());
            resource.setName(name);
            resource.setUrl(request.getParameter("url"));
            resource.setParentId(request.getParameter("parentId"));
            resource.setPermssion(request.getParameter("permssion"));
            resource.setCreateTime(new Date());
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
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 修改角色
     * @param request
     * @param response
     */
    @RequestMapping(value = "/resource/update", method = RequestMethod.POST)
    public void updateResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            Resource resource = new Resource();
            resource.setId(request.getParameter("resourceId"));
            resource.setName(name);
            resource.setUrl(request.getParameter("url"));
            resource.setParentId(request.getParameter("parentId"));
            resource.setPermssion(request.getParameter("permssion"));
            resource.setUpdateTime(new Date());
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
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 删除角色
     * @param request
     * @param response
     * @param roleId
     */
    @RequestMapping(value = "/resource/delete", method = RequestMethod.POST)
    public void deleteResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Integer flag = resourceService.deleteResource(request.getParameter("resourceId"));
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
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
}
