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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Role;
import cn.financial.model.RoleResource;
import cn.financial.service.RoleService;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.UuidUtil;

/**
 * 角色(角色资源关联表)
 * @author gs
 * 2018/3/13
 */
@Controller
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
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<Role> role = roleService.listRole("");//查询全部参数为空
            dataMap.put("roleList", role);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
    	return dataMap;
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
    @ResponseBody
    public Map<String, Object> getRoleById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleId = null;
            if(null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))){
                roleId = request.getParameter("roleId");//角色id
            }
            Role role = roleService.getRoleById(roleId);
            dataMap.put("roleById", role);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            e.printStackTrace();
        }
        return dataMap;
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
    @ResponseBody
    public Map<String, Object> insertRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleName = null;
            if(null!=request.getParameter("roleName") && !"".equals(request.getParameter("roleName"))){
                roleName = new String(request.getParameter("roleName").getBytes("ISO-8859-1"), "UTF-8");//角色名称 
            }
            List<Role> roleNameList = roleService.listRole(roleName);//根据roleName查询角色信息
            if(roleNameList.size()>0){//roleName不能重复
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.ROLENAME_EXISTENCE));
                return dataMap;
            }
            Role role = new Role();
            role.setId(UuidUtil.getUUID());
            role.setRoleName(roleName);
            int roleList = roleService.insertRole(role);
            if(roleList>0){
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
     * 修改角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleName = null, roleId = null;
            if(null!=request.getParameter("roleName") && !"".equals(request.getParameter("roleName"))){
                roleName = new String(request.getParameter("roleName").getBytes("ISO-8859-1"), "UTF-8");//角色名称
            }
            if(null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))){
                roleId = request.getParameter("roleId");//角色id
            }
            Role role = new Role();
            role.setId(roleId);
            role.setRoleName(roleName);
            Integer roleList = roleService.updateRole(role);
            if(roleList>0){
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
     * 删除角色
     * @param request
     * @param response
     * @param roleId
     */
    /*@RequiresPermissions("role:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleId = null;
            if(null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))){
                roleId = request.getParameter("roleId");//角色id
            }
            Integer flag = roleService.deleteRole(roleId);
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
    
    /**
     * 查询所有/根据角色id查对应的功能权限(角色功能权限关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"jurisdiction:view","role:view"})
    @RequestMapping(value = "/roleResourceIndex", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleId = null;
            if(null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))){
                roleId = request.getParameter("roleId");//角色id
            }
            JSONObject jsonObject = roleResourceService.roleResourceList(roleId);
            dataMap.put("roleResourceList", jsonObject);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 新增(角色功能权限关联表)必须勾选父节点父节点相当于查看功能
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param createTime
     */
    @RequiresPermissions({"jurisdiction:create","role:create"})
    @RequestMapping(value = "/roleResourceInsert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String resourceId = null, roleId = null;
            if(null!=request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId"))){
                resourceId = request.getParameter("resourceId");//资源权限id ,json格式数据
            }
            if(null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))){
                roleId = request.getParameter("roleId");//角色id
            }
            JSONArray sArray = JSON.parseArray(resourceId);
            int roleResourceList = 0;
            RoleResource roleResource = null;
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String resourceIdStr = (String)object.get("resourceId");//获取key-resourceId键值
                System.out.println("resouceId:==="+resourceIdStr);
                if(resourceIdStr!=null && !"".equals(resourceIdStr)){
                    roleResource = new RoleResource();
                    roleResource.setId(UuidUtil.getUUID());
                    roleResource.setsId(resourceIdStr);
                    roleResource.setrId(roleId);
                    roleResourceList = roleResourceService.insertRoleResource(roleResource);
                }
            }
            if(roleResourceList>0){
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
    @ResponseBody
    public Map<String, Object> updateRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String resourceId = null, roleId = null;
            if(null!=request.getParameter("resourceId") && !"".equals(request.getParameter("resourceId"))){
                resourceId = request.getParameter("resourceId");//资源权限id ,json格式数据
            }
            if(null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))){
                roleId = request.getParameter("roleId");//角色id
            }
            int roleResourceDelete = roleResourceService.deleteRoleResource(roleId);//删除
            if(roleResourceDelete > 0){
                JSONArray sArray = JSON.parseArray(resourceId);
                int roleResourceList = 0;
                RoleResource roleResource = null;
                for (int i = 0; i < sArray.size(); i++) {
                    JSONObject object = (JSONObject) sArray.get(i);
                    String resourceIdStr = (String)object.get("resourceId");//获取key-resourceId键值
                    System.out.println("resouceId:==="+resourceIdStr);
                    if(resourceIdStr!=null && !"".equals(resourceIdStr)){
                        roleResource = new RoleResource();
                        roleResource.setId(UuidUtil.getUUID());
                        roleResource.setsId(resourceIdStr);
                        roleResource.setrId(roleId);
                        roleResourceList = roleResourceService.updateRoleResource(roleResource);//修改（新增数据）
                    }
                }
                if(roleResourceList>0){
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                }else{
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                }
            }else{
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
            
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
}
