package cn.financial.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Role;
import cn.financial.model.RoleResource;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.model.response.ResultUtils;
import cn.financial.model.response.RoleResourceResult;
import cn.financial.model.response.RoleResult;
import cn.financial.service.UserRoleService;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.service.impl.RoleServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
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
    private RoleServiceImpl roleService;
    @Autowired
    private RoleResourceServiceImpl roleResourceService;
    @Autowired
    private UserRoleService userRoleService;
    
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
    public Map<String, Object> listRole(HttpServletRequest request){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        User currentUser = (User) request.getAttribute("user");
    	try {
    	    List<Role> roleNameList = new ArrayList<>();
            String rName = "N";//不是超级管理员
            if(currentUser.getId()!=null && !currentUser.getId().equals("")){
                //根据当前登录用户名查询是否是超级管理员
                List<UserRole> userRole = userRoleService.listUserRole(currentUser.getName(), null);//根据用户名查询用户是否是超级管理员
                if(!CollectionUtils.isEmpty(userRole)){
                    for(UserRole uRole : userRole){
                        Role roleName = roleService.getRoleById(uRole.getrId());
                        roleNameList.add(roleName); 
                    }
                }
            }
            if(!CollectionUtils.isEmpty(roleNameList)){
                for(Role roles : roleNameList){
                    if(roles.getRoleName().equals("超级管理员")){//是超级管理员
                        rName = null;
                    }
                }
            }
            List<Role> role = roleService.listRole(null,rName);//查询全部参数为空
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
    /*@RequiresPermissions("role:view")
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
    }*/
    /**
     * 新增角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="新增角色信息及角色功能权限关联信息(必须勾选父节点,父节点相当于查看权限)",notes="新增角色信息及角色功能权限关联信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleName",value="角色名称",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="resourceId",value="功能权限id,传入json格式,例如：[{\"resourceId\":\"e845649552244dec9cf4673d2ea3a384\"},{\"resourceId\":\"921bfa83018e467091faf34f91b7e401\"},{\"resourceId\":\"03767670e631466fb284391768110a59\"}]",
        paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils insertRole(String roleName, String resourceId){
        ResultUtils result = new ResultUtils();
        try {
            if(roleName == null || roleName.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLENAME_NULL, result);
                return result;
            }
            if(resourceId == null || resourceId.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCEID_NULL, result);
                return result;
            }
            List<Role> roleNameList = roleService.listRole(roleName,null);//根据roleName查询角色信息
            if(roleNameList.size()>0){//roleName不能重复
                ElementXMLUtils.returnValue(ElementConfig.ROLENAME_EXISTENCE, result);
                return result;
            }
            Role role = new Role();
            role.setId(UuidUtil.getUUID());
            role.setRoleName(roleName);
            int roleList = roleService.insertRole(role);
            if(roleList>0){
                RoleResource roleResource = new RoleResource();
                roleResource.setId(UuidUtil.getUUID());
                roleResource.setsId(resourceId);
                roleResource.setrId(role.getId());
                int roleResourceList = roleResourceService.insertRoleResource(roleResource);
                if(roleResourceList > 0){
                    ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                }else{
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="修改角色信息",notes="修改角色信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="roleName",value="角色名称",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="resourceId",value="功能权限id,传入json格式,例如：[{\"resourceId\":\"e845649552244dec9cf4673d2ea3a384\"},{\"resourceId\":\"921bfa83018e467091faf34f91b7e401\"},{\"resourceId\":\"03767670e631466fb284391768110a59\"}]",
                paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils updateRole(String roleName, String roleId, String resourceId){
        ResultUtils result = new ResultUtils();
        try {
            if(roleId == null || roleId.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
                return result;
            }
            if(roleName!=null && !roleName.equals("")){
                List<Role> roleNameList = roleService.listRole(roleName,null);//根据roleName查询角色信息
                if(roleNameList.size()>0){//roleName不能重复
                    ElementXMLUtils.returnValue(ElementConfig.ROLENAME_EXISTENCE, result);
                    return result;
                }
            }
            Role role = new Role();
            role.setId(roleId);
            role.setRoleName(roleName);
            Integer roleList = roleService.updateRole(role);
            int roleResourceList = 0;
            if(resourceId!=null && !resourceId.equals("")){
                RoleResource roleResource = new RoleResource();
                roleResource.setId(UuidUtil.getUUID());
                roleResource.setsId(resourceId);
                roleResource.setrId(roleId);
                roleResourceList = roleResourceService.updateRoleResource(roleResource);//修改（新增数据）
            }
            if(roleResourceList == -3){
                ElementXMLUtils.returnValue(ElementConfig.USER_RESOURCE, result);
            }else if(roleList>0 || roleResourceList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
     * 根据当前登录用户查询关联角色id查对应的功能权限(角色功能权限关联表)/查询全部功能权限信息(根据当前登录用户查询关联角色id及对应的功能权限信息)
     * @param request
     * @param response
     */
    @RequiresPermissions("role:view")
    @RequestMapping(value = "/roleResourceIndex", method = RequestMethod.POST)
    @ApiOperation(value="不传角色id查询全部功能权限信息/根据角色id查对应的功能权限信息",notes="全部功能权限信息指(根据当前登录用户查询关联角色id及对应的功能权限信息)", response = RoleResourceResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query")})
    @ResponseBody
    public Map<String, Object> listRoleResource(HttpServletRequest request, String roleId){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            User currentUser = (User) request.getAttribute("user");
            List<JSONObject> resourceObject = new ArrayList<>();
            //List<JSONObject> resources = new ArrayList<>();
            if(currentUser.getName()!=null && !currentUser.getName().equals("")){
                //根据当前登录用户名查询对应角色功能权限信息
                resourceObject = roleResourceService.roleResourceList(currentUser.getName());
                /*if(!CollectionUtils.isEmpty(resourceObject)){
                    for(JSONObject json : resourceObject){
                        if(json.containsKey("children") && CollectionUtils.isNotEmpty(json.getJSONArray("children"))){
                            for (Object item : json.getJSONArray("children")) {
                                JSONObject  itemChildren = (JSONObject)JSONObject.toJSON(item);
                                if(itemChildren.getString("name").equals("系统设置")){
                                    for(Object obj : itemChildren.getJSONArray("children")){
                                        JSONObject  itemChildrens = (JSONObject)JSONObject.toJSON(obj);
                                        if(!(itemChildrens.getString("name").equals("组织机构管理") || itemChildrens.getString("name").equals("数据模板配置"))){
                                            resources.add(itemChildrens);
                                        }
                                    }
                                    itemChildren.put("children", resources);
                                }
                            }
                        }
                    }
                }*/
            }
            if(roleId == null || roleId.equals("")){
                dataMap.put("roleResourceList", resourceObject);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }else{
                //筛选角色关联功能权限信息
                List<RoleResource> roleResource = roleResourceService.listRoleResource(roleId);
                HashSet<Object> twoList = new HashSet<>();
                if(!CollectionUtils.isEmpty(roleResource)){
                    for(RoleResource roleRes : roleResource){
                        twoList.add(roleRes.getsId());
                    }
                    roleService.addRole(resourceObject, twoList);
                }
                dataMap.put("roleResourceList", resourceObject);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }
            
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMapList;
    }
    /**
     * 新增(角色功能权限关联表)必须勾选父节点,父节点相当于查看权限
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param createTime
     */
    /*@RequiresPermissions("role:create")
    @RequestMapping(value = "/roleResourceInsert", method = RequestMethod.POST)
    @ApiOperation(value="新增(角色功能权限关联信息)",notes="必须勾选父节点,父节点相当于查看权限", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="resourceId",value="功能权限id,传入json格式,例如：[{\"resourceId\":\"e845649552244dec9cf4673d2ea3a384\"},{\"resourceId\":\"921bfa83018e467091faf34f91b7e401\"},{\"resourceId\":\"03767670e631466fb284391768110a59\"}]",
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
    }*/
    /**
     * 修改(角色功能权限关联表)先删除角色关联的功能权限信息，再重新添加该角色的功能权限信息
     * （根据角色id修改角色功能权限关联信息）
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param updateTime
     */
    /*@RequiresPermissions("role:update")
    @RequestMapping(value = "/roleResourceUpdate", method = RequestMethod.POST)
    @ApiOperation(value="修改(角色功能权限关联信息)",notes="先删除角色关联的功能权限信息，再重新添加该角色的功能权限信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value="角色id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="resourceId",value="功能权限id,传入json格式,例如：[{\"resourceId\":\"e845649552244dec9cf4673d2ea3a384\"},{\"resourceId\":\"921bfa83018e467091faf34f91b7e401\"},{\"resourceId\":\"03767670e631466fb284391768110a59\"}]", 
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
    }*/
}
