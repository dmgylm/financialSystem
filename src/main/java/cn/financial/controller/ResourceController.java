package cn.financial.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Resource;
import cn.financial.service.ResourceService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 功能权限表
 * @author gs
 * 2018/3/15
 */
@Controller
@Api(value="功能权限controller",tags={"功能权限操作接口"})
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
    @RequiresPermissions("jurisdiction:view")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ApiOperation(value="查询全部功能权限信息",notes="查询全部功能权限信息")
    @ResponseBody
    public Map<String, Object> listResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<Resource> resource = resourceService.listResource();
            List<TreeNode<Resource>> nodes = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();
            if(!CollectionUtils.isEmpty(resource)){
                for (Resource rss : resource) {
                    TreeNode<Resource> node = new TreeNode<>();
                    node.setId(rss.getCode().toString());//当前code
                    String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
                    node.setParentId(b);//父节点
                    node.setName(rss.getName());//权限名称
                    node.setPid(rss.getId());//当前权限id
                   // node.setNodeData(rss);
                    nodes.add(node);
                }
                jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
            }
            dataMap.put("resourceList", jsonObject);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
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
    @RequiresPermissions("jurisdiction:view")
    @RequestMapping(value = "/resourceById", method = RequestMethod.POST)
    @ApiOperation(value="根据功能权限id查询功能权限信息",notes="根据功能权限id查询功能权限信息")
    @ApiImplicitParams({@ApiImplicitParam(name="resourceId",value="功能权限id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public Map<String, Object> getResourceById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
        	String resourceId = null;
        	if(null != request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId"))) {
        		  resourceId = request.getParameter("resourceId");
        	}
            Resource resource = resourceService.getResourceById(resourceId,"");
            dataMap.put("resourceById", resource);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            e.printStackTrace();
        }
        return dataMap;
    }
    /**
     * 新增
     * @param request
     * @param response
     */
    @RequiresPermissions("jurisdiction:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="新增功能权限信息",notes="新增功能权限信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="功能权限名称",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="parentId",value="父节点",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="url",value="路径",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="permssion",value="权限",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public Map<String, Object> insertResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try { 
            String name = null;//功能权限名称
            String parentId = null;//父id
            String url = null;//路径
            String permssion = null;//权限
            
            if( null != request.getParameter("name") && !"".equals(request.getParameter("name")) ) {
            	name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            }
            if( null != request.getParameter("parentId") && !"".equals(request.getParameter("parentId")) ) {
                parentId = request.getParameter("parentId");//父id
            }
            if( null != request.getParameter("url") && !"".equals(request.getParameter("url")) ) {
            	url = request.getParameter("url");
            }
            if( null != request.getParameter("permssion") && !"".equals(request.getParameter("permssion")) ) {
            	permssion = request.getParameter("permssion");
            }
            Resource parent = resourceService.getResourceById("",parentId);//根据code查询parentId
            Resource resource = new Resource();
            resource.setId(UuidUtil.getUUID());
            resource.setName(name);
            resource.setUrl(url);
            resource.setPermssion(permssion);
            if(parent != null && !"".equals(parent)){  
                if(parent.getParentId() != null && !"".equals(parent.getParentId()) && !"0".equals(parent.getParentId())){
                    resource.setParentId(parent.getParentId()+"/"+parent.getCode());
                }else{
                    resource.setParentId("1");
                }
            }else{//没数据返回代表父id不存在直接把1赋值给parentId
                resource.setParentId("1");
            }
            int resourceList = resourceService.insertResource(resource);
            if(resourceList>0){
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }else{
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            } 

        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 修改
     * @param request
     * @param response
     */
    @RequiresPermissions("jurisdiction:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="修改功能权限信息",notes="修改功能权限信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="功能权限名称",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="resourceId",value="功能权限id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="url",value="路径",dataType="string", paramType = "query")})
    @ResponseBody
    public Map<String, Object> updateResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
        	String name = null;
            //String permssion = null;
            String url = null;
            //String parentId = null;
            String resourceId = null;
            
            if( null != request.getParameter("name") && !"".equals(request.getParameter("name")) ) {
            	name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            }
            /*if( null != request.getParameter("permssion") && !"".equals(request.getParameter("permssion")) ) {
            	permssion = request.getParameter("permssion");
            }*/
            if( null != request.getParameter("url") && !"".equals(request.getParameter("url")) ) {
            	url = request.getParameter("url");
            }
            /*if( null != request.getParameter("parentId") && !"".equals(request.getParameter("parentId")) ) {
            	parentId = request.getParameter("parentId");
            }*/
            if( null != request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId")) ) {
            	resourceId = request.getParameter("resourceId");
            }
            Resource parent = resourceService.getResourceById(resourceId,"");//根据id查询parentId(父id是否存在)
            Resource resource = new Resource();
            resource.setId(resourceId);
            resource.setName(name);
            resource.setUrl(url);
            //resource.setPermssion(permssion);
            if(parent != null && !"".equals(parent)){  
                if(parent.getParentId() != null && !"".equals(parent.getParentId()) && !"0".equals(parent.getParentId())){
                    resource.setParentId(parent.getParentId());
                }else{
                    resource.setParentId("1");
                }
            }else{//没数据返回代表父id不存在直接把1赋值给parentId
                resource.setParentId("1");
            }
            Integer resourceList = resourceService.updateResource(resource);
            if(resourceList>0){
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }else{
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
            
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
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
    /*@RequiresPermissions("jurisdiction:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value="删除功能权限信息",notes="删除功能权限信息")
    @ApiImplicitParams({@ApiImplicitParam(name="resourceId",value="功能权限id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public Map<String, Object> deleteResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String resourceId = null;
            if( null != request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId")) ) {
            	resourceId = request.getParameter("resourceId");
            }
            Integer flag = resourceService.deleteResource(resourceId);
            if(flag>0){
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }else{
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            } 
            
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }  
        return dataMap;
    }*/
}
