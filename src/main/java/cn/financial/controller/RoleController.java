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

import cn.financial.model.Role;
import cn.financial.model.RoleResource;
import cn.financial.model.response.ResultUtils;
import cn.financial.model.response.RoleInfo;
import cn.financial.model.response.RoleResourceInfo;
import cn.financial.model.response.RoleResult;
import cn.financial.service.RoleService;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 角色(角色资源关联表)
 * @author gs
 * 2018/3/13
 */
@Controller
@Api(value="角色controller",tags={"角色操作接口"})
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceServiceImpl roleResourceService;
    
    protected Logger logger = LoggerFactory.getLogger(RoleController.class);
    /**
     * 查询所有角色
     * @param request
     * @param response
     */
    @RequiresPermissions("role:view")
    @RequestMapping(value = "/roleList", method = RequestMethod.POST)
    @ApiOperation(value="查询所有角色信息",notes="查询所有角色信息", response = RoleResult.class)
    @ResponseBody
    public Map<String, Object> listRole(){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<Role> role = roleService.listRole("");//查询全部参数为空
            dataMap.put("roleList", role);
            dataMapList.put("data", dataMap);
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
    	return dataMapList;
    }
    /**
     * 根据roleId查询
     * @param request
     * @param response
     * @param roleId
     * @return
     */
    @RequiresPermissions("role:view")
    @RequestMapping(value = "/roleById", method = RequestMethod.POST)
    @ApiOperation(value="根据角色id查询角色信息",notes="根据角色id查询角色信息", response = RoleInfo.class)
    @ApiImplicitParams({@ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public Map<String, Object> getRoleById(String roleId){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Role role = roleService.getRoleById(roleId);
            if(role == null){
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL));
                return dataMapList;
            }
            dataMap.put("roleList", role);
            dataMapList.put("data", dataMap);
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            e.printStackTrace();
        }
        return dataMapList;
    }
    /**
     * 新增角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="新增角色信息",notes="新增角色信息", response = ResultUtils.class)
    @ApiImplicitParams({@ApiImplicitParam(name="roleName",value="角色名称",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils insertRole(String roleName){
        ResultUtils result = new ResultUtils();
        try {
            if(roleName == null || roleName.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLENAME_NULL, result);
                return result;
            }else{
                roleName = new String(roleName.getBytes("ISO-8859-1"), "UTF-8");
            }
            List<Role> roleNameList = roleService.listRole(roleName);//根据roleName查询角色信息
            if(roleNameList.size()>0){//roleName不能重复
                ElementXMLUtils.returnValue(ElementConfig.ROLENAME_EXISTENCE, result);
                return result;
            }
            Role role = new Role();
            role.setId(UuidUtil.getUUID());
            role.setRoleName(roleName);
            int roleList = roleService.insertRole(role);
            if(roleList>0){
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
     * 修改角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="修改角色信息",notes="修改角色信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="roleName",value="角色名称",dataType="string", paramType = "query")})
    @ResponseBody
    public ResultUtils updateRole(String roleName, String roleId){
        ResultUtils result = new ResultUtils();
        try {
            if(roleName != null && !roleName.equals("")){
                roleName = new String(roleName.getBytes("ISO-8859-1"), "UTF-8");
            }
            Role role = new Role();
            role.setId(roleId);
            role.setRoleName(roleName);
            Integer roleList = roleService.updateRole(role);
            if(roleList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
            }else if(roleList>0){
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
     * 删除角色
     * @param request
     * @param response
     * @param roleId
     */
    /*@RequiresPermissions("role:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value="删除角色信息",notes="删除角色信息", response = ResultUtils.class)
    @ApiImplicitParams({@ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils deleteRole(String roleId){
        ResultUtils result = new ResultUtils();
        try {
            Integer flag = roleService.deleteRole(roleId);
            if(flag == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
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
    }*/
    
    /**
     * 根据角色id查对应的功能权限(角色功能权限关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"jurisdiction:view","role:view"})
    @RequestMapping(value = "/roleResourceIndex", method = RequestMethod.POST)
    @ApiOperation(value="根据角色id查对应的功能权限信息",notes="根据角色id查对应的功能权限(角色功能权限关联表)", response = RoleResourceInfo.class)
    @ApiImplicitParams({@ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public RoleResourceInfo listRoleResource(String roleId){
        RoleResourceInfo result = new RoleResourceInfo();
        try {
            List<RoleResource> roleResource = roleResourceService.listRoleResource(roleId);
            if(roleResource == null){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
                return result;
            }
            List<TreeNode<RoleResource>> nodes = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();
            if(!CollectionUtils.isEmpty(roleResource)){
                for (RoleResource rss : roleResource) {
                    TreeNode<RoleResource> node = new TreeNode<>();
                    node.setId(rss.getCode().toString());//当前code
                    String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
                    node.setParentId(b);//父id
                    node.setName(rss.getName());//功能权限名称
                    node.setPid(rss.getsId());//当前权限id
                    // node.setNodeData(rss);
                    nodes.add(node);
                }
                jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
            }
            result.setRoleResourceList(jsonObject);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 新增(角色功能权限关联表)必须勾选父节点,父节点相当于查看权限
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param createTime
     */
    @RequiresPermissions({"jurisdiction:create","role:create"})
    @RequestMapping(value = "/roleResourceInsert", method = RequestMethod.POST)
    @ApiOperation(value="新增(角色功能权限关联信息)",notes="必须勾选父节点,父节点相当于查看权限", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="resourceId",value="功能权限id,传入json格式,例如：[{\"resourceId\":\"921bfa83018e467091faf34f91b7e401\"},{\"resourceId\":\"03767670e631466fb284391768110a59\"}](斜线不要加，这里代表转译符)",
            paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils insertRoleResource(String roleId, String resourceId){
        ResultUtils result = new ResultUtils();
        try {
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getUUID());
            roleResource.setsId(resourceId);
            roleResource.setrId(roleId);
            int roleResourceList = roleResourceService.insertRoleResource(roleResource);
            if(roleResourceList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
            }else if(roleResourceList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCEID_NULL, result);
            }else if(roleResourceList > 0){
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
     * 修改(角色功能权限关联表)先删除角色关联的功能权限信息，再重新添加该角色的功能权限信息
     * （根据角色id修改角色功能权限关联信息）
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param updateTime
     */
    @RequiresPermissions({"jurisdiction:update","role:update"})
    @RequestMapping(value = "/roleResourceUpdate", method = RequestMethod.POST)
    @ApiOperation(value="修改(角色功能权限关联信息)",notes="先删除角色关联的功能权限信息，再重新添加该角色的功能权限信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="resourceId",value="功能权限id,传入json格式,例如：[{\"resourceId\":\"921bfa83018e467091faf34f91b7e401\"},{\"resourceId\":\"03767670e631466fb284391768110a59\"}](斜线不要加，这里代表转译符)", 
            paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils updateRoleResource(String resourceId, String roleId){
        ResultUtils result = new ResultUtils();
        try {
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getUUID());
            roleResource.setsId(resourceId);
            roleResource.setrId(roleId);
            int roleResourceList = roleResourceService.updateRoleResource(roleResource);//修改（新增数据）
            if(roleResourceList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
            }else if(roleResourceList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCEID_NULL, result);
            }else if(roleResourceList == -3){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCE, result);
            }else if(roleResourceList > 0){
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
