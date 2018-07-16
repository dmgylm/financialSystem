package cn.financial.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.financial.model.response.ResourceResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.model.response.RsourceInfo;
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
    private ResourceService resourceService;
    
    protected Logger logger = LoggerFactory.getLogger(ResourceController.class);
    /**
     * 查询所有
     * @param request
     * @param response
     */
    @RequiresPermissions("jurisdiction:view")
    @RequestMapping(value = "/resourceList", method = RequestMethod.POST)
    @ApiOperation(value="查询全部功能权限信息",notes="查询全部功能权限信息", response = ResourceResult.class)
    @ResponseBody
    public Map<String, Object> listResource(){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> dataMapList = new HashMap<String, Object>();
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
            dataMapList.put("data", dataMap);
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
    	return dataMapList;
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
    @ApiOperation(value="根据功能权限id查询功能权限信息",notes="根据功能权限id查询功能权限信息", response = RsourceInfo.class)
    @ApiImplicitParams({@ApiImplicitParam(name="resourceId",value="功能权限id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public RsourceInfo getResourceById(String resourceId){
        RsourceInfo result = new RsourceInfo();
        try {
            if(resourceId == null || resourceId.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCEID_NULL, result);
                return result;
            }
            Resource resource = resourceService.getResourceById(resourceId,"");
            result.setData(resource);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 新增
     * @param request
     * @param response
     */
    @RequiresPermissions("jurisdiction:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="新增功能权限信息",notes="新增功能权限信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="功能权限名称",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="parentId",value="父节点",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="url",value="路径",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="permssion",value="权限",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils insertResource(String name, String parentId, String url, String permssion){
        ResultUtils result = new ResultUtils();
        try {
            if(parentId == null || parentId.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCE_PARENTID_NULL, result);
                return result;
            }
            if(name == null || name.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCE_NAME_NULL, result);
                return result;
            }else{
                name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
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
            if(resourceList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCE_PERMSSION_NULL, result);
            }else if(resourceList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            } 

        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 修改
     * @param request
     * @param response
     */
    @RequiresPermissions("jurisdiction:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="修改功能权限信息",notes="修改功能权限信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="功能权限名称",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="resourceId",value="功能权限id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="url",value="路径",dataType="string", paramType = "query")})
    @ResponseBody
    public ResultUtils updateResource(String name, String url, String resourceId){
        ResultUtils result = new ResultUtils();
        try {
            if(resourceId == null || resourceId.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCEID_NULL, result);
                return result;
            }
            if(name != null && !name.equals("")){
                name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
            }
            Resource parent = resourceService.getResourceById(resourceId,"");//根据id查询parentId(父id是否存在)
            Resource resource = new Resource();
            resource.setId(resourceId);
            resource.setName(name);
            resource.setUrl(url);
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
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 删除
     * @param request
     * @param response
     * @param resourceId
     */
    @RequiresPermissions("jurisdiction:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value="删除功能权限信息",notes="删除功能权限信息", response = ResultUtils.class)
    @ApiImplicitParams({@ApiImplicitParam(name="resourceId",value="功能权限id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils deleteResource(String resourceId){
        ResultUtils result = new ResultUtils();
        try {
            Integer flag = resourceService.deleteResource(resourceId);
            if(flag == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCEID_NULL, result);
            }else if(flag>0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            } 
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }  
        return result;
    }
}
